/*-
 *
 * *************************************************************************************************************
 *  Copyright (C) 2013 by Cognitive Medical Systems, Inc
 *  (http://www.cognitivemedciine.com) * * Licensed under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in compliance *
 *  with the License. You may obtain a copy of the License at * *
 *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable
 *  law or agreed to in writing, software distributed under the License is *
 *  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. * See the License for the specific language
 *  governing permissions and limitations under the License. *
 * *************************************************************************************************************
 *
 * *************************************************************************************************************
 *  Socratic Grid contains components to which third party terms apply. To comply
 *  with these terms, the following * notice is provided: * * TERMS AND
 *  CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION * Copyright (c) 2008,
 *  Nationwide Health Information Network (NHIN) Connect. All rights reserved. *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that * the following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the *     following disclaimer. * - Redistributions in
 *  binary form must reproduce the above copyright notice, this list of
 *  conditions and the *     following disclaimer in the documentation and/or
 *  other materials provided with the distribution. * - Neither the name of the
 *  NHIN Connect Project nor the names of its contributors may be used to endorse
 *  or *     promote products derived from this software without specific prior
 *  written permission. * * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS
 *  AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED * WARRANTIES, INCLUDING,
 *  BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 *  OR CONTRIBUTORS BE LIABLE FOR * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, *
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 *  OR BUSINESS INTERRUPTION HOWEVER * CAUSED AND ON ANY THEORY OF LIABILITY,
 *  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 *  OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, * EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. * * END OF TERMS AND CONDITIONS *
 * *************************************************************************************************************
 */
package org.socraticgrid.fhirlib;

// import net.sf.saxon.lib.NamespaceConstant;
import org.socraticgrid.codeconversion.SearchProcessor;
import org.socraticgrid.codeconversion.elements.CodeReference;
import org.socraticgrid.codeconversion.elements.CodeSearch;
import org.socraticgrid.codeconversion.elements.SearchOptions;

import org.socraticgrid.documenttransformer.interfaces.SimpleTransformStep;

import org.springframework.util.xml.SimpleNamespaceContext;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;


/**
 * Replaces codes in FHIR structure based on XPATH.
 *
 * <p>Possible points</p>
 *
 * <p>CD Type CS Type Interrelated XPATHS (With role) - Key and relative paths...</p>
 *
 * @author  Jerry Goodnough
 */
public class ComplexFHIRCodeTranslator implements SimpleTransformStep
{
    private static final Logger logger = Logger.getLogger(
            ComplexFHIRCodeTranslator.class.getName());
    private SearchProcessor codeProcessor;
    private String defaultNamespaceUri = "http://www.w3.org/2005/Atom";

    // Was" http://hl7.org/fhir";
    private SimpleNamespaceContext nameContext = new SimpleNamespaceContext();
    private List<XMLNamespace> namespaceList;
    private List<CodeReplacement> replacementList;

    public SearchProcessor getCodeProcessor()
    {
        return codeProcessor;
    }

    /**
     * Get the value of defaultNamespaceUri.
     *
     * @return  the value of defaultNamespaceUri
     */
    public String getDefaultNamespaceUri()
    {
        return defaultNamespaceUri;
    }

    public List<CodeReplacement> getReplacementList()
    {
        return replacementList;
    }

    @PostConstruct
    public void initialize()
    {
        nameContext.bindNamespaceUri("fhir", "http://hl7.org/fhir");
        nameContext.bindNamespaceUri("atom", "http://www.w3.org/2005/Atom");
        nameContext.bindDefaultNamespaceUri(this.defaultNamespaceUri);

        if (namespaceList != null)
        {
            Iterator<XMLNamespace> itr = namespaceList.iterator();

            while (itr.hasNext())
            {
                XMLNamespace ns = itr.next();
                nameContext.bindNamespaceUri(ns.getPrefix(), ns.getURI());
            }
        }
    }

    public void setCodeProcessor(SearchProcessor codeProcessor)
    {
        this.codeProcessor = codeProcessor;
    }

    /**
     * Set the value of defaultNamespaceUri.
     *
     * @param  defaultNamespaceUri  new value of defaultNamespaceUri
     */
    public void setDefaultNamespaceUri(String defaultNamespaceUri)
    {
        this.defaultNamespaceUri = defaultNamespaceUri;
    }

    public void setNamespaceList(List<XMLNamespace> namespaceList)
    {
        this.namespaceList = namespaceList;
    }

    public void setReplacementList(List<CodeReplacement> replacementList)
    {
        this.replacementList = replacementList;
    }

    @Override
    public boolean transform(StreamSource src, StreamResult result)
        throws TransformerException
    {

        // TODO: Consider added XPathVariable Resolver..
        return this.transform(src, result, null);
    }

    @Override
    public boolean transform(StreamSource src, StreamResult result, Properties props)
        throws TransformerException
    {
        boolean conversionOccured = false;

        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(true);

            // System.conversionOccured.println("Document Builder Factory class =
            // "+dbf.getClass().getCanonicalName());
            Document doc = dbf.newDocumentBuilder().parse(src.getInputStream());
            XPathFactory xpf = XPathFactory.newInstance(
                    XPathConstants.DOM_OBJECT_MODEL);

            // System.conversionOccured.println("XPath Factory class =
            // "+xpf.getClass().getCanonicalName());
            XPath xpath = xpf.newXPath();
            xpath.setNamespaceContext(nameContext);

            Iterator<CodeReplacement> itr = this.replacementList.iterator();

            while (itr.hasNext())
            {
                CodeReplacement cr = itr.next();
                String path = cr.getXPath();
                XPathExpression exp = xpath.compile(path);
                NodeList nodes = (NodeList) exp.evaluate(doc,
                        XPathConstants.NODESET);
                int nodesFnd = nodes.getLength();

                for (int idx = 0; idx < nodesFnd; idx++)
                {
                    Node x = nodes.item(idx);
                    String cdType = cr.getCodeType();

                    switch (cdType)
                    {
                        case "CD":
                        {

                            if (this.handleCD(xpath, x, cr) == true)
                            {
                                conversionOccured = true;
                            }

                            break;
                        }
                        case "SC":
                        {

                            if (this.handleCD(xpath, x, cr) == true)
                            {
                                conversionOccured = true;
                            }

                            break;
                        }
                        default:
                        {
                            logger.log(Level.WARNING,
                                "ComplexFHIRCodeTranslator called with invalid code type: {0}",
                                cdType);

                            break;
                        }
                    }

                    // Applies to type Coding
                }
            }

            if (conversionOccured == true)
            {

                // /PrescriptionList/Prescription[1]/medicine/identification/coding/system/@value
                TransformerFactory tFactory = TransformerFactory.newInstance();
                javax.xml.transform.Transformer transformer =
                    tFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);
            }
        }
        catch (ParserConfigurationException | SAXException | IOException |
                XPathFactoryConfigurationException | XPathExpressionException |
                DOMException | TransformerFactoryConfigurationError |
                TransformerException ex)
        {
            logger.log(Level.SEVERE, null, ex);
            throw new TransformerException("Error during transformation", ex);
            // TODO: Throw parser error
        }

        return conversionOccured;
    }

    /**
     * Handle Coding.
     *
     * @param   xpath
     * @param   x
     * @param   cr
     *
     * @return
     *
     * @throws  XPathExpressionException
     */
    protected boolean handleCD(XPath xpath, Node x, CodeReplacement cr)
        throws XPathExpressionException
    {
        boolean out = false;

        // Applies to type Coding
        Node system = (Node) xpath.evaluate("fhir:system/@value", x,
                XPathConstants.NODE);
        Node code = (Node) xpath.evaluate("fhir:code/@value", x,
                XPathConstants.NODE);
        Node display = (Node) xpath.evaluate("fhir:display/@value", x,
                XPathConstants.NODE);
        String sourceSystem;

        if (system == null)
        {
            sourceSystem = cr.getDefaultSystem();
        }
        else
        {
            sourceSystem = system.getNodeValue();

            if ((sourceSystem == null) || sourceSystem.isEmpty())
            {
                sourceSystem = system.getNodeValue();
            }
        }

        String sourceText = (display == null) ? "" : display.getNodeValue();
        String sourceCode = (code == null) ? "" : code.getNodeValue();
        CodeSearch cs = new CodeSearch();
        cs.setDisplay(sourceText);
        cs.setSearchType(cr.getSearchType());
        cs.setTargetSystem(cr.getTargetSystem());
        cs.setCode(sourceCode);
        cs.setSystem(sourceSystem);

        CodeReference fnd = codeProcessor.findCode(cs);

        if (fnd != null)
        {

            if (system != null)
            {
                system.setNodeValue(fnd.getSystem());
                out = true;
            }

            if (code != null)
            {
                code.setNodeValue(fnd.getCode());
            }

            if (cr.getReplaceDisplay())
            {
                display.setNodeValue(fnd.getDisplay());
                out = true;
            }
        }

        return out;
    }

    /**
     * Handle Simple Code Lookup.
     *
     * @param   xpath
     * @param   x
     * @param   cr
     *
     * @return
     *
     * @throws  XPathExpressionException
     */
    protected boolean handleSC(XPath xpath, Node x, CodeReplacement cr)
        throws XPathExpressionException
    {
        boolean out = false;
        Node code = (Node) xpath.evaluate("@value", x, XPathConstants.NODE);
        String sourceSystem;
        sourceSystem = cr.getDefaultSystem();

        String sourceText = "";
        String sourceCode = (code == null) ? "" : code.getNodeValue();
        CodeSearch cs = new CodeSearch();
        cs.setDisplay(sourceText);
        cs.setSearchType(cr.getSearchType());
        cs.setTargetSystem(cr.getTargetSystem());
        cs.setCode(sourceCode);
        cs.setSystem(sourceSystem);

        CodeReference fnd = codeProcessor.findCode(cs);

        if (fnd != null)
        {

            if (code != null)
            {
                code.setNodeValue(fnd.getCode());
            }
        }

        return out;
    }
}
