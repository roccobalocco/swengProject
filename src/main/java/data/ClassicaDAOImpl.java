package data;

import models.Classica;
import models.Risultati;
import models.Votazione;

import java.util.*;

/**
 * @author Piemme
 */
public class ClassicaDAOImpl extends VotazioneDAOImpl {

    /**
     * Default constructor
     */
    private ClassicaDAOImpl() { }

    private static ClassicaDAOImpl uniqueInstance;

    public static ClassicaDAOImpl getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ClassicaDAOImpl();
        return uniqueInstance;
    }

    /**
     * @return
     */
    public List<Classica> getAllOrdinale() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Classica> getAllCategorico() {
        // TODO implement here
        return null;
    }

    public Risultati getRisultati() {
        return null;
    }

    public <T extends Votazione> T getVotazione(String id) {
        return null;
    }

    public <T extends Votazione> boolean updateVotazione(String id, T v) {
        return false;
    }

    public <T extends Votazione> boolean addVotazione(T v) {
        return false;
    }

    public boolean deleteVotazione(String id) {
        return false;
    }

    public Risultati getRisultati(Votazione v) {
        return null;
    }
}