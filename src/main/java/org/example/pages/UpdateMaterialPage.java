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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateMaterialPage {

    private Connection connection;
    private JFrame frame = new JFrame("Food System App");

    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private ArrayList<String> malzemelerAd = new ArrayList<String>();
    private ArrayList<String> birimler = new ArrayList<String>();

    private JTextField malzemeArama = new JTextField(null);
    private JTextArea MalzemeİsimAlani = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(MalzemeİsimAlani);

    private JButton geriButton = new JButton("Geri");
    private JButton bilgiGetirButton = new JButton("Bilgileri Getir");
    private JButton guncellemeleriKaydetButton = new JButton("Güncellemeleri Kaydet");

    private JTextField  malzemeAdiGetiriciField = new JTextField(null);
    private JTextField malzemeAdiField = new JTextField(null);
    private JTextField malzemeToplamMiktarField = new JTextField(null);
    private JTextField malzemeBirimField = new JTextField(null);
    private JTextField malzemeBirimFiyatField = new JTextField(null);

    public void createAndShowGUI() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        connection = ConnectDB.getConnection();

        malzemelerListesiYapma();

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

        JLabel jLabelMalzemeGuncelleme = new JLabel("MALZEME GÜNCELLEME");
        jLabelMalzemeGuncelleme.setBounds(200, 70, 400, 40);
        jLabelMalzemeGuncelleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeGuncelleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeGuncelleme.setForeground(Color.BLACK);
        jLabelMalzemeGuncelleme.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeGuncelleme.setOpaque(true);
        jLabelMalzemeGuncelleme.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel jLabelMalzemeAdiBilgisi = new JLabel("Malzeme Adı : ");
        jLabelMalzemeAdiBilgisi.setBounds(200, 130, 150, 40);
        jLabelMalzemeAdiBilgisi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdiBilgisi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdiBilgisi.setForeground(Color.BLACK);
        jLabelMalzemeAdiBilgisi.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeAdiBilgisi.setOpaque(true);
        jLabelMalzemeAdiBilgisi.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeAdiGetiriciField.setBounds(370, 130, 230, 40);
        malzemeAdiGetiriciField.setFont(new Font("Arial", Font.BOLD, 15));

        bilgiGetirButton.setBounds(620,125,150,50);
        bilgiGetirButton.setFont(new Font("Arial",Font.BOLD,15));

        bilgiGetirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String malzemeAdi = malzemeAdiGetiriciField.getText().toString();

                    if (!malzemelerAd.contains(malzemeAdi) && !malzemelerAd.contains(ilkHarfiBuyukYapmak(malzemeAdi))) {
                        JOptionPane.showMessageDialog(null, "Lütfen var olan bir malzeme adı girmeye dikkat edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for(Malzeme malzeme : malzemeler){
                        if(malzeme.getMalzemeAdi().equalsIgnoreCase(malzemeAdi) || malzeme.getMalzemeAdi().equalsIgnoreCase(ilkHarfiBuyukYapmak(malzemeAdi))){
                            malzemeAdiField.setText(malzeme.getMalzemeAdi());
                            malzemeToplamMiktarField.setText(malzeme.getToplamMiktar());
                            malzemeBirimField.setText(malzeme.getMalzemeBirim());
                            malzemeBirimFiyatField.setText(malzeme.getMalzemeFiyat().toString());
                        }
                    }
            }
        });

        JLabel jLabelMalzemeListesi = new JLabel("MALZEME LİSTESİ");
        jLabelMalzemeListesi.setBounds(250, 200, 300, 40);
        jLabelMalzemeListesi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeListesi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeListesi.setForeground(Color.BLACK);
        jLabelMalzemeListesi.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeListesi.setOpaque(true);
        jLabelMalzemeListesi.setFont(new Font("Arial", Font.BOLD, 24));

        malzemeArama.setBounds(200, 250, 400, 40);
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

        MalzemeİsimAlani.setBounds(200, 300, 400, 120);
        MalzemeİsimAlani.setLineWrap(true);
        MalzemeİsimAlani.setWrapStyleWord(true);
        MalzemeİsimAlani.setEditable(false);
        MalzemeİsimAlani.setFont(new Font("Arial", Font.BOLD, 15));

        for (Malzeme malzeme : malzemeler) {
            MalzemeİsimAlani.append(malzeme.getMalzemeAdi() + "\n\n");
        }

        scrollPane.setBounds(200, 300, 400, 120);
        scrollPane.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelMalzemeBilgileri = new JLabel("MALZEME BİLGİLERİ");
        jLabelMalzemeBilgileri.setBounds(225, 440, 350, 40);
        jLabelMalzemeBilgileri.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBilgileri.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBilgileri.setForeground(Color.BLACK);
        jLabelMalzemeBilgileri.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeBilgileri.setOpaque(true);
        jLabelMalzemeBilgileri.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel jLabelMalzemeAdi= new JLabel("Malzeme Adı : ");
        jLabelMalzemeAdi.setBounds(200, 500, 150, 40);
        jLabelMalzemeAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdi.setForeground(Color.BLACK);
        jLabelMalzemeAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeAdi.setOpaque(true);
        jLabelMalzemeAdi.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeAdiField.setBounds(370,500,200,40);
        malzemeAdiField.setFont(new Font("Arial",Font.BOLD,15));
        malzemeAdiField.setEditable(false);

        JLabel jLabelMalzemeToplamMiktar= new JLabel("Toplam Miktar : ");
        jLabelMalzemeToplamMiktar.setBounds(200, 560, 150, 40);
        jLabelMalzemeToplamMiktar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeToplamMiktar.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeToplamMiktar.setForeground(Color.BLACK);
        jLabelMalzemeToplamMiktar.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeToplamMiktar.setOpaque(true);
        jLabelMalzemeToplamMiktar.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeToplamMiktarField.setBounds(370,560,200,40);
        malzemeToplamMiktarField.setFont(new Font("Arial",Font.BOLD,15));

        JLabel jLabelMalzemeBirim= new JLabel("Malzeme Birimi : ");
        jLabelMalzemeBirim.setBounds(200, 620, 150, 40);
        jLabelMalzemeBirim.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirim.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirim.setForeground(Color.BLACK);
        jLabelMalzemeBirim.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeBirim.setOpaque(true);
        jLabelMalzemeBirim.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeBirimField.setBounds(370,620,200,40);
        malzemeBirimField.setFont(new Font("Arial",Font.BOLD,15));

        JLabel jLabelMalzemeBirimFiyat= new JLabel("Birim Fiyat : ");
        jLabelMalzemeBirimFiyat.setBounds(200, 680, 150, 40);
        jLabelMalzemeBirimFiyat.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirimFiyat.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirimFiyat.setForeground(Color.BLACK);
        jLabelMalzemeBirimFiyat.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeBirimFiyat.setOpaque(true);
        jLabelMalzemeBirimFiyat.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeBirimFiyatField.setBounds(370,680,200,40);
        malzemeBirimFiyatField.setFont(new Font("Ariel",Font.BOLD,15));

        guncellemeleriKaydetButton.setBounds(590,675,175,50);
        guncellemeleriKaydetButton.setFont(new Font("Arial",Font.BOLD,15));

        guncellemeleriKaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String malzemeAdi = malzemeAdiField.getText().trim();
                String malzemeToplamMiktar = malzemeToplamMiktarField.getText().trim();
                String malzemeBirim = malzemeBirimField.getText().trim();
                String malzemeBirimFiyat = malzemeBirimFiyatField.getText().trim();

                birimler.add("mililitre");
                birimler.add("gram");
                birimler.add("adet");
                birimler.add("diş");
                birimler.add("çay kaşığı");
                birimler.add("tatlı kaşığı");
                birimler.add("yemek kaşığı");

                if(malzemeAdi.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Malzeme adını boş bırakamazsınız, Lütfen malzeme adı girip bilgileri getir butonuna tıklayın.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Double malzemeToplamMiktarControl = Double.parseDouble(malzemeToplamMiktar);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Toplam malzeme miktarı bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(malzemeBirim.isEmpty() || !birimler.contains(malzemeBirim)){
                    JOptionPane.showMessageDialog(null, "Malzeme birimini mililitre, gram, adet, diş, çay kaşığı, tatlı kaşığı, yemek kaşığı 'ndan farklı giremezsiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double malzemeBirimFiyatControl = Double.parseDouble(malzemeBirimFiyat);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Malzeme birim fiyatı bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String updateQuery = "UPDATE malzemeler SET toplammiktar = ?, malzemebirim = ?, birimfiyat = ? WHERE malzemeadi = ?";

                    PreparedStatement stmt = connection.prepareStatement(updateQuery);

                    stmt.setString(1, malzemeToplamMiktar);
                    stmt.setString(2, malzemeBirim);
                    stmt.setBigDecimal(3, new BigDecimal(malzemeBirimFiyat));
                    stmt.setString(4, malzemeAdi);

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Malzeme başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        UpdateMaterialPage updateMaterialPage = new UpdateMaterialPage();
                        updateMaterialPage.createAndShowGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "Malzeme güncellenemedi. Lütfen malzeme adını kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        frame.add(geriButton);
        frame.add(jLabelMalzemeGuncelleme);
        frame.add(jLabelMalzemeAdiBilgisi);
        frame.add(malzemeAdiGetiriciField);
        frame.add(bilgiGetirButton);
        frame.add(jLabelMalzemeListesi);
        frame.add(malzemeArama);
        frame.add(scrollPane);
        frame.add(jLabelMalzemeBilgileri);
        frame.add(jLabelMalzemeAdi);
        frame.add(malzemeAdiField);
        frame.add(jLabelMalzemeToplamMiktar);
        frame.add(malzemeToplamMiktarField);
        frame.add(jLabelMalzemeBirim);
        frame.add(malzemeBirimField);
        frame.add(jLabelMalzemeBirimFiyat);
        frame.add(malzemeBirimFiyatField);
        frame.add(guncellemeleriKaydetButton);


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

                if (malzemeMiktar == null) {
                    malzemeMiktar = "0";
                }

                Malzeme malzeme = new Malzeme(malzemeId, malzemeAdi, malzemeMiktar, malzemeBirim, birimFiyat, null);
                malzemeler.add(malzeme);
                malzemelerAd.add(malzemeAdi);
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

        MalzemeİsimAlani.setText(results.toString());
    }

    public String ilkHarfiBuyukYapmak(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
