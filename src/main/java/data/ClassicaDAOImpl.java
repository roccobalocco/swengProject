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

    public Risultati getRisultati(Votazione v) {
        return null;
    }
}