/*
 * Groovy Editor (GE) is a library that brings a groovy console to the Eclipse RCP.
 * GE is developed by CNRS http://www.cnrs.fr/.
 *
 * GE is part of the OrbisGIS project. GE is free software;
 * you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation;
 * version 3.0 of the License.
 *
 * GE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details http://www.gnu.org/licenses.
 *
 *
 *For more information, please consult: http://www.orbisgis.org
 *or contact directly: info_at_orbisgis.org
 */
package org.orbisgis.ui.editors.groovy;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroovyCompletionProcessorTest {

    private GroovyCompletionProcessor groovyCompletionProcessor;

    @BeforeEach
    void beforeEach(){
        groovyCompletionProcessor = new GroovyCompletionProcessor();
    }

    @Test
    void getColumnTest() {
        assertEquals(5,groovyCompletionProcessor.getColumn("abcd\nabcde",2,3));
    }

    @Test
    void getColumnIfLineIsZeroTest() {
        assertEquals(3,groovyCompletionProcessor.getColumn("abcd\nabcde",0,3));
    }

    @Test
    void buildProposalsIfParametersTest() {
        List<String> orderedLabelList = new ArrayList<>();
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        proposals[0] = new CompletionProposal("variable.startsWith(String param0" , 3,
                20, 21,
                null, "String param0",
                null, null);
        assertEquals(proposals[0].getDisplayString(),groovyCompletionProcessor.buildProposals(orderedLabelList,"variable.startsWith(",10,"String param0")[0].getDisplayString());
    }

    @Test
    void buildProposalsIfNoParameterAndWithParenthesisTest() {
        List<String> orderedLabelList = new ArrayList<>();
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        proposals[0] = new CompletionProposal("variable.startsWith(" , 3,
                20, 20,
                null, "No parameter",
                null, null);
        assertEquals(proposals[0].getDisplayString(),groovyCompletionProcessor.buildProposals(orderedLabelList,"variable.startsWith(",10,"")[0].getDisplayString());
    }

    @Test
    void buildProposalsIfNoParameterAndNoParenthesisAndNoPointTest() {
        List<String> orderedLabelList = new ArrayList<>();
        orderedLabelList.add("variable");
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        IContextInformation contextInfo = new ContextInformation(null, "variable");
        proposals[0] = new CompletionProposal("variable" , 3,
                4, 20,
                null, "variable",
                contextInfo, null);
        assertEquals(proposals[0].getDisplayString(),groovyCompletionProcessor.buildProposals(orderedLabelList,"vari",10,"")[0].getDisplayString());
    }

    @Test
    void buildProposalsIfNoParameterAndNoParenthesisAndWithPointTest() {
        List<String> orderedLabelList = new ArrayList<>();
        orderedLabelList.add("toString");
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        IContextInformation contextInfo = new ContextInformation(null, "variable.toString");
        proposals[0] = new CompletionProposal("variable.toString()" , 3,
                9, 20,
                null, "toString",
                contextInfo, null);
        assertEquals(proposals[0].getDisplayString(),groovyCompletionProcessor.buildProposals(orderedLabelList,"variable.",10,"")[0].getDisplayString());
    }

}