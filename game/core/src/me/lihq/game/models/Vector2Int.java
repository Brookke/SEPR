package me.lihq.game.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * This class defines a 2D vector to represent coordinates within the game.
 */
public class Vector2Int
{

    public int x = 0;// initialise x as 0
    public int y = 0;// initialise y as 0

    /**
     * Creates a new Vector2Int object.
     *
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    public Vector2Int(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x coordinate.
     *
     * @return returns value of x for this object.
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Getter for y coordinate.
     *
     * @return returns value of y for this object.
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * This is a method override which allows the use of equality in the newly defined Vector2Int objects.
     *
     * @param obj - The Vector2Int object.
     * @return Returns true or false depending on whether the equality is true or false.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector2Int other = (Vector2Int) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }

    /**
     * This method returns a string representation of the object
     *
     * @return String that represents the values in this object
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
