package util;

import java.io.IOException;

public interface Observable{

    void subscribe(Observer o);

    void unsubcribe(Observer o);

    void notifyObservers(String s) throws IOException;

}
