package data;

import models.Admin;
import models.Elettore;

import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class AdminDAOImpl implements AdminDAO {

    /**
     * Default constructor
     */
    private AdminDAOImpl() {
    }

    private static AdminDAOImpl uniqueInstance;

    public static AdminDAOImpl getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new AdminDAOImpl();
        }// TODO implement here
        return uniqueInstance;
    }
    /**
     * @param cf
     * @param psw 
     * @return
     */
    public Admin getAdmin(String cf, String psw) {
        Admin utente = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM utenti WHERE cf = \"" + cf + "\" AND password = \"" + psw + "\" AND isAdmin = " + "1" ;
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next()){
                utente = Admin.getInstance(resultSet.getString(1));
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



}