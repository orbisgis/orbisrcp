package org.orbisgis.ui.editors.groovy;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jkiss.dbeaver.model.runtime.DBRProcessController;
import org.jkiss.dbeaver.model.runtime.DBRProcessDescriptor;
import org.jkiss.dbeaver.ui.ActionUtils;
import org.jkiss.dbeaver.ui.UIUtils;
import org.orbisgis.core.ui.ToolbarButton;

public class GroovyConsoleView extends ViewPart implements DBRProcessController
{
	static Composite group = null;
	static ToolbarButton button = null;
	
	public static final String VIEW_ID = "org.jkiss.dbeaver.groovy.output.clear";
    private static final String CLEAR_VIEW_MENU_ID = VIEW_ID + ".menu";
    public static final String CLEAR_CMD_ID = "org.orbisgis.ui.editors.groovy.clearOutput";
  

	@Override
	public DBRProcessDescriptor getProcessDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void terminateProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite parent) {
		group = UIUtils.createPlaceholder(parent, 1);
        group.setLayout(new FillLayout());
		GroovyConsoleContent.initialize(group);
		createDeleteOutputButton(group);
	}
	
	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private void createDeleteOutputButton(Composite group) {
		final MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(manager -> {
            manager.add(ActionUtils.makeCommandContribution(getSite(), CLEAR_CMD_ID));
        });
        group.setMenu(menuMgr.createContextMenu(group));
	}
	
	public static class GroovyConsoleContent{
	
		static Text text = null;

		public static void initialize(Composite group) {
	    	text = new Text(group, SWT.HORIZONTAL | SWT.READ_ONLY | SWT.V_SCROLL);
	    	text.setLayoutData(new GridData());
		}
		
		public static void writeIntoConsole(String message) {
			System.out.println("\n***\n in writeIntoConsole : " + message + "\n***\n");
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					if(message.equals("ERASE")){
						text.setText("");
					} 
					else if (message.equals("END")) {
						text.setText(text.getText() + "\n"
				    			+ "Groovy script successfully executed.");
					}
					else if (text.getText() == null || text.getText().trim().isEmpty()) {
						text.setText("groovy> " + message);
					}
					else if(message == null || message.trim().isEmpty()){
					} 
					else {
			    	text.setText(text.getText() + "\n"
			    			+ "groovy> " + message);
				   	text.setLayoutData(new GridData());
					}
				}
			});
		}
		
		
	}

}
