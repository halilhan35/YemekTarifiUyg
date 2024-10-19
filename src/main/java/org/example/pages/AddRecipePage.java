package org.example.pages;

import org.example.classes.Malzeme;
import org.example.database.ConnectDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;

public class AddRecipePage {

    private Connection connection;
    JFrame frame = new JFrame("Food System App");

    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private ArrayList<String> tarifler = new ArrayList<String>();
    private ArrayList<String> secilenMalzemeler = new ArrayList<>();
    private ArrayList<String> birimler = new ArrayList<String>();

    private JTextField malzemeArama = new JTextField(null);
    private JTextArea MalzemeSecimAlani = new JTextArea();
    private JTextField MalzemeKacGram = new JTextField(null);
    private JScrollPane scrollPane = new JScrollPane(MalzemeSecimAlani);

    private JButton geriButton = new JButton("Geri");
    private JTextField tarifAdiField = new JTextField(null);
    private JTextField kategoriField = new JTextField(null);
    private JTextArea malzemeText = new JTextArea();
    private JScrollPane scrollPaneYatay = new JScrollPane(malzemeText);
    private JTextField hazirlanmaSuresiField = new JTextField(null);
    private JTextField talimatlarField = new JTextField(null);

    private int yeniTarifId;

    public void createAndShowGUI() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        connection = ConnectDB.getConnection();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File("src/main/java/org/example/drawables/background.jpeg"));
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        malzemelerListesiYapma();

        panel.setLayout(null);
        frame.setContentPane(panel);

        geriButton.setBounds(15, 15, 150, 50);
        geriButton.setFont(new Font("Arial", Font.BOLD, 16));

        geriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainPage mainPage = new MainPage();
                mainPage.createAndShowGUI();
            }
        });

        JLabel jLabelTarifEkleme = new JLabel("TARİF EKLEME");
        jLabelTarifEkleme.setBounds(200, 70, 400, 40);
        jLabelTarifEkleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifEkleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifEkleme.setForeground(Color.BLACK);
        jLabelTarifEkleme.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifEkleme.setOpaque(true);
        jLabelTarifEkleme.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel jLabelTarifAdi = new JLabel("Tarif Adı : ");
        jLabelTarifAdi.setBounds(200, 130, 150, 40);
        jLabelTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setForeground(Color.BLACK);
        jLabelTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifAdi.setOpaque(true);
        jLabelTarifAdi.setFont(new Font("Arial", Font.BOLD, 15));

        tarifAdiField.setBounds(370, 130, 230, 40);
        tarifAdiField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelKategori = new JLabel("Kategori : ");
        jLabelKategori.setBounds(200, 190, 150, 40);
        jLabelKategori.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelKategori.setVerticalAlignment(SwingConstants.CENTER);
        jLabelKategori.setForeground(Color.BLACK);
        jLabelKategori.setBackground(new Color(255, 255, 255, 200));
        jLabelKategori.setOpaque(true);
        jLabelKategori.setFont(new Font("Arial", Font.BOLD, 15));

        kategoriField.setBounds(370, 190, 230, 40);
        kategoriField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelmalzeme = new JLabel("Malzemeler : ");
        jLabelmalzeme.setBounds(200, 250, 150, 40);
        jLabelmalzeme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelmalzeme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelmalzeme.setForeground(Color.BLACK);
        jLabelmalzeme.setBackground(new Color(255, 255, 255, 200));
        jLabelmalzeme.setOpaque(true);
        jLabelmalzeme.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeText.setBounds(370, 245, 230, 50);
        malzemeText.setFont(new Font("Arial", Font.BOLD, 15));
        malzemeText.setEditable(false);

        scrollPaneYatay.setBounds(370, 245, 230, 50);
        scrollPaneYatay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneYatay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        malzemeArama.setBounds(200, 310, 400, 40);
        malzemeArama.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeArama.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                MalzemeArama();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                MalzemeArama();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                MalzemeArama();
            }
        });

        MalzemeSecimAlani.setBounds(200, 370, 400, 110);
        MalzemeSecimAlani.setLineWrap(true);
        MalzemeSecimAlani.setWrapStyleWord(true);
        MalzemeSecimAlani.setEditable(false);
        MalzemeSecimAlani.setFont(new Font("Arial", Font.BOLD, 15));

        for (Malzeme malzeme : malzemeler) {
            MalzemeSecimAlani.append(malzeme.getMalzemeAdi() + "\n\n");
        }

        scrollPane.setBounds(200, 370, 400, 110);
        scrollPane.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelMalzemeKacGram = new JLabel("Kaç Birim?");
        jLabelMalzemeKacGram.setBounds(610, 350, 180, 40);
        jLabelMalzemeKacGram.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeKacGram.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeKacGram.setForeground(Color.BLACK);
        jLabelMalzemeKacGram.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeKacGram.setOpaque(true);
        jLabelMalzemeKacGram.setFont(new Font("Arial", Font.BOLD, 15));

        MalzemeKacGram.setBounds(610, 392, 180, 40);
        MalzemeKacGram.setFont(new Font("Arial", Font.BOLD, 15));

        JButton secimKaydetmeButton = new JButton("Seçilen Malzemeyi Kaydet");
        secimKaydetmeButton.setBounds(610, 430, 180, 50);
        secimKaydetmeButton.setFont(new Font("Arial", Font.BOLD, 12));

        secimKaydetmeButton.addActionListener(new ActionListener() {
            int j = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String secilenMalzeme = MalzemeSecimAlani.getSelectedText();
                String malzemeMiktar = MalzemeKacGram.getText().toString();

                if (secilenMalzeme != null && !secilenMalzeme.isEmpty() && !secilenMalzemeler.contains(secilenMalzeme) && !malzemeMiktar.isEmpty()) {
                    secilenMalzemeler.add(secilenMalzeme);
                    birimler.add(malzemeMiktar);
                } else {
                    JOptionPane.showMessageDialog(null, "Malzeme şuanda eklenemiyor, Eksik bir şey var.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

                StringBuilder combinedText = new StringBuilder();
                for (int i = 0; i < secilenMalzemeler.size(); i++) {
                    String malzeme = secilenMalzemeler.get(i);
                    String birim = birimler.get(i);
                    for (Malzeme malzemeNesnesi : malzemeler) {
                        if (malzemeNesnesi.getMalzemeAdi().equals(malzeme)) {
                            combinedText.append(birim).append(" ")
                                    .append(malzemeNesnesi.getMalzemeBirim()).append(" ")
                                    .append(malzeme).append(", ");
                        }
                    }
                }
                malzemeText.setText(combinedText.toString());
            }
        });

        JLabel jLabelHazirlanmaSuresi = new JLabel("Hazırlanma Süresi : ");
        jLabelHazirlanmaSuresi.setBounds(200, 500, 150, 40);
        jLabelHazirlanmaSuresi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelHazirlanmaSuresi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelHazirlanmaSuresi.setForeground(Color.BLACK);
        jLabelHazirlanmaSuresi.setBackground(new Color(255, 255, 255, 200));
        jLabelHazirlanmaSuresi.setOpaque(true);
        jLabelHazirlanmaSuresi.setFont(new Font("Arial", Font.BOLD, 15));

        hazirlanmaSuresiField.setBounds(370, 500, 230, 40);
        hazirlanmaSuresiField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelTalimatlar = new JLabel("Talimatlar : ");
        jLabelTalimatlar.setBounds(200, 560, 150, 40);
        jLabelTalimatlar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTalimatlar.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTalimatlar.setForeground(Color.BLACK);
        jLabelTalimatlar.setBackground(new Color(255, 255, 255, 200));
        jLabelTalimatlar.setOpaque(true);
        jLabelTalimatlar.setFont(new Font("Arial", Font.BOLD, 15));

        talimatlarField.setBounds(370, 560, 230, 40);
        talimatlarField.setFont(new Font("Arial", Font.BOLD, 15));

        JButton tarifiKaydetButton = new JButton("Tarifi Kaydet");
        tarifiKaydetButton.setBounds(325, 620, 150, 50);
        tarifiKaydetButton.setFont(new Font("Arial", Font.BOLD, 15));

        tarifiKaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tarifAdi = tarifAdiField.getText().trim();
                String kategori = kategoriField.getText().trim();
                int hazirlanmaSuresi;
                String talimatlar = talimatlarField.getText().trim();

                tarifAdlariAlma();

                if (tarifler.contains(tarifAdi) || tarifler.contains(ilkHarfiBuyukYapmak(tarifAdi))) {
                    JOptionPane.showMessageDialog(null, "Bu tarif zaten var.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tarifAdi.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tarif adını boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (kategori.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kategoriyi boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (secilenMalzemeler.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "En az bir malzeme seçmelisiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    hazirlanmaSuresi = Integer.parseInt(hazirlanmaSuresiField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Hazırlanma süresi bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (talimatlar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Talimatları açıklamalısınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query = "INSERT INTO tarifler (tarifadi, kategori, hazirlamasuresi, talimatlar) VALUES (?, ?, ?, ?) RETURNING tarifid";

                try {
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, tarifAdi);
                    stmt.setString(2, kategori);
                    stmt.setInt(3, hazirlanmaSuresi);
                    stmt.setString(4, talimatlar);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        yeniTarifId = rs.getInt(1);
                        malzemeTarifIliskisi();
                        favorilerIliskisi();
                        JOptionPane.showMessageDialog(null, "Tarif başarıyla kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        AddRecipePage addRecipePage = new AddRecipePage();
                        addRecipePage.createAndShowGUI();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Tarif veritabanına kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JLabel jLabelMalzemeEklemeUyarisi = new JLabel("Aradığınız malzeme yoksa \"Malzeme Ekle\" butonuna tıklayıp ekleyebilirsiniz.");
        jLabelMalzemeEklemeUyarisi.setBounds(40, 700, 550, 40);
        jLabelMalzemeEklemeUyarisi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEklemeUyarisi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEklemeUyarisi.setForeground(Color.BLACK);
        jLabelMalzemeEklemeUyarisi.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeEklemeUyarisi.setOpaque(true);
        jLabelMalzemeEklemeUyarisi.setFont(new Font("Arial", Font.BOLD, 15));

        JButton malzemeEkleButton = new JButton("Malzeme Ekle");
        malzemeEkleButton.setBounds(610, 695, 150, 50);
        malzemeEkleButton.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AddMaterialPage addMaterialPage = new AddMaterialPage();
                addMaterialPage.createAndShowGUI();
            }
        });

        frame.add(geriButton);
        frame.add(jLabelTarifEkleme);
        frame.add(jLabelTarifAdi);
        frame.add(tarifAdiField);
        frame.add(jLabelKategori);
        frame.add(kategoriField);
        frame.add(jLabelmalzeme);
        frame.add(scrollPaneYatay);
        frame.add(malzemeArama);
        frame.add(scrollPane);
        frame.add(jLabelMalzemeKacGram);
        frame.add(MalzemeKacGram);
        frame.add(secimKaydetmeButton);
        frame.add(jLabelHazirlanmaSuresi);
        frame.add(hazirlanmaSuresiField);
        frame.add(jLabelTalimatlar);
        frame.add(talimatlarField);
        frame.add(tarifiKaydetButton);
        frame.add(jLabelMalzemeEklemeUyarisi);
        frame.add(malzemeEkleButton);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    private void malzemelerListesiYapma() {
        try {
            String query = "SELECT * FROM malzemeler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int malzemeId = rs.getInt("malzemeid");
                String malzemeAdi = rs.getString("malzemeadi");
                String malzemeMiktar = rs.getString("toplammiktar");
                String malzemeBirim = rs.getString("malzemebirim");
                Double birimFiyat = rs.getDouble("birimfiyat");

                Malzeme malzeme = new Malzeme(malzemeId, malzemeAdi, malzemeMiktar, malzemeBirim, birimFiyat, null);
                malzemeler.add(malzeme);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void MalzemeArama() {
        String searchTerm = malzemeArama.getText().toLowerCase();
        StringBuilder results = new StringBuilder();

        for (Malzeme malzeme : malzemeler) {
            if (malzeme.getMalzemeAdi().toLowerCase().contains(searchTerm)) {
                results.append(malzeme.getMalzemeAdi()).append("\n\n");
            }
        }

        MalzemeSecimAlani.setText(results.toString());
    }

    private void malzemeTarifIliskisi() {
        int i = 0;
        for (Malzeme malzeme : malzemeler) {
            if (secilenMalzemeler.contains(malzeme.getMalzemeAdi())) {
                String query = "INSERT INTO tarifmalzeme (tarifid, malzemeid, malzememiktar) VALUES (?, ?, ?)";

                try {
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, yeniTarifId);
                    stmt.setInt(2, malzeme.getMalzemeID());
                    stmt.setDouble(3, Double.parseDouble(birimler.get(i)));
                    stmt.executeUpdate();
                    i++;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanına kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void favorilerIliskisi() {
        String query = "INSERT INTO favoriler (tarifid,favoridurumu) VALUES (?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, yeniTarifId);
            stmt.setBoolean(2, false);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanına kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tarifAdlariAlma() {
        try {
            String query = "SELECT tarifadi FROM tarifler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tarifAdi = rs.getString("tarifadi");
                tarifler.add(tarifAdi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String ilkHarfiBuyukYapmak(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
