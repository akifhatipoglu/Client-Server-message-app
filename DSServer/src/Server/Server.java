package Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Server extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private DatagramSocket datagramsocket;
	private DatagramPacket udpInput,udpOutput;
	private int serverSocket;
	private byte [] data = new byte[1024];
	private final boolean TCP = true;
	private final boolean UDP = false;
	private boolean	connectionType;

	// constructor
	public Server(String serverSocket,boolean connectionType) {
		super("KOU MSN Server");
		Arrays.fill(data, (byte)0);
		this.connectionType = connectionType;
		this.serverSocket = Integer.parseInt(serverSocket);
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(600, 300);
		setVisible(true);
	}

	// set up and run the server
	public void startRunning() {
		try {
			if(connectionType == TCP){
				server = new ServerSocket(serverSocket, 100);
			}
			if(connectionType == UDP){
				datagramsocket = new DatagramSocket(serverSocket);
			}
			while (true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
				} catch (EOFException eofException) {
					showMessage("\n Server connection has terminated! ");
				} finally {
					closeCrap();
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// wait for connection, then display connection information
	private void waitForConnection() throws IOException {
		showMessage(" waiting to client ... \n");
		if(connectionType == TCP){
			connection = server.accept();
			showMessage(" Connected: "
					+ connection.getInetAddress().getHostName());
		}
		
	}

	// get stream to send and receive data
	private void setupStreams() throws IOException {
		if(connectionType == TCP){
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
		}
		if(connectionType == UDP){
			Arrays.fill(data, (byte)0);
			udpInput = new DatagramPacket(data, data.length);
			udpOutput =  new DatagramPacket(data, data.length);
		}
		showMessage("\n Streams is ready! \n");
	}

	// during the chat conversation
	private void whileChatting() throws IOException {
		String message = " Successful connection! ";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				if(connectionType == TCP){
					message = (String) input.readObject();
					showMessage("\n" + message);
					Toolkit.getDefaultToolkit().beep();
				}
				if(connectionType == UDP){
					if(!message.equals(new String(data).trim())){
						message = new String(data).trim();
						showMessage("\nClient - " + message);
						Toolkit.getDefaultToolkit().beep();
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n Error!");
			}
		} while (!message.equals("CLIENT - END"));
	}

	// close streams and sockets after you are done chatting
	private void closeCrap() {
		showMessage("\n Connection closing ... \n");
		ableToType(false);
		try {
			if(connectionType == TCP){
				output.close();
				input.close();
				connection.close();
			}
			if(connectionType == UDP){
				datagramsocket.close();
			}
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// send a message to client
	private void sendMessage(String message) {
		try {
			if(connectionType == TCP){
				output.writeObject("SERVER - " + message);
				output.flush();
			}
			if(connectionType == UDP){
				datagramsocket.receive(udpInput);
				udpOutput.setAddress(udpInput.getAddress());
				udpOutput.setPort(udpInput.getPort());
				System.out.println(message);
				udpOutput.setData(message.getBytes());
				datagramsocket.send(udpOutput);
			}
			showMessage("\nSERVER - " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n ERROR: Error in message sending.");
		}
	}

	// updates chatWindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(text);
			}
		});
	}

	// let the user type stuff into their box
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}
	
	
	

}