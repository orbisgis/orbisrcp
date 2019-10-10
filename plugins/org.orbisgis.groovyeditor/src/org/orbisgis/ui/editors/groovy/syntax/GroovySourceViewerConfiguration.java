package org.orbisgis.ui.editors.groovy.syntax;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class GroovySourceViewerConfiguration extends SourceViewerConfiguration {
    public ITokenScanner tokenScanner;
    public IRule patternRule;
    public IRule endOfLineRule;

    public GroovySourceViewerConfiguration(){
        tokenScanner = createTokenScanner();
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
        PresentationReconciler reconciler= new PresentationReconciler();
        DefaultDamagerRepairer defDamagerRepairer= new DefaultDamagerRepairer(tokenScanner);
        reconciler.setDamager(defDamagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(defDamagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    private ITokenScanner createTokenScanner() {
        RuleBasedScanner scanner= new RuleBasedScanner();
        scanner.setRules(createRules());
        return scanner;
    }
    private IRule[] createRules() {
        Display display = Display.getCurrent();
        Color darkGreen = display.getSystemColor(SWT.COLOR_DARK_GREEN);

        IToken lineCommentToken= new Token(new TextAttribute(darkGreen));
        //IToken tokenB= new Token(new TextAttribute(blue));
        //patternRule = new PatternRule("<", ">", tokenA, '\\', false);
        endOfLineRule = new EndOfLineRule("++ ", lineCommentToken);
        return new IRule[] {endOfLineRule};
    }
}
