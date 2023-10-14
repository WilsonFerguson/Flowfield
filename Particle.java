import java.util.ArrayList;

import library.core.*;

class Particle extends PComponent implements EventIgnorer {
    PVector lastPos, pos, vel, acc;
    float accelerationStrength = 2;
    color col;

    public Particle() {
        pos = PVector.randomPosition();
        lastPos = pos.copy();
        vel = PVector.zero();
        acc = PVector.zero();

        float index = pos.x + pos.y * width;
        float hue = index / (width * height);
        col = color.fromHSB(hue, 0.5, 0.5);
    }

    public void update(float[][] flowfield) {
        int x = constrain((int) pos.x, 0, flowfield.length - 1);
        int y = min(max((int) pos.y, 0), flowfield[0].length - 1);
        float value = flowfield[x][y];

        // Random turning
        value += random(-Settings.randomTurning, Settings.randomTurning);
        PVector angle = PVector.fromAngle(value);
        acc.add(angle.mult(accelerationStrength));

        vel.mult(0);
        vel.add(acc);

        lastPos = pos.copy();
        pos.add(vel);
        if (pos.x < 0) {
            pos.x = width;
            lastPos.x = width;
        } else if (pos.x > width) {
            pos.x = 0;
            lastPos.x = 0;
        }
        if (pos.y < 0) {
            pos.y = height;
            lastPos.y = height;
        } else if (pos.y > height) {
            pos.y = 0;
            lastPos.y = 0;
        }

        acc.mult(0);
    }

    public void show() {
        stroke(col, 10);
        strokeWeight(1.5);
        line(lastPos, pos);
    }
}
