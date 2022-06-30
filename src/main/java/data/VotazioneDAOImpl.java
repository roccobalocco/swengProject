package data;

import models.Classica;
import models.Votazione;

import java.io.IOException;
import java.util.*;

/**
 * @author Piemme
 */
public abstract class VotazioneDAOImpl implements VotazioneDAO {

    /**
     * @return una lista con al suo intero due liste, la prima con le votazioni Classiche, la seconda con i referendum
     */
    public List<List<? extends Votazione>> getAllVotazioni() throws IOException {
        List<List<? extends Votazione>> votazioni = new LinkedList<>();
        List<Classica> classica = new LinkedList<>(ClassicaDAOImpl.getInstance().getAllCategorico());
        classica.addAll(ClassicaDAOImpl.getInstance().getAllOrdinale());
        votazioni.add(classica);
        votazioni.add(ReferendumDAOImpl.getInstance().getAll());
        return votazioni;
    }

}