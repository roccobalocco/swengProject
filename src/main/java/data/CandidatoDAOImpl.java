package data;

import models.Candidato;
import models.Classica;
import models.Gruppo;
import models.Persona;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CandidatoDAOImpl implements CandidatoDAO{

    private static CandidatoDAOImpl uniqueInstance;

    private CandidatoDAOImpl(){ }

    public static CandidatoDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new CandidatoDAOImpl();
        return uniqueInstance;
    }

    /**
     * Il db deve essere attivo!
     * @return una lista con al suo interno due liste, la prima di tipo Gruppo e la seconda di tipo Persona
     */
    @Override
    public List<List<? extends Candidato>> getAllCandidati() {
        List<Gruppo>  lg = new LinkedList<>();
        List<Persona> lp = new LinkedList<>();
        List<List<? extends Candidato>> lc = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM gruppi";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                lg.add(new Gruppo(resultSet.getInt(1), resultSet.getString(2)));

            query = "SELECT * FROM persone";
            //creo oggetto statement per esecuzione query
            statement = conn.prepareStatement(query);
            //eseguo la query
            resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                lp.add(new Persona(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));

            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        lc.add(lg);
        lc.add(lp);
        return lc;
    }

    /**
     *
     * @param id non-negative int
     * @return null se non esiste il gruppo, il gruppo corrispondente altrimenti
     */
    @Override
    public Gruppo getGruppo(int id) {
        Gruppo g = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM gruppi WHERE `id` = " + id;
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                g = new Gruppo(resultSet.getInt(1), resultSet.getString(2));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return g;
    }


    /**
     *
     * @param id non-negative int
     * @return null se non esiste il candidato, il candidato corrispondente altrimenti
     */
    @Override
    public Persona getPersona(int id) {
        Persona p = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM gruppi WHERE `id` = " + id;
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                p = new Persona(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return p;
    }


    @Override
    public boolean addGruppo(Gruppo c) {
        if(getGruppo(c.getId()) != null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO gruppi (`id`, `nome`) VALUES (" + c.getId() +", '" + c.getNome() + "')";
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
        return true;
    }

    @Override
    public boolean addPersona(Classica c, Gruppo g, Persona p){
        if(getPersona(p.getId()) != null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `persone` (`id`, `nome`, `voti`, `voti_gruppiFK`, `gruppoFK`) " +
                    "VALUES( " + p.getId() + ", '" + p.getNome() + "', 0, '" + c.getId() + "', '" + g.getId() +"')";
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
        }
        return true;
    }

    @Override
    public boolean deletePersona(int id) {
        if(getPersona(id) == null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM persone WHERE `id` = " + id;
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
        return true;
    }

    @Override
    public boolean deleteGruppo(int id) {
        if(getGruppo(id) == null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM gruppi WHERE `id` = " + id;
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
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Gruppo> getGruppi() {
        return (List<Gruppo>) getAllCandidati().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Persona> getPersone() {
        return (List<Persona>) getAllCandidati().get(1);
    }

    public int getNextIdGruppo() {
        int i = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM gruppi";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                i = resultSet.getInt(1) + 1;
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return i;
    }

    @Override
    public int getNextIdPersona() {
        int i = 0;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT MAX(id) FROM persone";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                i = resultSet.getInt(1) + 1;
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return i;
    }

    @Override
    public List<Persona> getPersone(Classica c) {

        return null;
    }

    @Override
    public List<Gruppo> getGruppi(Classica c) {

        return null;
    }

}
