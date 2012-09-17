/*
 * File: BouncingBall.java
 * -----------------------
 * This program graphically simulates a bouncing ball. */
import acm.program.*;
import acm.graphics.*;

public class BouncingBall extends GraphicsProgram {

	/** Size (diameter) of the ball */ 
	private static final int DIAM_BALL = 30;

	/** Amount Y velocity is increased each cycle as a 
	 * result of gravity */
	private static final double GRAVITY = 3;

	/** Animation delay or pause time between ball moves */ 
	private static final int DELAY = 50;

	/** Initial X and Y location of ball */
	private static final double X_START = DIAM_BALL / 2; 
	private static final double Y_START = 100;

	/** X Velocity */
	private static final double X_VEL = 5;

	/** Amount Y Velocity is reduced when it bounces */ 
	private static final double BOUNCE_REDUCE = 0.9;

	/** Starting X and Y Velocties */
	private double xVel = X_VEL;
	private double yVel = 0.0;

	/* private instance variable */
	private GOval ball;

	public void run() {
		setup();

		// Simulation ends when ball goes off right hand 
		// end of screen
		while (ball.getX() < getWidth()) {
			moveBall();
			checkForCollision();
			pause(DELAY);
		} 
	}

	/** Create and place ball. */
	private void setup() {
		ball = new GOval(X_START, Y_START, DIAM_BALL, DIAM_BALL); 
		ball.setFilled(true);
		add(ball);
	}

	/** Update and move ball */
	private void moveBall() {
		// increase yVelocity due to gravity on each cycle 
		yVel += GRAVITY;
		ball.move(xVel, yVel);
	}

	/** Determine if collision with floor, update velocities 
	 * and location as appropriate. */
	private void checkForCollision() {
		// determine if ball has dropped below the floor 
		if (ball.getY() > getHeight() - DIAM_BALL) {
			// change ball's Y velocity to now bounce upwards 
			yVel = -yVel * BOUNCE_REDUCE;
			
			// assume bounce will move ball an amount above the
			// floor equal to the amount it would have dropped
			// below the floor.
			double diff = ball.getY() - (getHeight() - DIAM_BALL); 
			ball.move(0, -2 * diff);
		} 
	}

}