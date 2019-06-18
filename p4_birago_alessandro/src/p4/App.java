package p4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Classe per gestire la singola app
 * 
 * @author Accetta Cristian
 *
 */
public class App {
	
    /** Attributes */
    private String nome;

    /** Associations */
    private ArrayList<Store> stores;
    private Database db;
    

    public App(String nome, Database db) {
    	this.nome = nome;
    	this.db = db;
    	stores = new ArrayList<Store>();
    }
    
    public String getNome() {
    	return this.nome;
    }
	
	public ArrayList<Store> getStore() {
		return stores;
	}
	
	public String getOneStore(int i) {
		return stores.get(i).getNome();
	}
	
	public void addStore(Store store) {
		stores.add(store);
	}
    
	
    /**
     * Funzione per visualizzare tutti i dati dell'app presenti nel database
     * @throws SQLException 
     */
	public void VisualizzazioneDati(String nomeTab, String cond) throws SQLException {
    	
    	ArrayList<String> columns = new ArrayList<String>();
    	
    	columns.add("COUNT(ordinal_position) as colonne");
    	String condition = "table_schema = '"+ Database.nomeDb +"' AND table_name = '"+nomeTab+"'";
    	ResultSet n_column= db.select("information_schema", columns, "COLUMNS", condition);
    	int colonne = 0;
    	if(n_column.next()) {
    		 colonne = n_column.getInt("colonne");
    	}
    	
    	ArrayList<String> columns1 = new ArrayList<String>();
    	columns1.add("COUNT(*) as righe");
    	ResultSet n_rows = db.select(Database.nomeDb, columns1, nomeTab, cond);
    	int righe = 0;
    	if(n_rows.next()) {
   		 	righe = n_rows.getInt("righe");
    	}
    	
    	ArrayList<String> columns2 = new ArrayList<String>();
    	columns2.add("column_name");
    	ResultSet header = db.select("information_schema", columns2, "COLUMNS", condition);
		
		String[] columnHeader = new String[colonne];
		int j = 0;
		while(header.next()){
			columnHeader[j] = header.getString("column_name");
			j++;
		}
		
		ArrayList<String> columns3 = new ArrayList<String>();
		columns3.add("*");
		ResultSet sql1 = db.select(Database.nomeDb, columns3, nomeTab, cond);
		
		Object[][] data = new Object[righe][colonne];
		int i = 0;
		
		while(sql1.next()) {
    		for(int m = 0; m < colonne;m++) {
    			data[i][m] = sql1.getObject(columnHeader[m]);
    		}
    		i++;
    	}
		
		JFrame frame = new JFrame();

		JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
		
		JTable table = new JTable(data,columnHeader);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JScrollPane tableContainer = new JScrollPane(table);

	    panel.add(tableContainer, BorderLayout.CENTER);
	    frame.getContentPane().add(panel);
	    frame.setTitle(nomeTab.substring(nomeTab.indexOf("_")+1));
	    frame.pack();
	    frame.setVisible(true);
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    frame.setSize((int)dim.getWidth(), (int)(dim.getHeight() * 0.96));
	}

	
	/**
     * Funzione per scaricare tutti i dati dell'app presenti nel database
	 * @throws SQLException 
	 * @throws IOException 
     */
    public void ScaricamentoDati(String nomeTabella, String intervDate) throws SQLException, IOException {
    	ArrayList<String> columns = new ArrayList<String>();
    	
    	columns.add("COUNT(ordinal_position) as colonne");
    	String condition = "table_schema = 'p4' AND table_name = '"+nomeTabella+"'";
    	ResultSet n_column= db.select("information_schema", columns, "COLUMNS", condition);
    	int colonne = 0;
    	if(n_column.next()){
    		 colonne = n_column.getInt("colonne");
    	}
    	
    	ArrayList<String> columns1 = new ArrayList<String>();
    	columns1.add("COUNT(*) as righe");
    	ResultSet n_rows = db.select("p4", columns1, nomeTabella, intervDate);
    	int righe = 0;
    	if(n_rows.next()){
   		 	righe = n_rows.getInt("righe");
    	}
    	
    	ArrayList<String> columns2 = new ArrayList<String>();
    	columns2.add("column_name");
    	ResultSet header = db.select("information_schema", columns2, "COLUMNS", condition);
    	
    	String[] columnHeader = new String[colonne];
		int j = 0;
		int i = 0;
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		
		
		Row row = sheet.createRow((short)i);
		
		while(header.next()){
			columnHeader[j] = header.getString("column_name");
			row.createCell(j).setCellValue(columnHeader[j]);
			
			sheet.autoSizeColumn(j);
			
			j++;
		}
		i++;
		j = 0;
		
		ArrayList<String> columns3 = new ArrayList<String>();
		columns3.add("*");
		ResultSet sql1 = db.select("p4", columns3, nomeTabella, intervDate);
		
		Object[][] data = new Object[righe][colonne];
		int g = 0;
		
		while(sql1.next()) {
			row = sheet.createRow((short)i);
    		for(int m = 0; m < colonne;m++){
    			data[g][m] = sql1.getObject(columnHeader[m]);
    			if(data[g][m] instanceof Integer){
    				row.createCell(j).setCellValue(sql1.getInt(columnHeader[m]));
    			}else if(data[g][m] instanceof Date){
    				SimpleDateFormat datetemp = new SimpleDateFormat("yyyy-MM-dd");
    				String cellValue = datetemp.format(sql1.getDate(columnHeader[m]));
    				row.createCell(j).setCellValue(cellValue);
    			}else if(data[g][m] instanceof Float){
    				row.createCell(j).setCellValue(sql1.getFloat(columnHeader[m]));
    			}else if(data[g][m] instanceof String){
    				row.createCell(j).setCellValue(sql1.getString(columnHeader[m]));
    			}
    			sheet.autoSizeColumn(j);
    			j++;
    		}
    		g++;
    		i++;
    		j=0;
    	}
		
		FileOutputStream fileOut = new FileOutputStream(nomeTabella.toLowerCase()+".xls");
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * Funzione per aggregare i dati dell'app (se provenienti da almeno 2 store) 
     */
    public void AggregazioneDati() {
    	boolean google = false;
    	boolean finsa = false;
    	int numStore = 0;
    	String storesText = "";
    	String filename = "";
    	FileReader textFileReader = null;
    	Scanner scanner = null;
    	ArrayList<String> columns = new ArrayList<String>();
    	ArrayList<String> operations = new ArrayList<String>();
    	ArrayList<String> types = new ArrayList<String>();
    	ArrayList<Object> values = new ArrayList<Object>();
    	HashMap<Date, HashMap<String, Object>> googleData = new HashMap<Date, HashMap<String, Object>>();
    	HashMap<Date, HashMap<String, Object>> finsaData = new HashMap<Date, HashMap<String, Object>>();
    	HashMap<Date, HashMap<String, Object>> tempData = new HashMap<Date, HashMap<String, Object>>();
    	
    	ArrayList<String> googleColumns = new ArrayList<String>();
    	googleColumns = getAttributiStore("configs/attributiGoogle.txt");
    	ArrayList<String> finsaColumns = new ArrayList<String>();
    	finsaColumns = getAttributiStore("configs/attributiFinsa.txt");
    	
    	//Controllo per quali store sono presenti dei dati dell'app corrente
		tempData = estrazioneDatiDb(googleColumns, "google");
		if(tempData != null && !tempData.isEmpty()) {
			google = true;
			storesText += "Google";
			numStore++;
		}
		tempData = estrazioneDatiDb(finsaColumns, "finsa");
		if(tempData != null && !tempData.isEmpty()) {
			finsa = true;
	    	storesText += "Finsa";
			numStore++;
		}
		if(numStore < 2) {
			return;
		}
		
		//Assegno il file di configurazione adeguato
		if(google && finsa) {
			filename = "configs/aggregazioneGoogleFinsa.txt";
		}
    	try { 
			textFileReader = new FileReader(filename);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(textFileReader != null) {
			scanner = new Scanner(textFileReader);
		}
		// Finchè c'è almeno una riga da leggere nel file...
		while(scanner.hasNextLine()){ 
			String nextLine = scanner.nextLine();
			columns.add(nextLine.split(",")[0]);
			operations.add(nextLine.split(",")[1]);
			types.add(nextLine.split(",")[2]);
		}
		
		//Stampa di debug
		/*for(int i = 0; i < columns.size(); i++){
			System.out.println(columns.get(i) + " - " + operations.get(i));
		}*/
		
		//Estrazione dati dell'app corrente (provenienti dal Google Play Store) dal database
		googleData = estrazioneDatiDb(columns, "google");
		Set<Date> googleDates = googleData.keySet();
		//Stampa di debug
		/*for(Date d : googleDates){
			System.out.println(d);
			for(int j = 1; j < columns.size(); j++){
				System.out.println(googleData.get(d).get(columns.get(j)));
			}
		}*/
		//Estrazione dati dell'app corrente (provenienti dal Finsa Store) dal database
		finsaData = estrazioneDatiDb(columns, "finsa");
		Set<Date> finsaDates = finsaData.keySet();
		
		//Aggregazione dei dati
		for(Date d : googleDates) {
			if(cercaInSet(d, finsaDates)) {	//Se la data � presente nell'altro set di date
				values.add(d);
				for(int i = 1; i < columns.size(); i++) {
					Object temp = null;
					int numAvg = numStore;
					switch(operations.get(i)) {
						case "sum":
							if(googleData.get(d).get(columns.get(i)) == null || (int) googleData.get(d).get(columns.get(i)) == 0) {
								googleData.get(d).remove(columns.get(i));
								googleData.get(d).put(columns.get(i), 0);
								numAvg--;
							}
							if(finsaData.get(d).get(columns.get(i)) == null || (int) finsaData.get(d).get(columns.get(i)) == 0) {
								finsaData.get(d).remove(columns.get(i));
								finsaData.get(d).put(columns.get(i), 0);
								numAvg--;
							}
							temp = (int) googleData.get(d).get(columns.get(i)) + (int) finsaData.get(d).get(columns.get(i));
							break;
						case "avg":
							if(googleData.get(d).get(columns.get(i)) == null || Float.parseFloat(finsaData.get(d).get(columns.get(i)).toString()) == 0) {
								googleData.get(d).remove(columns.get(i));
								googleData.get(d).put(columns.get(i), new Float(0.0));
								numAvg--;
							}
							if(finsaData.get(d).get(columns.get(i)) == null || Float.parseFloat(finsaData.get(d).get(columns.get(i)).toString()) == 0){
								finsaData.get(d).remove(columns.get(i));
								finsaData.get(d).put(columns.get(i), new Float(0.0));
								numAvg--;
							}
//							temp = ((float) googleData.get(d).get(columns.get(i)) + (float) finsaData.get(d).get(columns.get(i))) / numAvg;
							temp = ((float) googleData.get(d).get(columns.get(i)) + Float.parseFloat(finsaData.get(d).get(columns.get(i)).toString())) / numAvg;
							
							if(Float.isNaN((float)temp)) temp = (float)0.0;
							
							break;
					}
					values.add(temp);
				}
			}
		}
		
		String nomeTabella = "dati" + nome + storesText.toLowerCase();
		
		//Creo la tabella dei dati aggregati
		db.createTable(columns.get(0), nomeTabella, Database.nomeDb, columns, types);
		//Inserisco i dati aggregati nel database
		if(values != null && values.size() != 0) {
			db.insert(values, nomeTabella, Database.nomeDb, columns);
		}
		
		//Chiusura del file e gestione delle eccezioni
		try {
			scanner.close();
			textFileReader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    
    
    private ArrayList<String> getAttributiStore(String filename) {
    	FileReader textFileReader = null;
    	Scanner scanner = null;
    	ArrayList<String> columns = new ArrayList<String>();
    	try{
			textFileReader = new FileReader(filename);
		} catch(Exception e){
			e.printStackTrace();
		}
		if(textFileReader != null){
			scanner = new Scanner(textFileReader);
		}
		// Finch� c'� almeno una riga da leggere nel file...
		while(scanner.hasNextLine()){
			columns.add(scanner.nextLine());
		}
		return columns;
    }
    
    
    
    /**
     * Estrazione dati app dal database
     * @param columns Colonne da estrarre
     * @param store Lo store da cui estrarre i dati
     * @return Hashmap contenente tutti i dati estratti dal db
     */
    private HashMap<Date, HashMap<String, Object>> estrazioneDatiDb(ArrayList<String> columns, String store) {
    	String condition = "true";
		ResultSet rs = db.select(Database.nomeDb, columns, "dati" + nome + store, condition);
		int i = 1;
		HashMap<Date, HashMap<String, Object>> result = new HashMap<Date, HashMap<String, Object>>();
		try {
			if(rs == null) return null;
			HashMap<String, Object> temp = null;
			while(rs.next()) {
				temp = new HashMap<String, Object>();
				while(i < columns.size()) {
					temp.put(columns.get(i), rs.getObject(columns.get(i)));
					i++;
				}
				result.put((Date) rs.getObject(columns.get(0)), temp);
				i = 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
    }
    
    
    
    
    private boolean cercaInSet(Date key, Set<Date> set){
    	for(Date d : set){
    		if(key.equals(d)){
    			return true;
    		}
    	}
    	return false;
    }
	
}