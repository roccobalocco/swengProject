package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Piemme
 * Semplice classe astratta per unire le caratteristiche delle votazioni Classica e Referendum, che poi implementano proprio questa classe!
 * Unise le loro caratteristiche comune e permette di ottenere rapidamente alcune informazioni.
 */
public abstract class Votazione {

    /**
     * Default constructor
     */
    public Votazione(String desc, LocalDate scad, int id) {
        Objects.requireNonNull(desc, "Descrizione non puó essere nulla");
        Objects.requireNonNull(scad, "Scadenza non puó essere nulla");
        if(desc.length() < 0)
            throw new IllegalArgumentException("Descrizione non contiene nulla");
        descrizione = desc;
        scadenza = scad;
        this.id = id;
    }

    private final int id;
    public final String descrizione;
    private final LocalDate scadenza;

    /**
     * @return true se é passata la data di scadenza, false altrimenti
     */
    public boolean fineVotazione(){
        return scadenza.isBefore(LocalDate.now());
    }

    public int getId(){ return id; }

    public String getScadenza() {
        return scadenza.toString();
    }

    public LocalDate getScadenzaLD(){ return scadenza; }
}