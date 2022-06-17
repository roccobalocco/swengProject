package data;

import models.Votazione;

import java.util.*;

/**
 * @author Piemme
 */
public abstract class VotazioneDAOImpl implements VotazioneDAO {

    /**
     * Default constructor
     */
    public VotazioneDAOImpl() {
    }

    /**
     * 
     */
    private List<Votazione> votazioni;


    /**
     * @return
     */
    public List getAllVotazione() {
        // TODO implement here
        return null;
    }

    /**
     * @param id String 
     * @return
     */
    public Votazione getVotazione(String id) {
        // TODO implement here
        return null;
    }

    /**
     * @param v Votazione 
     * @return
     */
    public Boolean addVotazione(Votazione v) {
        // TODO implement here
        return null;
    }

    /**
     * @param id String 
     * @param v Votazione 
     * @return
     */
    public Boolean updateVotazione(String id, Votazione v) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public boolean deleteVotazione() {
        // TODO implement here
        return true;
    }

    /**
     * @return
     */
    public List getAllVotazioni() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Votazione getVotazione() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public boolean addVotazione() {
        // TODO implement here
        return false;
    }

}