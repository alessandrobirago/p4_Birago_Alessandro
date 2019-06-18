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
 * Classe di test della struttura del Convertitore
 * 
 * @author Birago Alessandro
 */
public class ConvertitoreTest {
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
	public void testConversioneDatiFinsa() {
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
		assertEquals("Test ConversioneDatiFinsa fallito", 13, map.size());
		System.out.println("Test ConversioneDatiFinsa completato");
	}
	
	@Test
	public void testConversioneDatiGoogle() {
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
		assertEquals("Test ConversioneDatiGoogle fallito", 13, map.size());
		System.out.println("Test ConversioneDatiGoogle completato");
	}
	
	@Test
	public void testConversioneDatiStoreSconosciuto() {
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
		assertEquals("Test ConversioneDatiStoreSconosciuto fallito", null, map);
		System.out.println("Test ConversioneDatiStoreSconosciuto completato");
	}
	
	@Test
	public void testConversioneSingoloFileInesistente() {
		a.addStore(store);
		store = new Store("google");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./google/fileinesistente.csv"));
		listFile.add(new File ("./google/crashes_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/installs_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/ratings_com.app_201705_overview.csv"));
		listFile.add(new File ("./google/retained_installers_com.app_201705_channel.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneSingoloFileInesistente fallito", 0, map.size());
		System.out.println("Test ConversioneSingoloFileInesistente completato");
	}
	
	@Test
	public void testConversioneFileInesistenti() {
		a.addStore(store);
		store = new Store("google");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./google/files.csv"));
		listFile.add(new File ("./google/che.csv"));
		listFile.add(new File ("./google/non.csv"));
		listFile.add(new File ("./google/esistono.csv"));
		listFile.add(new File ("./google/davvero.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneFileInesistenti fallito", 0, map.size());
		System.out.println("Test ConversioneFileInesistenti completato");
	}
	
	@Test
	public void testConversioneFileVuoto() {
		a.addStore(store);
		store = new Store("test");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./test/test1.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneFileVuoto fallito", 0, map.size());
		System.out.println("Test ConversioneFileVuoto completato");
	}
	
	@Test
	public void testConversioneStringhe() {
		a.addStore(store);
		store = new Store("test");
		store.addApp(a);
		ArrayList<File> listFile = new ArrayList<>();
		listFile.add(new File ("./test/test.csv"));
		convert.files = listFile;
		HashMap<Date, HashMap<String, Object>> map = convert.conversioneDati(store);
		assertEquals("Test ConversioneStrighe fallito", 10, map.size());
		System.out.println("Test ConversioneStrighe completato");
	}

}
