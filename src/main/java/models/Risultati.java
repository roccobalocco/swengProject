package models;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class Risultati {
    private final String descrizione;
    private final List candidati;

    public Risultati(String d, List c){
        descrizione = d;
        candidati = c;
    }
}
