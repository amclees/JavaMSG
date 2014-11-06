import java.net.*;
import java.sql.*;
import java.util.*;
import java.io.*;

public class ThreadServer extends Thread {
	
	public Server server;
	public Socket client;

	public long startTime;
	
	public ThreadServer(Server server, Socket client) {
		this.client = client;
		this.server = server;
		this.startTime = System.currentTimeMillis();
				
		start();
	}
	
	public void run() {
		String msg = "";
		
		DataInputStream  in = null;
		try {
			in = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		while (true) {
		boolean exit = true;
		try {
			msg = in.readUTF();
			System.out.println("Got MSG " + msg);
			
			server.sendAll(msg);
			
			exit = false;
		}
		catch(EOFException eof) {
			eof.printStackTrace();
			System.out.println("EOF Error");
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IO Error");
		}
		
		if (exit) {
			server.removeClient(client);
			System.out.println("Disconnected client");
			break;
		}
		}
	}
	
}
