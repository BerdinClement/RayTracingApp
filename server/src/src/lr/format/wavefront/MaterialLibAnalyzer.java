package lr.format.wavefront;

import lr.Materiau;
import lr.format.wavefront.material.MaterialFormat;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;

public class MaterialLibAnalyzer extends AbstractWavefrontAnalyzer {

    public MaterialLibAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "mtllib";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.eolIsSignificant(false);
        tokenizer.wordChars('.', '.');
        tokenizer.wordChars('_', '_');
        tokenizer.nextToken();

        //TODO: actually do something here
        String filename = tokenizer.sval;
        System.out.println("Material Library from file " + filename);
        HashMap<String, Materiau> library = new MaterialFormat().charger(filename);
        library.forEach((s, m) -> System.out.println(
                "Materiau " + s + "\n" + m
        ));
        this.format.appendLibrary(library);
    }
}
