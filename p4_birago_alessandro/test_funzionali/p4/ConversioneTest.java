package p4;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import p4.App;
import p4.Convertitore;
import p4.Database;
import p4.Store;


/**
 * Classe di test della funzionalità di conversione dei dati (per google store, 
 * finsa store ed uno store non presente a sistema)
 * @author Birago Alessandro
 */
public class ConversioneTest {
	
	private Store store;
	private Convertitore convert;
	private App a;
	private Database db;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Preparazione per il Test del Convertitore");
		a = new App("app", db);
		convert = new Convertitore();
	}

	@Test
	public void testConversioneStoreFinsa() {
		a.addStore(store);
		store = new Store("finsa");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./finsastore/buyers_7d_com.app_201705_channel.csv"));
		listFile.add(new File ("./finsastore/crashes_com.app_201705_overview.csv"));
		listFile.add(new File ("./finsastore/installs_com.app_201705_overview.csv"));
		listFile.add(new File ("./finsastore/ratings_com.app_201705_overview.csv"));
		listFile.add(new File ("./finsastore/retained_installers_com.app_201705_channel.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneStoreFinsa fallito", 13, map.size());
		System.out.println("Test ConversioneStoreFinsa completato");
	}
	
	@Test
	public void testConversioneStoreGoogle() {
		a.addStore(store);
		store = new Store("google");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./google/buyers_7d_com.app_201705_channel.csv"));
		listFile.add(new File ("./google/crashes_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/installs_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/ratings_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/retained_installers_com.app_201705_channel.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneStoreGoogle fallito", 13, map.size());
		System.out.println("Test ConversioneStoreGoogle completato");
	}
	
	@Test
	public void testConversioneStoreSconosciuto() {
		a.addStore(store);
		store = new Store("Sconosciuto");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./google/buyers_7d_com.app_201705_channel.csv"));
		listFile.add(new File ("./google/crashes_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/installs_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/ratings_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/retained_installers_com.app_201705_channel.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneStoreSconosciuto fallito", null, map);
		System.out.println("Test ConversioneStoreSconosciuto completato");
	}

}
