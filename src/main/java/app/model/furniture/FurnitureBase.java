package app.model.furniture;

import app.model.boundary.Boundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class FurnitureBase implements Furniture
{
    private ArrayList<Boundary> boundaries;
    private Texture texture;

    public FurnitureBase()
    {
        boundaries = new ArrayList<>();
        texture = null;
    }

    public void draw(GraphicsContext gc)
    {
        if(texture != null)
            texture.draw(gc);

        boundaries.forEach(e -> e.draw(gc));
    }

    public void setBoundaries(ArrayList<Boundary> boundaries)
    {
        this.boundaries.addAll(boundaries);
    }

    public ArrayList<Boundary> getBoundaries()
    {
        return boundaries;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
}