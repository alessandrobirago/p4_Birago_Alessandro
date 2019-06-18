package p4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Classe per effettuare la conversione dei dati provenienti dagli store in un formato unico
 * Map<Calendar, Map<String, Object>>
 * 
 * @author Birago Alessandro, Oliveri Matteo
 *
 */
public class Convertitore {
	
    /** Attributes */
    public List<File> files;
    
    private ArrayList<ArrayList<String>> confList;
    private HashMap<Date, HashMap<String, Object>> map;
    
    public Convertitore() {
		confList = new ArrayList<>();
		map = new HashMap<>();
	}
    
    
    /**
     * Funzione per convertire i dati in Map<Date, Map<String, Object>>
     *
     * @param store Lo store dal quale provengono i dati
     * @return Hashmap dei dati convertiti
     */
    public HashMap<Date, HashMap<String, Object>> conversioneDati(Store store) {
    	//Leggo e carico il file di configurazione per lo specifico store
    	switch(store.getNome()) {
    	case "google":
    		loadConfFile("./configs/convertitoreGoogle.txt");
    		break;
    	case "finsa":
    		loadConfFile("./configs/convertitoreFinsa.txt");
    		break;
    	case "test":
    		loadConfFile("./configs/convertitoreTest.txt");
    		break;
		default:
			return null;
    	}
    	
    	//Per ogni file dati metto in una lista:
    	//- le chiavi(cioe' le colonne del file) da recuperare
    	//- il tipo di dato
    	for(File f : files) {
    		ArrayList<String> listKey = findListKey(f.getName());
    		ArrayList<String> listType = findListType(f.getName());
    		if(listKey == null) {
    			break;	//passa al file successivo
    		}
    		//Scorro il file e faccio l'insert nell'Hashmap
    		readFileCSV(f, listKey, listType);
    	}
    	
    	return map;
    }
    
    
    
    /**
     * Legge il file di configurazione della conversione
     * 
     * @param path Percorso del file di configurzione
     */
    private void loadConfFile(String path) {
    	File confFile = new File(path);
    	
    	//Legge il file di quello Store e recupera la configurazione
		try {
			Scanner confScan = new Scanner(confFile);
			
			ArrayList<String> keysList = null;
			ArrayList<String> typesList = null;
			while(confScan.hasNextLine()) {
				//Inizializza keysList e typeList
				keysList = new ArrayList<>();
				keysList.add("0");	//il flag 0 per identifica la lista delle chiavi
				typesList = new ArrayList<>();
				typesList.add("1");	//il flag 1 per identifica la lista dei tipi
				
				//Legge nome del file
				String fileName = confScan.nextLine();
				keysList.add(fileName);
				typesList.add(fileName);
				
				//Legge ogni chiave e tipo per quel file
				while(confScan.hasNextLine()) {
					String keyName = confScan.nextLine();
					
					//Se la linea � vuota, inizia un nuovo file
					if(keyName.isEmpty()) {	
						confList.add(keysList);
						confList.add(typesList);
						break;	//esce dal ciclo
					}
					
					//Altrimenti continua la lettura delle chiavi
					String typeName = null;
					if(confScan.hasNextLine()){
						typeName = confScan.nextLine();
					}
					keysList.add(keyName);
					typesList.add(typeName);
				}
			}
			
			//Ultimo insert
			confList.add(keysList);
			confList.add(typesList);
			
			confScan.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
	}
    
    
    /**
     * Funzione che dato il nome del file, ne trova la lista delle chiavi 
     * 
     * @return la lista delle chiavi, se viene trovata. null altrimenti 
     */
    private ArrayList<String> findListKey(String fileName) {
    	for(ArrayList<String> listKeys : confList) {
    		
    		if(listKeys.get(0).equals("0") && listKeys.get(1).equals(fileName)) {
    			listKeys.remove(0);	//viene eliminato il flag 0 usato per identificare la lista
    			return listKeys;
    		}
    	}
    	
    	return null;
	}
    
    /**
     * Funzione che dato il nome del file, ne trova la lista dei tipi 
     * 
     * @return la lista dei tipi, se viene trovata. null altrimenti 
     */
    private ArrayList<String> findListType(String fileName) {
    	for(ArrayList<String> listTypes : confList) {
    		if(listTypes.get(0).equals("1") && listTypes.get(1).equals(fileName)) {
    			listTypes.remove(0);	//viene eliminato il flag 1 usato per identificare la lista
    			return listTypes;
    		}
    	}
    	
    	return null;
	}
    
    
    
    
    
    /**
     * Fuzione che dato il file ne estrae specifiche colonne all'interno di una HashMap
     * 
     * @param f Nome del file
     * @param keyList Lista delle chiavi prese dal file di configurazione
     * @prarm typesList Lista dei tipi presi dal file di configurazione
     * @return la lista dei tipi, se viene trovata. null altrimenti 
     */
    private void readFileCSV(File f, ArrayList<String> keysList, ArrayList<String> typesList) {
    	InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(f), "UTF-16");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
			
		Scanner fileScan = new Scanner(isr);
    	Scanner lineScan;
    	
    	//Acquisisco il nome delle colonne del file
    	ArrayList<String> listTitoli = getTitoli(fileScan);
    	
    	String readValue;
    	int n = 0;  //Indice per le date
    	Date previousDate = null;
    	Map<Integer, Map<String, Float>> mapFloat = new HashMap<>();
    	
    	//Ciclo sulle righe del file
    	while(fileScan.hasNextLine()) {	//Fino alla fine del file
			//Leggo una riga dal file
			lineScan = new Scanner(fileScan.nextLine().replace("\"", ""));
			lineScan.useDelimiter(",");
			
			int i = 0;	//Indice per i titoli
			Date currentDate = null;
			
			//Ciclo sulle colonne di quella linea
			while(lineScan.hasNext()) {
				readValue = lineScan.next();	//Leggo valore dalla riga
				
				switch(listTitoli.get(i)) {
				case "Date":
					currentDate = parseDate(readValue);
					//Verifico se ci sono più righe con la stessa data
					if((previousDate != null) && currentDate.equals(previousDate)) {
						n++;
					} else{
						previousDate = currentDate;
						n=0;
						mapFloat = new HashMap<>();
					}
					break;
					
				default:
					int count = 0;  //Indice per i tipi
					
					//Scorro le chiavi(colonne) dalla lista
					for(String k : keysList) {
						count++;
						
						//Verifico che il titolo nella posizione i sia una chiave
						if(k.equals(listTitoli.get(i))) {
							
							//Se non c'è ancora l'HashMap per quella data, la si crea
							if(map.get(currentDate) == null) {
								map.put(currentDate, new HashMap<String, Object>());
							}
							
							//Se l'HashMap è ancora vuota parso il dato e lo inserisco altrimenti aggrego il dato
							if(map.get(currentDate).get(k) == null){
								writeValue(typesList.get(count-1), currentDate, k, readValue);
							} else{
								//Aggrego in base al tipo di dato
								switch(typesList.get(count-1)) {
								
									case "Integer":
										//Considero solo i dati non vuoti
										if (!readValue.equals("")) {
											//Aggrego gli interi sommandoli 
											try{
												int prevRead = Integer.parseInt(map.get(currentDate).get(k).toString());
												int currentRead = Integer.parseInt(readValue);
												int aggReadValue = prevRead + currentRead;
												map.get(currentDate).put(k, aggReadValue);
											} catch (NumberFormatException e){
												System.out.println("Errore nell'inserimento valore nella HashMap: Integer non valido!");
												e.printStackTrace();
											}
										}
										break;
										
									case "Float":
										//Considero solo i dati non vuoti
										if (!readValue.equals("")) {
											//Aggrego i float facendone la media
											try{
												float prevRead = Float.parseFloat(map.get(currentDate).get(k).toString());
												float currentRead = Float.parseFloat(readValue);
												if(n<=1){
													float aggReadValue = (prevRead + currentRead)/2;
													map.get(currentDate).put(k, aggReadValue);
													Map<String, Float> tempMap = new HashMap<>();
													tempMap.put(k, prevRead);
													mapFloat.put(0, tempMap);
													tempMap = new HashMap<>();
													tempMap.put(k, currentRead);
													mapFloat.put(1, tempMap);
												} else {
													Map<String, Float> tempMap = new HashMap<>();
													tempMap.put(k, currentRead);
													float aggReadValue = currentRead;
													for(int l=0; l<n; l++){
														aggReadValue += Float.parseFloat(mapFloat.get(l).get(k).toString());
													}
													aggReadValue = aggReadValue /(n+1);
													map.get(currentDate).put(k, aggReadValue);
													mapFloat.put(n, tempMap);
												}
											} catch(NumberFormatException e){
												System.out.println("Errore nell'inserimento valore nella HashMap: Float non valido!");
												e.printStackTrace();
											}
										}
										break;
										
									case "String":
										if (readValue.equals("")){
											map.get(currentDate).put(k, null);
										} else{
											map.get(currentDate).put(k, readValue);
										}
										break;
										
									default:
										System.out.println("Errore nell'inserimento valore nella HashMap: tipo non corretto!");
										map.get(currentDate).put(k, null);
										break;
								}
							}
						}
					}
					
					break;
				}
				
				i++;  //Incremento l'indice dei titoli
			}
    	}
	}
    
    
    /**
	 * Legge la prima riga di un CSV a cui e' stato applicato fileScan
	 * 		e restituisce i nomi delle colonne
	 *  
	 * @param fileScan scanner applicato al file CSV
	 * @return ArrayList con nomi delle colonne del CSV 
	 */
	public ArrayList<String> getTitoli(Scanner fileScan) {
		//Titoli
		ArrayList<String> listTitoli = new ArrayList<>();
		String titoli = fileScan.nextLine();
	
		Scanner lineScan = new Scanner(titoli);
		lineScan.useDelimiter(",");
		while(lineScan.hasNext()) {	//Per ogni nome di colonna
			listTitoli.add(lineScan.next());
		}
		lineScan.close();
		return listTitoli;
	}
    
	
	/**
	 * Parsa una data come String in un oggetto Date
	 * 
	 * @param s stringa contentente la data
	 * @return oggetto Date
	 */
	private Date parseDate(String s) {
		Date out = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			out = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	
	/**
     * Scrive i dati all'interno della HashMap map
     *
     * @param type Il formato del dato
     * @param currentDate La data del dato da inserire
     * @param k Il nome della colonna del dato
     * @param readValue il valore da inserire nella HashMap nella giusta posizione
     * @return Hashmap dei dati convertiti
     */
	private void writeValue(String type, Date currentDate, String k, String readValue) {
		switch(type) {
		case "Integer":
			//Se e' un intero scrivo il dato se non e' vuoto altrimenti salvo il valore come null
			if (readValue.equals("")){
				map.get(currentDate).put(k, null);
			} else{
				try{
					int result = Integer.parseInt(readValue);
					map.get(currentDate).put(k, result);
				} catch (NumberFormatException e){
					System.out.println("Errore nell'inserimento valore nella HashMap: Integer non valido!");
					map.get(currentDate).put(k, null);
					e.printStackTrace();
				}
			}
			break;
			
		case "Float":
			//Se e' un float scrivo il dato se non e' vuoto altrimenti salvo il valore come null
			if (readValue.equals("")){
				map.get(currentDate).put(k, null);
			} else{
				try{
					float result = Float.parseFloat(readValue);
					map.get(currentDate).put(k, result);
				} catch (NumberFormatException e){
					System.out.println("Errore nell'inserimento valore nella HashMap: Float non valido!");
					map.get(currentDate).put(k, null);
					e.printStackTrace();
				}
			}
			break;
			
		case "String":
			//Se e' una stringa scrivo il dato se non e' vuota altrimenti salvo il valore come null
			if (readValue.equals("")){
				map.get(currentDate).put(k, null);
			} else{
				map.get(currentDate).put(k, readValue);
			}
			break;
			
		default:
			map.get(currentDate).put(k, null);
			break;
		}
	}
    
    

 
}