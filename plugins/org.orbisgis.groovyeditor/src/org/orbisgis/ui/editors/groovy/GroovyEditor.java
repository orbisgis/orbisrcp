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

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.swt.SWT;
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

import net.prominic.groovyls.GroovyServices;
import net.prominic.groovyls.config.CompilationUnitFactory;

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
        System.out.println("\n in GroovyEditor() \n");
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
        
        System.out.println("\n in createPartControl(Composite parent)");
        IDocument document = getDocument();
        System.out.println("\n document.get() : " + document.get());
        
        String LANGUAGE_GROOVY = "groovy";
        //Path workspaceRoot = Paths.get(System.getProperty("user.home")).resolve(".local/share/DBeaverData/workspace6/General/Groovy/");
        Path workspaceRoot = Paths.get(System.getProperty("user.dir")).resolve("./build/test_workspace/");
        Path srcRoot = workspaceRoot.resolve("./src/main/groovy");
    	if (!Files.exists(workspaceRoot)) {
    		workspaceRoot.toFile().mkdirs();
		}
        GroovyServices services = new GroovyServices(new CompilationUnitFactory());
        services.setWorkspaceRoot(workspaceRoot);
        services.connect(new LanguageClient() {

			@Override
			public void telemetryEvent(Object object) {

			}

			@Override
			public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
				return null;
			}

			@Override
			public void showMessage(MessageParams messageParams) {

			}

			@Override
			public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {

			}

			@Override
			public void logMessage(MessageParams message) {

			}
		});
        Path filePath = workspaceRoot.resolve("Completion.groovy");
        //Path filePath = workspaceRoot.resolve("Script-5.groovy");
		String uri = filePath.toUri().toString();
		System.out.println("\n uri : " + uri);
        StringBuilder contents = new StringBuilder();
		contents.append("class Completion {\n");
		contents.append("  public Completion() {\n");
		contents.append("    String localVar\n");
		contents.append("    localVar.\n");
		contents.append("  }\n");
		contents.append("}");
		TextDocumentItem textDocumentItem = new TextDocumentItem(uri, LANGUAGE_GROOVY, 3, contents.toString());
		System.out.println("\n textDocumentItem : " + textDocumentItem);
		services.didOpen(new DidOpenTextDocumentParams(textDocumentItem));
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier(uri);
		System.out.println("\n textDocument : " + textDocument);

		Position position = new Position(3, 13);
		try {
			Either<List<CompletionItem>, CompletionList> result= services.completion(new CompletionParams(textDocument, position)).get();
			System.out.println("\n result : " + result);
			System.out.println("\n result.isLeft() : " + result.isLeft());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			result = services.completion(new CompletionParams(textDocument, position)).get();
			System.out.println("\n result : " + result);
			System.out.println("\n result.isLeft() : " + result.isLeft());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
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
