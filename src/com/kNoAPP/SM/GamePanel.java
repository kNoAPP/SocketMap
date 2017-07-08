package com.kNoAPP.SM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	//Settings
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 6;
	
	public static int frame; 
	
	public static Player me;
	
	//Window Stuffs
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//Image
	private BufferedImage image;
	private Graphics2D g;
	
	//Server
	private ServerConnection sc;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		try {
			sc = new ServerConnection(new Socket(InetAddress.getByName("192.99.57.0"), 14555));
			new Thread(sc).start();
		} catch (Exception ex) {System.exit(0);}
		
		me = new Player(sc.getMe(), Color.BLUE);
		me.add();
	}
	
	public void run() {
		init(); //Begins the Program
		
		long start;
		long elapsed;
		long wait;
		
		//Game Loop
		while(running) {
			start = System.nanoTime();
			if(frame == Integer.MAX_VALUE-1) frame = 0;
			else frame++;
			
			update();
			
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed/100000;
			if(wait < 0) wait = 5;
			
			try{
				Thread.sleep(wait);
			} catch (Exception ex) {ex.printStackTrace();}
		}
	}
	
	private void update() {
		for(Player p : Player.onlinePlayers) p.update(sc);
	}
	
	private void draw() {
		for(Player p : Player.onlinePlayers) p.draw(g);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {

	}
	
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_W) {
			me.up = true;
			sc.out("up true");
		}
		if(key.getKeyCode() == KeyEvent.VK_S) {
			me.down = true;
			sc.out("down true");
		}
		if(key.getKeyCode() == KeyEvent.VK_A) {
			me.left = true;
			sc.out("left true");
		}
		if(key.getKeyCode() == KeyEvent.VK_D) {
			me.right = true;
			sc.out("right true");
		}
		
		if(key.getKeyCode() == KeyEvent.VK_K) {
			sc.out("end");
		}
	}
	
	public void keyReleased(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_W) {
			me.up = false;
			sc.out("up false");
		}
		if(key.getKeyCode() == KeyEvent.VK_S) {
			me.down = false;
			sc.out("down false");
		}
		if(key.getKeyCode() == KeyEvent.VK_A) {
			me.left = false;
			sc.out("left false");
		}
		if(key.getKeyCode() == KeyEvent.VK_D) {
			me.right = false;
			sc.out("right false");
		}
	}
	
	public static double[] center() {
		return new double[]{WIDTH/2, HEIGHT/2};
	}
}
