package lr.format.simple;

import lr.Materiau;
import lr.Primitive;
import lr.Scene;
import lr.Source;
import lr.format.FormatAbstrait;

import java.util.ArrayList;
import java.util.List;

/**
 * Format simple de description de Scène
 * @see Scene
 */
public class FormatSimple extends FormatAbstrait<Scene> {

    private Materiau materiau;
    private final List<Primitive> primitiveList;
    private final List<Source> sourceList;

    public FormatSimple() {
        materiau = new Materiau();
        this.primitiveList = new ArrayList<>();
        this.sourceList = new ArrayList<>();
        this.ajouter(
                new AnalyseurMateriau(this),
                new AnalyseurSource(this),
                new AnalyseurSphere(this),
                new AnalyseurPolygone(this)
        );
    }

    @Override
    protected Scene generate() {
        Scene scene = new Scene();
        primitiveList.forEach(scene::ajouter);
        sourceList.forEach(scene::ajouter);
        return scene;
    }

    protected void setMateriau(Materiau materiau) {
        this.materiau = materiau;
    }

    protected Materiau getMateriau() {
        return this.materiau;
    }

    protected void add(Primitive primitive) {
        this.primitiveList.add(primitive);
    }

    protected void add(Source source) {
        this.sourceList.add(source);
    }
}
