package p4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JTextField;

public class ButtonListenerScaricamento implements ActionListener{

	private Sistema s;
	private App a;
	private ArrayList<JButton> buttons;
	private String store;
	private JTextField field;
	private JTextField field2;
	
	public ButtonListenerScaricamento(Sistema s, App a, ArrayList<JButton> b, String store, JTextField f, JTextField f2){
		super();
		this.s = s;
		this.a = a;
		this.buttons = b;
		this.store = store;
		this.field = f;
		this.field2 = f2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String date1 = field.getText();
		String date2 = field2.getText();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date result1 =  format.parse(date1);
			Date result2 =  format.parse(date2);
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			String resultDa = dt.format(result1);
			String resultA = dt.format(result2);
			
			String cond = "data >= '" + resultDa + "' AND data <= '" + resultA + "'";
			
			a.ScaricamentoDati("dati" + a.getNome().toLowerCase() + store, cond);
		} catch (ParseException | SQLException | IOException e1) {
			e1.printStackTrace();
		}
	}
		
}
