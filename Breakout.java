/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
		(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Start value for vy */
	private static final double VY_START = 3.0;
	
	/** Animation delay or pause time between ball moves */ 
	private static final int DELAY = 25;

	/**
	 * instance variables
	 */
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/* Method: init() */
	/** Sets up the Breakout program. */
	public void init() {
		setupBricks();
		setupPaddle();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setupBall();
		while (true) {
			moveBall();
			checkForCollision();
			pause(DELAY);
		} 
	}

	/**
	 * setup bricks
	 */
	private void setupBricks() {
		int y = BRICK_Y_OFFSET;
		int firstX = (WIDTH - BRICK_WIDTH * NBRICKS_PER_ROW - BRICK_SEP * (NBRICKS_PER_ROW - 1)) / 2;
		Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};

		for (int j = 0; j < NBRICK_ROWS; j ++) {
			int x = firstX;
			Color c = colors[j / 2];
			for (int i = 0; i < NBRICKS_PER_ROW; i++) {
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick.setLocation(x, y);
				brick.setFilled(true);
				brick.setColor(c);
				add(brick);
				x += BRICK_WIDTH + BRICK_SEP;
			}
			y += BRICK_HEIGHT + BRICK_SEP;
		}
	}

	/**
	 * setup paddle
	 */
	private void setupPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setLocation(
				(WIDTH - PADDLE_WIDTH) / 2, 
				HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT
		);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}

	/**
	 * bind paddle to mouse movement
	 */
	public void mouseMoved(MouseEvent e) {
		double x = e.getX();
		double minX = PADDLE_WIDTH / 2;
		double maxX = WIDTH - PADDLE_WIDTH / 2;
		if (x < minX) {
			x = minX;
		} else if (x > maxX) {
			x = maxX;
		}
		paddle.setLocation(
				x - minX, 
				HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT
		);
	}
	
	/**
	 * setup ball
	 */
	private void setupBall() {
		double d = 2 * BALL_RADIUS;
		ball = new GOval(d, d);
		ball.setFilled(true);
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
		add(ball);
		
		vy = VY_START;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
	}

	/**
	 * move ball
	 */
	private void moveBall() {
		ball.move(vx, vy);
	}
	
	/**
	 * check for collision and change direction
	 */
	private void checkForCollision() {
		double x = ball.getX();
		double y = ball.getY();
		double d = 2 * BALL_RADIUS;
		if ((y > HEIGHT - d) || (y < 0)) {
			vy = -vy;
		}
		if ((x > WIDTH - d) || (x < 0)) {
			vx = -vx;
		}
		
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			vy = -vy;
		} else if (collider != null) {
			remove(collider);
		}
	}

	/**
	 * search for colliding object
	 * @return colliding object || null
	 */
	private GObject getCollidingObject() {
		double x = ball.getX();
		double y = ball.getY();
		double d = 2 * BALL_RADIUS;
		GObject object;

		object = getElementAt(x, y);
		if (object != null) return object;
		object = getElementAt(x + d, y);
		if (object != null) return object;
		object = getElementAt(x, y + d);
		if (object != null) return object;
		object = getElementAt(x + d, y + d);
		if (object != null) return object;
		return null;
	}


}
