package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Wall extends TextureImp
{
    public Wall(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.SANDYBROWN;
    }
}
