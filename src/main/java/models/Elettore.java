package models;

import java.time.Period;
import java.util.*;

/**
 * @author Piemme
 */
public class Elettore extends Utente {

    /**
     * Default constructor
     */
    private Elettore(String nome, String cognome, String cf, Date dn) {
        super(cf);
        this.nome = nome;
        this.cognome = cognome;
        this.dn = dn;

    }

    private static Elettore uniqueInstance;
    private String nome, cognome;
    private Date dn;

    /**
     * @return
     */
    public static Elettore getInstance(String nome, String cognome, String cf, Date dn) {
        if(uniqueInstance == null){
            uniqueInstance = new Elettore(nome, cognome, cf, dn);
        }// TODO implement here
        return uniqueInstance;
    }

    /**
     *
     * @return null se non c'é alcun elettore, l'elettore altrimenti
     */
    public static Elettore getInstance(){
        return uniqueInstance;
    }
    public String toString(){
        StringBuilder s = new StringBuilder("Nome: ");
        s.append(nome + "\n");
        s.append("Cognome: " + cognome + "\n");
        s.append("Data di nascita: " + dn.toString() + "\n");
        return s.toString() + super.toString();
    }

    /**
     *
     * @return true se l'etá dell'elettore é almeno 18 anni compiuti, false altrimenti
     */
    public boolean puoVotare(){
        Date now = new Date();
        if(now.getYear() - dn.getYear() > 18)
            return true;
        else if(now.getYear() - dn.getYear() < 18)
            return false;

        if(now.getMonth() - dn.getMonth() > 0)
            return true;
        if(now.getMonth() - dn.getMonth() < 0)
            return false;

        return now.getDay() - dn.getDay() >= 0;
    }
}