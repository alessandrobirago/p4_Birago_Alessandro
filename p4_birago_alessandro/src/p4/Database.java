package p4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe per gestire tutte le funzionalit� del database
 * 
 * @author Accetta Cristian
 */
public class Database {
	
	private String url = "jdbc:mysql://localhost:3306";
	private String user = "root";
	private String password = "password";
	private Connection con;
	
	public static String nomeDb = "p4";
	public static String nomeTabApp = "apps";
	public static String nomeTabStore = "store";
	
	/**
	 * CONNESSIONE AL DATABASE
	 */
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.con = DriverManager.getConnection(url, user, password);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
 
    /**
     * Funzione per creare un database
     *
     * @param nome Il nome del nuovo database tipo String
     * @return boolean true se il database � stato creato, false altrimenti
     */
    public boolean createDatabase(String nome) {
    	try {
			String existDb = "SHOW DATABASES LIKE '" + nome + "'";
			Statement ps = con.createStatement();
			int rs = ps.executeUpdate(existDb);
			
			if(rs == 0){
				ps.close();
				String db = "CREATE DATABASE " + nome;
				ps = con.createStatement();
				int rs1 = ps.executeUpdate(db);
				if(rs1 == 1){
					return true;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
    	return false;
    }
    
    /**
     * Funzione per cancellare un database
     *
     * @param nome Il nome del database tipo String
     * @return boolean true se il database � stato cancellato, false altrimenti
     */
    public boolean dropDatabase(String nome){
    	
    	try {
			String existDb = "SHOW DATABASES LIKE '"+nome+"'";
			Statement ps = con.createStatement();
			ResultSet rs = ps.executeQuery(existDb);
			if(rs.next()){
				ps.close();
				String db = "DROP DATABASE " + nome;
				ps = con.createStatement();
				int rs1 = ps.executeUpdate(db);
				if(rs1 == 1){
					return true;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
    	return false;
         
    }
    
    /**
     * Funzione per creare una tabella
     *
     * @param primary_key chiave primaria della tabella tipo String
     * @param nomeTabella Il nome della tabella tipo String
     * @param nomeDb nome database in uso tipo String
     * @param columns array di nomi di colonne da inserire nella tabella tipo String
     * @param datatypes array di tipo ( esempio INT,VARCHAR(23) etc ) da assegnare alle colonne tipo String
     * @return boolean true se la tabella � stata creata, false altrimenti
     */
    public boolean createTable(String primary_key,String nomeTabella, String nomeDb, ArrayList<String> columns, ArrayList<String> datatypes){
    	
		try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+nomeTabella+"'";
				
			ResultSet rs1 = ps.executeQuery(existTb);
				
			if(!rs1.next()){
				ps.close();
				String createTable = "CREATE TABLE " + nomeTabella + "(";
				
				for(int i = 0; i < columns.size(); i++){
					createTable = createTable.concat("`");
					createTable = createTable.concat(columns.get(i));
					createTable = createTable.concat("` ");
					createTable = createTable.concat(datatypes.get(i));
					createTable = createTable.concat(",");
				}
				createTable = createTable.concat("PRIMARY KEY (`" + primary_key + "`)");
				createTable = createTable.concat(")");
				
				ps = con.createStatement();
				int rs2 = ps.executeUpdate(createTable);
				if(rs2 == 1){
					return true;
				}
			}
				
			
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	return false;
    }
    
    /**
     * Funzione per cancellare una tabella
     *
     * @param nomeDb nome database in uso tipo String
     * @param nomeTabella Il nome della tabella da cancellare tipo String
     * @return boolean true se la tabella � stata cancellata, false altrimenti
     */
    public boolean dropTable(String nomeDb,String nomeTabella){
    	
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+nomeTabella+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String dropTable = "DROP TABLE " + nomeTabella ;
				ps = con.createStatement();
				int rs2 = ps.executeUpdate(dropTable);
				if(rs2 == 1){
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    
    /**
     * Funzione per aggiungere una colonna ad una tabella
     *
     * @param nomeDb Il nome del database in uso tipo String
     * @param table nome della tabella da alterare(cio� aggiungere, cancellare o modificare una colonna) tipo String
     * @param datatype Il tipo di dato della colonna tipo String
     * @param c Il nome della colonna da aggiungere cancellare o modificare tipo String
     * @param ADD = aggiungere colonna DROP = cancellare colonna MODIFY = modificare colonna
     * @return boolean true se la colonna � stata aggiunta cancellata o modificata, false altrimenti
     */
    
    
    public boolean alterTable(String nomeDb,String table, String datatype,String c,String a){
    	
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String alter = "ALTER TABLE " + table;
				switch(a){
					case "ADD":
						alter = alter.concat(" ADD COLUMN " + c + " " + datatype);
					break;
					case "DROP":
						alter = alter.concat(" DROP COLUMN " + c);
					break;
					case "MODIFY":
						alter = alter.concat(" MODIFY COLUMN " + c + " " + datatype);
					break;
				}
				
				ps = con.createStatement();
				int rs = ps.executeUpdate(alter);
				if(rs == 1){
					return true;
				}
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
    	}
    	
    	return false;
    }
    
    /**
     * Funzione per ottenere dati da un database
     *
     * @param nomeDb nome della database in uso tipo String
     * @param columns nomi delle colonne da selezionare nella query  ArrayList di tipo String
     * @param table nome della tabella dove agisce la query tipo String
     * @param condition condizione WHERE per estrarre i valori 
     * come si scrive : nome colonna = valore ( se valore � stringa metterlo tra apici ) aggiungere eventuali AND/OR e di nuovo nome colonna = valore
     * @return ResultSet query eseguita e dati da mostrare, altrimenti ritorna null
     */
    
    
    public ResultSet select(String nomeDb, ArrayList<String> columns, String table, String condition){
    	ResultSet rs = null;
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String select = "SELECT ";
				for(int i = 0;i < columns.size();i++){
					select = select.concat(columns.get(i));
					if(columns.size()-1 != i){
						select = select.concat(",");
					}
				}
					
				select = select.concat(" FROM " + table);
				
				if(condition != null && !condition.isEmpty()){
					select = select.concat(" WHERE " + condition);
				}
				
				Statement ps1 = con.createStatement();
					
				rs = ps1.executeQuery(select);
				return rs;
			}
		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
    	return null;
    }
    
    
    /**
     * Funzione per inserire dati in una tabella
     *
     * @param values I valori delle colonne da inserire
     * @param table Il nome della tabella
     * @param nomeDb nome del database in uso
     * @param columns colonne della tabella dove vengono inseriti i valori
     * @return boolean true se i valori sono stati inseriti nelle colonne, false altrimenti
     */
    public boolean insert(ArrayList<Object> values, String table,String nomeDb,ArrayList<String> columns) {
    	String sql = null;
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				sql = "INSERT INTO " + table + "(";
				for(int i = 0; i < columns.size(); i++){
					sql = sql.concat("`" + columns.get(i) + "`");
					if(columns.size()-1 != i){
						sql = sql.concat(",");
					}
				}
				
				sql = sql.concat(") VALUES ");
				int prodotto = values.size() / columns.size();
				
				for(int i = 0; i < prodotto; i++){
					sql = sql.concat("(");
					for(int a = 0; a < columns.size(); a++){
						sql = sql.concat("?");
						if(columns.size()-1 != a){
							sql = sql.concat(",");
						}
					}
					sql = sql.concat(")");
					if(prodotto-1 != i){
						sql = sql.concat(",");
					}
				}
				
				
				PreparedStatement ps1 = con.prepareStatement(sql);
				int b = 0;
				int j = 1;
				for(int i = 0; i < prodotto; i++){
					for(int y = 0; y < columns.size(); y++){
						ps1.setObject(j, values.get(b));
						b++;
						j++;
					}
					
				}
				
				int rs = ps1.executeUpdate();
				
				if(rs == 1){
					return true;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	
    	
    	return false;
    }
    
    /**
     * Funzione per cancellare valori da una tabella
     *
     * @param table Il nome della tabella
     * @param nomeDb nome del database in uso
     * @param conditions le condizioni per cancellare quei determinati valori
     * come si scrive : nome colonna = valore ( se valore � stringa metterlo tra apici ) aggiungere eventuali AND/OR e di nuovo nome colonna = valore	   
     * @return boolean true se quelle determinate righe sono state cancellate, false altrimenti
     */
    public boolean delete(String nomeDb,String table, String conditions){
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String delete = "DELETE FROM " + table;
				if(conditions != null && !conditions.isEmpty()){
					delete = delete.concat(" WHERE " + conditions);
				}
				
				Statement ps1 = con.createStatement();
				
				int rs = ps1.executeUpdate(delete);
				
				if(rs == 1){
					return true;
				}
				
			}
		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
    	return false;
    }
    
    
    /**
     * Funzione per aggiornare i dati di una tabella
     *
     * @param nomeDb nome del database in uso
     * @param table Il nome della tabella
     * @param columns Le colonne da aggiornare
     * @param values I nuovi valori delle colonne
     * @param condition Le condizioni che devono essere verificate
     * @return boolean true se quelle determinate colonne sono state aggiornate, false altrimenti
     */
    public boolean update(String nomeDb,String table, ArrayList<String> columns, ArrayList<Object> values, String condition){
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String update = "UPDATE " + table + " SET ";
				for(int i = 0;i < columns.size();i++){
					update = update.concat(columns.get(i));
					update = update.concat(" = " + values.get(i));
					if(columns.size()-1 != i){
						update = update.concat(",");
					}
				}
					
				if(condition != null && !condition.isEmpty()){
					update = update.concat(" WHERE " + condition);
				}
				
				Statement ps1 = con.createStatement();
				
				int rs = ps1.executeUpdate(update);
				
				if(rs == 1){
					return true;
				}
			}
		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
    	return false;
    }
    
    /**
     * Funzione che cancella tutti i dati di una tabella
     * 
     * @param table nome tabella
     * @param nomeDb nome database in uso
     * @return boolean true se la tabella � vuota, false altrimenti
     */
    public boolean truncate(String table, String nomeDb){
    	try {
			Statement ps = con.createStatement();
			ps.execute("USE " + nomeDb);
			String existTb = "SHOW TABLES FROM " + nomeDb + " LIKE '"+table+"'";
			ResultSet rs1 = ps.executeQuery(existTb);
			if(rs1.next()){
				ps.close();
				String sql = "TRUNCATE " + table;
				ps = con.createStatement();
				int rs2 = ps.executeUpdate(sql);
				if(rs2 == 1){
					return true;
				}
			}
		} catch (SQLException e1) {
				e1.printStackTrace();
		}
    	 return false;
     }

 }