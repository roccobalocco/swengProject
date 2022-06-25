package data;

import models.Candidato;
import models.Gruppo;
import models.Persona;
import models.Referendum;

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
            String query = "SELECT * FROM candidati WHERE id = gruppi_fk";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                lg.add(new Gruppo(resultSet.getInt(1), resultSet.getString(2)));

            query = "SELECT * FROM candidati WHERE id <> gruppi_fk";
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
     * @param id identificatore del candidato (persona o gruppo politico)
     * @return null se id non corrisponde a nessun candidato, Gruppo o Persona identificata altrimenti
     * @param <T> possibile sottitipo di Candidato
     */
    @Override
    public <T extends Candidato> T getCandidato(int id) {
        Candidato c = null;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM candidati WHERE `id` = " + id;
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            resultSet.next();
            if(resultSet.getInt(1) == resultSet.getInt(3))
                c = new Gruppo(resultSet.getInt(1), resultSet.getString(2));
            else
                c = new Persona(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
            //chiudo resultset e connessione
            resultSet.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return (T) c;
    }

    @Override
    public boolean addGruppo(Gruppo c) {
        if(getCandidato(c.getId()) != null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO candidati (`id`, `nome`, `gruppi_fk`) VALUES ( "+ c.getId() +", '" + c.getNome() + "', " + c.getId() + ")";
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
    public boolean addPersona(Persona p) {
        if(getCandidato(p.getId()) != null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "INSERT INTO candidati (`id`, `nome`, `gruppi_fk`) VALUES ( "+ p.getId() +", '" + p.getNome() + "', " + p.getGruppo() + ")";
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
    public boolean deleteCandidato(int id) {
        if(getCandidato(id) == null)
            return false;
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "DELETE FROM candidati WHERE `id` = " + id;
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
    public List<Gruppo> getGruppi() {
        return (List<Gruppo>) getAllCandidati().get(0);
    }

    @Override
    public List<Persona> getPersone() {
        return (List<Persona>) getAllCandidati().get(1);
    }
}
