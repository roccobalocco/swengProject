package data;

import models.Risultati;
import models.Votazione;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    public <T extends Votazione> List<T> getAllVotazioni();

    public abstract <T extends Votazione> T getVotazione(String id);

    public abstract <T extends Votazione> boolean updateVotazione(String id, T v);

    public abstract <T extends Votazione> boolean addVotazione(T v);

    public abstract boolean deleteVotazione(String id);

    public abstract Risultati getRisultati(Votazione v);

}