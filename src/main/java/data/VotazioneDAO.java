package data;

import models.Risultati;
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
    public boolean addVotazione();

    /**
     * 
     */
    public boolean deleteVotazione();

    public Risultati getRisultati();

}