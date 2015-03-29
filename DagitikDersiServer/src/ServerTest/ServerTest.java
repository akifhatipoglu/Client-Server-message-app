package ServerTest;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Server.Server;

public class ServerTest {
	private final static boolean TCP = true;
	private final static boolean UDP = false;
	
	public static void main(String[] args) {
		int answer = JOptionPane.showConfirmDialog(null, "TCP SERVER ise YES / UDP SERVER ise NO seciniz");
		if(answer == JOptionPane.YES_OPTION){
			String socketNumber = JOptionPane.showInputDialog (null,"Server'a ait kullanacağınız Socket numarasını giriniz:","Socket No",JOptionPane.INFORMATION_MESSAGE); 
			Server serverKOU = new Server(socketNumber,ServerTest.TCP);
			serverKOU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverKOU.startRunning();
		}
		if(answer == JOptionPane.NO_OPTION){
			String socketNumber = JOptionPane.showInputDialog (null,"Server'a ait kullanacağınız Socket numarasını giriniz:","Socket No",JOptionPane.INFORMATION_MESSAGE); 
			Server serverKOU = new Server(socketNumber,ServerTest.UDP);
			serverKOU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverKOU.startRunning();
		}
		if(answer == JOptionPane.CANCEL_OPTION){
			System.exit(0);
		}
	}
}
