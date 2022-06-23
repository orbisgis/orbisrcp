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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.controls.VerticalButton;
import org.jkiss.dbeaver.ui.controls.VerticalFolder;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.syntax.GroovySourceViewerConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GroovyEditor extends AbstractDecoratedTextEditor implements ISaveablePart2 {

    private static final Logger LOGGER = new Logger(GroovyEditor.class);

    private static final int SIDE_TOOLBAR_MARGIN_TOP = 3;
    private static final int SIDE_TOOLBAR_MARGIN_BOTTOM = 10;
    private static final int SIDE_TOOLBAR_VERTICAL_SPACING = 3;

    private GroovyJob job;

    public GroovyEditor(){
        super();
        setKeyBindingScopes(new String[]{"org.orbisgis.ui.editors.groovy"});
        setSourceViewerConfiguration(new GroovySourceViewerConfiguration());
    }

    /**
     * Checks if the cursor is between simple quotation marks
     * @param caretOffset the caret
     * @param text the text
     * @param keyCode the keyCode
     * @return true if the caret is between simple quotation marks
     */
    public boolean cursorIsBetweenSimpleQuotationMarks(int caretOffset, String text, int keyCode){
        boolean doubleMarksInSimpleMarks = false;
        ArrayList<Integer> simpleQuotePositions = findPositions(text).get(0);
        if(simpleQuotePositions.size() > 1) {
            for (int i = 0; i < simpleQuotePositions.size();i = i + 2){
                if(i+1 < simpleQuotePositions.size()) {
                    // checks if the cursor is between simple quotation marks
                    int a = simpleQuotePositions.get(i + 1);
                    int b = a + 1;
                    if (caretOffset > simpleQuotePositions.get(i) && caretOffset < b && keyCode != 8) {
                        doubleMarksInSimpleMarks = true;
                    }
                }
            }
        }
        return doubleMarksInSimpleMarks;
    }

    /**
     * Find the positions of a character in a text and returns an ArrayList of these positions
     * @param text the text
     * @return an ArrayList containing the positions of the character in the text
     */
    public ArrayList<ArrayList<Integer>> findPositions(String text) {
        ArrayList<ArrayList<Integer>> quotationMarksList = new ArrayList<>();
        ArrayList<Integer> simpleQuotePositions = new ArrayList<>();
        ArrayList<Integer> doubleQuotePositions = new ArrayList<>();
        for (int i = 0; i < text.length(); i++){
            if (text.charAt(i) == "'".charAt(0)) {
                simpleQuotePositions.add(i);
            }
            if (text.charAt(i) == "\"".charAt(0)) {
                doubleQuotePositions.add(i);
            }
        }
        ArrayList<Integer> doubleQuotePositionsToDelete = new ArrayList<>();
        if(simpleQuotePositions.size() > 1) {
            for (int i = 0; i < simpleQuotePositions.size();i = i + 2){
                for (Integer doubleQuotePosition : doubleQuotePositions) {
                    // check if a double quotation marks is not between simple quotation marks
                    if(i+1 < simpleQuotePositions.size()) {
                        if (doubleQuotePosition > simpleQuotePositions.get(i) && doubleQuotePosition < simpleQuotePositions.get(i + 1)) {
                            doubleQuotePositionsToDelete.add(doubleQuotePosition);
                        }
                    }
                }
            }
        }
        doubleQuotePositions.removeAll(doubleQuotePositionsToDelete);
        quotationMarksList.add(simpleQuotePositions);
        quotationMarksList.add(doubleQuotePositions);
        return quotationMarksList;
    }

    /**
     * Catch the last pressed key, if it is an opening symbol like ([{"', the corresponding closing symbol is written
     * automatically after the cursor.
     * If the deleted key is pressed : if the deleted character is an opening symbol like ([{"' and if the following
     * character is its corresponding closing symbol, so this corresponding closing symbol is also deleted.
     * @param parent the composite
     * @param ruler the iVerticalRuler
     * @param styles the styles
     * @return the possibly edited view
     */
    @Override
    public ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        ISourceViewer viewer = super.createSourceViewer(parent, ruler, styles);
        if(viewer instanceof ITextViewerExtension) {

            ((ITextViewerExtension) viewer).appendVerifyKeyListener(verifyEvent -> {
                String lastCharacter = String.valueOf(verifyEvent.character);
                StyledText styledText = (StyledText) verifyEvent.getSource();
                String source = styledText.getText();

                ArrayList<Integer> doubleQuotationMarkPositions = findPositions(source).get(1);
                int caretOffset = styledText.getCaretOffset();
                boolean cursorInSimpleMarks = cursorIsBetweenSimpleQuotationMarks(caretOffset, source, verifyEvent.keyCode);

                int nbQuotationMarkBeforeCaret = 0;
                for (Integer quotationMarkPosition : doubleQuotationMarkPositions) {
                    if (quotationMarkPosition < caretOffset) {
                        nbQuotationMarkBeforeCaret++;
                    } else {
                        break;
                    }
                }
                // if the quotation mark number before the caret is even
                if((nbQuotationMarkBeforeCaret % 2) == 0 && !cursorInSimpleMarks) {
                    switch (lastCharacter) {
                        case "(":
                            styledText.insert(")");
                            break;
                        case "[":
                            styledText.insert("]");
                            break;
                        case "{":
                            styledText.insert("}");
                            break;
                        case "\"":
                            styledText.insert("\"");
                            break;
                        case "'":
                            styledText.insert("'");
                            break;
                    }
                    // if the delete key is pressed
                    if (verifyEvent.keyCode == 8) {
                        // if there is a character after the cursor and if the delete key is not pressed at the first
                        // possible position of the caret
                        if (caretOffset < source.length() && caretOffset != 0) {
                            String deletedCharacter = String.valueOf(source.charAt(caretOffset - 1));
                            String followingCharacter = String.valueOf(source.charAt(caretOffset));
                            if (deletedCharacter.equals("(") && followingCharacter.equals(")")
                                    || deletedCharacter.equals("[") && followingCharacter.equals("]")
                                    || deletedCharacter.equals("{") && followingCharacter.equals("}")
                                    || deletedCharacter.equals("'") && followingCharacter.equals("'")) {
                                styledText.replaceTextRange(caretOffset, 1, "");
                            }
                        }
                    }
                    if (caretOffset < source.length() && caretOffset != 0) {
                        String followingCharacter = String.valueOf(source.charAt(caretOffset));
                        if(followingCharacter.equals(lastCharacter) && followingCharacter.equals(")")
                        || followingCharacter.equals(lastCharacter) && followingCharacter.equals("]")
                        || followingCharacter.equals(lastCharacter) && followingCharacter.equals("}")){
                            styledText.replaceTextRange(caretOffset, 1, "");
                        }
                    }
                }else {
                    // if we type a double quotation mark and if the following character is also a double quotation mark,
                    // the cursor will go to the next position without inserting anything more.
                    if (caretOffset < source.length() && caretOffset != 0) {
                        String followingCharacter = String.valueOf(source.charAt(caretOffset));
                        if(followingCharacter.equals(lastCharacter) && followingCharacter.equals("\"")){
                            styledText.replaceTextRange(caretOffset, 1, "");
                        }
                    }
                    // if the delete key is pressed
                    if (verifyEvent.keyCode == 8) {
                        // if there is a character after the cursor and if the delete key is not pressed at the first
                        // possible position of the caret
                        if (caretOffset < source.length() && caretOffset != 0) {
                            String deletedCharacter = String.valueOf(source.charAt(caretOffset - 1));
                            String followingCharacter = String.valueOf(source.charAt(caretOffset));
                            if (deletedCharacter.equals("\"") && followingCharacter.equals("\"")) {
                                styledText.replaceTextRange(caretOffset, 1, "");
                            }
                        }
                    }
                }
            });
        }
        return viewer;
    }

    @Override
    public int promptToSaveOnClose() {
        return ISaveablePart2.YES;
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite groovyEditor = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout(3, false);
        gl.verticalSpacing = 0;
        gl.horizontalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        groovyEditor.setLayout(gl);

        createControlsBar(groovyEditor);

        GroovyEditorControl editorControl = new GroovyEditorControl(groovyEditor, this);
        super.createPartControl(editorControl);
        editorControl.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void createControlsBar(Composite editorPanel) {
        try {
            ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(new URL("platform:/plugin/org.orbisgis.groovyeditor/icons/groovy_script.png"));
            setTitleImage(imageDescriptor.createImage());
        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to get Groovy script icon.", e);
        }

        VerticalFolder sideToolBar = new VerticalFolder(editorPanel, SWT.LEFT);
        sideToolBar.setCheckCommandEnablement(true);
        ((GridLayout)sideToolBar.getLayout()).marginTop = 3;
        ((GridLayout)sideToolBar.getLayout()).marginBottom = 10;
        ((GridLayout)sideToolBar.getLayout()).verticalSpacing = 3;

        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_EXECUTE_SELECTION, false);
        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_EXECUTE_SCRIPT, false);
        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_CLEAR, false);
        UIUtils.createEmptyLabel(sideToolBar, 1, 1).setLayoutData(new GridData(GridData.FILL_VERTICAL));
        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_LIST_CLASSPATHS, false);
        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_ADD_DIR_TO_CLASSPATH, false);
        VerticalButton.create(sideToolBar, SWT.LEFT | SWT.PUSH, getSite(), GroovyEditorCommands.CMD_ADD_JAR_TO_CLASSPATH, false);

        GridLayout layout = (GridLayout)sideToolBar.getLayout();
        layout.marginTop = SIDE_TOOLBAR_MARGIN_TOP;
        layout.marginBottom = SIDE_TOOLBAR_MARGIN_BOTTOM;
        layout.verticalSpacing = SIDE_TOOLBAR_VERTICAL_SPACING;

        sideToolBar.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
    }

    private IDocument getDocument(){
        IDocumentProvider documentProvider = getDocumentProvider();
        if(documentProvider != null){
            return documentProvider.getDocument(getEditorInput());
        }
        return null;
    }

    public void clear(){
        IDocument document = getDocument();
        if(document != null){
            document.set("");
        }
    }

    public void execute(){
        this.doSave(new NullProgressMonitor());
        IDocument document = getDocument();
        if(document != null){
            job = new GroovyJob(getPartName(), document.get());
            job.schedule();
        }
    }

    public void executeSelection() {
        this.doSave(new NullProgressMonitor());
        IDocument document = getDocument();
        if(document != null){
            ITextSelection selection = (ITextSelection) getSelectionProvider().getSelection();
            if (!selection.getText().isEmpty()) {
                job = new GroovyJob(getPartName(), selection.getText());
                job.schedule();
            }
        }
    }

    public void stop(){
        this.getRunningJob().cancel();
    }

    public GroovyJob getRunningJob() {
        return job;
    }

}
