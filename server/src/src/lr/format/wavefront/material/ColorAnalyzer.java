package lr.format.wavefront.material;

import lr.Point;

import java.io.IOException;
import java.io.StreamTokenizer;

public class ColorAnalyzer extends AbstractMaterialAnalyzer {

    private final String name;

    public ColorAnalyzer(String name, MaterialFormat format) {
        super(format);
        this.name = name;
    }

    @Override
    public String getNom() {
        return this.name;
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        float[] color = new float[3];
        for (int i = 0 ; i < 3 ; i++) {
            tokenizer.nextToken();
            color[i] = (float)tokenizer.nval;
        }
        this.format.setColor(this.name, color);
        System.out.println(this.name + " = " + new Point(color[0], color[1], color[2]));
    }
}
