package data;

import models.Elettore;
import models.Votazione;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     * @param cf
     * @param psw
     */
    Elettore getElettore(String cf, String psw);

    /**
     *
     * @param cf
     * @param psw
     * @return
     */
    boolean haVotato(String cf, String psw, Votazione v);

    boolean isElettore(String cf, String psw);
}