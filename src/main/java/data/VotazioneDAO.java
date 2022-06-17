package data;

import models.Votazione;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    /**
     * @return
     */
    public List getAllVotazioni();

    /**
     * @return
     */
    public Votazione getVotazione();

    /**
     * 
     */
    public void addVotazione();

    /**
     * 
     */
    public void deleteVotazione();

}