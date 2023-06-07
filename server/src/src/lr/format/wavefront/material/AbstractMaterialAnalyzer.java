package lr.format.wavefront.material;

import lr.format.Analyseur;

public abstract class AbstractMaterialAnalyzer implements Analyseur {
    protected MaterialFormat format;

    public AbstractMaterialAnalyzer(MaterialFormat format) {
        this.format = format;
    }
}
