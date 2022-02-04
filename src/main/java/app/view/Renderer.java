package app.view;

import app.controller.Beam;
import app.controller.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    public ArrayList<Beam> beams = new ArrayList<>(); // This will be removed and replaced with the Map

    public Renderer(int width, int height)
    {
        super(width, height);
        createRays();
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(400,250,1,1);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        for(Beam r: beams)
        {
            gc.strokeLine(r.getU().getX(), r.getU().getY(), r.getV().getX(), r.getV().getY());
        }
    }

    // Temp method to create a set of rays - GraphicsEngine should generate this
    private void createRays()
    {
        Vector origin = new Vector(400,250);
        Vector end = new Vector(0, 0);
        Beam beam = new Beam(origin, end);
        for(int i = 0; i < 100; i++)
        {
            beams.add(beam.rotate(i * 5.0));

        }
    }
}
