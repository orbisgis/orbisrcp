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
package org.orbisgis.ui.editors.groovy.completion;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.ui.Icons;

public class ComplProposal implements ICompletionProposal,
                                            ICompletionProposalExtension4,
                                            ICompletionProposalExtension6 {

    private static final Logger LOGGER = new Logger(GroovyCompletionProcessor.class);

    private final int currPos;
    private final int replaceOff;
    private final int replaceLength;
    private final String replaceStr;
    private final int cursorPos;
    private final String label;
    private final CompletionItemKind kind;
    private final String[] additionalInfo;

    public ComplProposal(int currPos, int replaceOff, int replaceLength, String replaceStr, int cursorPos,
                         String label, CompletionItemKind kind, String... additionalInfo){
        this.currPos = currPos;
        this.replaceOff = replaceOff;
        this.replaceLength = replaceLength;
        this.replaceStr = replaceStr;
        this.cursorPos = cursorPos;
        this.label = label;
        this.kind = kind;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public void apply(IDocument document) {
        try {
            document.replace(this.replaceOff, this.replaceLength, this.replaceStr);
        } catch (BadLocationException e) {
            LOGGER.error("Bad location", e);
        }
    }

    @Override
    public Point getSelection(IDocument iDocument) {
        return new Point(this.replaceOff + this.cursorPos, 0);
    }

    @Override
    public String getAdditionalProposalInfo() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return label;
    }

    @Override
    public Image getImage() {
        switch(kind){
            case Method:
                return Icons.COMPLETION_METHOD;
            case Variable:
                return Icons.COMPLETION_VARIABLE;
        }
        return null;
    }

    @Override
    public IContextInformation getContextInformation() {
        return null;
    }

    @Override
    public boolean isAutoInsertable() {
        return true;
    }

    @Override
    public StyledString getStyledDisplayString() {
        return new StyledString()
                .append(label.substring(0, currPos), new ColorStyler(SWT.COLOR_DARK_CYAN))
                .append(label.substring(currPos))
                .append(getQualifier(), StyledString.QUALIFIER_STYLER);
    }

    private String getQualifier(){
        switch(kind){
            case Method:
                return " ("+ String.join(", ", additionalInfo) +")";
            case Variable:
                return " "+(additionalInfo.length>0?additionalInfo[0]:"");
        }
        return null;
    }

    private static class ColorStyler extends StyledString.Styler {

        int col;

        public ColorStyler(int color){
            this.col = color;
        }

        @Override
        public void applyStyles(final TextStyle textStyle) {
            textStyle.foreground = Display.getCurrent().getSystemColor(col);
        }
    }
}
