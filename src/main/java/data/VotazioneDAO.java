package data;

import models.Risultati;
import models.Votazione;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    public List<Votazione> getAllVotazioni();

    public abstract Votazione getVotazione(String id);

    public abstract boolean updateVotazione(String id, Votazione v);

    public abstract boolean addVotazione(Votazione v);

    public abstract boolean deleteVotazione(Votazione v);

    public abstract Risultati getRisultati(Votazione v);

}