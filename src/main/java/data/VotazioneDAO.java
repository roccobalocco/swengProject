package data;

import models.Risultati;
import models.Votazione;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    List<List<? extends Votazione>> getAllVotazioni();

    <T extends Votazione> T getVotazione(String id);

    <T extends Votazione> boolean updateVotazione(String id, T v);

    <T extends Votazione> boolean addVotazione(T v);

    boolean deleteVotazione(String id);

    Risultati getRisultati(Votazione v);

}