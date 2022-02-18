package org.orbisgis.ui.editors.groovy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class GroovyCompletionProcessor implements IContentAssistProcessor {
	
	private List<String> elements;
	
    /**
     * Constructeur. Initialise la liste des elements disponibles
     */
    public GroovyCompletionProcessor() {
		elements = new ArrayList<String>();
		elements.add("Airbus");
		elements.add("Rafale");
		elements.add("Falcon 7x");
		elements.add("B777");
		elements.add("A380");
    }

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		 System.out.println("\n offset : " + offset);
		int currOffset = offset-1;
		String currWord = "";
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'A', 'B'};
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
