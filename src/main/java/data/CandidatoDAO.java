package data;

import models.*;

import java.io.IOException;
import java.util.List;

public interface CandidatoDAO {


    List<List<? extends Candidato>> getAllCandidati() throws IOException;

    Gruppo getGruppo(int id) throws IOException;
    Persona getPersona(int id) throws IOException;

    boolean addGruppo(Gruppo g) throws IOException;

    boolean addPersona(Classica c, Gruppo g, Persona p) throws IOException;

    boolean deletePersona(int id) throws IOException;

    boolean  deleteGruppo(int id) throws IOException;

    int getNextIdGruppo() throws IOException;

    int getNextIdPersona() throws IOException;

    List<Gruppo> getGruppi(Classica c) throws IOException;

    List<Gruppo> getGruppi() throws IOException;

    List<Persona> getPersone(Classica c) throws IOException;

    List<Persona> getPersone() throws IOException;
}
