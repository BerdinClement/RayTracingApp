package lr.format;

/**
 * Interface commune de tous les formats pouvant générer un objet à partir d'une description dans un fichier.
 * @param <T> classe dont relève les objets générés à partir de ce format
 */
public interface Format<T> {
    T charger(String nomFichier);
}
