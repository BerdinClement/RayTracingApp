package lr.format.wavefront;

import lr.format.Analyseur;

public abstract class AbstractWavefrontAnalyzer implements Analyseur {
    protected WavefrontFormat format;

    public AbstractWavefrontAnalyzer(WavefrontFormat format) {
        this.format = format;
    }
}
