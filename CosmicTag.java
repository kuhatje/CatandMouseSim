// ICS4U - MR. RADULOVIC
// COSMIC TAG OOP
// JEFF L. JUNE 22 2021

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CosmicTag extends Application {
    // Class variables
    final double DISPLAY_X = 800; double DISPLAY_Y = 600;
    final double BLUE_SPEED = 1; double RED_SPEED = 4;
    final double PLASMA_RADIUS = 150;
    final double SHIP_SIZE = 3;
    final Color plasma_color = Color.LIGHTGREEN;
    private Ship BlueShip, RedShip;
    private boolean simulation_running, paused;
    boolean blue_ship_centered = false;
    boolean blue_ship_positioned=false;
    boolean blue_ship_ready_to_dash = false;
    final Point2D origin = new Point2D(0,0);

    // Methods to convert between cartesian and "screen" coordinates.
    public double to_screen_x(double x){
        return x+400;
    }
    public double to_screen_y(double y){
        return -y+300;
    }
    public double to_cartesian_x(double x){
        return x-400;
    }
    public double to_cartesian_y(double y){
        return -y+300;
    }

    // Initialize JavaFX
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(DISPLAY_X, DISPLAY_Y);
        Group group = new Group();
        HBox buttons = new HBox();

        Button start_stop_button = new Button("START");
        Button pause_resume_button = new Button("PAUSE");
        buttons.getChildren().addAll(start_stop_button, pause_resume_button);

        // Draw some objects on the canvas
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().setFill(Color.LIGHTGREEN);
        canvas.getGraphicsContext2D().fillOval(to_screen_x(0)-1, to_screen_y(0)-1, 2, 2);
        canvas.getGraphicsContext2D().setFill(plasma_color);
        canvas.getGraphicsContext2D().fillOval(to_screen_x(0)-PLASMA_RADIUS, to_screen_y(0)-PLASMA_RADIUS, PLASMA_RADIUS*2, PLASMA_RADIUS*2);
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillOval(to_screen_x(0)-1, to_screen_y(0)-1, 2, 2);

        // Create the blue and red ship objects and put them on the canvas
        BlueShip = new Ship(100, 0, SHIP_SIZE, Color.BLUE, BLUE_SPEED);
        RedShip = new Ship(0, PLASMA_RADIUS, SHIP_SIZE, Color.RED, RED_SPEED);
        BlueShip.draw(canvas);
        RedShip.draw(canvas);

        group.getChildren().addAll(canvas, buttons);
        Scene scene = new Scene(group);

        stage.setScene(scene);
        stage.show();


         //Main running loop contained here. All of the following code is repeated each frame.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                if (simulation_running) {
                    if (!paused){
                        // Clear the canvas so that each new frame can be drawn
                        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                        // If the red ship touches the blue ship, red wins. If the blue ship leaves the plasma zone, blue wins.
                        if (BlueShip.closeTo(RedShip.getPosition())){
                            System.out.println("Red Wins");
                            System.exit(0);
                        }
                        else if (BlueShip.getPosition().subtract(origin).length() > PLASMA_RADIUS) {
                            System.out.println("Blue Wins.");
                            System.exit(0);
                        }

                        // Find the standard angles of the red ship and blue ship positions, then
                        // use this information to determine whether the red ship circle clockwise or counterclockwise.
                        double r_angle = RedShip.getPosition().angle();
                        double b_angle = BlueShip.getPosition().angle();
                        if (r_angle >= 0 && r_angle <= Math.PI) {
                            if (b_angle >= r_angle && b_angle <= r_angle + Math.PI) {
                                RedShip.circle(origin, true);
                            } else {
                                RedShip.circle(origin, false);
                            }
                        }
                        else if (r_angle > Math.PI && r_angle < 2 * Math.PI) {
                            if (b_angle >= r_angle || b_angle >= 0 && b_angle <= r_angle - Math.PI) {
                                RedShip.circle(origin, true);
                            }
                            else{RedShip.circle(origin, false);}
                        }


                        // Blue ship's method for escaping the plasma zone:

                        // First, the blue ship moves to the origin.
                        if (!blue_ship_centered){
                            BlueShip.moveTowardCenter();
                            if (BlueShip.closeTo(origin)){
                                blue_ship_centered = true;
                            }
                        }

                        // When the ship has been centered, move it a specific distance from the center
                        // this is the "sweet spot orbit". (this is the orbit where the blue ship has just a little
                        // less angular velocity as the red ship, so the blue ship has control over the position between
                        // it and the red ship.
                        else if (!blue_ship_positioned){
                            if (!(Math.abs(origin.subtract(BlueShip.getPosition()).length()-((PLASMA_RADIUS/4)-(0.02)*(PLASMA_RADIUS/4)))<2)){
                                BlueShip.moveAway(RedShip);
                            }
                            else{blue_ship_positioned=true;}

                        }

                        // After the blue ship is positioned in the "sweet spot orbit", it circles the center until
                        // the red ship is positioned DIRECTLY behind the center from the blue ship.
                        else{
                            if (!blue_ship_ready_to_dash){
                                BlueShip.circle(origin, true);
                            }
                            Vector2D u = RedShip.getPosition().subtract(origin);
                            Vector2D v = BlueShip.getPosition().subtract(origin);
                            if (u.normalize().add(v.normalize().getX(),v.normalize().getY()).length() < 0.1 && v.normalize().magOfCrossProduct(u.normalize())<0.01){
                                blue_ship_ready_to_dash=true;
                            }
                        }

                        // When the red ship is behind the origin as desired, the blue ship makes a mad dash to
                        // the closest point on the edge of the plasma zone, and this will always beat the red ship.
                        if (blue_ship_ready_to_dash){
                            BlueShip.madDash();
                        }

                        // refresh the canvas each frame.
                        canvas.getGraphicsContext2D().setFill(Color.BLACK);
                        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        canvas.getGraphicsContext2D().setFill(plasma_color);
                        canvas.getGraphicsContext2D().fillOval(to_screen_x(0) - PLASMA_RADIUS, to_screen_y(0) - PLASMA_RADIUS, PLASMA_RADIUS * 2, PLASMA_RADIUS * 2);
                        canvas.getGraphicsContext2D().setFill(Color.BLACK);
                        canvas.getGraphicsContext2D().fillOval(to_screen_x(0)-1, to_screen_y(0)-1, 2, 2);

                        BlueShip.draw(canvas);
                        RedShip.draw(canvas);
                }}}};


        // Mouse and various button events defined here
        start_stop_button.setOnAction(e -> {
            if (!simulation_running){
                simulation_running = true;
                start_stop_button.setText("STOP");}
            else{
                System.exit(0); }});
        pause_resume_button.setOnAction(e -> {
            if (!paused){
                paused = true;
                pause_resume_button.setText("RESUME");
            }
            else{
                paused = false;
                pause_resume_button.setText("PAUSE");
            }});
        group.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double[] pos = {to_cartesian_x(event.getSceneX()), to_cartesian_y(event.getSceneY())};
                if (new Point2D(pos[0],pos[1]).subtract(origin).length()<=PLASMA_RADIUS){
                    BlueShip.getPosition().set(pos[0],pos[1]);
                    blue_ship_centered = false;
                    blue_ship_positioned=false;
                    blue_ship_ready_to_dash=false;}
                }

        });

        timer.start();
    }

}
