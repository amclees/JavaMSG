import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;

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
		InetAddress clientAddress = client.getInetAddress();
		String username = "default"; //put username in here later
		System.out.printf("Started new thread for client %s at %s%n", username, clientAddress);
		try {
			in = new DataInputStream(client.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (true) {
		boolean exit = true;
		try {
			msg = in.readUTF();
			System.out.printf("Got MSG %s from %s at %s%n", msg, username, clientAddress);
			
			//server.log(msg);
			server.sendAll(msg);
			
			exit = false;
		}
		catch(EOFException eof) {
			eof.printStackTrace();
			System.out.println("EOF Error");
		}
		catch(IOException ioe) {
			System.out.println("Client Left");
		}
	
		if (exit) {
			
			server.removeClient(client);
			System.out.println("Disconnected client");
			break;
		}
		}
	}
	
}
