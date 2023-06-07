package lr.format.wavefront;

import java.util.ArrayList;
import java.util.List;

public class WavefrontObject {
    private List<Integer> points;
    private List<Integer> normals;
    private List<int[]> polygons;
    private String name;

    public WavefrontObject(String name) {
        this.name = name;
        this.points = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public int getPoint(int index) {
        return this.points.get(index);
    }

    public int getNormal(int index) {
        return this.normals.get(index);
    }

    public int[] getPolygon(int index) {
        return this.polygons.get(index);
    }

    public void addPoint(int index) {
        this.points.add(index);
    }

    public void addNormal(int index) {
        this.normals.add(index);
    }

    public void addPolygon(int[] polygon) {
        this.polygons.add(polygon);
    }
}
