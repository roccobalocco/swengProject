package data;

import models.Classica;
import models.Risultati;
import models.Votazione;

import java.io.IOException;
import java.util.*;

/**
 * 
 */
public interface VotazioneDAO {

    /**
     * @return una lista con al suo intero due liste, la prima con le votazioni Classiche, la seconda con i referendum
     */
    static List<List<? extends Votazione>> getAllVotazioni() throws IOException {
        List<List<? extends Votazione>> votazioni = new LinkedList<>();
        List<Classica> classica = new LinkedList<>(ClassicaDAOImpl.getInstance().getAllCategorico());
        classica.addAll(ClassicaDAOImpl.getInstance().getAllOrdinale());
        votazioni.add(classica);
        votazioni.add(ReferendumDAOImpl.getInstance().getAll());
        return votazioni;
    }

    <T extends Votazione> T getVotazione(int id) throws IOException;

    <T extends Votazione> boolean updateVotazione(T v) throws IOException;

    <T extends Votazione> boolean addVotazione(T v) throws IOException;

    boolean deleteVotazione(int id) throws IOException;

    Risultati getRisultati(Votazione v) throws IOException;

}