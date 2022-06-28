package data;

import models.*;

import java.sql.*;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Piemme
 */
public class ClassicaDAOImpl extends VotazioneDAOImpl {

    private ClassicaDAOImpl() { }

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
    public boolean addVotazione(){
        if(uniqueInstance.appoggio != null && (ClassicaDAOImpl.uniqueInstance.getVotazione(uniqueInstance.appoggio.getId())) == null)
            return this.addVotazione(uniqueInstance.appoggio);
        assert uniqueInstance.appoggio != null;
        System.out.println("Appoggio NON inserito: " + uniqueInstance.appoggio);
        return false;
    }
    /**
     * Il metodo inserisce il Gruppo nel DB e lo collega alla votazione 'c'
     * @param c votazione di competenza
     * @param g gruppo da inserire nel db
     */
    public void addGruppo(Classica c, Gruppo g){
        try{
            if(CandidatoDAOImpl.getInstance().addGruppo(g))
                System.out.println("Inserimento Gruppo andato a buon fine");
            else
                System.out.println("Inserimento Gruppo NON andato a buon fine");
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `voti_gruppi` (`votazione_fk2`, `gruppi_fk`, `voti_gruppo`) " +
                    "VALUES( " + c.getId() + ", '" + g.getId() + "', 0)";
            System.out.println("Query che sta per essere eseguita:\n" + query);
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
    }

    /**
     * Il meotodo inserisce la Persona nel DB e la collega alla votazione e al suo gruppo, SOLO PER CATEGORICO PREFERENZIALE
     * @param c votazione di competenza
     * @param g gruppo di appartenenza del parametro 'p'
     * @param p persona da inserire, facente parte del partito 'g'
     */
    public void addPersona(Classica c, Gruppo g, Persona p){
        if(CandidatoDAOImpl.getInstance().addPersona(c, g, p))
            System.out.println("Inserimento Persona andato a buon fine");
        else
            System.out.println("Inserimento Persona NON andato a buon fine");
    }

    public List<Classica> getAllOrdinale() {
        List<Classica> l = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `ordinale` = 1";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                l.add(new Classica(resultSet.getString(5), resultSet.getDate(4).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), true, false, resultSet.getInt(1), resultSet.getBoolean(3) ));
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return l;
    }

    public List<Classica> getAllCategorico() {
        List<Classica> l = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `ordinale` = 0";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                l.add(new Classica(resultSet.getString(5), resultSet.getDate(4).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), false, resultSet.getBoolean(7), resultSet.getInt(1),  resultSet.getBoolean(3) ));
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return l;
    }

    public Risultati getRisultati() {
        //TODO
        return null;
    }

    /**
     *
     * @param id l'identificatore non nullo della votazione
     * @return null se non esiste la votazione corrispondente, la votazione altrimenti
     * @param <T> la sottoclasse di Votazione --> Classica
     */
    @SuppressWarnings("unchecked")
    public <T extends Votazione> T getVotazione(int id) {
        Classica c = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM votazione WHERE `id` = " + id;
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                c = new Classica(resultSet.getString(5), resultSet.getDate(4).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), false, resultSet.getBoolean(7), resultSet.getInt(1), resultSet.getBoolean(3));
            //chiudo resultset e connession
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return (T) c;
    }


    public <T extends Votazione> boolean updateVotazione(T v) {
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
                    "`preferenza` " + (c.whichType()  == 2) + ", " +
                    "WHERE `id` = " + c.getId();
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
        return true;
    }

    public <T extends Votazione> boolean addVotazione(T v) {
        Classica c = (Classica) v;
        System.out.println("Inserimento di votazione: \n" + c.toString());
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `votazione`" +
                "(`id`, `assoluta`, `scadenza`, `descrizione`,`ordinale`, `preferenza`) VALUES" +
                "(" + c.getId() + ", " + (c.isAssoluta() ? 1 : 0) + ", '" + c.getScadenza() + " 00:00:00'," +
                c.descrizione + ", " + (c.whichType() == 0) + ", " + (c.whichType() == 2) + ")";

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
        return true;
    }

    public boolean deleteVotazione(int id) {
        if(getVotazione(id) == null)
                return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM votazione WHERE `id` = " + id;

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
        return true;
    }

    public Risultati getRisultati(Votazione v) {
        //TODO
        return null;
    }

    public int getNextId(){
        int id = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM votazione";
            System.out.println("Query che sta per essere eseguita:\n" + query);
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

}