package org.orbisgis.ui.editors.groovy;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.core.ui.Toolbar;
import org.orbisgis.core.ui.ToolbarButton;
import org.orbisgis.ui.editors.groovy.sql.DataSource;
import org.orbisgis.ui.editors.groovy.syntax.GroovySourceViewerConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

        Toolbar sideToolBar = new Toolbar(editorPanel, SWT.LEFT);

        GridLayout layout = (GridLayout)sideToolBar.getLayout();
        layout.marginTop = SIDE_TOOLBAR_MARGIN_TOP;
        layout.marginBottom = SIDE_TOOLBAR_MARGIN_BOTTOM;
        layout.verticalSpacing = SIDE_TOOLBAR_VERTICAL_SPACING;

        ToolbarButton.create(sideToolBar, getSite(), GroovyEditorCommands.CMD_EXECUTE_SELECTION);
        ToolbarButton.create(sideToolBar, getSite(), GroovyEditorCommands.CMD_EXECUTE_SCRIPT);
        ToolbarButton.create(sideToolBar, getSite(), GroovyEditorCommands.CMD_CLEAR);

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

    public void execute(Map<String, DataSource> dataSources, List<String> closedDatasources){
        IDocument document = getDocument();
        if(document != null){
            job = new GroovyJob(getPartName(), document.get(), dataSources, closedDatasources);
            job.schedule();
        }
    }

    public void executeSelection(Map<String, DataSource> dataSources, List<String> closedDatasources){
        IDocument document = getDocument();
        if(document != null){
            ITextSelection selection = (ITextSelection) getSelectionProvider().getSelection();
            if (!selection.getText().isEmpty()) {
                job = new GroovyJob(getPartName(), selection.getText(), dataSources, closedDatasources);
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
