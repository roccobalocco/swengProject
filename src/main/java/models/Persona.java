package models;

import data.CandidatoDAOImpl;

import java.io.IOException;

public class Persona extends Candidato{

    private final int gruppoFK;

    public Persona(int id, String nome, int gFK) {
        super(id, nome);
        gruppoFK = gFK;
    }

    public int getGruppo(){ return gruppoFK; }

    public String toString(){
        try {
            return "Candidato politico " + this.getNome() + " \n " + CandidatoDAOImpl.getInstance().getPersona(this.getId()).getNome();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
