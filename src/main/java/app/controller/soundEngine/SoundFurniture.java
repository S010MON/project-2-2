package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface SoundFurniture {
    void draw(GraphicsContext gc);

    void setSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries);

    ArrayList<SoundBoundary> getSoundBoundaries();

    boolean isBlocked(SoundRay soundRay);

    boolean hitsCorner(SoundRay soundRay);

    boolean isCorner(Vector pos);

    boolean onOutline(Vector pos);

    boolean isInside(Vector pos);
}
