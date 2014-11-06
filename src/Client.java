import javafx.*;
import javafx.application.*;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class Client extends Application {
	public static Task<Void> listen;
	private static Socket server;
	private static DataInputStream in;
	private static DataOutputStream out;
	public static Text msgContent;
	@Override
	public void start(Stage primaryStage) {

		/*
		 * Create Main Chat Scene
		 */
		GridPane mainPane = new GridPane();
		mainPane.setPadding(new Insets(16, 16, 16, 16));
		mainPane.setAlignment(Pos.TOP_LEFT);
		Scene main = new Scene(mainPane, 720, 480);
		
		ScrollPane messages = new ScrollPane();
		messages.setFitToWidth(true); 
		messages.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		messages.setHbarPolicy(ScrollBarPolicy.NEVER);
		messages.setPrefSize(350, 300);
		messages.setVvalue(1);
		
		msgContent = new Text("Welcome to JavaMSG\n"); //work on this
		
		msgContent.setWrappingWidth(250);
		messages.setContent(msgContent);
		
		mainPane.add(messages, 0, 0);
		
		TextField msg = new TextField();
		mainPane.add(msg, 0, 1);
		
		
		
		
		/*
		 * Create Login Pane
		 */
		
		GridPane loginPane = new GridPane();
		loginPane.setPadding(new Insets(4, 4, 10, 10));
		loginPane.setAlignment(Pos.CENTER);
		
		Text loginTitle = new Text("JavaMSG");
		loginTitle.setFont(Font.font("Arial", 48));
		loginPane.add(loginTitle, 0, 0);
		
		loginPane.add(new Label("Username: "), 0, 1);
		TextField userField = new TextField();
		loginPane.add(userField, 0, 2);
		
		loginPane.add(new Label("Password: "), 1, 1);
		PasswordField passField = new PasswordField();
		loginPane.add(passField, 1, 2);
		
		Button submitLogin = new Button("Log In");
		loginPane.add(submitLogin, 0, 3);
		Button registerBtn = new Button("Register");
		loginPane.add(registerBtn, 1, 3);
		
		loginPane.add(new Label("Port: "), 2, 1);
		TextField portField = new TextField();
		portField.setText("25568");
		loginPane.add(portField, 2, 2);
		
		loginPane.add(new Label("Host Address: "), 2, 3);
		TextField serverField = new TextField();
		loginPane.add(serverField, 2, 4);
		/*
		 * Create Registration Pane
		 */
		
		GridPane registerPane = new GridPane();
		registerPane.setPadding(new Insets(4, 4, 10, 10));
		registerPane.setAlignment(Pos.CENTER);
		
		Text registerTitle = new Text("Register");
		registerTitle.setFont(Font.font("Arial", 48));
		registerPane.add(registerTitle, 0, 0);
		
		
		registerPane.add(new Label("Username: "), 0, 1);
		TextField userFieldReg = new TextField();
		registerPane.add(userFieldReg, 0, 2);
		
		registerPane.add(new Label("Password: "), 1, 1);
		PasswordField passFieldReg = new PasswordField();
		registerPane.add(passFieldReg, 1, 2);
		
		Button registerBtn2 = new Button("Register");
		registerPane.add(registerBtn2, 0, 3);
		
		Scene register = new Scene(registerPane, 720, 480);
		
		submitLogin.setOnAction(e -> {
			login(userField.getText(), passField.getText());
			primaryStage.setScene(main);
		});
		
		registerBtn.setOnAction(e -> {
			primaryStage.setScene(register);
		});
		
		registerBtn2.setOnAction(e -> {
			// Create account on server
		});
	
		msg.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				sendMsg(msg.getText());
				messages.setVvalue(1);
				msg.setText("");
			}
		});
		
		
		Scene login = new Scene(loginPane, 720, 480);
		primaryStage.setTitle("JavaMSG");
		primaryStage.setScene(login);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	public static void sendMsg(String msg) {
		try {
			out.writeUTF(msg);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
	public static void login(String host, String portSt) {
		try {
			int port = Integer.parseInt(portSt);
			server = new Socket(host, port);
			
			in = new DataInputStream(server.getInputStream());
			out = new DataOutputStream(server.getOutputStream());
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid Port");
			return;
		}
		catch (IOException e) {
			System.out.println("Error Connecting to Server");
			return;
		}
		listen = new Task<Void>() {
			@Override protected Void call() throws Exception {
				String msg = "";
				try {
					
					while (true) {
						msg = in.readUTF(); 
						System.out.println("Read message from server");
						updateMessage(msgContent.getText() + msg + "\n");
						System.out.println("Added message from server to view");
					}
					
				}
				catch(Exception ioe) {
					ioe.printStackTrace();
					System.out.println("Failed to read message from server and add to view.");
				}
				return null;
			}
		};
		listen.run();
		msgContent.textProperty().bind(listen.messageProperty());
	}
}