package gui;

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

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Igra");
        menuBar.add(menu);
        JMenu nastavitve = new JMenu("Nastavitve");
        menuBar.add(nastavitve);

        velikostPlosce = new JMenuItem("velikost plošče");
        nastavitve.add(velikostPlosce);
        velikostPlosce.addActionListener(this);

        clovekRacunalnik = new JMenuItem("človek – računalnik");
        menu.add(clovekRacunalnik);
        clovekRacunalnik.addActionListener(this);

        racunalnikClovek = new JMenuItem("računalnik – človek");
        menu.add(racunalnikClovek);
        racunalnikClovek.addActionListener(this);

        racunalnikRacunalnik = new JMenuItem("računalnik – računalnik");
        menu.add(racunalnikRacunalnik);
        racunalnikRacunalnik.addActionListener(this);

        clovekClovek = new JMenuItem("človek – človek");
        menu.add(clovekClovek);
        clovekClovek.addActionListener(this);

        polje = new IgralnoPolje(this);
        GridBagConstraints poljeLayout = new GridBagConstraints();
        poljeLayout.gridx = 0;
        poljeLayout.gridy = 0;
        poljeLayout.fill = GridBagConstraints.BOTH;
        poljeLayout.weightx = 1.0;
        poljeLayout.weighty = 1.0;
        getContentPane().add(polje, poljeLayout);

        status = new JLabel();
        status.setFont(new Font(status.getFont().getName(), status.getFont().getStyle(), 20));
        GridBagConstraints statusLayout = new GridBagConstraints();
        statusLayout.gridx = 0;
        statusLayout.gridy = 1;
        statusLayout.anchor = GridBagConstraints.CENTER;
        getContentPane().add(status, statusLayout);

        novaIgra(new Racunalnik(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
    }

    private void novaIgra(Strateg noviModer, Strateg noviRdec) {
        if (moder != null) moder.prekini();
        if (rdec != null) rdec.prekini();
        this.igra = new Igra(Igralec.RDECI);
        moder = noviModer;
        rdec = noviRdec;
        Stanje stanje = igra.stanje();
        if (stanje == Stanje.NP_MODRI) {
            moder.naPotezi();
        } else if (stanje == Stanje.NP_RDECI) {
            rdec.naPotezi();
        }
        osveziPrikaz();
    }

    private void osveziPrikaz() {
        if (igra == null) {
            status.setText("");
        }
        else {
            Stanje stanje = igra.stanje();
            if (stanje == Stanje.NP_MODRI) {
                status.setText("Na potezi je modri.");
            } else if (stanje == Stanje.NP_RDECI) {
                status.setText("Na potezi je rdeči.");
            } else if (stanje == Stanje.Z_MODRI) {
                status.setText("Zmagal je modri.");
            } else if (stanje == Stanje.Z_RDECI) {
                status.setText("Zmagal je rdeči.");
            } else if (stanje == Stanje.NEODLOCENO) {
                status.setText("Neodločen izid.");
            }
        }
        polje.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clovekRacunalnik){
            if (4 <= Plosca.velikost && Plosca.velikost <= 10) {
                novaIgra(new Racunalnik(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
            }
        }
        else if (e.getSource() == racunalnikClovek) {
            if (4 <= Plosca.velikost && Plosca.velikost <= 10) {
                novaIgra( new Clovek(this, Igralec.MODRI), new Racunalnik(this, Igralec.RDECI));
            }
        }
        else if (e.getSource() == racunalnikRacunalnik) {
            if (4 <= Plosca.velikost && Plosca.velikost <= 10) {
                novaIgra(new Racunalnik(this, Igralec.MODRI), new Racunalnik(this, Igralec.RDECI));
            }
        }
        else if (e.getSource() == clovekClovek) {
            novaIgra(new Clovek(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
        } else if(e.getSource() == velikostPlosce) {
            String n = JOptionPane.showInputDialog("Velikost plošče (3 - 20):");
            int m;
            try {
                m = Integer.parseInt(n);
            } catch (Exception E) {
                m = Plosca.velikost;
            }
            if (m >= 2 && m <= 21) {
                Plosca.velikost = m;
            }

            this.polje = new IgralnoPolje(this);
            GridBagConstraints poljeLayout = new GridBagConstraints();
            poljeLayout.gridx = 0;
            poljeLayout.gridy = 0;
            poljeLayout.fill = GridBagConstraints.BOTH;
            poljeLayout.weightx = 1.0;
            poljeLayout.weighty = 1.0;
            getContentPane().add(polje, poljeLayout);

            novaIgra(new Clovek(this, Igralec.MODRI), new Clovek(this, Igralec.RDECI));
            status.setText("Igra človek proti človeku. Začne rdeči!");
        }
    }

    Polje[][] getPlosca() {
        return (igra == null ? null : igra.plosca.plosca);
    }

    void klikniPolje(int i, int j) {
        if (igra != null) {
            Stanje stanje = igra.stanje();
            if (stanje == Stanje.NP_RDECI) {
                rdec.klik(i, j);
            } else if (stanje == Stanje.NP_MODRI) {
                moder.klik(i, j);
            } else if (stanje == Stanje.Z_RDECI || stanje == Stanje.Z_MODRI) {
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
        return new Igra(igra);
    }
}
