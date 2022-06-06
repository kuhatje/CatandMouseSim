public interface ShipStrategy {
    void madDash();
    
    void circle(Point2D p, boolean CCW);
    
    void moveAway(Ship s);
    
    void moveTowardCenter();

    void changeDir(Vector2D d);

    void oppositeDir();

    
}
