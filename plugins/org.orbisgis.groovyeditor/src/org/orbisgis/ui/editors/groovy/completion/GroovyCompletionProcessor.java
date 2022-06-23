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
package org.orbisgis.ui.editors.groovy.completion;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prominic.groovyls.GroovyServices;
import net.prominic.groovyls.config.CompilationUnitFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.GroovyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.eclipse.lsp4j.CompletionItemKind.Method;
import static org.eclipse.lsp4j.CompletionItemKind.Variable;

/**
 * Methods to create the list of the suggested autocompletion words.
 *
 * @author Adrien Bessy, CNRS
 */
public class GroovyCompletionProcessor implements IContentAssistProcessor {
	
	private static final Logger LOGGER = new Logger(GroovyCompletionProcessor.class);
	
	private static final String GROOVY_LANGUAGE = "groovy";
	private static final int GROOVY_VERSION = 3;

	private GroovyServices services;
	private String scriptUri;

	public GroovyCompletionProcessor(){
		//Avoid waiting the completion service instantiation
		Display.getDefault().asyncExec(() -> {
					Path workspaceRoot = GroovyUtils.getGroovyFolder().getFullPath().toFile().toPath();
					services = new GroovyServices(new CompilationUnitFactory());
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

					//Load the jars in ./groovy if exists
					JsonObject settings	 = buildGroovySettings();
					if(settings!=null) {
						DidChangeConfigurationParams dcp = new DidChangeConfigurationParams();
						dcp.setSettings(settings);
						services.didChangeConfiguration(dcp);
					}
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					String name = activePage.getActiveEditor().getEditorInput().getName();
					Path filePath = workspaceRoot.resolve(name);
					scriptUri = filePath.toUri().toString();
				}
		);
	}

	/**
	 * Build a json array to set a list of jar available in "user.home"./groovy folder
	 * Those jars are set to the completion manager
	 * @return
	 */
	private JsonObject buildGroovySettings() {
		File grabFolder = new File(System.getProperty("user.home") + File.separator + "/.groovy");

		if(grabFolder.exists()&& grabFolder.isDirectory()){
			try (Stream<Path> walk = Files.walk(grabFolder.toPath())) {
				JsonArray jars = new JsonArray();
				walk
						.filter(p -> !Files.isDirectory(p))   // not a directory
						.map(p -> p.toString().toLowerCase()) // convert path to string
						.filter(f -> f.endsWith("jar"))       // check end with
						.forEach(it -> jars.add(it));        // collect all matched to a List

				JsonObject classpath = new JsonObject();
				classpath.add("classpath", jars);

				JsonObject groovy = new JsonObject();
				groovy.add("groovy", classpath);

				JsonObject settings = new JsonObject();
				settings.add("settings", groovy);

				return settings;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static Position getPosition(ITextViewer viewer, int offset){
		String text = viewer.getDocument().get();
		//Get caret line/col position from text and offset
		String subText = text.substring(0, offset);
		int line = 0;
		int col = 0;
		for(int i = 0; i<offset; i++) {
			if(subText.charAt(i) == '\n') {
				col=0;
				line++;
			}
			col++;
		}
		col--;
		return new Position(line, col);
	}

	/**
	* Build the list of the completion proposals provided from the Groovy Langage Server project according to the word type.
    * @param viewer the editor view
    * @param offset the cursor position in the document
    * @return the list of the completion proposals
    */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		if (offset == 0) {
			return null;
		}
		IDocument document = viewer.getDocument();
		int currOffset = offset-1;
		//If no text, skip completion
		try {
			while (currOffset >= 0 && !Character.isWhitespace(document.getChar(currOffset))) {
				currOffset--;
			}
		} catch (BadLocationException e1) {
			LOGGER.error("Error on getting the access to a position in a document.", e1);
		}

		//Notification to service : open document
		TextDocumentItem textDocumentItem = new TextDocumentItem(scriptUri, GROOVY_LANGUAGE, GROOVY_VERSION, document.get());
		services.didOpen(new DidOpenTextDocumentParams(textDocumentItem));
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier(scriptUri);

		Position position = getPosition(viewer, offset);

		Either<List<CompletionItem>, CompletionList> result = null;
		try {
			result = services.completion(new CompletionParams(textDocument, position)).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error on getting the completion result.", e);
		}

		//If no completion available, return null
		if (result == null || result.getLeft().isEmpty()) {
			return null;
		}
		return buildProposals(result.getLeft(), offset, document.get());
	}

	private static ICompletionProposal[] buildProposals(List<CompletionItem> items, int offset, String text){
		List<ICompletionProposal> proposals = new ArrayList<>();
		for (CompletionItem item : items) {
			if (item.getKind() != Method && item.getKind() != Variable) {
				continue;
			}

			String replaceStr;
			int[] bounds;
			switch (item.getKind()) {
				case Method:
					replaceStr = item.getLabel();
					bounds = getWordBoundary(offset, text, List.of('('), List.of('.'));
					if (bounds != null) {
						int cursorPos;
						if (bounds[1] == text.length() || text.charAt(bounds[1]) != '(') {
							replaceStr += "()";
							cursorPos = replaceStr.length() - 1;
						} else {
							cursorPos = replaceStr.length() + 1;
						}
						int replaceOff = bounds[0];
						int replaceLength = bounds[1] - bounds[0];
						proposals.add(new ComplProposal(offset - replaceOff, replaceOff, replaceLength, replaceStr,
								cursorPos, item.getLabel(), item.getKind()));
					}
					break;
				case Variable:
					replaceStr = item.getLabel();
					bounds = getWordBoundary(offset, text, List.of('.'), null);
					if (bounds != null) {
						int replaceOff = bounds[0];
						int replaceLength = bounds[1] - bounds[0];
						int cursorPos = replaceStr.length();
						proposals.add(new ComplProposal(offset - replaceOff, replaceOff, replaceLength, replaceStr,
								cursorPos, item.getLabel(), item.getKind()));
					}
					break;
			}
		}
		return proposals.toArray(new ICompletionProposal[0]);
	}

	private static int[] getWordBoundary(int offset, String text, List<Character> forbiddenStart, List<Character> forbiddenEnd) {
		int start = 0;
		for(int j=offset-1; j>=0; j--){
			char c = text.charAt(j);
			if(forbiddenStart != null && forbiddenStart.contains(c)){
				return null;
			}
			if(!(c>='a' && c<='z' || c=='_' || c>='A' && c<='Z'|| c>='0' && c<='9')){
				start = j+1;
				break;
			}
		}
		int end = text.length();
		for(int j=offset; j<text.length(); j++){
			char c = text.charAt(j);
			if(forbiddenEnd != null && forbiddenEnd.contains(c)){
				return null;
			}
			if(!(c>='a' && c<='z' || c=='_' || c>='A' && c<='Z'|| c>='0' && c<='9')){
				end = j;
				break;
			}
		}
		return new int[]{start, end};
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
