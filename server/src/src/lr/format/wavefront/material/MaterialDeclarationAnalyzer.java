package lr.format.wavefront.material;

import java.io.IOException;
import java.io.StreamTokenizer;

public class MaterialDeclarationAnalyzer extends AbstractMaterialAnalyzer {

    public MaterialDeclarationAnalyzer(MaterialFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "newmtl";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        this.format.pushCurrentMaterial();
        tokenizer.nextToken();
        String materialName = tokenizer.sval;
        this.format.setMaterialName(materialName);
        System.out.println("Material declaration " + materialName);
    }
}
