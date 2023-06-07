package lr.format;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * Interface commune des Analyseur syntaxiques. Chaque Analyseur prend en charge
 * le décodage d'un aspect particulier d'une instance à reconstruire à partir de
 * sa description contenue dans un fichier.
 */
public interface Analyseur {
    /**
     * nom du mot-clé que cet Analyseur est en mesure de décoder.
     * @return nom du mot clé
     */
    public String getNom();

    /**
     * analyse une section d'un fichier décrivant une instance d'objet à reconstituer
     * @param tokenizer StreamTokenizer parcourant le fichier
     * @throws IOException
     */
    public void analyser(StreamTokenizer tokenizer) throws IOException;
}
