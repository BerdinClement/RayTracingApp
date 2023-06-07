package lr.format.wavefront;

import java.io.IOException;
import java.io.StreamTokenizer;

public class UseMaterialAnalyzer extends AbstractWavefrontAnalyzer {
    public UseMaterialAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "usemtl";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.eolIsSignificant(false);

        tokenizer.nextToken();
        String materialName = tokenizer.sval;
        System.out.println("usemtl material named = " + materialName);
        this.format.pickMaterial(materialName);
    }
}
