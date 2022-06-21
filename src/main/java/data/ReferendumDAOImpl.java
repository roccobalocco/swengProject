package data;

import models.Elettore;
import models.Referendum;
import models.Risultati;
import models.Votazione;

import java.sql.*;
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
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendums.add(new Referendum(resultSet.getString(2), resultSet.getDate(7), resultSet.getBoolean(6)));

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

    /**
     * @return
     */
    public <T extends Votazione> List<T> getAllNoQuorum() {
        List<T> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(!r.hasQuorum()){ aq.add((T) r); } });
        return aq;
    }

    public <T extends Votazione> T getVotazione(String id) {
        T referendum = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum WHERE id = \"" + id + "\"";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendum = (T) new Referendum(resultSet.getString(2), resultSet.getDate(7), resultSet.getBoolean(6));

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
        boolean up = false;
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE referendum SET 'descrizione' = \"" + v.descrizione + "\", quorum = \"" + v.hasQuorum()  + "\", 'scadenza' = \"" + v.getScadenza() + "\", WHERE id = \"" + id + "\"";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            resultSet.next();
            up = resultSet.getBoolean(1);
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return up;
    }

    public <T extends Votazione> boolean addVotazione(T t) {
        boolean av = false;
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO refendum ('id', 'descrizione', 'quorum', 'scadenza')";
            query += " VALUES (\"" + getNextId() + "\", \"" + v.descrizione + "\", \"" + v.hasQuorum() + "\", \"" + v.getScadenza() + "\" )";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            resultSet.next();
            av = resultSet.getBoolean(1);
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return av;
    }


    public boolean deleteVotazione(String id){
        boolean dv = false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM referendum WHERE 'id' = \"" + id + "\"";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            resultSet.next();
            dv = resultSet.getBoolean(1);
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return dv;
    }


    public Risultati getRisultati(Votazione r) {
        if(r.fineVotazione()){
            return new Risultati();
        }else {
            return null;
        }
    }

    private String getNextId(){
        int id = -1;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT id FROM referendum WHERE MAX(id)";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            resultSet.next();
            id = resultSet.getInt(1) + 1;
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return "" + id;
    }
}