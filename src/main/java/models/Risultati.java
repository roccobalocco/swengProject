package models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@SuppressWarnings("DuplicatedCode")
public class Risultati {

    private boolean isClassica = true;
    private Classica c = null;
    private Referendum r = null;
    private int si = 0, no = 0, bianca = 0, totale = 0;
    private final Map<Gruppo, Integer> votiGruppi;
    private final Map<Persona, Integer> votiPersone;


    public Risultati(Classica c, int tot, Map<Gruppo, Integer> vg, Map<Persona, Integer> vp){
        this.c = c;
        totale = tot;
        votiGruppi =  vg;
        votiPersone = vp;
    }

    public Risultati(Classica c, Map<Gruppo, Integer> vg, Map<Persona, Integer> vp){
        this.c = c;
        votiGruppi =  vg;
        votiPersone = vp;
    }

    public Risultati(Classica c, Map<Gruppo, Integer> vg){
        this.c = c;
        votiGruppi = vg;
        votiPersone = null;
    }

    public Risultati(Referendum r, int tot){
        votiGruppi = null; votiPersone = null; this.r = r;
        totale = tot;
        isClassica = false;
    }

    public Risultati(Referendum r){
        votiGruppi = null; votiPersone = null; this.r = r;
        isClassica = false;
    }

    private boolean hasWin(int voti){
        return (int) (totale/2 - ((totale/2)%1.0)) < voti;
    }

    public boolean setRef(int si, int no, int bianca){
        if(isClassica)
            return false;
        this.si += si; this.no += no; this.bianca += bianca;
        return true;
    }

    public boolean setVoti(Gruppo g, Integer voti){
        if(!isClassica)
            return false;
        assert votiGruppi != null;
        votiGruppi.put(g, voti + votiGruppi.get(g));
        return true;
    }

    public boolean setVoti(Persona p, Integer voti){
        if(!isClassica)
            return false;
        assert votiPersone != null;
        votiPersone.put(p, voti + votiPersone.get(p));
        return true;
    }

    public String toString(){
        if(c == null && r == null)
            return "ERRORE NELLA CHIAMATA";
        StringBuilder content = new StringBuilder();
        if(isClassica) {
            assert c != null;
            content.append(c.descrizione).append("\n");
            content.append("\n");
            content.append("Scadenza: ").append(c.getScadenza());
            content.append("\n");
            content.append("Si: ").append(si).append("\n");
            content.append("No: ").append(no).append("\n");
            content.append("Bianca: ").append(bianca).append("\n\n\n");
            content.append(vinceRef());

        }else{
            content.append(r.descrizione).append("\n");
            content.append("\n");
            content.append("Scadenza: ").append(r.getScadenza());
            content.append("\n");
            content.append(gpString());
            content.append(vinceCla());
        }
        return content.toString();
    }
    public boolean printRisultati(String path) throws FileNotFoundException, DocumentException {
        if(c == null && r == null)
            return false;
        Document document = new Document();
        if(isClassica) {
            PdfWriter.getInstance(document, new FileOutputStream(path + "/" + c.descrizione.replaceAll("[ -/'\"]", "_") +
                    c.getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            String content = c.descrizione + "\n" + "\n" +
                    "Scadenza: " + c.getScadenza() +
                    "\n" +
                    "Si: " + si + "\n" +
                    "No: " + no + "\n" +
                    "Bianca: " + bianca + "\n\n\n" +
                    vinceRef();
            Chunk chunk = new Chunk(content, font);

            document.add(chunk);
            document.close();
        }else{
            PdfWriter.getInstance(document, new FileOutputStream(path + "/" + r.descrizione.replaceAll("[ -/'\"]", "_") +
                    r.getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            String content = r.descrizione + "\n" + "\n" +
                    "Scadenza: " + r.getScadenza() +
                    "\n" +
                    gpString() +
                    vinceCla();
            Chunk chunk = new Chunk(content, font);

            document.add(chunk);
            document.close();
        }
        return true;
    }

    private String gpString() {
        StringBuilder s = new StringBuilder();
        if(c.whichType() == 0 || c.whichType() == 2)
            s.append("I voti espressi tramite ordine sono corrisposti in base al numero di candidati, se n é il numero di candidati, andranno n voti al primo, n-1 al secondo, e cosí via fino all'ultimo con un solo voto, a preferenza\n\n ");
        s.append("Partiti/Gruppi:\n");
        assert votiGruppi != null;
        votiGruppi.forEach((g, i) -> {
            s.append(g.toString()).append(" -- Voti:").append(i).append("\n");
            if(c.whichType() == 2) {
                assert votiPersone != null;
                votiPersone.forEach((p, ii) -> {
                    if (p.getGruppo() == g.getId())
                        s.append("\t").append(p).append(" -- Preferenze secondo il calcolo del sistema: ").append(ii).append("\n");
                });
            }
        });
        return s.toString();
    }

    private String vinceCla() {
        String s = "";
        boolean pari = false;
        int max = 0;
        LinkedList<Gruppo> l = new LinkedList<>();
        Gruppo w = null;
        if(c.whichType() != 2){
            assert votiGruppi != null;
            for (Map.Entry<Gruppo, Integer> entry : votiGruppi.entrySet()) {
                Gruppo g = entry.getKey();
                Integer i = entry.getValue();
                if (i > max) {
                    l = new LinkedList<>();
                    max = i;
                    w = g;
                    l.add(g);
                    pari = false;
                }else if(i == max){
                    l.add(g);
                    pari = true;
                }
            }

            if(!c.isAssoluta() || hasWin(max))
                if(pari)
                    return "Non é possibile stabilire un vincitore a causa di una paritá tra: " + l + "\n\n";
                else
                    return "Il vincitore é: " + w + " -- Con " + max + " voti\n\n";
            return "Non é stata raggiunta la maggioranza assoluta, non c'é nessun vincitore\n\n";
        }else{
            assert votiGruppi != null;
            for (Map.Entry<Gruppo, Integer> entry : votiGruppi.entrySet()) {
                Gruppo g = entry.getKey();
                Integer i = entry.getValue();
                if (i > max) {
                    l = new LinkedList<>();
                    max = i;
                    w = g;
                    l.add(g);
                    pari = false;
                }else if(i == max){
                    l.add(g);
                    pari = true;
                }
            }

            if(!c.isAssoluta() || hasWin(max))
                if(pari)
                    return "Non é possibile stabilire un vincitore a causa di una paritá tra: " + l + "\n\n";
                else {
                    AtomicReference<String> ss = new AtomicReference<>("");
                    Gruppo finalW = w;
                    int finalMax = max;
                    assert votiPersone != null;
                    votiPersone.forEach((p, i) -> {
                        if(p.getGruppo() == finalW.getId())
                            ss.set("Il vincitore é: " + finalW + "\nPer il candidato: " + p + "\nCon " + finalMax + " voti\n\n");
                    });
                    return ss.toString();
                }

            return "Non é stata raggiunta la maggioranza assoluta, non c'é nessun vincitore\n\n";
        }
    }

    private String vinceRef() {
        if(r.hasQuorum()) {
            if (hasWin(si))
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n\n";
            if (hasWin(no))
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n\n";
            return "Non é stato raggiunto il quorum\nSoli " + (si + no + bianca) + " su " + totale + "voti\n\n";
        }else{
            if (si > no)
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n\n";
            if (si < no)
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n\n";
            return "Situazione di paritá tra si [" + si + "] e no [" + no + "]\n\n";
        }
    }

}
