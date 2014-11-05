import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ClientThread implements Runnable{
	private Socket server;
	private DataInputStream in;
	private DataOutputStream out;
	public Text msgContent;
	public void login(Stage stage, Scene main, String username, String password, String portString, String host) {
		try {
			int port = Integer.parseInt(portString);
			this.server = new Socket(host, port);
			
			this.in = new DataInputStream(server.getInputStream());
			this.out = new DataOutputStream(server.getOutputStream());
			stage.setScene(main);
			Platform.runLater(new ClientThread());
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid Port");
			return;
		}
		catch (IOException e) {
			System.out.println("Error Connecting to Server");
			return;
		}
	}
	public void run() {
		try {
			while (true) {
				String msg = in.readUTF();
				msgContent.setText(msgContent.getText() + msg + "\n");
			}
		} 
		catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}
	public void sendMsg(String msg) {
		try {
			out.writeUTF(msg);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
}
