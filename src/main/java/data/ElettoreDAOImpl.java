package data;

import models.Classica;
import models.Elettore;
import models.Referendum;
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

    public void vota(Classica c) {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO v_c VALUES ('" + Elettore.getInstance().getCF() +
                    "', " + c.getId() + ")";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //scrivo query
            query = "UPDATE votazione SET `voti` = `voti` + 1";
            //creo oggetto per esecuzione query
            statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //chiudo connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public boolean isElettore(String cf, String psw){
        return getElettore(cf, psw) != null;
    }

    public int getVotanti(Classica c) throws IOException {
        int tot = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT COUNT(cf_fk) FROM v_c WHERE votazione_fk = " + c.getId() ;
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                tot = resultSet.getInt(1);

            //chiudo resultSet e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Richiesta votanti per Votazione Classica : " + c + "]");
        return tot;
    }

    public int getVotanti(Referendum r) throws IOException {
        int tot = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT COUNT(cf_fk) FROM v_r WHERE referendum_fk = " + r.getId() ;
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                tot = resultSet.getInt(1);

            //chiudo resultSet e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Richiesta votanti per Referendum : " + r + "]");
        return tot;
    }

    @Override
    public void subscribe(Observer o) { uniqueInstance.obs.add(o); }

    @Override
    public void unsubcribe(Observer o) { uniqueInstance.obs.remove(o); }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : uniqueInstance.obs)
            if(o != null)
                o.update(s);
    }

}