package org.orbisgis.ui.editors.groovy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jkiss.dbeaver.model.runtime.DBRProcessController;
import org.jkiss.dbeaver.model.runtime.DBRProcessDescriptor;
import org.jkiss.dbeaver.ui.UIUtils;

public class GroovyConsoleView extends ViewPart implements DBRProcessController
{
	Composite group = null;

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
        this.group = UIUtils.createPlaceholder(parent, 1);
        this.group.setLayout(new FillLayout());
        Label label = new Label(this.group, SWT.HORIZONTAL);
    	label.setText("Je suis ici !!");
    	label.setLayoutData(new GridData());
	}
	
	public void writeIntoConsole(Object message) {
		Label label = new Label(this.group, SWT.HORIZONTAL);
		System.out.println("\n***\n in writeIntoConsole : " + message + "\n***\n");
    	label.setText((String) message);
    	label.setLayoutData(new GridData());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
