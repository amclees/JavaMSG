import java.sql.*;
import java.util.*;
import java.io.*;
import java.net.*;


//Class.forName("com.mysql.jdbc.Driver");
public class Server  {
	public Hashtable clients = new Hashtable();
	public ServerSocket socket;
	
	public Server(int port) throws IOException {
		acceptConnections(port);
	}
	public void acceptConnections(int port) throws IOException {
		socket = new ServerSocket(port);
		while(true) {
			Socket client = socket.accept();
			
			DataOutputStream clientOutStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
			
			clients.put(client, clientOutStream);
			
			new ThreadServer(this, client);
		}
	}
	public static void main(String[] args) throws IOException {
		if(args.length == 1) {
			Server server = new Server(Integer.parseInt(args[0]));
		}
		else {
			System.out.println("Port is the only argument accepted!");
		}
	}
	
	Enumeration getClients() {
		return clients.elements();
	}
}
