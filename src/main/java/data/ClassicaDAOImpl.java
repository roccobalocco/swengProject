package data;

import models.*;
import util.Observer;
import util.Observable;
import util.Util;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class ClassicaDAOImpl implements VotazioneDAO, Observable{

    private ClassicaDAOImpl() {
        obs = new LinkedList<>();
        obs.add(Admin.getInstance());
    }

    private final List<Observer> obs;
    private Classica appoggio;
    private static ClassicaDAOImpl uniqueInstance;

    public static ClassicaDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ClassicaDAOImpl();
        return uniqueInstance;
    }

    public void setAppoggio(Classica c){ uniqueInstance.appoggio = c; }
    public Classica getAppoggio(){ return uniqueInstance.appoggio; }

    /**
     * Inserisci la votazione presente nel Singleton
     */
    public boolean addVotazione() throws IOException {
        if(uniqueInstance.appoggio != null && (ClassicaDAOImpl.uniqueInstance.getVotazione(uniqueInstance.appoggio.getId())) == null)
            return this.addVotazione(uniqueInstance.appoggio);
        assert uniqueInstance.appoggio != null;
        //System.out.println("Appoggio NON inserito: " + uniqueInstance.appoggio);
        return false;
    }

    /**
     * Il metodo inserisce il Gruppo nel DB e lo collega alla votazione 'c'
     * @param c votazione di competenza
     * @param g gruppo da inserire nel db
     */
    public void addGruppo(Classica c, Gruppo g) throws IOException {
        try{
            if(CandidatoDAOImpl.getInstance().addGruppo(g))
                System.out.println("Inserimento Gruppo andato a buon fine");
            else
                System.out.println("Inserimento Gruppo NON andato a buon fine");
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `voti_gruppi` (`votazione_fk2`, `gruppi_fk`, `voti_gruppo`) " +
                    "VALUES( " + c.getId() + ", " + g.getId() + ", 0)";
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //chiudo connession
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Aggiunta Gruppo: " + g + " a Votazione: " + c + "]");
    }

    public List<Classica> getAllOrdinale() throws IOException {
        List<Classica> l = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `ordinale` = 1";
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                l.add(new Classica(resultSet.getString(5), Util.toDateTime(resultSet.getDate(4)), true, false, resultSet.getInt(1), resultSet.getBoolean(3) ));
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Richiesta Votazioni Ordinali]");
        return l;
    }

    public List<Classica> getAllCategorico() throws IOException {
        List<Classica> l = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `ordinale` = 0";
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                l.add(new Classica(resultSet.getString(5), Util.toDateTime(resultSet.getDate(4)), false, resultSet.getBoolean(7), resultSet.getInt(1),  resultSet.getBoolean(3) ));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Richiesta Votazioni categoriche]");
        return l;
    }

    /**
     * @param id l'identificatore non nullo della votazione
     * @return null se non esiste la votazione corrispondente, la votazione altrimenti
     * @param <T> la sottoclasse di Votazione --> Classica
     */
    @SuppressWarnings("unchecked")
    public <T extends Votazione> T getVotazione(int id) throws IOException {
        Classica c = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `id` = " + id;
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                c = new Classica(resultSet.getString(5), Util.toDateTime(resultSet.getDate(4)), false, resultSet.getBoolean(7), resultSet.getInt(1), resultSet.getBoolean(3));
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Richiesta Votazione con id: " + id + "]");
        return (T) c;
    }


    public <T extends Votazione> boolean updateVotazione(T v) throws IOException {
        Classica c = (Classica) v;
        if(getVotazione(c.getId()) == null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE `votazione` SET" +
                    "`assoluta` = " + c.isAssoluta() + "," +
                    "`scadenza` = '" + c.getScadenza() + " 00:00:00', " +
                    "`descrizione` = '" + c.descrizione + "', " +
                    "`ordinale` = " + (c.whichType()  == 0) + ", " +
                    "`preferenza` = " + (c.whichType()  == 2) + " WHERE `id` = " + c.getId();
            System.out.println("Query che sta per essere eseguita:\n" + query);
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
            return false;
        }
        getInstance().notifyObservers("[Aggiornamento Votazione: " + c + "]");
        return true;
    }

    public <T extends Votazione> boolean addVotazione(T v) throws IOException {
        Classica c = (Classica) v;
        //System.out.println("Inserimento di votazione: \n" + c.toString());
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `votazione`" +
                "(`id`, `assoluta`, `scadenza`, `descrizione`,`ordinale`, `preferenza`) VALUES" +
                "(" + c.getId() + ", " + (c.isAssoluta() ? 1 : 0) + ", '" + c.getScadenza() + " 00:00:00', '" +
                c.descrizione + "', " + (c.whichType() == 0) + ", " + (c.whichType() == 2) + ")";

            //System.out.println("Query che sta per essere eseguita:\n" + query);
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
            return false;
        }
        getInstance().notifyObservers(" [Aggiunta Votazione: " + c + "]");
        return true;
    }

    public void deleteVotazione(int id) throws IOException {
        if(getVotazione(id) == null)
                return;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM votazione WHERE `id` = " + id;
            //System.out.println("Query che sta per essere eseguita:\n" + query);
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
            return;
        }
        getInstance().notifyObservers(" [Cancellazione Votazione con id: " + id + "]");
    }

    public Risultati getRisultati(Votazione v) throws IOException {
        //TODO
        getInstance().notifyObservers(" [Richiesta Risultati per Votazione: " + v + "]");
        return null;
    }

    public int getNextId() throws IOException {
        int id = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM votazione";
            //System.out.println("Query che sta per essere eseguita:\n" + query);
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
        //System.out.println("Next id --> " + id);
        getInstance().notifyObservers(" [Richiesta prossimo id per Votazione]");
        return id;
    }

    public boolean canVote(Classica c) throws IOException {
        boolean cv = true;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM v_c WHERE v_c.cf_fk = '" + Elettore.getInstance().getCF() + "' AND v_c.votazione_fk = " + c.getId();
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                cv = false;
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        notifyObservers(" [Ha giá votato per: " + c + "]");
        return cv;
    }

    public void setTot(int v) throws IOException {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE votazione SET tot = " + v + " WHERE votazione.id = " + getInstance().getAppoggio().getId();
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //chiudo connessione
            conn.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Aggiunto totale possibili votanti: " + v + " per Referendum : " + appoggio + "]");
    }

    public void addVoti(Gruppo g, int v){
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE voti_gruppi SET voti_gruppo = voti_gruppo + " + v + " WHERE gruppi_fk = " + g.getId();
            //System.out.println("Query che sta per essere eseguita:\n" + query);
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
    }

    public void addVoti(Persona p, int v){
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE persone SET voti = voti + " + v + " WHERE id = " + p.getId();
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo connessione
            conn.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public Risultati getRisultati(Classica c){
        //TODO
        return null;
    }

    public int getVotanti(Classica c) {
        int tot = -1;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT tot FROM votazione WHERE id = " + c.getId();
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                tot = resultSet.getInt(1);
            //chiudo connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
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