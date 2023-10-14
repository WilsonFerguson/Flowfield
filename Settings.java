import library.core.*;

class Settings implements PConstants {

    public static final boolean useWarpedNoise = false;

    public static final int numParticles = 30000;
    public static final float zOffsetIncrease = 0;
    public static final float backgroundAlpha = 0;
    public static final float randomTurning = 0;
    /**
     * What to round each value in the flow field to. 0 means no rounding. An
     * example value is be PI / 4.
     */
    public static final float flowFieldRounding = 0;

    public static final color backgroundColor = new color(0);

    // Known cool combos (warped, num, zoffset, alpha, turning, rounding):
    // false, 1000, 0.01f, 10, 0, 0
    // false, 10000, 0, 5, 0, 0
    // false, 10000, 0, 35, 0, 0
    // false, 1000, 0, 5, 2, 0
    // Solid, epic looking one with bg = color(0): false, 10000, 0, 0, 0, 0
    // Solid (bg = black): true, 10000, 0, 0, 0, 0
    // Cool looking, computer like bg = black: false, 1000, 0, 5, 0, PI / 4
    // Weird (from high turning value): false, 1000, 0, 5, 5, 0
    // Weird: true, 1000, 0.01f, 0, 0, 0
}
