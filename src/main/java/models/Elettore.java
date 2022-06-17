package models;

import java.util.*;

/**
 * @author Piemme
 */
public class Elettore extends Utente {

    /**
     * Default constructor
     */
    private Elettore(String nome, String cognome, String cf, Date dn, String mail) {
        super(cf);
        this.nome = nome;
        this.cognome = cognome;
        this.dn = dn;
        this.mail = mail;

    }

    /**
     * 
     */
    private static Elettore uniqueInstance;
    private String nome, cognome, cf, mail;
    private Date dn;

    /**
     * @return
     */
    public static Elettore getInstance(String nome, String cognome, String cf, Date dn, String mail) {
        if(uniqueInstance == null){
            uniqueInstance = new Elettore(nome, cognome, cf, dn, mail);
        }// TODO implement here
        return uniqueInstance;
    }

}