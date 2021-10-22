package ma.ousama.abschlussarbeit.model;

import java.util.Objects;

/**
 * @author Masud Taher
 * @version 1.0
 */

public class Node implements Comparable<Node>  {

    private double x ;
    private double y ;

    private double x_transformed ;
    private double y_transformed ;

    public Node(final double xCoordinate, final double yCoordinate) {
        this.x = xCoordinate;
        this.y = yCoordinate;
        this.x_transformed = xCoordinate;
        this.y_transformed = yCoordinate;
    }

    public double getX_transformed() {
        return x_transformed;
    }

    public void setX_transformed(double x_transformed) {
        this.x_transformed = x_transformed;
    }

    public double getY_transformed() {
        return y_transformed;
    }

    public void setY_transformed(double y_transformed) {
        this.y_transformed = y_transformed;
    }

    public Node(double xCoordinate, double yCoordinate, double x_transformed  , double y_transformed) {
        this.x = xCoordinate;
        this.y = yCoordinate;
        this.x_transformed = x_transformed;
        this.y_transformed = y_transformed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }



    public void setX(double x) {
        this.x = x;
    }


    public void setY(double y) {
        this.y = y;
    }


    public Node sub(Node node) {
        return new Node(this.x - node.x, this.y - node.y);
    }

    public Node add(Node node) {
        return new Node(this.x + node.x, this.y + node.y);
    }

    public Node mult(double scalar) {
        return new Node(this.x * scalar, this.y * scalar);
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double dot(Node node) {
        return this.x * node.x + this.y * node.y;
    }

    public double cross(Node vector) {
        return this.y * vector.x - this.x * vector.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.x, x) == 0 && Double.compare(node.y, y) == 0 && Double.compare(node.x_transformed, x_transformed) == 0 && Double.compare(node.y_transformed, y_transformed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, x_transformed, y_transformed);
    }

    @Override
    public int compareTo(Node node) {
        if (this.y_transformed < node.getY_transformed()){
            return -1;
        } else if (this.y_transformed == node.getY_transformed()){ // if two points have the same y-coordinates then x-coordinates is looked at
            if (this.x_transformed < node.getX_transformed()){
                return -1;
            }else if(this.x_transformed == node.getX_transformed() ){
                return 0;
            }else {
                return 1;
            }
        } else{
            return 1;
        }
    }

    @Override
    public String toString() {
        return x + "\t" + y  ;
    }
}