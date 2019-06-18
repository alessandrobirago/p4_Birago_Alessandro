package p4;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ButtonListenerRimozioneApp implements ActionListener {

	private ButtonGroup group;
	private JFrame frame;
	private Sistema sistema;
	
	public ButtonListenerRimozioneApp(ButtonGroup group, JFrame frame, Sistema sistema) {
		this.group = group;
		this.frame = frame;
		this.sistema = sistema;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b.getText().equals("Ok")) {
			
			String action = group.getSelection().getActionCommand();
			int indexApp = Integer.parseInt(action);
			
			sistema.rimozioneApp(indexApp);
			
			frame.dispose();			
		} else if(b.getText().equals("Annulla")) {
			//Chiudi fin
			frame.dispose();
		}
	}

}
