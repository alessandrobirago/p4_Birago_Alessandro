package p4;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ButtonListenerDate implements ActionListener{

	private Sistema s;
	private App a;
	private ArrayList<JButton> buttons;
	private String buttonType;
	
	public ButtonListenerDate(Sistema s, App a, ArrayList<JButton> b, String bt){
		super();
		this.s = s;
		this.a = a;
		this.buttons = b;
		this.buttonType = bt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame2 = new JFrame("Inserire date dd/mm/yyyy");
		frame2.setVisible(true);
    	int height = 500;
    	int width = 500;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame2.setSize(width, height);
		frame2.setLocation((int)((dim.getWidth() - frame2.getWidth()) / 2), (int)((dim.getHeight() - frame2.getHeight()) / 2));
		frame2.setResizable(true);
		frame2.setVisible(true);
		JPanel panel2 = new JPanel();
		frame2.add(panel2);
		JLabel da = new JLabel("Da");
		JLabel al = new JLabel("a");
		JButton b = new JButton("Invia");
		
		JTextField field = new JTextField(10);
		JTextField field2 = new JTextField(10); 
		panel2.add(da);
		panel2.add(field);
		panel2.add(al);
		panel2.add(field2);
		panel2.add(b);
		if(buttonType.equals("Visualizza Dati")) {
			ButtonListenerVisualizzazione blv = new ButtonListenerVisualizzazione(this.s, this.a, buttons, e.getActionCommand().toLowerCase(), field, field2);
			b.addActionListener(blv);
		}
		else if(buttonType.equals("Scarica Dati")){
			ButtonListenerScaricamento bls = new ButtonListenerScaricamento(this.s, this.a, buttons, e.getActionCommand().toLowerCase(), field, field2);
			b.addActionListener(bls);
		}
	}
	
}
