package models;

public class Gruppo extends Candidato{

    public Gruppo(int id, String nome) {
        super(id, nome);
    }

    public String toString(){
        return "Unione Politica " + getNome() + "/n";
    }
}
