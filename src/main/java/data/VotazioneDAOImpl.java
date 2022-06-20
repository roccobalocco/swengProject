package data;

import models.Classica;
import models.Referendum;
import models.Votazione;

import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public abstract class VotazioneDAOImpl implements VotazioneDAO {

    /**
     * Default constructor
     */
    public VotazioneDAOImpl() { }

    /**
     * @return
     */
    public List<Votazione> getAllVotazioni() {
        List<Votazione> votazioni = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");

            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement("SELECT distinct * FROM votazioni");
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                votazioni.add(new Classica(resultSet.getString(6),resultSet.getDate(5), resultSet.getBoolean(7), resultSet.getBoolean(8)));

            //creo oggetto statement per esecuzione query
            statement = conn.prepareStatement("SELECT * FROM referendum");
            //eseguo la query
            resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                votazioni.add(new Referendum(resultSet.getString(2), resultSet.getDate(7), resultSet.getBoolean(6)));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return votazioni;
    }

}