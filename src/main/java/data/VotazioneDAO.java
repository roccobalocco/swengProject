package data;

import models.Risultati;
import models.Votazione;

import java.io.IOException;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    List<List<? extends Votazione>> getAllVotazioni();

    <T extends Votazione> T getVotazione(int id) throws IOException;

    <T extends Votazione> boolean updateVotazione(T v) throws IOException;

    <T extends Votazione> boolean addVotazione(T v) throws IOException;

    boolean deleteVotazione(int id) throws IOException;

    Risultati getRisultati(Votazione v) throws IOException;

}