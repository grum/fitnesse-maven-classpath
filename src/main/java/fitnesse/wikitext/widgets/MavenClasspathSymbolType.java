package fitnesse.wikitext.widgets;

import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.parser.*;
import util.Maybe;

import java.io.File;
import java.util.List;

/**
 * FitNesse SymbolType implementation which enables Maven classpath integration for FitNesse.
 */
public class MavenClasspathSymbolType extends SymbolType implements Rule, Translation {

    private MavenClasspathExtractor mavenClasspathExtractor;

    private String pomFile;

    public MavenClasspathSymbolType() {
        super("MavenClasspathSymbolType");
        this.mavenClasspathExtractor = new MavenClasspathExtractor();

        wikiMatcher(new Matcher().startLineOrCell().string("!pomFile"));

        wikiRule(this);
        htmlTranslation(this);
    }

    @Override
    public String toTarget(Translator translator, Symbol symbol) {
        pomFile = symbol.childAt(0).getContent();

        List<String> classpathElements = mavenClasspathExtractor.extractClasspathEntries(new File(pomFile));

        String classpathForRender = "";
        for (String element : classpathElements) {
            classpathForRender += HtmlUtil.metaText("classpath: " + element) + HtmlUtil.BRtag;

        }
        return classpathForRender;

    }

    @Override
    public Maybe<Symbol> parse(Symbol symbol, Parser parser) {
        Symbol next = parser.moveNext(1);

        if (!next.isType(SymbolType.Whitespace)) return Symbol.nothing;

        symbol.add(parser.moveNext(1).getContent());


        return new Maybe<Symbol>(symbol);
    }


    /**
     * Exposed for testing
     */
    protected void setMavenClasspathExtractor(MavenClasspathExtractor mavenClasspathExtractor) {
        this.mavenClasspathExtractor = mavenClasspathExtractor;
    }
}


