package models;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class Risultati {

    private boolean isClassica = true;
    private Classica c;
    private Referendum r;
    private int si, no, bianca, totale;
    private Map<Gruppo, Integer> votiGruppi;
    private Map<Persona, Integer> votiPersone;


    public Risultati(Classica c, int tot, Map<Gruppo, Integer> vg, Map<Persona, Integer> vp){
        this.c = c;
        totale = tot;
        votiGruppi =  vg;
        votiPersone = vp;
    }

    public Risultati(Classica c, Map<Gruppo, Integer> vg, Map<Persona, Integer> vp){
        this.c = c;
        votiGruppi =  vg;
        votiPersone = vp;
    }

    public Risultati(Classica c, Map<Gruppo, Integer> vg){
        this.r = r;
        votiGruppi = vg;
        votiPersone = null;
    }

    public Risultati(Referendum r, int tot){
        this.r = r;
        totale = tot;
        isClassica = false;
    }

    public Risultati(Referendum r){
        this.r = r;
        isClassica = false;
    }

    private boolean hasWin(int voti){
        return (totale/2) < voti;
    }

    public boolean setRef(int si, int no, int bianca){
        if(isClassica)
            return false;
        //TODO
        return true;
    }

    public boolean setVoti(Gruppo g, Integer voti){
        if(!isClassica)
            return false;
        //TODO
        return true;
    }

    public boolean setVoti(Persona g, Integer voti){
        if(!isClassica)
            return false;
        //TODO
        return true;
    }

    public void printRisultati(Path path){
        //TODO
    }

}
