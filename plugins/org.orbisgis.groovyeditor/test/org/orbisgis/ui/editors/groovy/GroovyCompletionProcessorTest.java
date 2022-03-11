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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroovyCompletionProcessorTest {

    private GroovyCompletionProcessor groovyCompletionProcessor;

    @BeforeEach
    public void beforeEach(){
        groovyCompletionProcessor = new GroovyCompletionProcessor();
    }

    @Test
    public void buildProposalsIfParametersTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> orderedLabelList = new ArrayList<>();
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        proposals[0] = new CompletionProposal("variable.startsWith(String param0" , 3,
                20, 21,
                null, "String param0",
                null, null);

        Method method = groovyCompletionProcessor.getClass().getDeclaredMethod("buildProposals", List.class, String.class, int.class, String.class);
        method.setAccessible(true);
        ICompletionProposal[] r = (ICompletionProposal[]) method.invoke(groovyCompletionProcessor,orderedLabelList,"variable.startsWith(",10,"String param0");

        assertEquals(proposals.length,r.length);
        assertEquals(proposals[0].getDisplayString(),r[0].getDisplayString());
    }

    @Test
    public void buildProposalsIfNoParameterAndWithParenthesisTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> orderedLabelList = new ArrayList<>();
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        proposals[0] = new CompletionProposal("variable.startsWith(" , 3,
                20, 20,
                null, "No parameter",
                null, null);

        Method method = groovyCompletionProcessor.getClass().getDeclaredMethod("buildProposals");
        method.setAccessible(true);
        ICompletionProposal[] r = (ICompletionProposal[]) method.invoke(groovyCompletionProcessor,orderedLabelList,"variable.startsWith(",10,"");

        assertEquals(proposals.length,r.length);
        assertEquals(proposals[0].getDisplayString(),r[0].getDisplayString());
    }

    @Test
    public void buildProposalsIfNoParameterAndNoParenthesisAndNoPointTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> orderedLabelList = new ArrayList<>();
        orderedLabelList.add("variable");
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        IContextInformation contextInfo = new ContextInformation(null, "variable");
        proposals[0] = new CompletionProposal("variable" , 3,
                4, 20,
                null, "variable",
                contextInfo, null);

        Method method = groovyCompletionProcessor.getClass().getDeclaredMethod("buildProposals");
        method.setAccessible(true);
        ICompletionProposal[] r = (ICompletionProposal[]) method.invoke(groovyCompletionProcessor,orderedLabelList,"vari",10,"");

        assertEquals(proposals.length,r.length);
        assertEquals(proposals[0].getDisplayString(),r[0].getDisplayString());
    }

    @Test
    public void buildProposalsIfNoParameterAndNoParenthesisAndWithPointTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> orderedLabelList = new ArrayList<>();
        orderedLabelList.add("toString");
        ICompletionProposal[] proposals = new ICompletionProposal[1];
        IContextInformation contextInfo = new ContextInformation(null, "variable.toString");
        proposals[0] = new CompletionProposal("variable.toString()" , 3,
                9, 20,
                null, "toString",
                contextInfo, null);

        Method method = groovyCompletionProcessor.getClass().getDeclaredMethod("buildProposals");
        method.setAccessible(true);
        ICompletionProposal[] r = (ICompletionProposal[]) method.invoke(groovyCompletionProcessor,orderedLabelList,"variable.",10,"");

        assertEquals(proposals.length,r.length);
        assertEquals(proposals[0].getDisplayString(),r[0].getDisplayString());
    }

}