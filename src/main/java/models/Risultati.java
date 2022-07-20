package models;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import util.Util;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Piemme
 * Classe del package "models" atta a semplificare il calcolo e la gestione dei risultati. A accoppiamenti con le classi Classica e Referendum, sfrutta inoltre varie Classi e Metodi del package "data"
 */
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
        int[] voti = ReferendumDAOImpl.getInstance().getVoti();
        si = voti[0];
        no = voti[1];
        bianca = voti[2];
    }

    private boolean hasWin(int voti){
        return (int) (totale/2 - ((totale/2)%1.0)) < voti;
    }

    public boolean setRef(int si, int no, int bianca){
        if(isClassica || si < 0 || no < 0 || bianca < 0)
            return false;
        this.si += si; this.no += no; this.bianca += bianca;
        return true;
    }

    public boolean setVoti(Gruppo g, Integer voti){
        if(!isClassica || g == null || voti == null || votiGruppi == null || voti < 0)
            return false;

        votiGruppi.put(g, voti + votiGruppi.get(g));
        return true;
    }

    public boolean setVoti(Persona p, Integer voti){
        if(!isClassica || votiPersone == null || voti < 0)
            return false;

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
            path = path + Util.bonify2(c.descrizione) + Util.bonify2(c.getScadenza()) + ".pdf";
            System.out.println(path);

            PDPage page = new PDPage();
            PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true);
            contents.beginText();
            PDFont font = PDType1Font.TIMES_ROMAN;
            contents.setFont(font, 12);
            contents.newLineAtOffset(50, 700);
            contents.showText(c.descrizione); contents.newLine();
            contents.newLineAtOffset(0, -25);
            contents.showText("Scadenza: " + c.getScadenza());
            contents.newLineAtOffset(0, -25);
            contents.endText();
            contents.close();

            gpString(document);

            document.save(path);

        }else{
            path = path + Util.bonify2(r.descrizione) + Util.bonify2(r.getScadenza()) + ".pdf";
            System.out.println(path);

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true);
            contents.beginText();
            PDFont font = PDType1Font.TIMES_ROMAN;
            contents.setFont(font, 12);
            contents.newLineAtOffset(50, 700);
            contents.showText(r.descrizione);
            contents.newLineAtOffset(0, -25);
            contents.showText("Scadenza: " + r.getScadenza());
            contents.newLineAtOffset(0, -25);
            contents.showText("Si: " + si);
            contents.newLineAtOffset(0, -25);
            contents.showText("No: " + no);
            contents.newLineAtOffset(0, -25);
            contents.showText("Bianca: " + bianca);
            contents.newLineAtOffset(0, -25);
            contents.showText(vinceRef().replace("\n", " -/- "));
            contents.endText();
            contents.close();
            document.save(path);
        }
        document.close();
        return true;
    }

    private void gpString(PDDocument document) throws IOException {
        PDPage page = new PDPage();
        PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true);
        contents.beginText();
        PDFont font = PDType1Font.TIMES_ROMAN;
        contents.setFont(font, 12);

        contents.newLineAtOffset(50, 625);

        if(c.whichType() == 0 || c.whichType() == 2) {
            contents.showText("I voti espressi tramite ordine sono corrisposti in base al numero di candidati, se n é il numero di candidati, ");
            contents.newLineAtOffset(0, -25);
            contents.showText("andranno n voti al primo, n-1 al secondo, e cosí via fino all'ultimo con un solo voto, a preferenza");
            contents.newLineAtOffset(0, -25);
        }

        contents.showText("Bianca: " + bianca);
        contents.newLineAtOffset(0, -25);

        if(votiGruppi != null)
            votiGruppi.forEach((g, i) -> {
                try {
                    System.out.println(g.toString());
                    contents.showText(g.toString().replace("\n", "").replace("\r", "") + " -- Voti: " + i );
                    contents.newLineAtOffset(0, -25);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(c.whichType() == 2) {
                    if(votiPersone != null)
                        votiPersone.forEach((p, ii) -> {
                            if (p.getGruppo() == g.getId()){
                                try {
                                    contents.showText(p.toString().replace("\n", "").replace("\r", "") + " -- Preferenze secondo il calcolo del sistema: " + String.valueOf(ii));
                                    contents.newLineAtOffset(0, -25);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                }
            });
        contents.newLineAtOffset(0, -25);
        contents.showText(vinceCla().replace("\n", " -/- "));

        contents.endText();
        contents.close();
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
                                ss.set("Il vincitore é: " + finalW + p + "Con " + finalMax + " voti\n");
                        });
                    return ss.toString();
                }

            return "Non é stata raggiunta la maggioranza assoluta, non c'é nessun vincitore\n";
        }
    }

    private boolean reachQuorum(){
        return (si + no + bianca) > (totale/2);
    }

    private String vinceRef() {
        if(r.hasQuorum()) {
            if (!reachQuorum())
                return "Non é stato raggiunto il quorum\nSoli " + (si + no + bianca) + " su " + totale + "voti\n";
            if(si > no)
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n";
            if (no > si)
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n";
            return "Paritá tra [ NO ] con " + no + " voti e [ SI ] con  "+ si + " voti \n";
        }else{
            if (si > no)
                return "Ha vinto il [ SI ] con " + si + " voti su " + (si + no + bianca) + " voti \n";
            if (si < no)
                return "Ha vinto il [ NO ] con " + no + " voti su " + (si + no + bianca) + " voti \n";
            return "Situazione di paritá tra si [" + si + "] e no [" + no + "]\n";
        }
    }

}
