package p4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ 
	ConvertitoreTest.class })

public class TestStrutturali {
	
	/**
	 * La Test Suite è finalizzata al testing della
	 * sola classe Converitore in modo da non dover 
	 * richiedere il funzionamento del database MySql
	 * Non raggiunge la copertura del 100% poiché non
	 * è stato possibile testare tutte le Exceptions
	 * essendo alcuni metodi di tipo private.
	 */

}
