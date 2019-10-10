package org.orbisgis.ui.editors.groovy.syntax;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.jkiss.dbeaver.ui.editors.sql.syntax.rules.LineCommentRule;

import java.util.Arrays;
import java.util.List;

public class GroovySourceViewerConfiguration extends SourceViewerConfiguration {
    public ITokenScanner tokenScanner;

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
        List<String> list = Arrays.asList(("abstract|as#assert#boolean#break#byte#case#catch#char#class#" +
                "const#continue#def#default#do#double#else#enum#extends#false#final#finally#float#for#goto#if#" +
                "implements#import#in#instanceof#interface#long#native#new#null#package#private#protected#public#" +
                "return#super#switch#synchronized#this#threadsafe#throw#throws#trait#transient#true#try#void#" +
                "volatile#while").split("#"));

        Color darkYellow = new Color(Display.getDefault(), 103, 103, 0);
        Color green = new Color(Display.getDefault(), 0, 138, 0);
        Color orange = new Color(Display.getDefault(), 138, 69, 0);
        Color blue = new Color(Display.getDefault(), 0, 103, 138);
        Color gray = new Color(Display.getDefault(), 169, 169, 169);

        IToken annotationToken = new Token(new TextAttribute(darkYellow));
        IToken groovyDocToken = new Token(new TextAttribute(green));
        IToken stringToken = new Token(new TextAttribute(green));
        IToken keywordToken = new Token(new TextAttribute(orange));
        IToken numberToken = new Token(new TextAttribute(blue));
        IToken commentToken = new Token(new TextAttribute(gray));

        IRule commentLineRule = new LineCommentRule("//", commentToken);
        IRule commentBlockRule = new MultiLineRule("/*", "*/", commentToken);
        IRule groovyDocBlockRule = new MultiLineRule("/**", "*/", groovyDocToken);
        IRule keywordRule = new KeywordRule(list, keywordToken);
        IRule multiLineDoubleQuoteStringRule = new MultiLineRule("\"", "\"", stringToken, '\\');
        IRule multiLineSingleQuoteStringRule = new MultiLineRule("'", "'", stringToken, '\u0000');
        IRule annotationRule = new LineCommentRule("@", annotationToken);
        IRule numberRule = new NumberRule(numberToken);

        return new IRule[] {commentLineRule, commentBlockRule, groovyDocBlockRule, keywordRule,
                multiLineDoubleQuoteStringRule, multiLineSingleQuoteStringRule, annotationRule, numberRule};
    }

    class KeywordRule implements IRule{

        private IToken token;
        private List<String> keywords;

        public KeywordRule(List<String> keywords, IToken token){
            this.keywords = keywords;
            this.token = token;
        }

        @Override
        public IToken evaluate(ICharacterScanner iCharacterScanner) {
            StringBuilder buffer = new StringBuilder();
            int i;
            while(Character.isLetter(i = iCharacterScanner.read())){
                buffer.append((char)i);
            }
            iCharacterScanner.unread();
            if(keywords.contains(buffer.toString())){
                return token;
            }
            else {
                for(int j=0; j<buffer.length(); j++){
                    iCharacterScanner.unread();
                }
                return Token.UNDEFINED;
            }
        }
    }
}
