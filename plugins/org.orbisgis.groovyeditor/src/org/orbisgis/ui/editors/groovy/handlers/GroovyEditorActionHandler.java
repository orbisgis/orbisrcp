package org.orbisgis.ui.editors.groovy.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.navigator.DBNDataSource;
import org.jkiss.dbeaver.ui.navigator.database.DatabaseNavigatorView;
import org.jkiss.dbeaver.utils.RuntimeUtils;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.GroovyEditor;
import org.orbisgis.ui.editors.groovy.GroovyEditorCommands;
import org.orbisgis.ui.editors.groovy.sql.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroovyEditorActionHandler extends AbstractHandler {

    private static final Logger LOGGER = new Logger(GroovyEditorActionHandler.class);

    @Override
    public Object execute(ExecutionEvent executionEvent) {
        GroovyEditor editor = RuntimeUtils.getObjectAdapter(HandlerUtil.getActiveEditor(executionEvent), GroovyEditor.class);
        if (editor == null) {
            LOGGER.error("No active Groovy editor found");
            return null;
        }
        String actionId = executionEvent.getCommand().getId();
        switch (actionId) {
            case GroovyEditorCommands.CMD_CLEAR:
                editor.clear();
                break;
            case GroovyEditorCommands.CMD_EXECUTE_SCRIPT:
                editor.execute(getDataSources(executionEvent), getClosedDataSources(executionEvent));
                break;
            case GroovyEditorCommands.CMD_EXECUTE_SELECTION:
                editor.executeSelection(getDataSources(executionEvent), getClosedDataSources(executionEvent));
                break;
            case GroovyEditorCommands.CMD_EXECUTE_STOP:
                editor.stop();
                break;
            default:
                LOGGER.error("Unsupported SQL editor command: " + actionId);
                break;
        }

        return null;
    }

    private List<DBPDataSourceContainer> getContainers(ExecutionEvent event){
        List<DBPDataSourceContainer> list = new ArrayList<>();
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        if(window != null) {
            IWorkbenchPage page = window.getActivePage();
            if(page != null) {
                IViewPart nav = window.getActivePage().findView(DatabaseNavigatorView.VIEW_ID);
                if (nav instanceof DatabaseNavigatorView) {
                    for (TreeItem item : ((DatabaseNavigatorView) nav).getNavigatorViewer().getTree().getItems()) {
                        if (item != null) {
                            Object data = item.getData();
                            if (data instanceof DBNDataSource) {
                                list.add(((DBNDataSource) data).getDataSourceContainer());
                            }
                            else{
                                LOGGER.warn("No data source found");
                            }
                        }
                        else{
                            LOGGER.warn("No tree item found");
                        }
                    }
                }
                else{
                    LOGGER.warn("No DatabaseNavigatorView found");
                }
            }
            else{
                LOGGER.warn("No IWorkbenchPage found");
            }
        }
        else{
            LOGGER.warn("No IWorkbenchWindow found");
        }
        return list;
    }

    private Map<String, DataSource> getDataSources(ExecutionEvent event) {
        Map<String, DataSource> map = new HashMap<>();
        for(DBPDataSourceContainer container : getContainers(event)) {
            if (container.isConnected()) {
                map.put(container.getName().replaceAll("\\W+", "_"), new DataSource(container));
            }
        }
        return map;
    }

    private List<String> getClosedDataSources(ExecutionEvent event) {
        List<String> list = new ArrayList<>();
        for(DBPDataSourceContainer container : getContainers(event)) {
            if (!container.isConnected()) {
                list.add(container.getName().replaceAll("\\W+", "_"));
            }
        }
        return list;
    }
}
