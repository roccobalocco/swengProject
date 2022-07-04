package data;

import models.Admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import util.Observable;
import util.Observer;

/**
 * @author Piemme
 */
public class DbManager implements Observable{

    private static DbManager uniqueInstance;
    private final List<Observer> obs;
    private DbManager(){
        obs = new LinkedList<>();
        obs.add(Admin.getInstance());
    }

    public static DbManager getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new DbManager();
        return uniqueInstance;
    }

    public void resetAllVotazioni() throws IOException {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SET SQL_SAFE_UPDATES=0; delete from swengdb.persone; delete from swengdb.voti_gruppi; delete from swengdb.votazione; delete from swengdb.gruppi; delete from swengdb.referendum; delete from swengdb.v_c; delete from swengdb.v_r;";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Cancellazione di tutte le votazioni]");
    }

    @Override
    public void subscribe(Observer o) {
        this.obs.add(o);
    }

    @Override
    public void unsubcribe(Observer o) {
        this.obs.remove(o);
    }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : this.obs)
                o.update(s);
    }
}