package gui;

// uvozimo vse potrebne knjižnice

import logika.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GlavnoOkno extends JFrame implements ActionListener {

    private IgralnoPolje polje;
    private Igra igra;
    private JLabel status;
    private Strateg moder;
    private Strateg rdec;

    private JMenuItem clovekRacunalnik;
    private JMenuItem racunalnikClovek;
    private JMenuItem clovekClovek;
    private JMenuItem racunalnikRacunalnik;
    private JMenuItem velikostPlosce;

    public GlavnoOkno() {
        this.setTitle("Igra Hex");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        // naredimo "menu bar" z opcijo igra in nastavitve
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Igra");
        menuBar.add(menu);
        JMenu nastavitve = new JMenu("Nastavitve");
        menuBar.add(nastavitve);

        // dodamo nastavitev velikosti plošče
        velikostPlosce = new JMenuItem("velikost plošče");
        nastavitve.add(velikostPlosce);
        velikostPlosce.addActionListener(this);

        // dodamo 4 različne načine igre
        clovekRacunalnik = new JMenuItem("računalnik - človek");
        menu.add(clovekRacunalnik);
        clovekRacunalnik.addActionListener(this);

        racunalnikClovek = new JMenuItem("človek - računalnik");
        menu.add(racunalnikClovek);
        racunalnikClovek.addActionListener(this);

        racunalnikRacunalnik = new JMenuItem("računalnik - računalnik");
        menu.add(racunalnikRacunalnik);
        racunalnikRacunalnik.addActionListener(this);

        clovekClovek = new JMenuItem("človek - človek");
        menu.add(clovekClovek);
        clovekClovek.addActionListener(this);

        // zgoraj (tik pod menu bar) naredimo igralno polje
        polje = new IgralnoPolje(this);
        GridBagConstraints poljeLayout = new GridBagConstraints();
        poljeLayout.gridx = 0;
        poljeLayout.gridy = 0;
        poljeLayout.fill = GridBagConstraints.BOTH;
        poljeLayout.weightx = 1.0;
        poljeLayout.weighty = 1.0;
        getContentPane().add(polje, poljeLayout);

        // spodaj pod igralno ploščo naredimo statusno vrstico
        status = new JLabel();
        status.setFont(new Font(status.getFont().getName(), status.getFont().getStyle(), 20));
        GridBagConstraints statusLayout = new GridBagConstraints();
        statusLayout.gridx = 0;
        statusLayout.gridy = 1;
        statusLayout.anchor = GridBagConstraints.CENTER;
        getContentPane().add(status, statusLayout);

        // naredimo/začnemo novo igro
        novaIgra(new Clovek(this, Igralec.RDECI), new Racunalnik(this, Igralec.MODRI));
    }


    private void novaIgra(Strateg noviModer, Strateg noviRdec) {
        if (moder != null) moder.prekini(); // če je moder v še v igri ga prekinemo
        if (rdec != null) rdec.prekini();  // če je rdeč še v igri ga prekinemo
        this.igra = new Igra(Igralec.MODRI); // naredimo novo igro, kjer začne modri
        moder = noviModer;  // nastavimo modrega
        rdec = noviRdec;  // nastavimo rdečega
        Stanje stanje = igra.stanje();  // določimo stanje igre
        // pogledamo kdo je na potezi, in izvedemo njegovo potezo
        if (stanje == Stanje.NP_MODRI) {
            moder.naPotezi();
        } else if (stanje == Stanje.NP_RDECI) {
            rdec.naPotezi();
        }
        osveziPrikaz();  // po končani potezi izrišemo novo situacijo
    }

    private void osveziPrikaz() {
        if (igra == null) {
            status.setText("");  // če ni igre je status prazen string
        } else {  // sicer določimo ustrezen status z ustreznim besedilom
            Stanje stanje = igra.stanje();
            if (stanje == Stanje.NP_MODRI) {
                status.setText("Na potezi je modri.");
            } else if (stanje == Stanje.NP_RDECI) {
                status.setText("Na potezi je rdeči.");
            } else if (stanje == Stanje.Z_MODRI) {
                status.setText("Zmagal je modri.");
            } else if (stanje == Stanje.Z_RDECI) {
                status.setText("Zmagal je rdeči.");
            }
            polje.repaint();  // na novo narišemo ploščo
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        /* na podlagi izbrane igre naredimo novo igro, ki je velikosti med vključno 4 in 11,
         saj sicer povzročimo preveč težav za AI. */
        if (e.getSource() == clovekRacunalnik) {
            if (4 <= Plosca.velikost && Plosca.velikost <= 11) {
                novaIgra(new Racunalnik(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
            }
        } else if (e.getSource() == racunalnikClovek) {
            if (4 <= Plosca.velikost && Plosca.velikost <= 11) {
                novaIgra(new Clovek(this, Igralec.MODRI), new Racunalnik(this, Igralec.RDECI));
            }
        } else if (e.getSource() == racunalnikRacunalnik) {
            if (4 <= Plosca.velikost && Plosca.velikost <= 11) {
                novaIgra(new Racunalnik(this, Igralec.MODRI), new Racunalnik(this, Igralec.RDECI));
            }
        } else if (e.getSource() == clovekClovek) {
            novaIgra(new Clovek(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
        } else if (e.getSource() == velikostPlosce) {
            String n = JOptionPane.showInputDialog("Velikost plošče (4 - 11):");
            int m;
            try {  // preverimo če je vneseno število
                m = Integer.parseInt(n);
            } catch (Exception E) {  // sicer pustimo prejšnjo velikost
                m = Plosca.velikost;
            }
            if (m > 3 && m < 12) {  // če je bilo vnešeno število, preverimo, ali je ustrezne velikosti
                Plosca.velikost = m;
            }

            // če spremenimo velikost plošče, naredimo novo igralno polje
            this.polje = new IgralnoPolje(this);
            GridBagConstraints poljeLayout = new GridBagConstraints();
            poljeLayout.gridx = 0;
            poljeLayout.gridy = 0;
            poljeLayout.fill = GridBagConstraints.BOTH;
            poljeLayout.weightx = 1.0;
            poljeLayout.weighty = 1.0;
            getContentPane().add(polje, poljeLayout);

            // naredimo novo igro
            Plosca.velikost = m;
            novaIgra(new Clovek(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
            status.setText("Začne modri.");
        }
    }

    // pridobimo ploščo
    Polje[][] getPlosca() {
        return (igra == null ? null : igra.plosca.plosca);
    }

    void klikniPolje(int i, int j) {
        if (igra != null) { // ko kliknemo polje in igra obstaja, se zgodi "klik" za modrega ali rdečega
            Stanje stanje = igra.stanje();
            if (stanje == Stanje.NP_RDECI) {
                rdec.klik(i, j);
            } else if (stanje == Stanje.NP_MODRI) {
                moder.klik(i, j);
            } else if (stanje == Stanje.Z_RDECI || stanje == Stanje.Z_MODRI) {  // če je kdo zmagal, naredimo novo igro
                novaIgra(moder, rdec);
            }
        }
    }

    public void odigraj(Tuple poteza) {
        if (igra.poteza(poteza.getX(), poteza.getY())) {
            osveziPrikaz();
            Stanje stanje = igra.stanje();
            if (stanje == Stanje.NP_MODRI) {
                moder.naPotezi();
            } else if (stanje == Stanje.NP_RDECI) {
                rdec.naPotezi();
            }
        }
    }

    public Igra kopijaIgre() {
        // ustvari novo kopijo trenutne igre
        return new Igra(igra);
    }
}
