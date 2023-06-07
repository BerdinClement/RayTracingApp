package lr.format.wavefront;

import java.io.IOException;
import java.io.StreamTokenizer;

public class TextureCoordinatesAnalyzer extends AbstractWavefrontAnalyzer {
    public TextureCoordinatesAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "vt";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.eolIsSignificant(false);

        float u,v;

        //TODO actually do something here
        tokenizer.nextToken();
        u = (float) tokenizer.nval;
        tokenizer.nextToken();
        v = (float) tokenizer.nval;

        System.out.println("Texture coordinates = (" + u + " ; " + v + ")");
    }
}
