import java.sql.*;
import java.util.*;
import java.io.*;
import java.net.*;


//Class.forName("com.mysql.jdbc.Driver");
public class Server  {
	public Hashtable clients = new Hashtable();
	public ServerSocket socket;
	public DataOutputStream logOut;
	
	public Server(int port) throws IOException {
		try {
			logOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("msg.log"))));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		acceptConnections(port);
	}
	public void acceptConnections(int port) throws IOException {
		socket = new ServerSocket(port);
		
		System.out.printf("Accepting connections on port %d%n", port);
		while(true) {
			Socket client = socket.accept();
			
			DataOutputStream clientOutStream = new DataOutputStream(client.getOutputStream());
			
			clients.put(client, clientOutStream);
			
			System.out.printf("Accepted Connection from default at %s%n", client.getInetAddress());
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
	
	public void sendAll(String message) {
		synchronized(clients) {
			for(Enumeration clientEnum = getClients(); clientEnum.hasMoreElements();) {
				DataOutputStream out = (DataOutputStream)clientEnum.nextElement();
				try {
					out.writeUTF(message);
					System.out.println("Wrote message to client.");
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("Failed to write message to client!");
				}
			}
		}
	}
	public void log(String msg) {
		System.out.println(msg);
		//Possibly do more later
	}
	
	public void removeClient(Socket client) {
		synchronized(clients) {
			System.out.printf("Removing client default at %s%n", client.getInetAddress());
			clients.remove(client);
			try {
				client.close();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
				System.out.println("Failed to remove client.");
				return;
			}
			System.out.println("Succeded in removing client.");
		}
	}
}
