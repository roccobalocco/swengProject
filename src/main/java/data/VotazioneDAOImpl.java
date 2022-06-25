package data;

import models.Classica;
import models.Referendum;
import models.Votazione;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Piemme
 */
public abstract class VotazioneDAOImpl implements VotazioneDAO {

    /**
     * @return una lista con al suo intero due liste, la prima con le votazioni Classiche, la seconda con i referendum
     */
    public List<List<? extends Votazione>> getAllVotazioni() {
        List<List<? extends Votazione>> votazioni = new LinkedList<>();
        List<Classica> classica = new LinkedList<>();
        List<Referendum> referendum = new LinkedList<>();
        new LinkedList<Referendum>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");

            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement("SELECT distinct * FROM votazione");
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                classica.add(new Classica(resultSet.getString(6), resultSet.getDate(5).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), resultSet.getBoolean(7), resultSet.getBoolean(8), resultSet.getInt(1)));

            //creo oggetto statement per esecuzione query
            statement = conn.prepareStatement("SELECT * FROM referendum");
            //eseguo la query
            resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendum.add(new Referendum(resultSet.getString(2), resultSet.getDate(7).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), resultSet.getBoolean(6), resultSet.getInt(1)));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
            votazioni.add(classica);
            votazioni.add(referendum);
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return votazioni;
    }

}