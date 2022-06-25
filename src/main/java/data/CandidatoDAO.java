package data;

import models.*;

import java.util.List;

public interface CandidatoDAO {


    List<List<? extends Candidato>> getAllCandidati();

    <T extends Candidato> T getCandidato(int id);

    boolean addGruppo(Gruppo g);

    boolean addPersona(Persona p);

    boolean deleteCandidato(int id);

    List<Gruppo> getGruppi();

    List<Persona> getPersone();
}
