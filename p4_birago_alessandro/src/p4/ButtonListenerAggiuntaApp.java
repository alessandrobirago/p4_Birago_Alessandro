package p4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ButtonListenerAggiuntaApp implements ActionListener {

	private Sistema sistema;
	private ArrayList<ArrayList<App>> listone;
	private ButtonGroup group;
	private JFrame frame;
	
	public ButtonListenerAggiuntaApp(Sistema s, ArrayList<ArrayList<App>> listone, ButtonGroup group, JFrame frame) {
		this.sistema = s;
		this.listone = listone;
		this.group = group;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b.getText().equals("Ok")) {
			
			String action = group.getSelection().getActionCommand();
			System.out.println(action);
			String nomeStore = action.split("_")[0];
			int indexApp = Integer.parseInt(action.split("_")[1]);
			
			sistema.aggiuntaApp(listone, nomeStore, indexApp);
			
			frame.dispose();			
		} else if(b.getText().equals("Annulla")) {
			//Chiudi fin
			frame.dispose();
		}
	}

	
	
}
