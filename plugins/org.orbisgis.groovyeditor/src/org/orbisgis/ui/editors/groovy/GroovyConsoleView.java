package org.orbisgis.ui.editors.groovy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jkiss.dbeaver.model.runtime.DBRProcessController;
import org.jkiss.dbeaver.model.runtime.DBRProcessDescriptor;
import org.jkiss.dbeaver.ui.UIUtils;

public class GroovyConsoleView extends ViewPart implements DBRProcessController
{


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
		GroovyConsoleContent.initialize(parent);
	}
	
	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	public static class GroovyConsoleContent{
		
		static Composite group = null;
		//static Label label = null;
		//static Label label2 = null;
		//static Display display = new Display();
    	//static Image image = new Image(display, "groovy.png");
		static Text text = null;
		//static Color green = new Color(display, 0, 114, 135);
		//static StyleRange styleRange = new StyleRange();

		
		public static void initialize(Composite parent) {
	        group = UIUtils.createPlaceholder(parent, 1);
	        group.setLayout(new FillLayout());
	        /*
	        label = new Label(group, SWT.HORIZONTAL);
	    	label.setLayoutData(new GridData());
	    	*/
	    	
	    	/*label2 = new Label(group, SWT.HORIZONTAL);
	    	label2.setText("groovy> ");
	    	label2.setLayoutData(new GridData());
	    	
	    	Color color = new Color(display, 0, 114, 135);
	        label2.setForeground(color);*/
	    	
	    	text = new Text(group, SWT.HORIZONTAL | SWT.READ_ONLY | SWT.V_SCROLL);
	    	text.setLayoutData(new GridData());
	    	/*
			styleRange.start = 0;
			styleRange.length = 7;
			styleRange.foreground = green;*/
		}
		
		public static void writeIntoConsole(String message) {
			System.out.println("\n***\n in writeIntoConsole : " + message + "\n***\n");
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					/*
					if (label.getText() == null || label.getText().trim().isEmpty()) {
						//label.setImage(image);
						label.setText("groovy> " + message);
					}else if(message == null || message.trim().isEmpty()){
					} 
					else {
			    	label.setText(label.getText() + "\n"
			    			+ "groovy> " + message);
				   	label.setLayoutData(new GridData());
					}
					*/
					if (text.getText() == null || text.getText().trim().isEmpty()) {
						//label.setImage(image);
						text.setText("groovy> " + message);
					}else if(message == null || message.trim().isEmpty()){
					} 
					else if(message.equals("ERASE")){
						text.setText("");
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
