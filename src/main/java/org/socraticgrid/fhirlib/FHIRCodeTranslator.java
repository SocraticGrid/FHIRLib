/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.fhirlib;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
//import net.sf.saxon.lib.NamespaceConstant;
import org.socraticgrid.codeconversion.SearchProcessor;
import org.socraticgrid.codeconversion.elements.CodeReference;
import org.socraticgrid.documenttransformer.TransformStep;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.annotation.PostConstruct;

/**
 * TODO: Make this an independent jar file. This will decouple the depandancy to
 * Code Conversion
 *
 * @author Jerry Goodnough
 */
public class FHIRCodeTranslator implements TransformStep
{
    private List<XMLNamespace> namespaceList;

    public void setNamespaceList(List<XMLNamespace> namespaceList)
    {
        this.namespaceList = namespaceList;
    }
    
    private List<CodeReplacement> replacementList;

    public List<CodeReplacement> getReplacementList()
    {
        return replacementList;
    }

    public void setReplacementList(List<CodeReplacement> replacementList)
    {
        this.replacementList = replacementList;
    }
    private SearchProcessor codeProcessor;

    public SearchProcessor getCodeProcessor()
    {
        return codeProcessor;
    }
 
    public void setCodeProcessor(SearchProcessor codeProcessor)
    {
        this.codeProcessor = codeProcessor;
    }
   
    private SimpleNamespaceContext context = new SimpleNamespaceContext();
   
    @PostConstruct 
    public void initialize()
    {
        context.bindDefaultNamespaceUri("http://hl7.org/fhir");
        context.bindNamespaceUri("fhir", "http://hl7.org/fhir"); 
        if (namespaceList != null)
        {
            Iterator<XMLNamespace> itr = namespaceList.iterator();
            while(itr.hasNext())
            {
                XMLNamespace ns = itr.next() ;
                context.bindNamespaceUri(ns.getPrefix(), ns.getURI());
            }
        }
    }

    public void transform(StreamSource src, StreamResult result, Properties props) throws TransformerException
    {
        //TODO: Consider added XPathVariable Resolver..
        this.transform(src, result);
    }
    
    public void transform(StreamSource src, StreamResult result) throws TransformerException
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(true);
            //System.out.println("Document Builder Factory class = "+dbf.getClass().getCanonicalName());
            Document doc = dbf.newDocumentBuilder().parse(src.getInputStream());
        
            XPathFactory xpf = XPathFactory.newInstance(XPathConstants.DOM_OBJECT_MODEL);
            
            //System.out.println("XPath Factory class = "+xpf.getClass().getCanonicalName());
            XPath xpath = xpf.newXPath();
   
            xpath.setNamespaceContext(context);
            
            Iterator<CodeReplacement> itr = this.replacementList.iterator();
            while (itr.hasNext())
            {
                CodeReplacement cr = itr.next();
                String path  = cr.getXPath();
                XPathExpression exp = xpath.compile(path);
                NodeList nodes = (NodeList) exp.evaluate(doc,XPathConstants.NODESET);
                int nodesFnd = nodes.getLength();
                for (int idx = 0; idx < nodesFnd; idx++)
                {
                    Node x = nodes.item(idx);

                    Node system = (Node) xpath.evaluate("fhir:system/@value", x, XPathConstants.NODE);
                    Node code = (Node) xpath.evaluate("fhir:code/@value", x, XPathConstants.NODE);
                    Node display = (Node) xpath.evaluate("fhir:display/@value", x, XPathConstants.NODE);

                    String sourceSystem;

                    if (system == null)
                    {
                        sourceSystem = cr.getDefaultSystem();
                    }
                    else
                    {
                        sourceSystem = system.getNodeValue();
                        if (sourceSystem == null || sourceSystem.isEmpty())
                        {
                            sourceSystem = system.getNodeValue();
                        }
                    }

                    String sourceCode = code.getNodeValue();
                    String sourceText = display == null ? "" : display.getNodeValue();
                    CodeReference fnd = codeProcessor.findCode(cr.getTargetSystem(), sourceSystem, sourceCode, sourceText);
                    if (fnd != null)
                    {
                        if (system != null)
                        {
                            system.setNodeValue(fnd.getSystem());
                        }
                        code.setNodeValue(fnd.getCode());
                        if (cr.getReplaceDisplay() && (display != null))
                        {
                            display.setNodeValue(fnd.getDisplay());
                        }
                    }
                }
            }
            //   /PrescriptionList/Prescription[1]/medicine/identification/coding/system/@value
            TransformerFactory tFactory = TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(doc);

            transformer.transform(source, result);


        }
        catch (Exception ex)
        {
            Logger.getLogger(FHIRCodeTranslator.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new TransformerException(
                    "Error during transformation", ex);
            //TODO: Throw parser error
        }
    }
}
