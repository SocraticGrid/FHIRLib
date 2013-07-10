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

import org.socraticgrid.codeconversion.elements.SearchOptions;


/**
 * General Search Information for the code replacment.
 *
 * @author  Jerry Goodnough
 */
public class CodeReplacement
{
    private String codeType = "CD";
    private String defaultSystem = "";
    private boolean replaceDisplay = false;
    private int searchType = SearchOptions.LITERAL_Code + SearchOptions.ANY_Display +
        SearchOptions.ANY_TargetSystem;
    private String targetSystem;
    private String XPath;

    /**
     * Get the value of codeType.
     *
     * @return  the value of codeType
     */
    public String getCodeType()
    {
        return codeType;
    }

    /**
     * Get the value of defaultSystem.
     *
     * @return  the value of defaultSystem
     */
    public String getDefaultSystem()
    {
        return defaultSystem;
    }

    /**
     * Get the value of replaceDisplay.
     *
     * @return  the value of replaceDisplay
     */
    public boolean getReplaceDisplay()
    {
        return replaceDisplay;
    }

    /**
     * Get the value of searchType.
     *
     * @return  the value of searchType
     */
    public int getSearchType()
    {
        return searchType;
    }

    /**
     * Get the value of targetSystem.
     *
     * @return  the value of targetSystem
     */
    public String getTargetSystem()
    {
        return targetSystem;
    }

    /**
     * Get the value of XPath.
     *
     * @return  the value of XPath
     */
    public String getXPath()
    {
        return XPath;
    }

    /**
     * Set the value of codeType. CD - Coding (Default) - Has System/Code/Display SC
     * - Code (Simple Code) - Value (Code)
     *
     * @param  codeType  new value of codeType
     */
    public void setCodeType(String codeType)
    {

        // TODO:  Assert this is a correct type
        this.codeType = codeType;
    }

    /**
     * Set the value of defaultSystem.
     *
     * @param  defaultSystem  new value of defaultSystem
     */
    public void setDefaultSystem(String defaultSystem)
    {
        this.defaultSystem = defaultSystem;
    }

    /**
     * Set the value of replaceDisplay.
     *
     * @param  replaceDisplay  new value of replaceDisplay
     */
    public void setReplaceDisplay(boolean replaceDisplay)
    {
        this.replaceDisplay = replaceDisplay;
    }

    /**
     * Set the value of searchType.
     *
     * @param  searchType  new value of searchType
     */
    public void setSearchType(int searchType)
    {
        this.searchType = searchType;
    }

    /**
     * Set the value of targetSystem.
     *
     * @param  targetSystem  new value of targetSystem
     */
    public void setTargetSystem(String targetSystem)
    {
        this.targetSystem = targetSystem;
    }

    /**
     * Set the value of XPath.
     *
     * @param  XPath  new value of XPath
     */
    public void setXPath(String XPath)
    {
        this.XPath = XPath;
    }
}
