package lr;

import java.awt.Color;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Permet de stocker et de manipuler une image (sous forme d'une grille de
 * pixels).
 */
public class Image {

    // attributs privés
    private int xsize;
    private int ysize;
    private Color[][] pixels;

    /**
     * Construit une image
     * 
     * @param largeur la résolution horizontale de l'image
     * @param hauteur la résolution verticale de l'image
     */
    public Image(int largeur, int hauteur) {
        xsize = largeur;
        ysize = hauteur;
        pixels = new Color[ysize][xsize];
    }

    // méthodes d'instance

    /**
     * fournit la résolution horizontale de l'image c'est à dire la largeur en
     * nombre de pixels
     * 
     * @return la résolution horizontale
     */
    public int getWidth() {
        return xsize;
    }

    /**
     * fournit la résolution verticale de l'image c'est à dire la hauteur en nombre
     * de pixels
     * 
     * @return la résolution verticale
     */
    public int getHeight() {
        return ysize;
    }

    /**
     * attribue une couleur à un pixel
     *
     * Fixe la couleur du pixel de coordonnées (x,y) avec la couleur contenue dans
     * le paramètre couleur
     * 
     * @param x       abscisse du pixel
     * @param y       ordonnée du pixel
     * @param couleur la couleur à attribuer
     */
    public void setPixel(int x, int y, Color couleur) {
        pixels[y][x] = new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue());
    }

    /**
     * sauvegarde de l'image
     *
     * @param nom nom du fichier de sauvegarde L'image est sauvegardée au format
     *            PPM. Si le nom de fichier ne contient pas l'extension PPM,
     *            celle-ci est rajoutée.
     */
    public void save(String nom) {
        // test de l'extension de fichier
        if (!nom.endsWith(".ppm"))
            nom = nom.concat(".ppm");

        try {

            PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter(nom), 100 * 1024));

            f.println("P3");
            f.println("# CREATOR : LR java");
            f.println(xsize + " " + ysize);
            f.println("255");

            StringBuilder s = new StringBuilder(xsize * 4 * 3);
            for (int y = 0; y < ysize; y++) {
                for (int x = 0; x < xsize; x++)
                    s.append(
                            pixels[y][x].getRed() + " " + pixels[y][x].getGreen() + " " + pixels[y][x].getBlue() + " ");
                f.println(s);
                s.delete(0, s.length());
            }

            f.close();

        } catch (IOException ioe) {
            System.out.println(ioe + " fichier " + nom);
        }
    }

    /**
     * Permet de sauver l'image sous le nom et le format spécifié.
     * 
     * @param nom    Nom du fichier sur le disque.
     * @param format Chaîne de caractères qui représente le format de sauvegarde.
     *               L'extention est automatiquement ajoutée à la fin du fichier.
     *               Les noms de format valides sont "jpeg", "bmp", "png" ...
     */
    public void save(String nom, String format) {
        // test le nom du fichier (et ajoute l'extention si nécessaire)

        if (!nom.endsWith("." + format))
            nom = nom.concat("." + format);

        // vérifier qu'on peut écrire le format demandé
        if (ImageIO.getImageWritersBySuffix(format).hasNext()) {

            // créer une instance de BufferedImage
            BufferedImage bi = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_RGB);

            // écrire dedans
            for (int y = 0; y < ysize; y++) {
                for (int x = 0; x < xsize; x++) {
                    // écriture d'un pixel
                    bi.setRGB(x, y, 65536 * pixels[y][x].getRed() + 256 * pixels[y][x].getGreen() + pixels[y][x].getBlue());
                }
            }

            // création du fichier
            File f = new File(nom);

            try {
                ImageIO.write(bi, format, f);
            } catch (IOException ioe) {
                System.out.println("erreur d'écriture sur fichier :" + nom + "\n" + ioe);
            }

            System.out.println("fichier sauvegardé dans " + f.getAbsolutePath());

        } else {
            // indiquer que le fichier n'est pas écrit
            System.out.println("Format non reconnu : l'image n'est pas sauvegardée");

            // indiquer les types de fichiers qui peuvent être écrits sur le système
            String[] formatsEcriture = ImageIO.getWriterFormatNames();
            System.out.println("Formats d'images disponibles en écriture : ");
            for (int i = 0; i < formatsEcriture.length; i++) {
                System.out.println(formatsEcriture[i]);
            }
        }
    }
}
