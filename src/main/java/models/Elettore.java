package models;

import util.Observer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Piemme
 */
@SuppressWarnings({"deprecation"})
public class Elettore extends Utente implements Observer {

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
    private final String nome, cognome;
    private final Date dn;

    /**
     */
    public static Elettore getInstance(String nome, String cognome, String cf, Date dn) {
        if(uniqueInstance == null)
            uniqueInstance = new Elettore(nome, cognome, cf, dn);
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
        return "Nome: " + nome + " -/- Cognome: " + cognome + " -/- Data di nascita: " + dn.toString() + "\n" + super.toString();
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

    @Override
    public void update(String s) throws IOException {
        File f = new File("src/main/resources/logFiles/elettoreLogs.log");
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw =  new BufferedWriter(fw);

        bw.write(LocalDateTime.now() + " - ");
        bw.write(Elettore.getInstance().toString() + " - ");
        bw.write(s);
        bw.write("\n");
        bw.close();
    }

}