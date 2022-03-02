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

import net.prominic.groovyls.GroovyServices;
import net.prominic.groovyls.config.CompilationUnitFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.core.logger.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Methods to create the list of the suggested autocompletion words.
 *
 * @author Adrien Bessy, CNRS
 */
public class GroovyCompletionProcessor implements IContentAssistProcessor {
	
	private static final Logger LOGGER = new Logger(GroovyCompletionProcessor.class);
	
	private static final String LANGUAGE_GROOVY = "groovy";

	/**
	* Build the list of the completion proposals provided from the Groovy Langage Server project according to the word type.
    * @param viewer the editor view
    * @param offset the cursor position in the document
    * @return the list of the completion proposals
    */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		int currOffset = offset-1;
		String currWord = "";
		if (currOffset >= 0) {
			char currChar;
			/*
			 * Find the start position of the word where we trigger the completion.
			 * We move from offset to offset until we meet a space or \n character.
			 * Each character is added at the start of the current word.
			 */
			try {
				while (currOffset >= 0 && !Character.isWhitespace(currChar = document.getChar(currOffset))) {
					currWord = currChar + currWord;
					currOffset--;
				}
			} catch (BadLocationException e1) {
				LOGGER.error("Error on getting the access to a position in a document.", e1);
			}
			Path workspaceRoot = Paths.get(System.getProperty("user.home")).resolve(".local/share/DBeaverData/");
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

			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			String name = activePage.getActiveEditor().getEditorInput().getName();
			Path filePath = workspaceRoot.resolve(name);
			String uri = filePath.toUri().toString();

			TextDocumentItem textDocumentItem = new TextDocumentItem(uri, LANGUAGE_GROOVY, 3, document.get());
			services.didOpen(new DidOpenTextDocumentParams(textDocumentItem));
			TextDocumentIdentifier textDocument = new TextDocumentIdentifier(uri);

			String substring = document.get().substring(0, offset);
			long line = substring.chars().filter(ch -> ch == '\n').count();
			int column;

			if (line == 0) {
				column = offset;
			} else {
				String[] allLine = substring.split("\n");
				int length = allLine.length;
				String lastLine = allLine[length - 1];
				column = lastLine.length();
			}

			Position position = new Position((int) line, column);
			Either<List<CompletionItem>, CompletionList> result = null;
			SignatureHelp signatureHelp = null;
			try {
				result = services.completion(new CompletionParams(textDocument, position)).get();
				signatureHelp = services.signatureHelp(new SignatureHelpParams(textDocument, position)).get();
			} catch (InterruptedException | ExecutionException e) {
				LOGGER.error("Error on getting the completion service result.", e);
			}

			List<SignatureInformation> signatures = null;
			if (signatureHelp != null) {
				signatures = signatureHelp.getSignatures();
			}
			String parameters = "";
			if (signatures != null) {
				if (signatures.size() != 0) {
					SignatureInformation signature = signatures.get(0);
					if (signature.getParameters().size() != 0) {
						List<ParameterInformation> params = signature.getParameters();

						for (int i = 0; i < signature.getParameters().size(); i++) {
							if (!parameters.equals("")) {
								parameters = parameters + ", " + params.get(i).getLabel().get();
							} else {
								parameters = parameters + params.get(i).getLabel().get();
							}
						}
					}
				}
			}

			List<CompletionItem> items = null;
			if (result != null) {
				items = result.getLeft();
			}
			List<String> orderedLabelList = new ArrayList<>();
			if (items != null) {
				for (CompletionItem proposal : items) {
					orderedLabelList.add(proposal.getLabel());
				}
				orderedLabelList.sort(String::compareToIgnoreCase);
			}

			ICompletionProposal[] proposals = null;
			if (items != null) {
				if (items.size() > 0) {
					proposals = buildProposals(orderedLabelList, currWord, offset - currWord.length(), parameters);
				}
			}

			return proposals;

		} else {
			// no text in the document, so no available completion!
		    return null;
		}
	}
	
	 /**
	 * Build the list of autocompletion elements from available elements.
     * @param orderedLabelList the list of available labels
     * @param replacedWord the word to replace in the editor
     * @param offset the cursor position in the document
     * @return the list of the suggested autocompletion words
     */
    private ICompletionProposal[] buildProposals(List<String> orderedLabelList, String replacedWord, int offset, String parameters) {
		int index = 0;

		ICompletionProposal[] proposals;

		// TO COMPLETE METHOD PARAMETERS
		if(!parameters.equals("") && replacedWord.contains("(")){ // if There is at least one parameter
			proposals = new ICompletionProposal[1];
			proposals[index] = new CompletionProposal(replacedWord + parameters , offset,
					replacedWord.length(), replacedWord.length() + 1,
					null, parameters,
					null, null);
		}
		else if (parameters.equals("") && replacedWord.endsWith("(")){ // if There is no parameter
			proposals = new ICompletionProposal[1];
			proposals[index] = new CompletionProposal(replacedWord , offset,
					replacedWord.length(), replacedWord.length(),
					null, "No parameter",
					null, null);
		}
		else { // TO COMPLETE VARIABLES AND METHODS
			String stringBeforePoint;
			String replacementString;
			int cursorPosition;
			proposals = new ICompletionProposal[orderedLabelList.size()];
			// Create proposals from model elements.
			for (String label : orderedLabelList) {
				if(!replacedWord.contains(".")){ // if the word doesn't contain a dot (to complete variables)
					stringBeforePoint = "";
					replacementString = stringBeforePoint + label;
					cursorPosition = label.length();
				}
				else{ // if the word contains a dot (to complete methods)
					stringBeforePoint = replacedWord.split("\\.")[0] + "."; // not keep what is after a dot,
					// for example for the completion of "elements.ad", we keep "element." to give "elements.add" and not "elements.adadd"
					replacementString = stringBeforePoint + label + "()";
					cursorPosition = stringBeforePoint.length() + label.length() + 1;
				}
				IContextInformation contextInfo = new ContextInformation(null, stringBeforePoint + label);
				proposals[index] = new CompletionProposal(replacementString, offset,
						replacedWord.length(), cursorPosition,
						null, label,
						contextInfo, null);
				index++;
			}
		}
		return proposals;
    }

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
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
