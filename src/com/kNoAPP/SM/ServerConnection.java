package com.kNoAPP.SM;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection implements Runnable {
	
	private Socket s;
	private Scanner in;
	private PrintWriter out;
	private String me;
	
	public ServerConnection(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			in = new Scanner(s.getInputStream());
			out = new PrintWriter(s.getOutputStream(), true);
			
			me = in.nextLine();
			out("add");
			
			while(true) {
				if(in.hasNext()) in(in.nextLine());
			}
		} catch(Exception ex) {ex.printStackTrace();}
		close();
	}
	
	public void in(String s) {
		System.out.println(s);
		String args[] = s.split("\\s+");
		if(args[1].equals("add")) new Player(args[0], Color.RED).add();
		if(args[1].equals("remove") && Player.getPlayer(args[0]) != null) Player.getPlayer(args[0]).remove();
		
		Player p = Player.getPlayer(args[0]);
		if(p != null) {
			if(args[1].equals("move")) {
				p.setX(Integer.parseInt(args[2]));
				p.setY(Integer.parseInt(args[3]));
			}
			if(args[1].equals("left")) {
				if(args[2].equals("true")) p.left = true;
				if(args[2].equals("false")) p.left = false;
			}
			if(args[1].equals("right")) {
				if(args[2].equals("true")) p.right = true;
				if(args[2].equals("false")) p.right = false;
			}
			if(args[1].equals("up")) {
				if(args[2].equals("true")) p.up = true;
				if(args[2].equals("false")) p.up = false;
			}
			if(args[1].equals("down")) {
				if(args[2].equals("true")) p.down = true;
				if(args[2].equals("false")) p.down = false;
			}
		}
	}
	
	public void out(String s) {
		if(out != null) {
			out.println(s);
			if(out.checkError()) close();
		}
	}
	
	public void close() {
		try {
			s.close();
		} catch (IOException e) {}
		System.exit(0);
	}
	
	public String getMe() {
		return me;
	}
}
