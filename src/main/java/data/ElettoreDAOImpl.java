package data;

import models.Admin;
import models.Elettore;
import models.Votazione;
import util.Observable;
import util.Observer;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Piemme
 */
public class ElettoreDAOImpl implements ElettoreDAO, Observable {

    /**
     * Default constructor
     */
    private ElettoreDAOImpl() {
        obs = new LinkedList<>();
        obs.add(Elettore.getInstance());
    }

    private final List<Observer> obs;
    private static ElettoreDAOImpl uniqueInstance;

    public static ElettoreDAOImpl getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new ElettoreDAOImpl();
        return uniqueInstance;
    }

    public Elettore getElettore(String cf, String psw) {
        Elettore utente = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM utenti WHERE `cf` = '" + cf + "' AND `password` = '" + psw + "'";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                utente = Elettore.getInstance(resultSet.getString(2), resultSet.getString(3), resultSet.getString(1), resultSet.getDate(4));
                //System.out.println("Ci sono dei risultati: " + Elettore.getInstance().toString());

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

    public boolean isElettore(String cf, String psw){
        return getElettore(cf, psw) != null;
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