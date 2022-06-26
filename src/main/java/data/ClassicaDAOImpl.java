package data;

import models.Classica;
import models.Risultati;
import models.Votazione;

import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class ClassicaDAOImpl extends VotazioneDAOImpl {

    /**
     * Default constructor
     */
    private ClassicaDAOImpl() { }

    private static ClassicaDAOImpl uniqueInstance;

    public static ClassicaDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ClassicaDAOImpl();
        return uniqueInstance;
    }

    /**
     * @return
     */
    public List<Classica> getAllOrdinale() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Classica> getAllCategorico() {
        // TODO implement here
        return null;
    }

    public Risultati getRisultati() {
        //TODO
        return null;
    }

    public <T extends Votazione> T getVotazione(String id) {
        //TODO
        return null;
    }

    public <T extends Votazione> boolean updateVotazione(String id, T v) {
        //TODO
        return false;
    }

    public <T extends Votazione> boolean addVotazione(T v) {
        //TODO
        return false;
    }

    public boolean deleteVotazione(String id) {
        //TODO
        return false;
    }

    public Risultati getRisultati(Votazione v) {
        //TODO
        return null;
    }

    public int getNextId(){
        int id = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM votazione";
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