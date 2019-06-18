package p4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe per gestire un singolo store
 * 
 * @author Accetta Cristian
 *
 */
public class Store {
	
    /** Attributes */
    private String nome;
    private ArrayList<App> list;

    public Store(String nome, ArrayList<App> list) {
    	this.nome = nome;
    	this.list = list;
    }
    
    public Store(String nome) {
		super();
		this.nome = nome;
		list = new ArrayList<>();
	}
    
    public String getNome() {
		return nome;
	}
	
//    public ArrayList<App> getList() {
//		return list;
//	}
//
//	public void setList(ArrayList<App> list) {
//		this.list = list;
//	}
    
    public void addApp(App a) {
    	list.add(a);
    }
    

	/**
     * Funzione per importare i dati dallo store
     *
     * @return List<File> La lista dei file importati nel sistema
     */
    public List<File> importazioneDati(App a) {
    	File confFile = null;
    	
    	switch(nome) {
    	case "google":
    		confFile = new File("configs/importGoogle.txt");
    		break;
    	case "finsa":
    		confFile = new File("configs/importFinsa.txt");
    		break;
		default:
			System.out.println("Impossibile importare i dati");
			return null;
    	}
    	
    	String absolutePath;
    	ArrayList<String> relativePaths = new ArrayList<>();
    	
    	//Legge file di configurazione 
    	// e recupera percorsi da aprire
		try {
			Scanner confScan = new Scanner(confFile);
			
			absolutePath = confScan.nextLine();
			
			while(confScan.hasNextLine()) {
				String relativePath = confScan.nextLine();
				
				relativePath = relativePath.replaceAll("%NOME%", a.getNome());//Rimpiazza segnalino nome con nome app
				
				relativePaths.add(relativePath);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		ArrayList<File> listFile = new ArrayList<>();
		
		//Carica i file
		for(String relativePath : relativePaths) {
			listFile.add(new File(absolutePath + relativePath));
		}
 		
    	return listFile;
    }

	public ArrayList<App> lista(Database db) {
		ArrayList<App> tempList = new ArrayList<>();
 		
		App a = new App("app", db);
		a.addStore(this);
		tempList.add(a);
		
		for(int i = 1; i<3; i++) {
			App otherApp = new App(nome + "app" + i, db);
			otherApp.addStore(this);
			tempList.add(otherApp);
		}
		
//    	for(int i=0; i<2; i++) {
//    		int rng = (int)(Math.random()*10000);
//    		a = new App("App"+ rng, db);
//    		a.addStore(this);
//    		tempList.add(a);
//    	}
    	
    	return tempList;
	}

	

}