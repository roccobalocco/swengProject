package models;

import data.CandidatoDAOImpl;

public class Persona extends Candidato{

    private final int gruppoFK;

    public Persona(int id, String nome, int gFK) {
        super(id, nome);
        gruppoFK = gFK;
    }

    public int getGruppo(){ return gruppoFK; }

    public String toString(){
        StringBuilder s = new StringBuilder("Candidato politico ");
        s.append(getNome()).append("\n");
        s.append(CandidatoDAOImpl.getInstance().getCandidato(getId()).getNome());
        return s.toString();
    }
}
