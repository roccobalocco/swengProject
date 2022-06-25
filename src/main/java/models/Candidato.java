package models;

public abstract class Candidato {
    private final int id;
    private final String nome;

    public Candidato(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public String getNome(){ return nome; }

    public int getId(){ return id; }

}
