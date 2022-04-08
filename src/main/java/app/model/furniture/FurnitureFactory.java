package app.model.furniture;

import app.controller.settings.SettingsObject;
import app.model.boundary.BoundaryFactory;
import app.model.soundBoundary.SoundBoundaryFactory;
import app.model.texture.TextureFactory;

public abstract class FurnitureFactory
{
    public static Furniture make(SettingsObject object)
    {
        Furniture furniture = new FurnitureBase();
        furniture.addBoundaries(BoundaryFactory.make(object));
        furniture.addSoundBoundaries(SoundBoundaryFactory.make(object));
        furniture.setTexture(TextureFactory.make(object.getType(), object.getRect()));
        return furniture;
    }
}
