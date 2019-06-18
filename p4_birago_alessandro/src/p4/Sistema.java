package p4;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * Classe per gestire il sistema
 * 
 * @author Birago Alessandro, Accetta Cristian
 *
 */
public class Sistema extends JFrame {
 
    /** Associations */
    private Database db;
    private ArrayList<App> apps;
    private ArrayList<Store> stores;

    /** Variabili per l'interfaccia grafica*/
    int height;
	int width;
	private ButtonListener bl;
	private Container c;
	private Container c1;
	private Container c2;
	
	
	/**
     * Costruttore per utilizzare l'interfaccia grafica
     */
    public Sistema() {
    	super("Manipolazione di dati statistici da app stores");
    	
    	db = new Database();
    	apps = new ArrayList<>();
    	stores = new ArrayList<>();
    	
    	//Inizializza il db
    	initDB();

    	//Recupera elenco di Store e App dal DB
    	getInfoFromDb();
    	
    	//avvia sequela di operazioni sulle app nel sistema
    	workOnData();
    	
    	//Inizializza interfaccia grafica
    	initGUI();
    }
    
    
    private void workOnData() {
    	//per ogni app
    	for(App a : apps) {
    		//per ogni store dell'app
    		for(Store s : a.getStore()) {
//    			Convertitore convert = new Convertitore();

    			//IMPORT
//    			convert.files = s.importazioneDati();	//come dire quale app?	
//														//un modo è parametrizzare i nomi del conf file
//    			
//    			//CONVERSIONE
//    			HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(s);
    			
    			HashMap<Date, HashMap<String, Object>> map = estrazioneDati(a, s);
    			
    			//NORMALIZZAZIONE
    			Normalizzatore normal = new Normalizzatore(db);
    			normal.normalizzazioneDati(a.getNome(), s.getNome(), map);
    		}
    		
    		//AGGREGAZIONE
    		a.AggregazioneDati();
    	}
	}


	private void initGUI() {
    	height = 500;
    	width = 500;
    	
    	c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		c1 = new JPanel();
		c1.setLayout(new FlowLayout());
		JLabel info = new JLabel("App inserite nel sistema:");
		c1.add(info);
		c.add(c1, BorderLayout.NORTH);
		
		c2 = new JPanel();
		c2.setLayout(new FlowLayout());	
		for(int i = 0; i < apps.size(); i++) {
			bl = new ButtonListener(this, apps.get(i), stores);
			JButton button = new JButton(apps.get(i).getNome());
			c2.add(button);
			button.addActionListener(bl);
		}
		c.add(c2, BorderLayout.CENTER);
		
		bl = new ButtonListener(this, null, stores);	//Listener per i pulsanti in basso
		Container c3 = new JPanel();
		c3.setLayout(new FlowLayout());
		JButton buttonAddApp = new JButton("Aggiungi App");
		c3.add(buttonAddApp);
		buttonAddApp.addActionListener(bl);
		JButton buttonRemoveApp = new JButton("Rimuovi App");
		c3.add(buttonRemoveApp);
		buttonRemoveApp.addActionListener(bl);
		
//		JButton buttonAddStore = new JButton("Aggiungi Store");
//		c3.add(buttonAddStore);
//		buttonAddStore.addActionListener(bl);
//		JButton buttonRemoveStore = new JButton("Rimuovi Store");
//		c3.add(buttonRemoveStore);
//		buttonRemoveStore.addActionListener(bl);
		
		c.add(c3, BorderLayout.SOUTH);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)((dim.getWidth() - this.getWidth()) / 2 - width / 2), (int)((dim.getHeight() - this.getHeight()) / 2 - height / 2));
		setSize(width, height);
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}
	
	/**
	 * Inizializzazione del DB
	 */
	private void initDB() {
		db.createDatabase(Database.nomeDb);
		
		/* CREATE store */
    	ArrayList<String> columnsStore = new ArrayList<String>();
    	ArrayList<String> datatypesStore = new ArrayList<String>();
    	columnsStore.add("id");
    	datatypesStore.add("int AUTO_INCREMENT");
    	columnsStore.add("nome");
    	datatypesStore.add("Varchar(20)");
    	
    	db.createTable("id", Database.nomeTabStore, Database.nomeDb, columnsStore, datatypesStore);
    	
    	/* CREATE apps */
    	//Creo la tabella in cui inserire le mie apps
    	ArrayList<String> columnsApps = new ArrayList<String>();
    	ArrayList<String> datatypesApps = new ArrayList<String>();
    	columnsApps.add("id");
    	datatypesApps.add("int AUTO_INCREMENT");
    	columnsApps.add("nome");
    	datatypesApps.add("Varchar(20)");
//    	for(int i=0; i<stores.size(); i++) {
//    		columnsApps.add(stores.get(i).getNome());
//    		datatypesApps.add("BOOLEAN DEFAULT 0");
//    	}
    	
    	db.createTable("id", Database.nomeTabApp, Database.nomeDb, columnsApps, datatypesApps);
		
    	
    	
//		ArrayList<String> colonne = new ArrayList<String>();
//		ArrayList<String> tipo = new ArrayList<String>();
//		colonne.add("Date");
//		colonne.add("PackageName");
//		colonne.add("Visitors");
//		colonne.add("Installers");
//		colonne.add("Buyers");
//		colonne.add("DailyCrashes");
//		colonne.add("DailyANRs");
//		colonne.add("DailyDeviceInstalls");
//		colonne.add("DailyDeviceUninstalls");
//		colonne.add("DailyDeviceUpgrades");
//		colonne.add("TotalUserInstalls");
//		colonne.add("DailyUserInstalls");
//		colonne.add("DailyUserUninstalls");
//		colonne.add("ActiveDeviceInstalls");
//		colonne.add("DailyAverageRating");
//		colonne.add("TotalAverageRating");
//		colonne.add("InstallersRetainedFor7Days");
//		colonne.add("InstallersTo7DaysRetentionRate");
//		colonne.add("InstallersRetainedFor15Days");
//		colonne.add("InstallersTo15DaysRetentionRate");
//		colonne.add("InstallersRetainedFor30Days");
//		colonne.add("InstallersTo30DaysRetentionRate");
//		tipo.add("DATE");
//		tipo.add("VARCHAR(100)");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("INT");
//		tipo.add("FLOAT");
//		tipo.add("FLOAT");
//		tipo.add("INT");
//		tipo.add("FLOAT");
//		tipo.add("INT");
//		tipo.add("FLOAT");
//		tipo.add("INT");
//		tipo.add("FLOAT");
//		
//		db.createTable("Date", "DatiAppGoogle", "p4", colonne, tipo);
	}
	
	
	/**
	 * Recupera le app dal DB
	 */
//	public ArrayList<App> getAppsFromDB(Database db) {
//		ArrayList<String> columns1 = new ArrayList<String>();
//		columns1.add("nome");
//		ResultSet table1 = db.select(Database.nomeDb, columns1, Database.nomeTabApp, null);
//		App app = null;
//		ArrayList<App> apps = new ArrayList<App>();
//		try {
//			while(table1.next()) {
//				app = new App(table1.getString("nome"), db);
//				apps.add(app);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return apps;
//		/*App a1 = new App("app", db);
//    	App a2 = new App("app2", db);
//    	apps.add(a1);
//    	apps.add(a2);
//    	Store google = new Store("Google", apps);
//    	stores.add(google);
//    	a1.addStore(google);
//    	a2.addStore(google);
//    	App a3 = new App("app3", db);
//    	apps.add(a3);
//    	Store windows = new Store("Windows", apps);
//    	stores.add(windows);
//    	a1.addStore(windows);
//    	a2.addStore(windows);
//    	a3.addStore(windows);*/
//	}
	
	
	/**
	 * Fa query di Select sul DB e recupera l'elenco degli Store nel sistema
	 * @param db Database
	 * @return elenco degli Store
	 */
//	private ArrayList<Store> getStoreFromDb(Database db) {
//		ArrayList<String> colNameStore = new ArrayList<String>();
//		colNameStore.add("nome");
//		
//		ResultSet resSelectStore = db.select(Database.nomeDb, colNameStore, Database.nomeTabStore, null);
//		
//		Store store = null;
//		ArrayList<Store> stores = new ArrayList<>();
//		try {
//			while(resSelectStore.next()) {
//				store = new Store(resSelectStore.getString("nome"));
//				stores.add(store);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return stores;
//	}
	
	
	/**
	 * Ottiene info su app e store dal db
	 */
	private void getInfoFromDb() {
		//Recupero STORE
		ArrayList<String> colSelectStores = new ArrayList<String>();
		colSelectStores.add("nome");
		
		ResultSet resSelectStores = db.select(Database.nomeDb, colSelectStores, Database.nomeTabStore, null);
		
		Store store = null;
//		ArrayList<Store> stores = new ArrayList<>();
		try {
			while(resSelectStores.next()) {
				store = new Store(resSelectStores.getString("nome"));
				stores.add(store);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Recupero APP	
		ArrayList<String> colSelectApps = new ArrayList<String>();
		colSelectApps.add("*");
		ResultSet resSelectApps = db.select(Database.nomeDb, colSelectApps, Database.nomeTabApp, null);
		App app = null;
//		ArrayList<App> apps = new ArrayList<App>();
		try {
			while(resSelectApps.next()) {
				app = new App(resSelectApps.getString("nome"), db);
				apps.add(app);
				for(Store s : stores) {
					//Se l'app proviene dallo Store s 
	    			if(resSelectApps.getInt(s.getNome())==1) {
	    				//Aggiunge lo Store all'ArrayList<Store> dell'App
	    				app.addStore(s);
	    				//Aggiunge l'App all'ArrayList<App> dello Store
	    				s.addApp(app);
	    			}
	    		}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	

	/**
     * Funzione per estrarre i dati dagli store<br>
     *
     * @return hashmap contentente tutti i dati estratti 
     */
    public HashMap<Date, HashMap<String, Object>> estrazioneDati(App a, Store s) {
//    	App a = findAppInList(apps, nomeApp);
//    	Store s = findStoreInList(a.getStore(), nomeStore);
    	
    	//Funzione per il testing: qui viene fatto tutto solo sullo store di Google
    	//pero' andra' fatto per tutti gli Store (per ora FinsaStore e Google)
//    	Store s = new Store("Google");

    	Convertitore convert = new Convertitore();
    	//Vado a importare i dati per quello specifico store
    	//Ritorna la lista con il percorso dei file da cui acquisire i dati
    	convert.files = s.importazioneDati(a);
    	
    	//Stampa di test dell'importazione
    	System.out.println("Testing importazioneDati():");
    	System.out.println(convert.files);
    	
    	//Vado a richiamare la conversione dati
    	HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(s);
    	
    	//Stampa di test della conversione
    	System.out.println("Testing conversioneDati(), store "+s.getNome()+":");
    	System.out.println(map);
    	
    	return map;
    }
    
    /**
     * Funzione per aggiungere uno store nel sistema
     *
     * @return boolean true se l'operazione è stata eseguita correttamente, false altrimenti
     */
    public boolean aggiuntaStore(int id, String nomeStore) {
    	System.out.println("INSERIRE ID E NOME DELLO STORE DA AGGIUNGERE:");
    	boolean trovato = false;
    	//Se lo Store inserito non è corretto continuo a richiedere il reinserimento dei parametri
    	while (!trovato){
    		System.out.println("ID:");
        	Integer num = Input.readInt();
        	System.out.println("NOME STORE:");
        	String name = Input.readLine();
        	
        	//Controllo la correttezza dell'ID (numero positivo, assente nel DB)
        	if (num>=0 && num<= Integer.MAX_VALUE) {
        		db = new Database();
        		//Creo la query per il DB
        		ArrayList<String> selezionare1 = new ArrayList<String>();
            	selezionare1.add(" COUNT(*)");
            	String condizione1 = "id = '" + num + "'";
            	//Eseguo la query
            	ResultSet r1 = db.select(Database.nomeDb, selezionare1, Database.nomeTabStore, condizione1);
            	int i = 1;
        		int countId = 1;
        		try {
        			while(r1.next()){
        				countId = r1.getInt(i);
        			}
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		
        		//Controllo la presenza dello store nel DB (conto il numero di righe della tabella Store)
        		if (countId==0){
        			//Creo ed eseguo la query per il DB
                	ArrayList<String> selezionare2 = new ArrayList<String>();
                	selezionare2.add(" COUNT(*)");
                	String condizione = "nome = '" + name + "'";
                	ResultSet r2 = db.select(Database.nomeDb, selezionare2, Database.nomeTabStore, condizione);
            		int j = 1;
            		int countNome = 1;
            		try {
            			while(r2.next()){
            				countNome = r2.getInt(j);
            			}
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
            		
            		//Se lo Store non è ancora presente lo aggiungo nel DB
            		if (countNome==0){
        				trovato = true;
        				ArrayList<Object> valori = new ArrayList<Object>();
        				ArrayList<String> colonneStore = new ArrayList<String>();
        				colonneStore.add("id");
        				colonneStore.add("nome");
        				valori.add(num);
        				valori.add(name);
        				//Aggiunta Store nel DB
        				db.insert(valori, Database.nomeTabStore, Database.nomeDb, colonneStore);
        				
        				//Aggiunta colonna nella tabella apps
        				db.alterTable(Database.nomeDb, Database.nomeTabApp, "BOOLEAN DEFAULT 0", name, "ADD");
        			} else {
        				System.out.println("Store già presente nel sistema");
        				System.out.println("Premere 1 per riporvare oppure 0 per uscire");
        				Integer uscita = Input.readInt();
        				if (uscita==0) {
        					System.out.println("Nessuno store aggiunto");
        					return false;
        				} else {
        					System.out.println("Riprova con un nuovo store");
        				}
        			}
        		} else {
        			System.out.println("L'ID immesso è già in uso per un altro store. Prova con un nuovo ID");
        		}
        	} else {
        		System.out.println("L'ID immesso non è valido (usa un valore positivo compreso tra 1 e 2.147.483.647");
        	}
    	}
    	System.out.println("Store aggiunto correttamente");
    	return true;
    }
    
    /**
     * Funzione per rimuovere uno store dal sistema
     *
     * @return boolean true se l'operazione è stata eseguita correttamente, false altrimenti
     */
    public boolean rimozioneStore(String nome){
    	//Cerco gli Store presenti nel sistema
    	db = new Database();
    	ArrayList<String> selezionare1 = new ArrayList<String>();
    	selezionare1.add(" COUNT(*)");
    	String condizione = null;
    	ResultSet r1 = db.select(Database.nomeDb, selezionare1, Database.nomeTabStore, condizione);
		int i = 1;
		int count = 0;
		try {
			while(r1.next()){
				count = r1.getInt(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Verifico che ci sia almeno uno Store da rimuovere
		if (count>0){
	    	ArrayList<String> selezionare2 = new ArrayList<String>();
	    	selezionare2.add("nome");
	    	ResultSet r2 = db.select(Database.nomeDb, selezionare2, Database.nomeTabStore, condizione);
			int j = 1;
			//Stampo gli store da rimuovere
			System.out.println("SELEZIONARE LO STORE DA RIMUOVERE:");
			try {
				while(r2.next()){
					System.out.println("- " + r2.getObject(j));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("(Inserire il nome dello store da rimuovere oppure 0 per uscire)");
			boolean trovato = false;
			
			//Controllo che il valore in input sia corretto
			while (!trovato) {
    			String name = Input.readLine();
    			//Lascio la possibilità di uscire immettendo 0
            	if (name.equals("0")) {
            		System.out.println("Uscita");
            		return false;
            	} else {
            		//Controllo che lo Store da rimuovere esista davvero
            		ArrayList<String> selezionare3 = new ArrayList<String>();
                	selezionare3.add(" COUNT(*)");
                	String condizione3 = "nome = '" + name + "'";
                	ResultSet r3 = db.select(Database.nomeDb, selezionare3, Database.nomeTabStore, condizione3);
            		int l = 1;
            		int count2 = 0;
            		try {
            			while(r3.next()) {
            				count2 = r3.getInt(l);
            			}
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
            		//Verifico che ci sia almeno uno Store da rimuovere
            		if (count2>0) {
            			String condizione4 = "nome = '" + name + "'";
            			//Rimuovo lo Store
            			db.delete(Database.nomeDb, Database.nomeTabStore, condizione4);
            			
            			//Aggiunta colonna nella tabella apps
        				db.alterTable(Database.nomeDb, Database.nomeTabApp, "BOOLEAN DEFAULT 0", name, "DROP");
            			
            			trovato = true;
            		} else {
            			System.out.println("Inserimento errato. Riprova oppure inserisci 0 per uscire");
            		}
            	}
			}
		} else {
			System.out.println("Nessuno store rimovibile");
    		return false;
		}
		System.out.println("Store eliminato correttamente");
		return true;
    }
    
    /**
     * Funzione per aggiungere una app nel sistema
     *
     * @return boolean true se l'operazione è stata eseguita correttamente, false altrimenti
     */
    public boolean aggiuntaApp(ArrayList<ArrayList<App>> listone, String names, int indice) {
    	/*
    	 * 1)Riprendere da ogni store una lista fittizia di apps
    	 * 2)Stampare all'utente tutte le app
    	 * 3)Una volta selezionata un'app bisogna: 
    	 * 	 verificare che l'apps selezionata non sia gia presente(nome e store uguali)
    	 * 	 in caso contrario inserire/aggiornata la tabella del DB.
    	 * 4)Aggiornare il vettore delle apps.
    	 */
    	
//    	ArrayList<ArrayList<App>> listone = new ArrayList<ArrayList<App>>();
//    	ArrayList<App> lista = new ArrayList<App>();
//    	
//    	//Creao un "listone di tutte le app presenti nei nostri app store e le visualizzo
//    	for(int i=0; i<stores.size(); i++) {
//    		lista = stores.get(i).lista(db);
//    		listone.add(lista);
//    		System.out.println(stores.get(i).getNome());
//    		for(int j=0; j<lista.size(); j++) {
//    			System.out.println(j+1+")"+lista.get(j).getNome());
//    		}
//    	}
//    	
//    	//Scelta dell'utente dell'app da inserire
//    	String input = new String();
//    	System.out.println(" ");
    	
//     	while(true) {
//       		System.out.println("Digitare lo store e il numero dell'applicazione da inserire. Premere 0 per terminare. ");
//       		input = Input.readLine();
//
//       		if(input.equals("0")) {
//       			break;
//       		}
//
//       		//Separazione del numero dell'App da quello dello Store
//			String[] str = input.split(" ",2);
//			String names = str[0];
//			String napp = str[1];
//		
//			int indice = 0;
//			try {
//				indice = Integer.parseInt(napp);
//			} catch(NumberFormatException e) {
//				System.out.println("L'applicazione che vuoi inserire non è valida");
//				continue;
//			}
	 
			//Tramite l'indice e lo store selezionato vado a cercare nel listone l'app da inserire.
			App app = null;
			String newstore = null;
		
			boolean find = false;
			
			for(int i=0; i<stores.size(); i++) {
				if(stores.get(i).getNome().equals(names)) {
					if (indice < listone.get(i).size() && indice >= 0) {
						app = listone.get(i).get(indice);
						newstore = app.getOneStore(0);
						find = true;
					} else {
						System.out.println("L'applicazione che vuoi inserire non � valida");
						continue;
					}
					break;
				}
				
			}
			
			if (find == false) {
				System.out.println("Digitazione App Store errata");
//				continue;
			}
				
			//Controllo se l'app digitata dall'utente esiste gi� come app inserita
				 
			//Se la lista di app e' vuota aggiungo la nuova app nell'array e nel db con query di insert
			if(apps.size() == 0) {
				apps.add(app);
				
				ArrayList<String> columns = new ArrayList<String>();
	        	columns.add("nome");
	        	columns.add(newstore);
	        	ArrayList<Object> values = new ArrayList<Object>();
	        	values.add(app.getNome());
	        	values.add(1);
	        	db.insert(values, Database.nomeTabApp, Database.nomeDb, columns);
				
	        	System.out.println("Applicazione Aggiunta con successo");
			} else {
				boolean temp = false;
				for(App ap : apps) {
		
					//Se nella lista di app interna al sistema, ne trovo una con stesso nome e stessa lista di store
					//ritorno un errore
					
					boolean confronto = confronto(app.getStore(), ap.getStore());
					
					//Stampa di debug
					//System.out.println(confronto);
					
					//Se stesso nome app e lista store uguale non faccio nulla
					if(app.getNome().equals(ap.getNome())  &&  confronto==true){
//						System.out.println("\nApplicazione gi� inserita");
						JOptionPane.showMessageDialog(null, "Applicazione gi� inserita");
						temp=true;
					}
				
					//Se stesso nome app e lista store diversa aggiorno array e faccio query di update
					if(app.getNome().equals(ap.getNome())  &&  confronto==false){
						System.out.println("\nApplicazione Aggiornata");
						ap.addStore(app.getStore().get(0));	//Aggiunge store all'app gi� presente nel Sistema
						
						//Aggiorna la tabella apps
						ArrayList<String> ucolumns = new ArrayList<String>();
			    		ucolumns.add(newstore);
			    		ArrayList<Object> values = new ArrayList<Object>();
			        	values.add(1);
			        	String condition = "nome = '" + app.getNome() + "'";
			    		db.update(Database.nomeDb, Database.nomeTabApp, ucolumns, values, condition);
			    		
						temp=true;
					}
	
				}

				//Se nome e lista store sono diversi allora aggiungo la nuova app nell'array
				// e nel db con query di insert
				if(temp == false) {
					apps.add(app);
					
					ArrayList<String> columns = new ArrayList<String>();
		        	columns.add("nome");
		        	columns.add(newstore);
		        	ArrayList<Object> values = new ArrayList<Object>();
		        	values.add(app.getNome());
		        	values.add(1);
		        	db.insert(values, Database.nomeTabApp, Database.nomeDb, columns);
		        	
					System.out.println("\nApplicazione Aggiunta con successo");
    				
    			}
			}	
       	
//       	}
       	
     	//Stampa di debugg
     	String stampa ="";
    	for(int i=0; i<apps.size(); i++) {
    		stampa ="";
    		for(int j=0; j<apps.get(i).getStore().size(); j++) {
    			stampa = stampa + apps.get(i).getOneStore(j)+" ";
    		}
    		System.out.println(apps.get(i).getNome() +" Store provenienza:"+stampa);
    	}
      		
    	c.setVisible(false);
    	c.remove(c2);
    	c2 = new JPanel();
		c2.setLayout(new FlowLayout());
		for(int i = 0; i < apps.size(); i++) {
			bl = new ButtonListener(this, apps.get(i), stores);
			JButton button = new JButton(apps.get(i).getNome());
			c2.add(button);
			button.addActionListener(bl);
		}
		c.add(c2, BorderLayout.CENTER);
    	c.setVisible(true);
    	
    	workOnData();
    	
    	return true;
    }
    
    /**
     * Funzione per rimuovere una app dal sistema
     *
     * @return boolean true se l'operazione � stata eseguita correttamente, false altrimenti
     */
    public boolean rimozioneApp(int indice) {
    	
    	/*
    	 * 1)Ritornare tutte le apps inserite nel DB
    	 * 2)Stamparle all'utente
    	 * 3)Selezionata un'app cancellarla dalla tabella del DB
    	 */
    
    	//Scelta dell'utente dell'app da cancellare
//    	String input = new String();
//    	System.out.println(" ");
    	
//     	while(true){
//     		     		
//     		String stampa ="";
//        	for(int i=0; i<apps.size(); i++) {
//        		stampa ="";
//        		for(int j=0; j<apps.get(i).getStore().size(); j++) {
//        			stampa = stampa + apps.get(i).getOneStore(j)+" ";
//        		}
//        		System.out.println(i+1+") "+apps.get(i).getNome() +" Store provenienza:"+stampa);
//        	}
//        	
//        	
//       		System.out.println("Digitare il numero dell'applicazione da cancellare premere 0 per terminare ");
//       		input = Input.readLine();
//
//       		if(input.equals("0")) {
//       			break;
//       		}
//
//					
//			int indice=0;
//			try{
//				indice = Integer.parseInt(input);
//			}catch(NumberFormatException e) {
//				System.out.println("L'applicazione che vuoi inserire non è valida");
//				continue;
//			}
			
			App a = apps.get(indice);

			//Cancellazione tabelle dell'app
			int i = 0;
			for(Store s : a.getStore()) {
				db.dropTable(Database.nomeDb, "dati" + a.getNome() + s.getNome());
				if(++i > 1) {	//incrementa. se pi� di un store, provo a rimuovere tab aggregati
					db.dropTable(Database.nomeDb, "dati" + a.getNome() + "googlefinsa");	
				}
			}
			
			//Cancellazione da tab App
			String condizione1 = "nome = '" + a.getNome() + "'";
			System.out.println(condizione1);
			db.delete(Database.nomeDb, Database.nomeTabApp, condizione1);
			
			//Cancellazione oggetto dal sistema
			apps.remove(apps.get(indice));
			System.out.println("Applicazione cancellata con successo");
			
//     	}
     	
     	c.setVisible(false);
    	c.remove(c2);
    	c2 = new JPanel();
		c2.setLayout(new FlowLayout());
		for(int j = 0; j < apps.size(); j++) {
			bl = new ButtonListener(this, apps.get(j), stores);
			JButton button = new JButton(apps.get(j).getNome());
			c2.add(button);
			button.addActionListener(bl);
		}
		c.add(c2, BorderLayout.CENTER);
    	c.setVisible(true);
    	
    	return true;
    }
    
    /**
     * Recupera una certa App contenuta in una lista in base al suo nome
     * @param listApp
     * @param nomeApp
     * @return
     */
    private App findAppInList(ArrayList<App> listApp, String nomeApp) {
    	for(App a : listApp) {
    		if(a.getNome().equals(nomeApp)) {
    			return a;
    		}
    	}
    	return null;
    }
    
    /**
     * Recupera uno Store in una lista in base al suo nome
     * @param listStore
     * @param nomeStore
     * @return
     */
    private Store findStoreInList(ArrayList<Store> listStore, String nomeStore) {
    	for(Store s : listStore) {
    		if(s.getNome().equals(nomeStore)) {
    			return s;
    		}
    	}
    	return null;
    }
    
    
    //Metodo per capire se uno store(array list di 1 elemento) e' gia presente nella lista degli store
    //dell'app(array list di x elementi)
    private boolean confronto(List<Store> s1, List<Store> sn) {
    	boolean bool = false;
    	for(int i=0; i<s1.size(); i++) {
    		for(int j=0; j<sn.size(); j++) {
    			if(s1.get(i).getNome().equals(sn.get(j).getNome())) {
    				bool = true;
    				break;
    			}
    		}
    		if(bool == true) {
				break;
			}
    	}
    	
    	return bool;
    }
    
    
    public ArrayList<App> getApps() {
		return apps;
	}
    
 
}