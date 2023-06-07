package lr.format.wavefront;

import java.io.IOException;
import java.io.StreamTokenizer;

public class ObjectAnalyzer extends AbstractWavefrontAnalyzer {

    public ObjectAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "o";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.eolIsSignificant(false);

        tokenizer.nextToken();
        String name = tokenizer.sval;
        format.add(new WavefrontObject(name));
        System.out.println("Object named " + name);
    }
}
