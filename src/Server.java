import java.sql.*;
import java.io.*;

//Class.forName("com.mysql.jdbc.Driver");
public class Server  {
	private static String messages;
	public static void main(String[] args) throws IOException {
		
	}
	public static void close() throws IOException {
		File msgFile = new File("messages.log");
		if(!msgFile.exists()) {
		    msgFile.createNewFile();
		} 
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(msgFile)));
		out.writeUTF(messages);
	}
	public static void open() throws IOException {
		File msgFile = new File("messages.log");
		if(!msgFile.exists()) {
			return;
		} 
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(msgFile)));
		messages = in.readUTF();
	}
}
