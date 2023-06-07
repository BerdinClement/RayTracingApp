package lr.format.simple;

import lr.Point;
import lr.Sphere;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * Analyseur décodant les caractéristiques d'une Sphere
 * @see Sphere
 */
public class AnalyseurSphere extends AnalyseurAbstrait {

    public AnalyseurSphere(FormatSimple format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "sphere";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        float xc, yc, zc, r;
        tokenizer.nextToken();
        xc = (float) tokenizer.nval;
        tokenizer.nextToken();
        yc = (float) tokenizer.nval;
        tokenizer.nextToken();
        zc = (float) tokenizer.nval;
        tokenizer.nextToken();
        r = (float) tokenizer.nval;

        Sphere s = new Sphere(r, new Point(xc, yc, zc), format.getMateriau());
        format.add(s);
    }
}
