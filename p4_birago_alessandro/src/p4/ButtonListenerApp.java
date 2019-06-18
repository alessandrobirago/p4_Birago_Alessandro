package p4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Classe per gestire il click dei pulsanti
 * @author Accetta Cristian
 */
public class ButtonListenerApp implements ActionListener {

	private Sistema s;
	private App a;
	
	public ButtonListenerApp(Sistema s, App a) {
		super();
		this.s = s;
		this.a = a;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b.getText().equals("Visualizza Dati") || b.getText().equals("Scarica Dati")) {
			//Recupera tutte le tabelle del DB
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("DISTINCT table_name");
			String condition = "table_schema = '" + Database.nomeDb+"'";
			ResultSet table = s.getDb().select("information_schema", columns, "COLUMNS", condition);
			
			//Recupera l'app dalla tabella delle app nel db
			ArrayList<String> colonne = new ArrayList<String>();
			colonne.add("google");
			colonne.add("finsa");
			String condition1 = "nome = '" + a.getNome() + "'";
			ResultSet tablel = s.getDb().select(Database.nomeDb, colonne, "apps", condition1);
			
	    	int height = 500;
	    	int width = 500;
	    	JFrame frame1 = new JFrame();
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame1.setLocation((int)((dim.getWidth() - frame1.getWidth()) / 2 - width / 2), (int)((dim.getHeight() - frame1.getHeight()) / 2 - height / 2));
			frame1.setSize(width, height);
			frame1.setResizable(true);
			frame1.setVisible(true);
			
			JPanel panel1 = new JPanel();
			frame1.add(panel1);
			frame1.setTitle(a.getNome());
			
			//Se sono presenti nel db le tabelle, aggiungi bottoni per ogni store dell'app 
			ArrayList<JButton> buttons = new ArrayList<JButton>();
			try {
				JButton button = null;
				while(table.next()) {
					if(table.getString("table_name").contains(a.getNome().toLowerCase())) {
						if(tablel.next()){
							int num_google = tablel.getInt("google");
							int num_finsa = tablel.getInt("finsa");
							if(num_google == 1){
								button = new JButton("Google");
								buttons.add(button);
								panel1.add(button);
							}
							if(num_finsa == 1) {
								button = new JButton("Finsa");
								buttons.add(button);
								panel1.add(button);
							}
							if(num_finsa == 1 && num_google == 1) {
								 button = new JButton("GoogleFinsa");
								 buttons.add(button);
								 panel1.add(button);
							}
						}
					}
				}
				
				for(int i = 0; i < buttons.size(); i++) {
					ButtonListenerDate bld = new ButtonListenerDate(this.s, this.a, buttons, b.getText());
					buttons.get(i).addActionListener(bld);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Pulsante " + b.getText() + " premuto!");
		}
	}
	
}
