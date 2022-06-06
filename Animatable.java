public interface Animatable {

    void calcNewPos(double t);
    void collidesWith(Ship s);
}
