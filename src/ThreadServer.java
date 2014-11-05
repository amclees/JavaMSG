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
		try {
			DataInputStream in = new DataInputStream(client.getInputStream());
			
			String msg = in.readUTF();
			System.out.println("Got MSG");
			
			server.sendAll(msg);
		}
		catch(EOFException eof) {
			eof.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			server.removeClient(client);
		}
	}
	
}
