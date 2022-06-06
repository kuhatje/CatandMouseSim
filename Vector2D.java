public class Vector2D{
    private double x;
    private double y;


    // Instantiate vector without parameters(default [0,0]),
    // with x and y components, or with two Point2D objects.
    public Vector2D(){
        this.x = 0;
        this.y = 0;}
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;}

    public void set(double x, double y){
        this.x = x; this.y = y;
    }
    public double getX(){
        return x; }
    public double getY(){
        return y; }

    public Vector2D add(double x, double y){
        return new Vector2D(this.x + x, this.y + y);
    }
    public Vector2D subtract(double x, double y){
        return new Vector2D(this.x - x, this.y - y);
    }

    public double length(){
        return Math.sqrt(x*x + y*y);}

    // Multiply vector components by given k
    public Vector2D scale(double k){
        return new Vector2D(k*x, k*y);
    }

    public Vector2D normalize(){
        return new Vector2D(this.x/this.length(), this.y/this.length());
    }

    public Vector2D setLength(double length){
        return this.normalize().scale(length);
    }

    public double magOfCrossProduct(Vector2D u){
        return Math.abs(this.x*u.getY() - this.y*u.getX());
    }

    public Vector2D perpendicular(){
        return new Vector2D(y,-1*x).normalize();
    }

    // Define toString method
    public String toString() {
        return "[" + x + ", " + y + "]";
    }


    public static void main(String[] args){
        Vector2D v = new Vector2D(5,6);

        System.out.println(v.perpendicular());
    }
}
