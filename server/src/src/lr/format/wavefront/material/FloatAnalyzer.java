package lr.format.wavefront.material;

import java.io.IOException;
import java.io.StreamTokenizer;

public class FloatAnalyzer extends AbstractMaterialAnalyzer{

    private final String name;

    public FloatAnalyzer(String name, MaterialFormat format) {
        super(format);
        this.name = name;
    }

    @Override
    public String getNom() {
        return this.name;
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.nextToken();
        float floatValue = (float)tokenizer.nval;
        this.format.setFloat(this.name, floatValue);
        System.out.println(this.name + " = " + floatValue);
    }
}
