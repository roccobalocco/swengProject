package models;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@SuppressWarnings("DuplicatedCode")
public class Risultati {

    private final boolean isClassica ;
    private Classica c = null;
    private Referendum r = null;
    private int si = 0, no = 0, bianca = 0, totale = 0;
    private final Map<Gruppo, Integer> votiGruppi;
    private final Map<Persona, Integer> votiPersone;


    public Risultati(Classica c, Map<Gruppo, Integer> vg, Map<Persona, Integer> vp){
        this.c = c;
        isClassica = true;
        votiGruppi =  vg;
        votiPersone = vp;
        if(c.isAssoluta())
            totale = ClassicaDAOImpl.getInstance().getVotanti(c);
    }

    public Risultati(Classica c, Map<Gruppo, Integer> vg){
        this.c = c;
        isClassica = true;
        votiGruppi = vg;
        votiPersone = null;
    }

    public Risultati(Referendum r){
        votiGruppi = null; votiPersone = null; this.r = r;
        isClassica = false;
        if(r.hasQuorum())
            totale = ReferendumDAOImpl.getInstance().getVotanti(r);
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
        if(votiGruppi != null)
            votiGruppi.put(g, voti + votiGruppi.get(g));
        return true;
    }

    public boolean setVoti(Persona p, Integer voti){
        if(!isClassica)
            return false;
        if(votiPersone != null)
            votiPersone.put(p, voti + votiPersone.get(p));
        return true;
    }

    public String toString(){
        if(c == null && r == null)
            return "ERRORE NELLA CHIAMATA";
        StringBuilder content = new StringBuilder();
        if(isClassica) {
            if(c != null) {
                content.append(c.descrizione).append("\n");
                content.append("\n");
                content.append("Scadenza: ").append(c.getScadenza());
                content.append("\n");
                content.append("Si: ").append(si).append("\n");
                content.append("No: ").append(no).append("\n");
                content.append("Bianca: ").append(bianca).append("\n\n\n");
                content.append(vinceRef());
            }

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

    public boolean printRisultati(String path) throws IOException {
        if(c == null && r == null)
            return false;

        PDDocument document = new PDDocument();
        document.addPage(new PDPage());

        if(isClassica) {
            path = path + c.descrizione.replaceAll("[ -/'\"]", "_") +
                    c.getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf";
            System.out.println(path);

            PDPage page = new PDPage();
            PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true);
            contents.beginText();
            PDFont font = PDType1Font.TIMES_ROMAN;
            contents.setFont(font, 15);
            contents.newLineAtOffset(50, 700);
            contents.showText(c.descrizione); contents.newLine();
            contents.newLineAtOffset(0, -50);
            contents.showText("Scadenza: " + c.getScadenza());
            contents.newLineAtOffset(0, -50);
            contents.showText(gpString().replace("\n", " -/- "));
            contents.newLineAtOffset(0, -50);
            contents.showText(vinceCla().replace("\n", " -/- "));
            contents.endText();
            contents.close();
            document.save(path);

        }else{
            path = path + r.descrizione.replaceAll("[ -/'\"]", "_") +
                    r.getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf";
            System.out.println(path);

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true);
            contents.beginText();
            PDFont font = PDType1Font.TIMES_ROMAN;
            contents.setFont(font, 15);
            contents.newLineAtOffset(50, 700);
            contents.showText(r.descrizione);
            contents.newLineAtOffset(0, -50);
            contents.showText("Scadenza: " + r.getScadenza());
            contents.newLineAtOffset(0, -50);
            contents.showText("Si: " + si);
            contents.newLineAtOffset(0, -50);
            contents.showText("No: " + no);
            contents.newLineAtOffset(0, -50);
            contents.showText("Bianca: " + bianca);
            contents.newLineAtOffset(0, -50);
            contents.showText(vinceRef().replace("\n", " -/- "));
            contents.endText();
            contents.close();
            document.save(path);
        }
        document.close();
        return true;
    }

    private String gpString() {
        StringBuilder s = new StringBuilder();
        if(c.whichType() == 0 || c.whichType() == 2)
            s.append("I voti espressi tramite ordine sono corrisposti in base al numero di candidati, se n é il numero di candidati, andranno n voti al primo, n-1 al secondo, e cosí via fino all'ultimo con un solo voto, a preferenza\n ");
        s.append("Partiti/Gruppi:\n");
       if(votiGruppi != null)
            votiGruppi.forEach((g, i) -> {
                s.append(g.toString()).append(" -- Voti:").append(i).append("\n");
                if(c.whichType() == 2) {
                    if(votiPersone != null)
                        votiPersone.forEach((p, ii) -> {
                            if (p.getGruppo() == g.getId())
                                s.append("\t").append(p).append(" -- Preferenze secondo il calcolo del sistema: ").append(ii).append("\n");
                        });
                }
            });
        return s.toString();
    }

    private String vinceCla() {
        boolean pari = false;
        int max = 0;
        LinkedList<Gruppo> l = new LinkedList<>();
        Gruppo w = null;
        if(c.whichType() != 2){
            if(votiGruppi != null)
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
                    return "Non é possibile stabilire un vincitore a causa di una paritá tra: " + l + "\n";
                else
                    return "Il vincitore é: " + w + " -- Con " + max + " voti\n";
            return "Non é stata raggiunta la maggioranza assoluta, non c'é nessun vincitore\n";
        }else{
            if(votiGruppi != null)
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
                    return "Non é possibile stabilire un vincitore a causa di una paritá tra: " + l + "\n";
                else {
                    AtomicReference<String> ss = new AtomicReference<>("");
                    Gruppo finalW = w;
                    int finalMax = max;
                    if(votiPersone != null)
                        votiPersone.forEach((p, i) -> {
                            if(p.getGruppo() == finalW.getId())
                                ss.set("Il vincitore é: " + finalW + "\nPer il candidato: " + p + "\nCon " + finalMax + " voti\n\n");
                        });
                    return ss.toString();
                }

            return "Non é stata raggiunta la maggioranza assoluta, non c'é nessun vincitore\n";
        }
    }

    private String vinceRef() {
        if(r.hasQuorum()) {
            if (hasWin(si))
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n";
            if (hasWin(no))
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n";
            return "Non é stato raggiunto il quorum\nSoli " + (si + no + bianca) + " su " + totale + "voti\n";
        }else{
            if (si > no)
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n";
            if (si < no)
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n";
            return "Situazione di paritá tra si [" + si + "] e no [" + no + "]\n";
        }
    }

}
