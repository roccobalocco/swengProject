package models;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class Risultati {
    private String descrizione;
    private List candidati;

    public Risultati(String d, List c){
        descrizione = d;
        candidati = c;
    }
}
