package models;

import java.util.*;

/**
 * @author Piemme
 */
@SuppressWarnings({"deprecation", "FieldMayBeFinal"})
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
    private final String nome;
    private final String cognome;
    private final Date dn;

    /**
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
        return "Nome: " + nome + "\nCognome: " + cognome + "\nData di nascita: " + dn.toString() + "\n" + super.toString();
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