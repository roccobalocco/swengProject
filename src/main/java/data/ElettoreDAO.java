package data;

import models.Elettore;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     */
    Elettore getElettore(String cf, String psw);

    boolean isElettore(String cf, String psw);
}