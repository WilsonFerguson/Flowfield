import library.core.*;

class Sketch extends Applet {
    Particle[] particles;
    float[][] flowfield;
    int numIterations = 3;

    float zOffset = 0;

    public void setup() {
        fullScreen();
        background(Settings.backgroundColor);

        generateFlowfield();
        particles = new Particle[Settings.numParticles];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
    }

    @SuppressWarnings("all") // Remove dead code warning when not rounding
    void generateFlowfield() {
        float zoom = 0.002f;
        flowfield = new float[width][height];
        zOffset += Settings.zOffsetIncrease;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float value = 0;
                if (Settings.useWarpedNoise) {
                    value = warpedNoise(x, y) * TWO_PI;
                } else {
                    value = noise(x * zoom, y * zoom, zOffset);
                }

                value *= TWO_PI;

                if (Settings.flowFieldRounding != 0) {
                    value = round(value / (Settings.flowFieldRounding)) * (Settings.flowFieldRounding);
                }

                flowfield[x][y] = value;
            }
        }
    }

    float fbm(float x, float y, float z, int octaves, float lacunarity, float gain) {
        float sum = 0;
        float frequency = 1;
        float amplitude = 1;
        float maxValue = 0;
        for (int i = 0; i < octaves; i++) {
            sum += noise(x * frequency, y * frequency, z * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return sum / maxValue;
    }

    float warpedNoise(float x, float y) {
        // fbm(p + fbm(p + fbm(p)))
        int octaves = 3;
        float lacunarity = 2;
        float gain = 0.5f;

        x *= 0.001f;
        y *= 0.001f;
        float output1 = fbm(x, y, zOffset, octaves, lacunarity, gain);
        PVector inupt1 = new PVector(output1, output1).add(new PVector(x, y));
        float output2 = fbm(inupt1.x, inupt1.y, zOffset, octaves, lacunarity, gain);
        PVector inupt2 = new PVector(output2, output2).add(new PVector(x, y));

        return fbm(inupt2.x, inupt2.y, zOffset, octaves, lacunarity, gain);
    }

    @SuppressWarnings("all")
    public void draw() {
        if (Settings.zOffsetIncrease != 0) {
            generateFlowfield();
        }

        background(Settings.backgroundColor, Settings.backgroundAlpha);

        // for (Particle particle : particles) {
        // for (int i = 0; i < numIterations; i++) {
        // particle.update(flowfield);
        // particle.show();
        // }
        // }

        shaderArray(new Shader() {
            public void Dispatch(int id) {
                for (int i = 0; i < numIterations; i++) {
                    particles[id].update(flowfield);
                    particles[id].show();
                }
            }
        }, particles.length);

        // shaderTexture(new Shader() {
        // public void Dispatch(PVector id) {
        // // Draw flowfield
        // int x = min(width - 1, (int) id.x);
        // int y = min(height - 1, (int) id.y);
        // float value = flowfield[x][y] / TWO_PI;
        // set(x, y, color(value * 255));
        // }
        // });
    }

    public void keyPressed() {
        if (key == ' ') {
            save("screenshot.png");
        }
    }
}
