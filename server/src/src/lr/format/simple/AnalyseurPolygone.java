package lr.format.simple;

import lr.Point;
import lr.Polygone;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * Analyseur décodant les caractéristiques d'un Polygone
 * @see Polygone
 */
public class AnalyseurPolygone extends AnalyseurAbstrait {

    /**
     *
     * @param format instance de FormatSimple chargée du décodage
     */
    public AnalyseurPolygone(FormatSimple format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "polygone";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        float x, y, z;
        int nbsom;
        Point[] tabsom;

        tokenizer.nextToken();
        nbsom = (int) tokenizer.nval;
        tabsom = new Point[nbsom + 1];
        for (int s = 0 ; s < nbsom ; s++) {
            tokenizer.nextToken();
            x = (float) tokenizer.nval;
            tokenizer.nextToken();
            y = (float) tokenizer.nval;
            tokenizer.nextToken();
            z = (float) tokenizer.nval;
            tabsom[s] = new Point(x, y, z);
        }
        tabsom[nbsom] = new Point(tabsom[0]);
        Polygone p = new Polygone(tabsom, format.getMateriau());
        format.add(p);
    }
}
