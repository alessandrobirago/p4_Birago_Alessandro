package p4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Classe per effettuare la normalizzazione dei dati convertiti e il loro inserimento nel database
 * 
 * @author Accetta Cristian
 *
 */
public class Normalizzatore {

	private Database db;
	
	public Normalizzatore(Database db) {
		this.db = db;
	}
	
	/**
     * Funzione per normalizzare i dati convertiti ed inserirli nel database
     *
     * @return boolean true se l'operazione è stata eseguita correttamente, false altrimenti
     * @throws SQLException 
     */
	
	//Aggiungere come parametri in ingresso lo store e la hashmap con cui stiamo lavorando, 
	//magari trammite un costruttore
	
    public boolean normalizzazioneDati(String app, String store, HashMap<Date, HashMap<String, Object>> hmap) {
    	
    	/*
    	 * 1)Leggo il file di conversione per ottenere le chiavi della hash map ed i campi per la mia tabella del DB.
    	 * 2)Per ogni data leggo i vari valori dettati dalle chiavi e genero delle query di update sulla mia tabella 
    	 */
//    	HashMap<Date, HashMap<String, Object>> hmap = new HashMap<Date, HashMap<String, Object>>();
//    	hmap = map;
//    	Set<Date> key;
		ArrayList<Date> keyd = new ArrayList<Date>();
		ArrayList<String> tcolums = new ArrayList<String>();
		ArrayList<String> kval = new ArrayList<String>();
    	ArrayList<String> type = new ArrayList<String>();
    	
    	SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
    	
    	String nomeTabella = "dati" + app + store;
		
    	//Riprendo le chiavi "date"
    	Set<Date> key = hmap.keySet();
    	Iterator<Date> it = key.iterator();
    			
    	for(Date k : key) {
    		//System.out.println("key: " + k);
    		keyd.add(it.next());
    	}
        
    	//stampa di debugg
    	/*for(int i=0; i<keyd.size(); i++) {
    		System.out.println(keyd.get(i));
    	}*/
    	
    	//Leggo e riempo i vettori che mi serviranno per creare la tabella nel Db e per accedere alla hashmap
    	try {
			File f = new File("configs/Config"+store);
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader reader = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(reader);
					
			String line = buffer.readLine();
			while(line != null) {
				//System.out.println(line);
				String[] str = line.split("_",3);
				tcolums.add(str[0]);
				kval.add(str[1]);
				type.add(str[2]);
				line = buffer.readLine();
			}
			
			buffer.close();
		} catch (Exception e) {
			
		}
    	
    	//Stampa di debugg
    	/*for(int i=0; i<colums.size(); i++) {
    		System.out.println(colums.get(i)+" "+kval.get(i));
    	}*/
    	    	
    	//Stampa di debugg
    	/*for(int j=0; j<type.size(); j++) {
    		System.out.println(type.get(j));
    	}*/
        
    	/*
    	 * tcolums contiene tutte le colonne utili per la tabella nel DB
    	 * kval contiene tutte le chiavi dell'hashmap interna (verificato ok)
    	 * keyd contiene tutte le chiavi data dell'hashmap (verificato ok)
    	 */
    	
    	//Creo la tabella in cui inserire i dati dell'app
    	ArrayList<String> columns = new ArrayList<String>();
    	columns.add("id");
    	columns.add("data");
    	for(int i=0; i<tcolums.size(); i++) {
    		columns.add(tcolums.get(i));
    	}
    	
    	ArrayList<String> datatypes = new ArrayList<String>();
    	datatypes.add("int AUTO_INCREMENT");
    	datatypes.add("DATE");
    	for(int i=0; i<tcolums.size(); i++) {
    		datatypes.add(type.get(i));
    	}
    	
    	db.createTable("id", nomeTabella, Database.nomeDb, columns, datatypes);
    	
    	//Riempo la tabella del DB
    	//Manca solo la gestione dei tipi di risorse sia per la creazione della tabella sia per il cast del valore da hashmap
    	for(int i=0; i<keyd.size(); i++) {
	    	ArrayList<String> scolumns = new ArrayList<String>();
	    	scolumns.add("data");
	    	    	
	    	ResultSet result;
	    	result = db.select(Database.nomeDb, scolumns, nomeTabella, null);
	    	boolean temp = false;
	       	Date kdata = keyd.get(i);
	    	
	    	//Cerco di capire se devo aggiungere una riga al DB o aggiornarlo
	    	try {
	    		if(result == null) break;
				while(result.next()) {
					Date data = result.getDate("data");
					if(kdata.equals(data)) {
						temp = true;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	//Se non ho trovato nulla devo inserire una nuova riga
	    	if(temp==false) {
	    		ArrayList<String> icolumns = new ArrayList<String>();
	        	icolumns.add("data");
	        	icolumns.add(tcolums.get(0));
	        	
	        	ArrayList<Object> ivalues = new ArrayList<Object>();
	        	ivalues.add(dFormat.format(kdata));
	        	ivalues.add(hmap.get(keyd.get(i)).get(kval.get(0)));
	        	db.insert(ivalues, nomeTabella, Database.nomeDb, icolumns);
	    	}
	    	
	    	//Altrimenti vedo se devo aggiornare la riga alla data presa in considerazione
	    	
    		for(int j=1; j<kval.size(); j++) {
	    		String condition;
		    	
				ArrayList<String> ucolumns = new ArrayList<String>();
	    		ucolumns.add(tcolums.get(j));
	    		ArrayList<Object> values = new ArrayList<Object>();
	        	values.add(hmap.get(keyd.get(i)).get(kval.get(j)));
	        	condition = "data = '" + dFormat.format(kdata) + "'";
	    		db.update(Database.nomeDb, nomeTabella, ucolumns, values, condition);
	    		
	    	}
    	}
    	return false;
    }
 
}