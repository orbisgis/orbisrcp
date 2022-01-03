package org.orbisgis.ui.editors.groovy;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
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
	
		static StyledText text = null;
		static Display display = null;
		static StyleRange style1 = null;
		static String message_END =  "\nGroovy script successfully executed.\n";
		static String message_BAD_END =  "\nError while execution the Groovy script.\n";

		public static void initialize(Composite group) {
	    	text = new StyledText(group, SWT.HORIZONTAL | SWT.READ_ONLY | SWT.V_SCROLL);
	    	display = PlatformUI.getWorkbench().getDisplay();
	    	text.setLayoutData(new GridData());
		}
		
		public static void writeIntoConsole(String message, boolean resultOfExecution) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					text.setText(text.getText() + "\n\n" + message);
				}
			});
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
						style1 = new StyleRange(text.getText().length(), message_END.length(), display.getSystemColor(SWT.COLOR_GREEN), null);
						text.setText(text.getText() + message_END);
						text.setStyleRange(style1);
						for (int i = -1; (i = text.getText().indexOf("groovy>", i + 1)) != -1; i++) {
						    StyleRange style1 = new StyleRange(i, 7, display.getSystemColor(SWT.COLOR_BLUE), null);
						    text.setStyleRange(style1);
						}
					}
					else if (message.equals("BAD_END")) {
						style1 = new StyleRange(text.getText().length(), message_BAD_END.length(), display.getSystemColor(SWT.COLOR_RED), null);
						text.setText(text.getText() + message_BAD_END);
						text.setStyleRange(style1);
						for (int i = -1; (i = text.getText().indexOf("groovy>", i + 1)) != -1; i++) {
						    StyleRange style1 = new StyleRange(i, 7, display.getSystemColor(SWT.COLOR_BLUE), null);
						    text.setStyleRange(style1);
						}
					}
					else if (text.getText() == null || text.getText().trim().isEmpty()) {
						text.setText("groovy> " + message);
					}
					else if(message == null || message.trim().isEmpty()){
					} 
					else {
				    	text.setText(text.getText() + "\n"
				    			+ "groovy> " + message);
					}
				}
			});
		}
		
	}

}
