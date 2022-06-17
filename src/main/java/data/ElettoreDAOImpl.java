package data;

import models.Elettore;
import models.Votazione;

import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class ElettoreDAOImpl implements ElettoreDAO {

    /**
     * Default constructor
     */
    public ElettoreDAOImpl() {
    }

    /**
     * @param user 
     * @param psw 
     * @return
     */
    public Elettore getElettore(String user, String psw) {
        Elettore utente = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votazioni?user=root&password=admin");
            //scrivo query
            String query = "SELECT * FROM utenti WHERE username = \"" + user + "\" AND password = \"" + psw + "\"";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                if (!resultSet.getBoolean(4))
                    utente = Elettore.getInstance(/*resultSet.getString(2), resultSet.getInt(1)*/);
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
    public boolean haVotato(String user, String psw, Votazione v) {

        return true;
    }

}