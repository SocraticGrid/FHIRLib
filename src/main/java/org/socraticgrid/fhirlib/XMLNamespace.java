/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.fhirlib;

/**
 *
 * @author Jerry Goodnough
 */
public class XMLNamespace
{
    
    private String prefix;

    /**
     * Get the value of prefix
     *
     * @return the value of prefix
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Set the value of prefix
     *
     * @param prefix new value of prefix
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
    private String URI;

    /**
     * Get the value of URI
     *
     * @return the value of URI
     */
    public String getURI()
    {
        return URI;
    }

    /**
     * Set the value of URI
     *
     * @param URI new value of URI
     */
    public void setURI(String URI)
    {
        this.URI = URI;
    }

}
