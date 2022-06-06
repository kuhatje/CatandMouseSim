import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Ship implements ShipStrategy, Drawable, Animatable{
    private final double size, speed;
    private final Color color;
    private Point2D position;

    public Ship(double x, double y, double ship_size, Color ship_color, double ship_speed) {
        position = new Point2D(x,y);
        size = ship_size;
        color = ship_color;
        speed = ship_speed;
    }

    public Point2D getPosition(){
        return position;
    }

    public void madDash(){
        Vector2D v = position.subtract(new Point2D(0,0));
        v = v.normalize();
        position = position.add(v.scale(speed));
    }
    public void moveAway(Ship s){
        Point2D other_pos = s.getPosition();
        Vector2D v = position.subtract(other_pos);
        v = v.normalize();
        position = position.add(v.scale(speed));
    }
    public void moveTowardCenter(){
        if (!(closeTo(new Point2D(0,0)))){
            Vector2D v = new Point2D(0,0).subtract(position);
            v = v.normalize();
            position = position.add(v);
        }
        else{
            position.set(0,0);
        }
    }
    public void changeDir(Vector2D d){
        d=d.normalize();
        position = position.add(d.scale(speed));
    }
    public void oppositeDir(){}
    public void moveTo(Point2D p){
        if (!(closeTo(p))){
            Vector2D v = p.subtract(position);
            v = v.normalize();
            position = position.add(v);
        }
        else{
            position.set(p.getX(), p.getY());}
    }
    public void circle(Point2D p, boolean CCW){
        double radius = position.subtract(p).length();
        double angle = position.angle();
        double angle_increment;
        if (CCW){
            angle_increment = (1/radius)*speed;}
        else{
            angle_increment = -(1/radius)*speed;
        }

        position.set(p.getX()+radius*Math.cos(angle+angle_increment), p.getY()+radius*Math.sin(angle+angle_increment));

    }

    public void draw(Canvas c){
        c.getGraphicsContext2D().setFill(color);
        c.getGraphicsContext2D().fillOval(position.to_screen().getX()-size, position.to_screen().getY()-size, size*2, size*2);


    }
    // Returns true if ship's position is within 2 of point p
    public boolean closeTo(Point2D p){
        return p.subtract(position).length() <= 2;
    }

    public void calcNewPos(double t){}
    public void collidesWith(Ship s){}

}
