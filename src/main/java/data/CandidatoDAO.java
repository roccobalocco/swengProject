package data;

import models.*;

import java.util.List;

public interface CandidatoDAO {


    List<List<? extends Candidato>> getAllCandidati();

    Gruppo getGruppo(int id);
    Persona getPersona(int id);

    boolean addGruppo(Gruppo g);

    boolean addPersona(Classica c, Gruppo g, Persona p);

    boolean deletePersona(int id);

    boolean  deleteGruppo(int id);

    List<Gruppo> getGruppi();

    List<Persona> getPersone();
}
