package data;

import models.*;
import util.Observable;
import util.Observer;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CandidatoDAOImpl implements CandidatoDAO, Observable {

    private final List<Observer> obs;
    private static CandidatoDAOImpl uniqueInstance;

    private CandidatoDAOImpl(){
        obs = new LinkedList<>();
        obs.add(Admin.getInstance());
    }

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
    public List<List<? extends Candidato>> getAllCandidati() throws IOException {
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
        getInstance().notifyObservers(" [Richiesta di tutti i candidati (Gruppi e Persone)]");
        return lc;
    }

    /**
     *
     * @param id non-negative int
     * @return null se non esiste il gruppo, il gruppo corrispondente altrimenti
     */
    @Override
    public Gruppo getGruppo(int id) throws IOException {
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
        getInstance().notifyObservers(" [Richiesta Gruppo con id: " + id + "]");
        return g;
    }


    /**
     *
     * @param id non-negative int
     * @return null se non esiste il candidato, il candidato corrispondente altrimenti
     */
    @Override
    public Persona getPersona(int id) throws IOException {
        Persona p = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM persone WHERE `id` = " + id;
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                p = new Persona(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(5));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("ERRORE ROW130-SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers(" [Richiesta Persona con id: " + id + "]");
        return p;
    }


    @Override
    public boolean addGruppo(Gruppo c) throws IOException {
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
        getInstance().notifyObservers(" [Aggiunta Gruppo: " + c + "]");
        return true;
    }

    @Override
    public boolean addPersona(Classica c, Gruppo g, Persona p) throws IOException {
        if(getPersona(p.getId()) != null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO `persone` (`id`, `nome`, `voti`, `voti_gruppiFK`, `gruppoFK`) VALUES( " + p.getId() + ", '" + p.getNome() + "', 0, " + c.getId() + ", " + g.getId() +")";
            System.out.println("Query che sta per essere eseguita:\n" + query);
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            statement.executeUpdate();

            //chiudo connessione
            conn.close();
        }catch(SQLException e){
            System.out.println("ROW 181 CandidatoDAOImpl--SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        this.notifyObservers(" [Aggiunta Persona: " + p + ", di Gruppo: " + g + " per Votazione: " + c + "]");
        return true;
    }

    @Override
    public boolean deletePersona(int id) throws IOException {
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
        getInstance().notifyObservers(" [Cancellazione persona con id: " + id + " ]");
        return true;
    }

    @Override
    public boolean deleteGruppo(int id) throws IOException {
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
        getInstance().notifyObservers(" [Cancellazione gruppo con id: " + id + " ]");
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Gruppo> getGruppi() throws IOException {
        getInstance().notifyObservers(" [Richiesta lista gruppi]");
        return (List<Gruppo>) getAllCandidati().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Persona> getPersone() throws IOException {
        getInstance().notifyObservers(" [Richiesta lista persone]");
        return (List<Persona>) getAllCandidati().get(1);
    }

    public int getNextIdGruppo() throws IOException {
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
        getInstance().notifyObservers(" [Richiesta prossimo id per gruppo]");
        return i;
    }

    @Override
    public int getNextIdPersona() throws IOException {
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
        getInstance().notifyObservers(" [Richiesta prossimo id per persona]");
        return i;
    }

    @Override
    public List<Persona> getPersone(Gruppo g) throws IOException {
        List<Persona> lp = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT persone.id, persone.nome, persone.gruppoFK " +
                    "FROM persone WHERE persone.gruppoFK = " + g.getId();
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
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
        getInstance().notifyObservers(" [Richiesta persone per gruppo " + g + " ]");
        return lp;
    }

    @Override
    public List<Gruppo> getGruppi(Classica c) throws IOException {
        List<Gruppo> lg = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT gruppi.id, gruppi.nome FROM gruppi JOIN voti_gruppi ON gruppi.id = voti_gruppi.gruppi_fk WHERE voti_gruppi.votazione_fk2 = " + c.getId();
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                lg.add(new Gruppo(resultSet.getInt(1), resultSet.getString(2)));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers(" [Richiesta gruppi per votazione " + c + " ]");
        return lg;
    }

    public void insertVoto(Classica c, Gruppo g, Integer i) {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE `voti_gruppi` SET `voti_gruppo` = `voti_gruppo` + " + i +
                    " WHERE `gruppi_fk` = " + g.getId() + " AND `votazione_fk2` = " + c.getId();
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

    public void insertVoto(Persona p, Integer i) {
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "UPDATE `persone` SET `voti` = `voti` + " + i +
                    " WHERE `id` = " + p.getId();
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

    public Map<Persona, Integer> getMapP() throws IOException {
        Map<Persona, Integer> mp = new HashMap<>();
        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        List<Gruppo> lg = CandidatoDAOImpl.getInstance().getGruppi(c);
        try {
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            for(Gruppo g : lg){
                List<Persona> lp = CandidatoDAOImpl.getInstance().getPersone(g);
                for(Persona p : lp) {
                    String query = "SELECT voti FROM persone WHERE id = " + p.getId();
                    //creo oggetto statement per esecuzione query
                    PreparedStatement statement = conn.prepareStatement(query);
                    //eseguo la query
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next())
                        mp.put(p, resultSet.getInt(1));
                    //chiudo connessione e resulset
                    resultSet.close();
                }
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mp;
    }

    public Map<Gruppo, Integer> getMapG() throws IOException {
        Map<Gruppo, Integer> mg = new HashMap<>();
        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        List<Gruppo> lg = CandidatoDAOImpl.getInstance().getGruppi(c);

        try {
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            for(Gruppo g : lg){
                String query = "SELECT voti_gruppo FROM voti_gruppi WHERE gruppi_fk = " + g.getId();
                //creo oggetto statement per esecuzione query
                PreparedStatement statement = conn.prepareStatement(query);
                //eseguo la query
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next())
                    mg.put(g, resultSet.getInt(1));
                //chiudo connessione e resulset
                resultSet.close();
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return mg;
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
