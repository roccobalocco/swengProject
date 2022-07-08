package data;

import models.*;
import util.Observable;
import util.Observer;
import util.Util;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class ReferendumDAOImpl implements VotazioneDAO, Observable {

    /**
     * Default constructor
     */
    private ReferendumDAOImpl() {
        obs = new LinkedList<>();
        obs.add(Admin.getInstance());
    }

    private final List<Observer> obs;
    private Referendum appoggio;

    private static ReferendumDAOImpl uniqueInstance;

    public static ReferendumDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ReferendumDAOImpl();
        return uniqueInstance;
    }
    public void setAppoggio(Referendum c){ uniqueInstance.appoggio = c; }
    public Referendum getAppoggio(){ return uniqueInstance.appoggio; }

    public List<Referendum> getAll(){
        List<Referendum> referendums = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum";
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendums.add(new Referendum(resultSet.getString(2), Util.toDateTime(resultSet.getDate(7)), resultSet.getBoolean(6), resultSet.getInt(1)));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return referendums;
    }

    public List<Referendum> getAllQuorum() {
        List<Referendum> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(r.hasQuorum()){ aq.add(r); } });
        return aq;
    }


    @SuppressWarnings("unchecked")
    public <T extends Votazione> List<T> getAllNoQuorum() {
        List<T> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(!r.hasQuorum()){ aq.add((T) r); } });
        return aq;
    }

    @SuppressWarnings("unchecked")
    public <T extends Votazione> T getVotazione(int id) {
        T referendum = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum WHERE `id` = " + id;
            //System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendum = (T) new Referendum(resultSet.getString(2), Util.toDateTime(resultSet.getDate(7)), resultSet.getBoolean(6), resultSet.getInt(1));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return referendum;
    }

    public <T extends Votazione> boolean updateVotazione(T t) {
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE referendum SET `descrizione` = \"" + v.descrizione + "\", `quorum` = " +
                    v.hasQuorum()  + ", `scadenza` = '" + v.getScadenza() + "' WHERE `id` = " + v.getId();
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();
            //chiudo resultset e connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }

    public <T extends Votazione> boolean addVotazione(T t) {
        Referendum v = (Referendum) t;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO referendum (`id`, `descrizione`, `quorum`, `scadenza`)";
            query += " VALUES (" + v.getId() + ", \"" + v.descrizione + "\", " + (v.hasQuorum() ? 1 : 0) + ", '" + v.getScadenza() + " 00:00:00' );";
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
        return true;
    }


    public void deleteVotazione(int id){
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM referendum WHERE `id` = " + id;
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

    public boolean canVote(Referendum r) throws IOException {
        boolean cv = true;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM v_r WHERE v_r.cf_fk = '" + Elettore.getInstance().getCF() + "' AND v_r.referendum_fk = " + r.getId();
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
        notifyObservers(" [Ha giá votato per: " + r + "]");
        return cv;
    }

    public int getNextId(){
        int id = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM referendum";
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
        System.out.println("Next id --> " + id);
        return id;
    }

    private String getVoto(int voto) throws IllegalArgumentException{
        switch (voto){
            case -1 -> {
                return "`bianca` = `bianca` + 1 ";
            }
            case 0 -> {
                return "`no` = `no` + 1 ";
            }
            case 1 -> {
                return "`si` = `si` + 1 ";
            }
            default -> throw new IllegalArgumentException("Intero non riconosciuto: " + voto);
        }
    }

    /**
     * Applica il voto nel Referendum settato in appoggio per l'utente autenticato al momento dell'invocazione
     * @param voto -1 se Scheda Bianca, 0 se No, 1 se Sí
     */
    public void vota(int voto){
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO v_r VALUES ('" + Elettore.getInstance().getCF() + "', " + appoggio.getId() + ")";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //scrivo query
            query = "UPDATE referendum SET " + getVoto(voto) + " WHERE `id` = " + appoggio.getId();
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
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

    public void setTot(int v) throws IOException {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE referendum SET tot = " + v + " WHERE referendum.id = " + getInstance().getAppoggio().getId();
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

    /**
     * Aggiunge i voti al referendum settato in appoggio a questa Classe
     * @requires (-1 <= opinione <= 1) && (v > 0) && (this.getInstance().getAppoggio() != null)
     * @param opinione -1 se bianca, 0 se no, 1 se si
     * @param v numero voti da inserire
     */
    public void addVoti(int opinione, int v) throws IOException {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE referendum SET " + getOpinione(opinione) + v +
                    " WHERE referendum.id = " + getInstance().getAppoggio().getId();
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
        getInstance().notifyObservers("[Aggiunta "+ opinione + ": " + v + " per Referendum : " + appoggio + "]");
    }

    /**
     * @param opinione -1 se bianca, 0 se no, 1 se si
     * @return stringa equivalente per aggiornare i voti nel db
     * @throws IllegalArgumentException se opinione > 1 || opinione < -1
     */
    private String getOpinione(int opinione) throws IllegalArgumentException{
        switch(opinione){
            case -1 -> { return "bianca = bianca + "; }
            case 0 -> { return "no = no + "; }
            case 1 -> { return "si = si + "; }
            default -> throw new IllegalArgumentException("Argomento non valido --> " + opinione);
        }
    }

    public Risultati getRisultati(Referendum r){
        //TODO
        return null;
    }

    public int getVotanti(Referendum r) {
        int tot = -1;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT tot FROM referendum WHERE id = " + r.getId();
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
    public void subscribe(util.Observer o) { uniqueInstance.obs.add(o); }

    @Override
    public void unsubcribe(util.Observer o) { uniqueInstance.obs.remove(o); }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : uniqueInstance.obs)
            if(o != null)
                o.update(s);
    }

}