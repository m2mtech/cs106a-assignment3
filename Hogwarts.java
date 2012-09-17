/*
 * File: Hogwarts.java
 * -------------------
 * This program is just testing your understanding of parameter passing. */
import acm.program.*;

public class Hogwarts extends ConsoleProgram {

	public void run() {
		bludger(2001);
	}
	
	private void bludger(int y) { // y = 2001;
		int x = y / 1000; // x = 2;
		int z = (x + y); // z = 2003;
		x = quaffle(z, y); // x = 1001
		println("bludger: x = " + x + ", y = " + y + ", z = " + z); // 3. line: bludger: x = 1001, y = 2001, z = 2003
	}
	
	private int quaffle(int x, int y) { // x = 2003; y = 2001; 
		int z = snitch(x + y, y); // z = 1001
		y /= z; // y = 1
		println("quaffle: x = " + x + ", y = " + y + ", z = " + z); // 2. line: quaffle: x = 2003, y = 1, z = 1001
		return z;
	}
	
	private int snitch(int x, int y) { // x = 4004; y = 2001;
		y = x / (x % 10); // y = 1001;
		println("snitch: x = " + x + ", y = " + y); // 1. line: snitch: x = 4004, y = 1001
		return y;
	} 

}