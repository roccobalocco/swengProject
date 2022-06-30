package models;

import java.util.Iterator;
import java.util.List;

public class Risultati {

    public Risultati(){
    }

    public Iterator<Risultati> iteratorAnon(){
        return new Iterator<Risultati>(){
            private int index = 0;
            @Override
            public boolean hasNext() {
                //TODO
                return true;
            }

            @Override
            public Risultati next() {
                //TODO
                return null;
            }

        };
    }
}
