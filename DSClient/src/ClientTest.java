import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientTest {
	private final static boolean TCP = true;
	private final static boolean UDP = false;
	
	public static void main(String[] args) {
		int answer = JOptionPane.showConfirmDialog(null, "Choose   TCP Client: YES / UDP Client : NO ");
		if(answer == JOptionPane.YES_OPTION){
			Client clientKOU;
			String serverIP = JOptionPane.showInputDialog (null,"Server IP :","Server IP",JOptionPane.INFORMATION_MESSAGE);
			String socketNumber = JOptionPane.showInputDialog (null,"Server socket no :","Socket No",JOptionPane.INFORMATION_MESSAGE);
			clientKOU = new Client(serverIP,socketNumber,ClientTest.TCP);
			clientKOU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			clientKOU.startRunning();
		}
		if(answer == JOptionPane.NO_OPTION){
			Client clientKOU;
			String serverIP = JOptionPane.showInputDialog (null,"Server IP :","Server IP",JOptionPane.INFORMATION_MESSAGE);
			String socketNumber = JOptionPane.showInputDialog (null,"Server socket no:","Socket No",JOptionPane.INFORMATION_MESSAGE);
			clientKOU = new Client(serverIP,socketNumber,ClientTest.UDP);
			clientKOU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			clientKOU.startRunning();
		}
		if(answer == JOptionPane.CANCEL_OPTION){
			System.exit(0);
		}

	}
}
