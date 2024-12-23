package AoC2024.utils;

import java.util.Objects;

public class Coordinate {
	public int y;
	public int x;
    
    
    public Coordinate(int x, int y) {
    	this.x = x;
    	this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public Coordinate add(Coordinate c) {
    	return new Coordinate(x+c.x, y+c.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + y + "," + x + ")";
    }
    
    public double distance(Coordinate c) {
    	return Math.sqrt( Math.pow(x-c.x,2)+Math.pow(y-c.y,2));
    }
    public double distanceSqrt(Coordinate c) {
    	return Math.pow(x-c.x,2)+Math.pow(y-c.y,2);
    }
}
