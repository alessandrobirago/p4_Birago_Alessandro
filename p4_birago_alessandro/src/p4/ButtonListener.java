package p4;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Classe per gestire il click dei pulsanti
 * @author Accetta Cristian
 */
public class ButtonListener implements ActionListener {

	private Sistema s;
	private App a;
	private ButtonListenerApp bla;
	private ArrayList<Store> listStore;
	
	public ButtonListener(Sistema s, App a, ArrayList<Store> listStore) {
		super();
		this.s = s;
		this.a = a;
		this.listStore = listStore;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b.getText().equals("Aggiungi App")) {
			//s.creazioneGruppo();
//			s.aggiuntaApp();
			
			JFrame frame = new JFrame("Aggiunta App");
	        frame.setLayout(new BorderLayout());
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        ButtonGroup buttonGroup = new ButtonGroup();

	        JLabel message = new JLabel("Scegli un'app da aggiungere\n");
	        panel.add(message);
	        
	        //Stampare lista store e loro app da inserire
	    	ArrayList<ArrayList<App>> listone = new ArrayList<ArrayList<App>>();
	        for(Store store : listStore) {
	        	ArrayList<App> listApps = store.lista(s.getDb());
	        	listone.add(listApps);
	        	
	        	JLabel nomeStore = new JLabel(store.getNome());
	        	panel.add(nomeStore);
	        	
	        	for(int i=0; i<listApps.size(); i++) {
	        		App tempApp = listApps.get(i);
	        		JRadioButton radioApp = new JRadioButton(tempApp.getNome());
	        		radioApp.setActionCommand(store.getNome() + "_" + i);
					buttonGroup.add(radioApp);
					panel.add(radioApp);
	        	}
	        }
	        frame.add(panel, BorderLayout.NORTH);
			
	        Container containerButtons = new JPanel();
	        containerButtons.setLayout(new FlowLayout());

	        ButtonListenerAggiuntaApp listener = new ButtonListenerAggiuntaApp(s, listone, buttonGroup, frame);
	        
	        JButton b1=new JButton("Ok");
			b1.addActionListener(listener);
			containerButtons.add(b1);

			JButton b2=new JButton("Annulla");
			b2.addActionListener(listener);
			containerButtons.add(b2);
			frame.add(containerButtons, BorderLayout.SOUTH);
			
			int width = 500;
			int height = 500;
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((int)((dim.getWidth() - frame.getWidth()) / 2 - width / 2), (int)((dim.getHeight() - frame.getHeight()) / 2 - height / 2));
			frame.setSize(width, height);
			frame.setVisible(true);
			
			
		} else if(b.getText().equals("Rimuovi App")) {
			//s.cancellazioneGruppo();
//			s.rimozioneApp();
			
			JFrame frame = new JFrame("Rimozione App");
	        frame.setLayout(new BorderLayout());
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        ButtonGroup buttonGroup = new ButtonGroup();

	        JLabel message = new JLabel("Scegli un'app da rimuovere\n");
	        panel.add(message);
	        
	        //Stampare lista store e loro app da inserire
	    	
	        for(int i=0; i<s.getApps().size(); i++) {
	        	App app = s.getApps().get(i);
        		JRadioButton radioApp = new JRadioButton(app.getNome());
        		radioApp.setActionCommand(Integer.toString(i));
				buttonGroup.add(radioApp);
				panel.add(radioApp);
	        }
	        frame.add(panel, BorderLayout.NORTH);
			
	        Container containerButtons = new JPanel();
	        containerButtons.setLayout(new FlowLayout());

	        ButtonListenerRimozioneApp listener = new ButtonListenerRimozioneApp(buttonGroup, frame, s);
	        
	        JButton b1=new JButton("Ok");
			b1.addActionListener(listener);
			containerButtons.add(b1);

			JButton b2=new JButton("Annulla");
			b2.addActionListener(listener);
			containerButtons.add(b2);
			frame.add(containerButtons, BorderLayout.SOUTH);
			
			int width = 500;
			int height = 500;
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((int)((dim.getWidth() - frame.getWidth()) / 2 - width / 2), (int)((dim.getHeight() - frame.getHeight()) / 2 - height / 2));
			frame.setSize(width, height);
			frame.setVisible(true);
			
			
			
		} else if(b.getText().equals("Aggiungi Store")) {
			s.aggiuntaStore(0, "");
		} else if(b.getText().equals("Rimuovi Store")) {
			s.rimozioneStore("");
		} else {
			//JOptionPane.showMessageDialog(null, "Pulsante " + b.getText() + " premuto!");
	    	int height = 500;
	    	int width = 500;
	    	
	    	//E' stata selezionata un'app, mostra interfaccia
	    	//	di selezione dello store
	    	
	    	bla = new ButtonListenerApp(this.s, this.a);
	    	
			JFrame f = new JFrame(b.getText());
			f.setLayout(new BorderLayout());
			
			Container c1 = new JPanel();
			c1.setLayout(new FlowLayout());
			
			JLabel info = new JLabel(b.getText());
			c1.add(info);
			
			f.add(c1, BorderLayout.NORTH);
			
			Container c2 = new JPanel();
			c2.setLayout(new FlowLayout());
			
			JButton buttonShow = new JButton("Visualizza Dati");
			c2.add(buttonShow);
			buttonShow.addActionListener(bla);
			
			JButton buttonDownload = new JButton("Scarica Dati");
			c2.add(buttonDownload);
			buttonDownload.addActionListener(bla);
			
			f.add(c2, BorderLayout.CENTER);
			
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation((int)((dim.getWidth() - f.getWidth()) / 2 - width / 2), (int)((dim.getHeight() - f.getHeight()) / 2 - height / 2));
			f.setSize(width, height);
			f.setResizable(true);
			f.setVisible(true);
		}
	}
	
}
