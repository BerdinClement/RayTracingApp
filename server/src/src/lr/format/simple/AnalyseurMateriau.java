package lr.format.simple;

import lr.Materiau;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * Analyseur décodant les caractéristiques d'un Materiau
 */
public class AnalyseurMateriau extends AnalyseurAbstrait {

    /**
     *
     * @param format instance de FormatSimple chargée du décodage
     * @see Materiau
     */
    public AnalyseurMateriau(FormatSimple format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "materiau";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        float[] ambiant = new float[3];
        float[] diffus = new float[3];
        float[] speculaire = new float[3];
        float coeff;

        tokenizer.nextToken();
        ambiant[0] = (float) tokenizer.nval;
        tokenizer.nextToken();
        ambiant[1] = (float) tokenizer.nval;
        tokenizer.nextToken();
        ambiant[2] = (float) tokenizer.nval;

        tokenizer.nextToken();
        diffus[0] = (float) tokenizer.nval;
        tokenizer.nextToken();
        diffus[1] = (float) tokenizer.nval;
        tokenizer.nextToken();
        diffus[2] = (float) tokenizer.nval;

        tokenizer.nextToken();
        speculaire[0] = (float) tokenizer.nval;
        tokenizer.nextToken();
        speculaire[1] = (float) tokenizer.nval;
        tokenizer.nextToken();
        speculaire[2] = (float) tokenizer.nval;

        tokenizer.nextToken();
        coeff = (float) tokenizer.nval;

        format.setMateriau(new Materiau(ambiant, diffus, speculaire, coeff));
    }
}
