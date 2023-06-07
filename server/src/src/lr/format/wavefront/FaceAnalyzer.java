package lr.format.wavefront;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class FaceAnalyzer extends AbstractWavefrontAnalyzer {
    public FaceAnalyzer(WavefrontFormat format) {
        super(format);
    }

    @Override
    public String getNom() {
        return "f";
    }

    @Override
    public void analyser(StreamTokenizer tokenizer) throws IOException {
        tokenizer.wordChars('/', '/');
        tokenizer.eolIsSignificant(true);

        int type;
        List<Integer> vertices = new ArrayList<>();
        List<Integer> normals = new ArrayList<>();

        System.out.println("polygon");
        type = tokenizer.nextToken();
        do {
            if (type == StreamTokenizer.TT_NUMBER) {
                vertices.add((int) tokenizer.nval);
            } else if (type == StreamTokenizer.TT_WORD) {
                // we're in trouble here: vertex is described as n1/n2/n3
                // and tokenizer.sval = "n2/n3"
                String[] parts = tokenizer.sval.split("/");
                if (parts.length > 2) {
                    System.out.println("parsing normal from " + tokenizer.sval + " => " + parts[2]);
                    int normalIndex = Integer.parseInt(parts[2]);
                    normals.add(normalIndex);
                }
            }
            type = tokenizer.nextToken();
        } while (type != StreamTokenizer.TT_EOL && type != StreamTokenizer.TT_EOF);

        System.out.println();
        System.out.println("Finished " + tokenizer);
        int[] vertexIndices = new int[vertices.size()];
        int[] normalIndices = new int[normals.size()];
        for (int i = 0 ; i < vertexIndices.length ; i++) {
            vertexIndices[i] = vertices.get(i);
        }
        for (int i = 0 ; i < normalIndices.length ; i++) {
            normalIndices[i] = normals.get(i);
        }
        this.format.addPolygon(vertexIndices, normalIndices);
    }
}
