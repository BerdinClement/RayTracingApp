package lr.format.wavefront;

import lr.*;
import lr.format.FormatAbstrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * Decodes .obj files
 */
public class WavefrontFormat extends FormatAbstrait<Scene> {

    private List<Point> points;
    private List<Vecteur> normals;
    private List<WavefrontObject> objects;
    private List<Primitive> primitives;
    private Materiau currentMaterial;
    private HashMap<String, Materiau> materialLibrary;

    public WavefrontFormat() {
        super();
        this.normals = new ArrayList<>();
        this.points = new ArrayList<>();
        this.primitives = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.materialLibrary = new HashMap<>();
        this.currentMaterial = new Materiau();
        this.ajouter(
                new ObjectAnalyzer(this),
                new VertexAnalyzer(this),
                new NormalAnalyzer(this),
                new SmoothingAnalyzer(this),
                new TextureCoordinatesAnalyzer(this),
                new FaceAnalyzer(this),
                new MaterialLibAnalyzer(this),
                new UseMaterialAnalyzer(this)
        );
    }

    public void add(WavefrontObject object) {
        this.objects.add(object);
    }

    public void add(Point point) {
        this.points.add(new Point(point));
        this.currentObject().ifPresent((o)-> o.addNormal(this.points.size() - 1));
    }

    public void add(Vecteur normal) {
        this.normals.add(normal);
        this.currentObject().ifPresent((o)-> o.addNormal(this.normals.size() - 1));
    }

    public void addPolygon(int[] vertices, int[] normals) {

        Vecteur[] n = new Vecteur[normals.length];
        Point[] p = new Point[vertices.length];
        System.out.println("Adding a polygon");
        System.out.println(" - " + vertices.length + " vertices");
        System.out.println(" - " + normals.length + " normals");
        if (vertices.length != normals.length) {
            System.out.println("No normals provided");
        }

        for (int i = 0 ; i < p.length; i++) {
            p[i] = this.getVertex(vertices[i]);
        }

        if (normals.length > 0) {
            for (int i = 0 ; i < p.length; i++) {
                try {
                    n[i] = this.getNormal(normals[i]);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(points.size() + " known vertices");
                    System.out.println(this.normals.size() + " known normals");
                    System.out.println("Trying to access vertex #" + normals[i]);
                    throw e;
                }
            }
        } else {
            n = new Vecteur[vertices.length];
            Vecteur v1 = new Vecteur(p[0], p[1]);
            Vecteur v2 = new Vecteur(p[1], p[2]);
            Vecteur v3 = v1.produitVectoriel(v2);
            v3.div(v3.norme());
            for (int i = 0 ; i < n.length; i++) {
                n[i] = new Vecteur(v3);
            }
        }

        //TODO: we can do better here, and take Objects into account
        System.out.println("using\n" + this.currentMaterial);
        this.primitives.add(new Polygone(p, n, new Materiau(this.currentMaterial)));
    }

    public Point getVertex(int index) {
        return new Point(this.points.get(index - 1));
    }

    public Vecteur getNormal(int index) {
        return this.normals.get(index - 1);
    }

    @Override
    public Scene charger(String nomFichier) {
        this.normals = new ArrayList<>();
        this.points = new ArrayList<>();
        this.primitives = new ArrayList<>();
        this.objects = new ArrayList<>();

        return super.charger(nomFichier);
    }

    @Override
    protected Scene generate() {
        Scene scene = new Scene();
        this.primitives.forEach(scene::ajouter);
        return scene;
    }

    private Optional<WavefrontObject> currentObject() {
        if (this.objects.size() > 0)
            return Optional.of(this.objects.get(this.objects.size() - 1));
        else return Optional.empty();
    }

    public void pickMaterial(String name) {
        this.currentMaterial = this.materialLibrary.get(name);
    }

    public void appendLibrary(HashMap<String, Materiau> library) {
        library.forEach(this.materialLibrary::put);
    }
}
