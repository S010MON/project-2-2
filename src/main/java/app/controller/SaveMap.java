package app.controller;

import javafx.geometry.Rectangle2D;
import java.awt.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SaveMap {

    public static void saveMap( Settings setting){
        try
        {
            String fileName = setting.getName();
            if (fileName == null)
                fileName = "Save_Map_" + String.valueOf(Math.round(Math.random() * 100000) + ".txt");
            else
                fileName = "Save_" + fileName + ".txt";
            String filePath = FilePath.getFilePath(fileName);

            File mapFile = new File(filePath);
            FileWriter writer = new FileWriter(mapFile);

            double reverseScale = 1/setting.getScaling();

            writeLine(writer, "name", setting.getName());
            writeLine(writer, "gameFile", filePath);
            writeLine(writer, "gameMode", Integer.toString(setting.getGamemode()));
            writeLine(writer, "height", Integer.toString((int)(setting.getHeight()*reverseScale)));
            writeLine(writer, "width", Integer.toString((int)(setting.getWidth()*reverseScale)));
            writeLine(writer, "scaling", Double.toString(setting.getScaling()));
            writeLine(writer, "numGuards", Integer.toString(setting.getNoOfGuards()));
            writeLine(writer, "numIntruders", Integer.toString(setting.getNoOfIntruders()));
            writeLine(writer, "baseSpeedIntruder", Double.toString(setting.getWalkSpeedIntruder()*reverseScale));
            writeLine(writer, "sprintSpeedIntruder", Double.toString(setting.getSprintSpeedIntruder()*reverseScale));
            writeLine(writer, "baseSpeedGuard", Double.toString(setting.getWalkSpeedGuard()*reverseScale));
            writeLine(writer, "sprintSpeedGuard", Double.toString(setting.getSprintSpeedGuard()*reverseScale));
            writeLine(writer, "timeStep", Double.toString(setting.getTimeStep()));
            writeLine(writer, "targetArea", setting.getTargetArea(), reverseScale);
            writeLine(writer, "spawnAreaIntruders", setting.getSpawnAreaIntruders(), reverseScale);
            writeLine(writer, "spawnAreaGuards", setting.getSpawnAreaGuards(), reverseScale);

            ArrayList<Rectangle2D> walls = setting.getWalls();
            for(Rectangle2D wall : walls)
            {
                writeLine(writer, "wall", wall, reverseScale);
            }

            ArrayList<Rectangle2D> towers = setting.getTowers();
            for(Rectangle2D tower : towers)
            {
                writeLine(writer, "tower", tower, reverseScale);
            }


            ArrayList<Rectangle2D> portals = setting.getPortals();
            ArrayList<Point> teleportPoints = setting.getTeleportTo();
            ArrayList<Double> teleportOrientations = setting.getTeleportOrientations();
            for(int i=0; i<portals.size(); i++)
            {
                writeLineTeleport(writer, portals.get(i), teleportPoints.get(i), teleportOrientations.get(i), reverseScale);
            }

            ArrayList<Rectangle2D> shades = setting.getShade();
            for(Rectangle2D shade : shades)
            {
                writeLine(writer, "shaded", shade, reverseScale);
            }


            ArrayList<Rectangle2D> textures = setting.getTextures();
            ArrayList<Integer> textureTypes = setting.getTextureType();
            ArrayList<Integer> textureOrientations = setting.getTextureOrientations();
            for(int i=0; i<textures.size(); i++)
            {
                writeLineTexture(writer, textures.get(i), textureTypes.get(i), textureOrientations.get(i), reverseScale);
            }


            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occured while creating a map file.");
            e.printStackTrace();
        }

    }

    private static void writeLineTexture(FileWriter writer, Rectangle2D rectangle, int textureType, int orientation, double reverseScale) throws IOException
    {
        String rectangleString = Integer.toString((int)(rectangle.getMinX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMinY()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxY()*reverseScale));
        String typeAndOrientation = Integer.toString(textureType) + " " + Integer.toString(orientation);
        writer.write("texture = " + rectangleString + " " + typeAndOrientation + System.getProperty( "line.separator" ));
    }

    private static void writeLineTeleport(FileWriter writer, Rectangle2D rectangle, Point point, double orientation, double reverseScale) throws IOException
    {
        String portal = Integer.toString((int)(rectangle.getMinX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMinY()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxY()*reverseScale));
        String pointString = Integer.toString((int)(point.getX()*reverseScale)) + " " + Integer.toString((int)(point.getY()*reverseScale));
        String orient = Double.toString(orientation);
        writer.write("teleport = " + portal + " " + pointString + " " + orient + System.getProperty( "line.separator" ));
    }

    private static void writeLine(FileWriter writer, String variable, Rectangle2D rectangle, double reverseScale) throws IOException
    {
        String value = Integer.toString((int)(rectangle.getMinX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMinY()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxX()*reverseScale)) + " "
                + Integer.toString((int)(rectangle.getMaxY()*reverseScale));
        writer.write(variable + " = " + value + System.getProperty( "line.separator" ));
    }

    private static void writeLine(FileWriter writer, String variable, String value) throws IOException
    {
        writer.write(variable + " = " + value + System.getProperty( "line.separator" ));
    }

}
