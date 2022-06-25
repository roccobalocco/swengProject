package data;

import models.*;
import java.sql.*;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Piemme
 */
public class ReferendumDAOImpl extends VotazioneDAOImpl {

    /**
     * Default constructor
     */
    private ReferendumDAOImpl() { }

    private static ReferendumDAOImpl uniqueInstance;

    public static ReferendumDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ReferendumDAOImpl();
        return uniqueInstance;
    }

    public List<Referendum> getAll(){
        List<Referendum> referendums = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendums.add(new Referendum(resultSet.getString(2), resultSet.getDate(7).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), resultSet.getBoolean(6), resultSet.getInt(1)));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return referendums;
    }

    public List<Referendum> getAllQuorum() {
        List<Referendum> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(r.hasQuorum()){ aq.add(r); } });
        return aq;
    }


    @SuppressWarnings("all")
    public <T extends Votazione> List<T> getAllNoQuorum() {
        List<T> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(!r.hasQuorum()){ aq.add((T) r); } });
        return aq;
    }

    @SuppressWarnings("all")
    public <T extends Votazione> T getVotazione(String id) {
        T referendum = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum WHERE `id` = \"" + id + "\"";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendum = (T) new Referendum(resultSet.getString(2), resultSet.getDate(7).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), resultSet.getBoolean(6), resultSet.getInt(1));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return referendum;
    }

    public <T extends Votazione> boolean updateVotazione(String id, T t) {
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE referendum SET `descrizione` = '" + v.descrizione + "', `quorum` = " + v.hasQuorum()  + ", `scadenza` = '" + v.getScadenza() + "', WHERE `id` = " + id;
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo resultset e connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }

    public <T extends Votazione> boolean addVotazione(T t) {
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO referendum (`id`, `descrizione`, `quorum`, `scadenza`)";
            query += " VALUES (" + v.getId() + ", '" + v.descrizione + "', " + (v.hasQuorum() ? 1 : 0) + ", '" + v.getScadenza() + " 00:00:00' );";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }


    public boolean deleteVotazione(String id){
        boolean dv = false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM referendum WHERE `id` = " + id;
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }


    public Risultati getRisultati(Votazione r) {
        if(r.fineVotazione()){
            return new Risultati(r.toString(), new LinkedList<>());
        }else {
            return null;
        }
    }

    public int getNextId(){
        int id = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM referendum";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                id = resultSet.getInt(1) + 1;
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        System.out.println("Next id --> " + id);
        return id;
    }
}