/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.fhirlib;

/**
 *
 * @author Jerry Goodnough
 */
public class CodeReplacement
{
    
    private String XPath;

    /**
     * Get the value of XPath
     *
     * @return the value of XPath
     */
    public String getXPath()
    {
        return XPath;
    }

    /**
     * Set the value of XPath
     *
     * @param XPath new value of XPath
     */
    public void setXPath(String XPath)
    {
        this.XPath = XPath;
    }
    private String targetSystem;

    /**
     * Get the value of targetSystem
     *
     * @return the value of targetSystem
     */
    public String getTargetSystem()
    {
        return targetSystem;
    }

    /**
     * Set the value of targetSystem
     *
     * @param targetSystem new value of targetSystem
     */
    public void setTargetSystem(String targetSystem)
    {
        this.targetSystem = targetSystem;
    }
    private boolean replaceDisplay;

    /**
     * Get the value of replaceDisplay
     *
     * @return the value of replaceDisplay
     */
    public boolean getReplaceDisplay()
    {
        return replaceDisplay;
    }
    private String defaultSystem="";

    /**
     * Get the value of defaultSystem
     *
     * @return the value of defaultSystem
     */
    public String getDefaultSystem()
    {
        return defaultSystem;
    }

    /**
     * Set the value of defaultSystem
     *
     * @param defaultSystem new value of defaultSystem
     */
    public void setDefaultSystem(String defaultSystem)
    {
        this.defaultSystem = defaultSystem;
    }

    /**
     * Set the value of replaceDisplay
     *
     * @param replaceDisplay new value of replaceDisplay
     */
    public void setReplaceDisplay(boolean replaceDisplay)
    {
        this.replaceDisplay = replaceDisplay;
    }

}
