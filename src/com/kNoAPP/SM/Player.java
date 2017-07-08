package com.kNoAPP.SM;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Player {

	private String name;
	private Color c;
	private double x, y, dx, dy;
	
	private long fix;
	
	boolean left,right,up,down;
	
	public static ArrayList<Player> onlinePlayers = new ArrayList<Player>();
	
	public Player(String name, Color c) {
		this.name = name;
		this.c = c;
		
		x = GamePanel.center()[0];
		y = GamePanel.center()[1];
		dx = 0;
		dy = 0;
		
		fix = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return c;
	}
	
	public double getX() {
		return x;
	}
	
	public double getDX() {
		return dx;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getDY() {
		return dy;
	}
	
	public void setDY(double dy) {
		this.dy = dy;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public long getFix() {
		return fix;
	}
	
	public void setFix(long fix) {
		this.fix = fix;
	}
	
	public void update(ServerConnection sc) {
		if(right) dx = dx<1 ? dx+0.1 : 1;
		if(left) dx = dx>-1 ? dx-0.1 : -1;
		if(up) dy = dy>-1 ? dy-0.1 : -1;
		if(down) dy = dy<1 ? dy+0.1 : 1;
		
		x += dx;
		y += dy;
		
		if(-0.02 < dx && dx < 0.02) dx = 0;
		else dx = dx > 0 ? dx-0.02 : dx+0.02;
		if(-0.02 < dy && dy < 0.02) dy = 0;
		else dy = dy > 0 ? dy-0.02 : dy+0.02;
		
		if(GamePanel.me == this && System.currentTimeMillis() >= fix) {
			sc.out("move " + (int)x + " " + (int)y);
			fix = System.currentTimeMillis() + 10*1000;
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(c);
		g.drawOval((int)x, (int)y, 2, 2);
		g.fillOval((int)x, (int)y, 2, 2);
	}
	
	public void add() {
		onlinePlayers.add(this);
	}
	
	public void remove() {
		onlinePlayers.remove(this);
	}
	
	public static Player getPlayer(String name) {
		if(onlinePlayers != null) 
			for(Player p : onlinePlayers) 
				if(p.getName() != null) 
					if(p.getName().equals(name)) return p;
		return null;
	}
}
