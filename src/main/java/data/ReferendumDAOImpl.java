package data;

import models.Elettore;
import models.Referendum;
import models.Risultati;
import models.Votazione;

import java.sql.*;
import java.util.*;

/**
 * @author Piemme
 */
public class ReferendumDAOImpl extends VotazioneDAOImpl {

    /**
     * Default constructor
     */
    private ReferendumDAOImpl() { }

    private static ReferendumDAOImpl uniqueInstance;

    public static ReferendumDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ReferendumDAOImpl();
        return uniqueInstance;
    }

    public List<Referendum> getAll(){
        List<Referendum> referendums = new LinkedList<>();
        try{
            //apro connessione
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swengdb?useSSL=false", "root", "root");
            //scrivo query
            String query = "SELECT * FROM referendum";
            //creo oggetto statement per esecuzione query
            PreparedStatement statement = conn.prepareStatement(query);
            //eseguo la query
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            while(resultSet.next())
                referendums.add(new Referendum(resultSet.getString(2), resultSet.getDate(7), resultSet.getBoolean(6)));

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

    /**
     * @return
     */
    public List<Referendum> getAllNoQuorum() {
        List<Referendum> aq = new LinkedList<>();
        this.getAll().forEach((Referendum r) -> { if(!r.hasQuorum()){ aq.add(r); } });
        return aq;
    }

    public Risultati getRisultati() {
        return null;
    }

    public Votazione getVotazione(String id) {
        return null;
    }

    public boolean updateVotazione(String id, Votazione v) {
        return false;
    }

    public boolean addVotazione(Votazione v) {
        return false;
    }

    public boolean deleteVotazione(Votazione v) {
        return false;
    }


    public Risultati getRisultati(Votazione r) {
        if(r.fineVotazione()){
            return new Risultati();
        }else {
            return null;
        }
    }
}