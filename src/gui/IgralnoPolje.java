package gui;

import logika.Plosca;
import logika.Polje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IgralnoPolje extends JPanel implements MouseListener {

    private GlavnoOkno okno;

    private final static double LINE_WIDTH = 3;
    private double[][][] sredisca;

    IgralnoPolje(GlavnoOkno okno) {
        super();
        setBackground(Color.WHITE);
        this.okno = okno;
        this.addMouseListener(this);
        this.sredisca = new double[11][11][2];
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    /*
    Določi dolžino stranice šestkotnika.
     */
    private double dolzinaStranice() {
        // pogledamo širino (* 0.8 zato, da ni čisto do roba, velikost + velikost/2 zato, ker je poševna)
        double dolzinaX = getWidth() * 0.8 / (Math.sqrt(3) * (Plosca.velikost + Plosca.velikost / 2.0));
        // velikost / 2 zato ker imajo šestkotniki "kapco"
        double dolzinaY = getHeight() * 0.8 / (Plosca.velikost + (Plosca.velikost / 2.0));
        // vzamemo manjšo od dolžin
        return Math.min(dolzinaX, dolzinaY);
    }

    private static int round(double x) {
        return (int) (x + 0.5);
    }

    /*
    (x, y) je središče šestkotnika, funkcija določi oglišča šestkotnika
    tako, da shrani x koordinate v svoj array in y koordinate v svoj array
    oba arraya sta v arrayu "pomozna" (zato, ker Graphics2D g2.fillPolygon
    zahteva x-e in y-e v ločenih arrayih).
     */
    private int[][] ogliscaSestkotnika(double x, double y, boolean polnilo) {
        double d;
        // če je polnilo do roba je grdo
        if (polnilo) d = dolzinaStranice() * 0.9;
        else d = dolzinaStranice();
        int[][] pomozna = new int[2][6];
        // prvo oglišče je spodaj na sredini, ostala so nanizana v pozitivni smeri
        pomozna[0][0] = round(x);
        pomozna[1][0] = round(y + d);
        pomozna[0][1] = round(x + (Math.sqrt(3) / 2) * d);
        pomozna[1][1] = round(y + d / 2);
        pomozna[0][2] = round(x + (Math.sqrt(3) / 2) * d);
        pomozna[1][2] = round(y - d / 2);
        pomozna[0][3] = round(x);
        pomozna[1][3] = round(y - d);
        pomozna[0][4] = round(x - (Math.sqrt(3) / 2) * d);
        pomozna[1][4] = round(y - d / 2);
        pomozna[0][5] = round(x - (Math.sqrt(3) / 2) * d);
        pomozna[1][5] = round(y + d / 2);
        return pomozna;
    }

    /*
    Funkcija nariše šestkotnik in ga pobarva z želeno barvo.
     */
    private void pobarvaj(Graphics2D g2, Color barva, double x, double y) {
        int[][] tocke = ogliscaSestkotnika(x, y, true);
        g2.setColor(barva);
        g2.fillPolygon(tocke[0], tocke[1], 6);
    }

    /*
    Funkcija zamakne x in y in vrstico, tako da bomo dobili pozicijo novega šestkotnika
     */
    private double[] zamakni(int x, int y) {
        double d = dolzinaStranice();
        double zamakniX = d * Math.sqrt(3); // premakne v šestkotnik na desni
        double zamakniY = d * 1.5; // premakne v šestkotnik spodaj
        double zamakniVrsto = y * Math.sqrt(3) / 2 * d; // zamik posamezne vrste

        // preračunamo, kje naslednje središče
        double[] koordinati = new double[2];
        koordinati[0] = (getWidth() - (Plosca.velikost + Plosca.velikost / 2.0) * d * Math.sqrt(3)) / 2.0 +
                d * Math.sqrt(3) / 2 + x * zamakniX + zamakniVrsto;
        koordinati[1] = getHeight() - (getHeight() - (Plosca.velikost + Plosca.velikost / 2.0) * d)
                / 2.0 - d - y * zamakniY;
        return koordinati;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        /*
        Narišemo šestkotnike.
        */
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke((float) LINE_WIDTH));

        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                double[] tocka = zamakni(i, j);
                int[][] tocke = ogliscaSestkotnika(tocka[0], tocka[1], false);
                sredisca[i][j] = new double[]{round(tocka[0]), round(tocka[1])};

                g2.drawPolygon(tocke[0], tocke[1], 6);
            }
        }

        /*
        Narišemo začetno in končno linijo za modrega
         */
        for (int i = 0; i < Plosca.velikost; i++) {
            double[] tockaSpodaj = zamakni(i, 0);
            double[] tockaZgoraj = zamakni(i, Plosca.velikost - 1);
            int[][] spodaj = ogliscaSestkotnika(tockaSpodaj[0], tockaSpodaj[1], false);
            int[][] zgoraj = ogliscaSestkotnika(tockaZgoraj[0], tockaZgoraj[1], false);
            g2.setColor(Color.BLUE);
            g2.drawLine(zgoraj[0][2], zgoraj[1][2], zgoraj[0][3], zgoraj[1][3]);
            g2.drawLine(zgoraj[0][3], zgoraj[1][3], zgoraj[0][4], zgoraj[1][4]);
            g2.drawLine(spodaj[0][5], spodaj[1][5], spodaj[0][0], spodaj[1][0]);
            g2.drawLine(spodaj[0][0], spodaj[1][0], spodaj[0][1], spodaj[1][1]);
        }

        /*
        Narišemo začetno in končno linijo za rdečega
         */
        for (int i = 0; i < Plosca.velikost; i++) {
            double[] tockaSpodaj = zamakni(0, i);
            double[] tockaZgoraj = zamakni(Plosca.velikost - 1, i);
            int[][] spodaj = ogliscaSestkotnika(tockaSpodaj[0], tockaSpodaj[1], false);
            int[][] zgoraj = ogliscaSestkotnika(tockaZgoraj[0], tockaZgoraj[1], false);
            g2.setColor(Color.RED);
            g2.drawLine(zgoraj[0][0], zgoraj[1][0], zgoraj[0][1], zgoraj[1][1]);
            g2.drawLine(zgoraj[0][2], zgoraj[1][2], zgoraj[0][1], zgoraj[1][1]);
            g2.drawLine(spodaj[0][3], spodaj[1][3], spodaj[0][4], spodaj[1][4]);
            g2.drawLine(spodaj[0][5], spodaj[1][5], spodaj[0][4], spodaj[1][4]);
        }

        /*
        Pobarvamo polja z ustrezno barvo.
         */
        Polje[][] plosca = okno.getPlosca();
        if (plosca != null) {
            for (int i = 0; i < Plosca.velikost; i++) {
                for (int j = 0; j < Plosca.velikost; j++) {
                    double[] tocka = zamakni(j, i);
                    if (plosca[i][j] == Polje.MODRO) {
                        pobarvaj(g2, Color.BLUE, tocka[0], tocka[1]);
                    } else if (plosca[i][j] == Polje.RDECE) {
                        pobarvaj(g2, Color.RED, tocka[0], tocka[1]);
                    }
                }
            }
        }
    }

    /*
    Kvadrat evklidske razdalje.
     */
    private int evklidskaRazdalja(double[] a, double[] b) {
        return (int) (Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    /*
    Pogledamo od središča katerega šestkotnika je klik najmanj oddaljen in
    če je oddaljen manj od dolžine stranice, vemo, kateri je naš izbrani šestkotnik.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int minRazdalja = (int) Math.pow(Math.min(getHeight(), getWidth()), 2);
        int izbX = 0;
        int izbY = 0;
        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                if (evklidskaRazdalja((sredisca[i][j]), new double[]{x, y}) < minRazdalja) {
                    izbX = j;
                    izbY = i;
                    minRazdalja = evklidskaRazdalja(sredisca[i][j], new double[]{x, y});
                }
            }
        }
        if (Math.sqrt(minRazdalja) < dolzinaStranice()) {  // če je klik preveč izven igralne plošče, ga ignoriramo
            okno.klikniPolje(izbX, izbY);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
