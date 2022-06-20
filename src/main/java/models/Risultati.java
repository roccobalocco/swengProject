package models;

import java.util.List;

public class Risultati {
    private Votazione v;
    private List candidati;

    public Risultati(Votazione v, List c){
        this.v = v;
        candidati = c;
    }

    public Risultati() {
        //temporaneo
    }
}
