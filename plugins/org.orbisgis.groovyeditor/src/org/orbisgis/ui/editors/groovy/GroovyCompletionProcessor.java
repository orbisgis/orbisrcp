package org.orbisgis.ui.editors.groovy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
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

import net.prominic.groovyls.GroovyServices;
import net.prominic.groovyls.config.CompilationUnitFactory;

public class GroovyCompletionProcessor implements IContentAssistProcessor {
	
	private List<String> elements;
	private ICompletionProposal info;
	
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
		if (currOffset >= 0) {
		    try {
				char currChar;
				/*
				 * Retrouver le debut du mot sur lequel on a declenche la completion
				 * On se deplace d'offset en offset jusqu'a rencontrer un caractere 
				 * de type 'whitespace' (espace, ou retour a la ligne, ...).
				 * Chaque caractere est ajoute au debut du mot en cours de lecture
				 */
				
				while (currOffset >= 0 && !Character.isWhitespace(currChar = document.getChar(currOffset))) {
				    currWord = currChar + currWord;
				    currOffset--;
				}
				System.out.println("\n currWord : " + currWord);
				
				//IDocument document = getDocument();
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
				TextDocumentItem textDocumentItem = new TextDocumentItem(uri, LANGUAGE_GROOVY, 3, document.get());
				System.out.println("\n textDocumentItem : " + textDocumentItem);
				services.didOpen(new DidOpenTextDocumentParams(textDocumentItem));
				TextDocumentIdentifier textDocument = new TextDocumentIdentifier(uri);
				System.out.println("\n textDocument : " + textDocument);

				String substring =  document.get().substring(0,offset);
				long line = substring.chars().filter(ch -> ch == '\n').count();
				System.out.println("\n line : " + line);
				int column = 0;
				if (line == 0) {
					column = offset;
				}else {
					String[] allLine = substring.split("\n");
					int length = allLine.length;
					String lastLine = allLine[length-1];
					column = lastLine.length();
				}
				System.out.println("\n column : " + column);
				
				Position position = new Position((int) line, column);
				Either<List<CompletionItem>, CompletionList> result = null;
				try {
					result = services.completion(new CompletionParams(textDocument, position)).get();
					System.out.println("\n result : " + result);
					System.out.println("\n result.isLeft() : " + result.isLeft());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				List<CompletionItem> items = result.getLeft();
				System.out.println("\n items : " + items);
				
				ICompletionProposal[] proposals = null;
				if (items.size() > 0) {
				    proposals = buildProposals(items, currWord, offset - currWord.length());
				}
				return proposals;
		    }catch (Exception e) {
		    	return null;
		    }
		} else {
		    // Pas de texte dans le document, donc pas de completion disponible ! 
		    return null;
		}
	}
	
	 /**
     * Construit la liste des elements d'autocompletion a partir des elements 
     * disponibles.
     * @param availableElements la liste des elements disponibles
     * @param replacedWord le mot a remplacer dans l'editeur
     * @param offset la position du curseur dans le document
     * @return la liste des suggestions d'autocompletion
     */
    private ICompletionProposal[] buildProposals(List<CompletionItem> availableElements, String replacedWord, int offset) {
		ICompletionProposal[] proposals = new ICompletionProposal[availableElements.size()];
		int index = 0;
		String stringBeforePoint = replacedWord.split("\\.")[0];
		System.out.println("stringBeforePoint : " + stringBeforePoint);
		// Create proposals from model elements.
		for (CompletionItem proposal : availableElements) {
		    IContextInformation contextInfo = new ContextInformation(null, stringBeforePoint + proposal.getLabel());
		    proposals[index] = new CompletionProposal(replacedWord + proposal.getLabel(), offset,
			    replacedWord.length(), replacedWord.length() + proposal.getLabel().length(), 
			    null, proposal.getLabel(), 
			    contextInfo, proposal.getDetail());
		    index++;
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
