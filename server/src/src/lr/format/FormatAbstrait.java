package lr.format;

import lr.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Classe représentant un format de fichier décrivant une instance d'une classe générique T.
 * Un Format est doté de plusieurs Analyseur, chacun prenant en charge le décodage
 * d'un aspect particulier de l'instance de T à générer lors du chargement d'un fichier
 * via la méthode charger
 * @param <T>
 */
public abstract class FormatAbstrait<T> implements Format<T> {

    protected final HashMap<String, Analyseur> listeAnalyseurs;
    protected boolean utilise = false;

    /**
     * Exception représentant qu'un format a déjà été utilisé pour décoder un fichier
     */
    public static final class IllegalFormatStateException extends RuntimeException {
        public IllegalFormatStateException() {
            super("Ce format a déjà servi à décoder un fichier : il ne peut plus être utilisé pour en décoder un autre");
        }
    }

    public FormatAbstrait() {
        this.listeAnalyseurs = new HashMap<>();
    }

    /**
     * Ajoute des Analyseur au format courant
     * @param analyseurs liste des Analyseur à ajouter
     */
    public void ajouter(Analyseur... analyseurs) {
        Arrays.stream(analyseurs).forEach(analyseur -> this.listeAnalyseurs.put(analyseur.getNom(), analyseur));
    }

    /**
     * Lance l'analyse d'un fichier et produit l'instance de T décrite dans ce fichier en retour. Cette
     * méthode d'aiguiller les différentes sections du fichier à analyser vers l'Analyseur concerné;
     * Une fois le fichier décodé, c'est la méthode generate() qui prend le relais pour préparer l'instance
     * de T à retourner.
     * @param nomFichier chemin vers le ficher à analyser
     * @return objet décrit dans le fichier.
     */
    public T charger(String nomFichier) {
        if (this.utilise) throw new IllegalFormatStateException();

        this.utilise = true;
        int numLigne = 0, res;
        Materiau mat = new Materiau();

        try { // lecture des entrées du fichier
            StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(new FileReader(nomFichier)));
            tokenizer.slashSlashComments(true);
            tokenizer.commentChar('#');

            while ((res = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
                // chaque ligne doit commencer par un mot clé
                numLigne++;
                if (res != StreamTokenizer.TT_WORD) {// on passe la ligne
                    do
                        res = tokenizer.nextToken();
                    while ((res != StreamTokenizer.TT_EOF) && (res != StreamTokenizer.TT_EOL));
                    System.out.println("erreur sur ligne " + numLigne);
                }

                // nom de la directive en début de ligne
                String motCle = tokenizer.sval;
                // interprétation de la ligne par l'analyseur correspondant au nom du mot clé
                System.out.println("Parsing " + tokenizer.sval + " Identifier " + tokenizer);
                try {
                    this.listeAnalyseurs.get(motCle).analyser(tokenizer);
                } catch (NullPointerException e) {
                    System.out.println("Unexpected token " + motCle);
                    this.listeAnalyseurs.keySet().forEach(k -> System.out.print(" " + k));
                    System.out.println();
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe + " fichier " + nomFichier);
        }

        return generate();
    }

    /**
     *  Génère une instance de T à partir des informations recueillis par les Analyseur.
     * @return instance de T générée
     */
    protected abstract T generate();
}
