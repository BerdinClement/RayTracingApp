package lr.format.wavefront;

import lr.Vecteur;

import java.io.IOException;
import java.io.StreamTokenizer;

public class NormalAnalyzer extends AbstractWavefrontAnalyzer {
    public NormalAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "vn";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.eolIsSignificant(false);

        tokenizer.nextToken();
        float x = (float) tokenizer.nval;
        tokenizer.nextToken();
        float y = (float) tokenizer.nval;
        tokenizer.nextToken();
        float z = (float) tokenizer.nval;

        this.format.add(new Vecteur(x, y, z));
        System.out.println("Vertex Normal described as " + new Vecteur(x, y, z));
    }
}
