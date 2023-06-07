package lr.format.wavefront.material;

import lr.Materiau;
import lr.format.FormatAbstrait;

import java.util.HashMap;

public class MaterialFormat extends FormatAbstrait<HashMap<String, Materiau>> {

    private String materialName = null;
    private HashMap<String, float[]> colors;
    private HashMap<String, Float> params;
    private HashMap<String, Materiau> materialMap;

    public MaterialFormat() {
        super();
        this.ajouter(
                new MaterialDeclarationAnalyzer(this),
                new ColorAnalyzer("Ka", this),
                new ColorAnalyzer("Kd", this),
                new ColorAnalyzer("Ks", this),
                new ColorAnalyzer("Ke", this),
                new FloatAnalyzer("Ns", this),
                new FloatAnalyzer("Ni", this),
                new FloatAnalyzer("d", this),
                new FloatAnalyzer("illum", this)
        );

        this.colors = new HashMap<>();
        this.params = new HashMap<>();
        this.materialMap = new HashMap<>();

        setFloat("Ns", 0);
        setFloat("Ni", 1);
        setFloat("d", 1);
        setFloat("illum", 2);
        setColor("Ka", new float[]{.8f, .8f, .8f});
        setColor("Kd", new float[]{.8f, .8f, .8f});
        setColor("Ks", new float[]{.8f, .8f, .8f});
        setColor("Ke", new float[]{0f, 0f, 0f});
    }

    @Override
    protected HashMap<String, Materiau> generate() {
        this.pushCurrentMaterial();
        return this.materialMap;
    }

    public void pushCurrentMaterial() {
        if (this.materialName == null) return;
        this.materialMap.put(this.materialName, new Materiau(
            this.colors.get("Ka"),
            this.colors.get("Kd"),
            this.colors.get("Ks"),
            this.params.get("Ns")
        ));
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setColor(String colorName, float[] color) {
        this.colors.put(colorName, color);
    }

    public void setFloat(String paramName, float value) {
        this.params.put(paramName, value);
    }
}
