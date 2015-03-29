import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame{
   
   private JTextField userText;
   private JTextArea chatWindow;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private String message = "";
   private String serverIP;
   private Socket connection;
   private int serverSocket;
   private DatagramSocket datagramsocket;
   private InetAddress adres;
   private DatagramPacket udpInput,udpOutput;
   private byte[] data = new byte[1024];
   private boolean connectionType;
   private final boolean TCP = true;
   private final boolean UDP = false;
   
   
   //constructor
   public Client(String host,String serverSocket,boolean connectionType){
      super("Client KOU!");
      Arrays.fill(data, (byte)0);
      this.connectionType = connectionType;
      this.serverSocket = Integer.parseInt(serverSocket);
      serverIP = host;
      userText = new JTextField();
      userText.setEditable(false);
      userText.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent event){
               sendMessage(event.getActionCommand());
               userText.setText("");
            }
         }
      );
      add(userText, BorderLayout.NORTH);
      chatWindow = new JTextArea();
      add(new JScrollPane(chatWindow), BorderLayout.CENTER);
      setSize(600,300);
      setVisible(true);
   }
   
   //connect to server
   public void startRunning(){
      try{
         connectToServer();
         setupStreams();
         whileChatting();
      }catch(EOFException eofException){
         showMessage("\n Client bağlantısı sonlandırıldı.");
      }catch(IOException ioException){
         ioException.printStackTrace();
      }finally{
         closeCrap();
      }
   }
   
   //connect to server
   private void connectToServer() throws IOException{
      showMessage("Bağlanmaya çalışılıyor... \n");
      
      if(connectionType == TCP){
    	  connection = new Socket(InetAddress.getByName(serverIP), serverSocket);
    	  showMessage("Bağlanılan: " + connection.getInetAddress().getHostName() );
      }
      if(connectionType == UDP){
    	  datagramsocket = new DatagramSocket();
    	  adres = InetAddress.getByName(serverIP);
      }
     }
   
   //set up streams to send and receive messages
   private void setupStreams() throws IOException{
	  if(connectionType == TCP){
	      output = new ObjectOutputStream(connection.getOutputStream());
	      output.flush();
	      input = new ObjectInputStream(connection.getInputStream());
	  }
	  if(connectionType == UDP){
		  Arrays.fill(data, (byte)0);
	      udpOutput = new DatagramPacket(data, data.length,adres,serverSocket);
	      udpInput = new DatagramPacket(data, data.length,adres,serverSocket);
	  }
      showMessage("\n Streams hazır! \n");
   }
   
   //while chatting with server
   private void whileChatting() throws IOException{
      ableToType(true);
      do{
         try{
        	 if(connectionType == TCP){
        		 message = (String) input.readObject();
        	 }
        	 if(connectionType == UDP){
        		 datagramsocket.receive(udpInput);
                 message = new String(data).trim();
        	 }
            showMessage("\n" + message);
            Toolkit.getDefaultToolkit().beep();
         }catch(ClassNotFoundException classNotfoundException){
            showMessage("\n hata...");
         }
      }while(!message.equals("SERVER - END"));
   }
   
   //close the streams and sockets
   private void closeCrap(){
      showMessage("\n Kapatılıyor...");
      ableToType(false);
      try{
    	 if(connectionType == TCP){ 
    		 output.close();
    		 input.close();
    		 connection.close();
    	 }
    	 if(connectionType == UDP){
    		 datagramsocket.close();
    	 }
      }catch(IOException ioException){
         ioException.printStackTrace();
      }
   }
   
   //send messages to server
   private void sendMessage(String message){
      try{
    	  if(connectionType == TCP){ 
    		  output.writeObject("CLIENT - " + message);
    		  output.flush();
    	  }
    	  if(connectionType == UDP){ 
    		  udpOutput.setData(message.getBytes());
    		  datagramsocket.send(udpOutput);
    	  } 
         showMessage("\nCLIENT - " + message);
      }catch(IOException ioException){
         chatWindow.append("\n Mesaj gönderiminde hata!");
      }
   }
   
   //change/update chatWindow
   private void showMessage(final String m){
      SwingUtilities.invokeLater(
         new Runnable(){
            public void run(){
               chatWindow.append(m);
            }
         }
      );
   }
   
   //gives user permission to type crap into the text box
   private void ableToType(final boolean tof){
      SwingUtilities.invokeLater(
         new Runnable(){
            public void run(){
               userText.setEditable(tof);
            }
         }
      );      
   }
}