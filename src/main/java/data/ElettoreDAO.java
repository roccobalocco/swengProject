package data;

import models.Elettore;
import models.Votazione;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     */
    Elettore getElettore(String cf, String psw);

    /**
     *
     */
    boolean haVotato(String cf, String psw, Votazione v);

    boolean isElettore(String cf, String psw);
}