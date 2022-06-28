package data;

import models.Risultati;
import models.Votazione;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    List<List<? extends Votazione>> getAllVotazioni();

    <T extends Votazione> T getVotazione(int id);

    <T extends Votazione> boolean updateVotazione(T v);

    <T extends Votazione> boolean addVotazione(T v);

    boolean deleteVotazione(int id);

    Risultati getRisultati(Votazione v);

}