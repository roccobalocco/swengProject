package data;

import models.Admin;
import util.Observable;
import util.Observer;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Piemme
 */
public class AdminDAOImpl implements AdminDAO, Observable {

    /**
     * Default constructor
     */
    private AdminDAOImpl() {
        obs = new LinkedList<>();
    }

    private final List<Observer> obs;
    private static AdminDAOImpl uniqueInstance;

    public static AdminDAOImpl getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new AdminDAOImpl();
        return uniqueInstance;
    }

    /**
     * @param cf un codice alfanumerico di 16 caratteri che venga trovato all'interno della base di dati, non nullo
     * @param psw stringa non nulla ne vuota
     * @return null se non viene trovato nulla, l'Admin se viene trovato l'utente
     */
    public Admin getAdmin(String cf, String psw) throws IllegalArgumentException, NullPointerException, IOException {
        Objects.requireNonNull(cf);
        Objects.requireNonNull(psw);
        if(cf.length() != 16)
            throw new IllegalArgumentException("Codice fiscale non riconosciuto");
        Admin utente = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM utenti WHERE `cf` = '" + cf + "' AND `password` = '" + psw + "' AND `isAdmin` = 1";
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
        getInstance().notifyObservers(" [Ricerca candidato] ");
        return utente;
    }


    public boolean isAdmin(String cf, String psw) throws IOException {
        return this.getAdmin(cf, psw) != null;
    }

    @Override
    public void subscribe(Observer o) { uniqueInstance.obs.add(o); }

    @Override
    public void unsubcribe(Observer o) { uniqueInstance.obs.remove(o); }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : uniqueInstance.obs)
            o.update(s);
    }

}