package data;

import models.Elettore;
import models.Votazione;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author Piemme
 */
public class ElettoreDAOImpl implements ElettoreDAO {

    /**
     * Default constructor
     */
    private ElettoreDAOImpl() {
    }

    private static ElettoreDAOImpl uniqueInstance;

    public static ElettoreDAOImpl getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new ElettoreDAOImpl();
        }// TODO implement here
        return uniqueInstance;
    }

    public Elettore getElettore(String cf, String psw) {
        Elettore utente = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM utenti WHERE cf = \"" + cf + "\" AND password = \"" + psw + "\"";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next()){
                utente = Elettore.getInstance(resultSet.getString(2), resultSet.getString(3), resultSet.getString(1), resultSet.getDate(4));
                System.out.println("Ci sono dei risultati");
            }
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return utente;
    }

    @Override
    public boolean haVotato(String cf, String psw, Votazione v) {
        return true;
    }
}