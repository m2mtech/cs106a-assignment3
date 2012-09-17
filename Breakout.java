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
	private static final double VY_START = 5.0;
	
	/** Animation delay or pause time between ball moves */ 
	private static final int DELAY = 15;

	/** Number of times the paddle has to be hit before doubling speed */ 
	private static final int SUCCESSFULL_PADDLEHITS_BEFORE_KICKER = 7;
	
	/**
	 * instance variables
	 */
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int numberOfBricks;
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	private GLabel label;
	private boolean startGame;
	private int numberOfPaddleHits;
	private GLabel scoreLabel;
	private int score;


	/* Method: init() */
	/** Sets up the Breakout program. */
	public void init() {
		setupBricks();
		setupPaddle();
		setupScore();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		for (int i = 0; i < NTURNS; i++) {
			displayMessage("press mouse button");
			startGame = false;
			while (!startGame) {
				pause(DELAY * 10);
			}
			removeMessage();
			setupBall();
			while ((ball.getY() < HEIGHT) && (numberOfBricks > 0)) {
				moveBall();
				checkForCollision();
				pause(DELAY);
			} 			
			if (numberOfBricks == 0) {
				displayMessage("Winner!");
				return;					
			}
		}
		displayMessage("Game Over");
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
		numberOfBricks = NBRICK_ROWS * NBRICKS_PER_ROW;
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
	 * setup score label
	 */
	private void setupScore() {
		score = 0;
		scoreLabel = new GLabel("Score: " + score + " ");
		scoreLabel.setFont("SansSerif-28");
		double x = getWidth() - scoreLabel.getWidth();
		double y = getHeight() - scoreLabel.getAscent();
		scoreLabel.setLocation(x, y);
		add(scoreLabel);
	}
	
	/**
	 * add score value and adjust the label
	 */
	private void addScore(int value) {
		score += value;
		scoreLabel.setLabel("Score: " + score + " ");
		double x = getWidth() - scoreLabel.getWidth();
		double y = scoreLabel.getY();
		scoreLabel.setLocation(x, y);
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
	 * start game when mouse button has been clicked
	 */
	public void mouseClicked(MouseEvent e) {
		startGame = true;
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
		
		numberOfPaddleHits = 0;
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
		if (y < 0) {
			vy = -vy;
			ball.move(0, -2 * y);
			bounceClip.play();
		}
		if (x > WIDTH - d) {
			vx = -vx;
			ball.move(-2 * (x - WIDTH + d), 0);
			bounceClip.play();
		}
		if (x < 0) {
			vx = -vx;
			ball.move(-2 * x, 0);
			bounceClip.play();
		}
		
		GObject collider = getCollidingObject();
		if (collider == paddle) {			
			double hitPosition = (2 * (x - paddle.getX()) + d - PADDLE_WIDTH) / (d + PADDLE_WIDTH);
			if (hitPosition < 0) {
				vx = -Math.abs(vx);
			} else {
				vx = Math.abs(vx);
			}
			numberOfPaddleHits++;
			if (numberOfPaddleHits % SUCCESSFULL_PADDLEHITS_BEFORE_KICKER == 0) {
				vx *= 2;
			}
			vy = -vy;
			ball.move(0, -2 * (y + d - HEIGHT + PADDLE_Y_OFFSET + PADDLE_HEIGHT));
			bounceClip.play();
		} else if (collider instanceof GRect) {
			if (collider.getColor() == Color.CYAN) addScore(10);
			else if (collider.getColor() == Color.GREEN) addScore(20);
			else if (collider.getColor() == Color.YELLOW) addScore(30);
			else if (collider.getColor() == Color.ORANGE) addScore(40);
			else if (collider.getColor() == Color.RED) addScore(50);
			remove(collider);
			numberOfBricks--;
			vy = -vy;
			bounceClip.play();
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
	
	/**
	 * print game messages
	 * @param message
	 */
	private void displayMessage(String message) {
		label = new GLabel(message);
		label.setFont("SansSerif-28");
		double x = (getWidth() - label.getWidth()) / 2;
		double y = (getHeight() + label.getAscent()) / 2;
		label.setLocation(x, y);
		add(label);
	}

	/**
	 * remove game messages
	 */
	private void removeMessage() {
		remove(label);
	}

}
