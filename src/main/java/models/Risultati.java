package models;

import java.util.Iterator;

public class Risultati {

    public Risultati(){
    }

    public Iterator<Risultati> iteratorAnon(){
        return new Iterator<>(){
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
