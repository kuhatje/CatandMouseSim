public class Point2D {
    private double x;
    private double y;

    // Instantiate Point2d without parameters, default to 0, 0
    public Point2D(){
        this.x = 0;
        this.y = 0;}

    // Instantiate Point2d with given x and y components
    public Point2D(double x, double y){
        this.x = x;
        this.y = y;}

    public void set(double x, double y){
        this.x = x; this.y = y;
    }

    public double getX(){
        return x;}
    public double getY(){
        return y; }

    // Subtracts a point, returning the Vector2D from the point to this one
    public Vector2D subtract(Point2D p){
        return new Vector2D(x-p.getX(), y-p.getY());
    }
    public Point2D add(Vector2D v){
        return new Point2D(x + v.getX(), y+v.getY());}

    public boolean equals(Point2D p){
        return this.x == p.getX() && this.y == p.getY();
    }

    public double angle(){
        double theta = 0;
        if (x>0 && y>=0){
            theta = Math.atan(y/x);
        }
        else if (x==0 && y>=0){
            theta = Math.PI/2;
        }
        else if (x<0){
            theta = Math.PI+Math.atan(y/x);}
        else if (x==0 && y<0){
            theta = 3*Math.PI/2;}
        else if (x>0 && y<0){
            theta = 2*Math.PI+Math.atan(y/x);
        }
        return theta;
    }

    public Point2D to_cartesian(){
        return new Point2D(x-400, -y+300);
    }
    public Point2D to_screen(){
        return new Point2D(x+400, -y+300);
    }

    // Define toString method
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args){
        Point2D p = new Point2D(-3, -600000000);

        System.out.println(p.angle()*180/Math.PI);
    }
}
