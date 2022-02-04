package app.controller;

public class Vector
{
    private double x;
    private double y;

    public Vector()
    {
        this.x = 0d;
        this.y = 0d;
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Vector add(Vector other)
    {
        return new Vector(this.x + other.getX(), this.y + other.getY());
    }

    public Vector sub(Vector other)
    {
        return new Vector(this.x - other.getX(), this.y - other.getY());
    }

    public Vector scale(double scalar)
    {
        return new Vector(this.getX() * scalar, this.getY() * scalar);
    }

    public double dist(Vector vector)
    {
        double a = Math.pow((this.getX() - vector.getX()),2);
        double b = Math.pow((this.getY() - vector.getY()),2);
        return Math.sqrt(a + b);
    }

    public Vector copy()
    {
        return new Vector(x, y);
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Vector)
        {
            Vector v = (Vector) other;
            return this.getX() == v.getX() && this.getY() == v.getY();
        }
        return false;
    }
}