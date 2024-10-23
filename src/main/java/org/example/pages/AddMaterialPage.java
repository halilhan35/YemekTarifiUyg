package org.example.pages;

import org.example.database.ConnectDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddMaterialPage {

    private Connection connection;
    private ArrayList<String> malzemeler = new ArrayList<>();
    private ArrayList<String> birimler = new ArrayList<String>();

    JFrame frame = new JFrame("Food System App");

    private JButton geriButton = new JButton("Geri");
    private JButton malzemeyiEkleButton = new JButton("Malzemeyi Ekle");

    JTextField malzemeAdiField = new JTextField(null);
    JTextField malzemeToplamMiktarField = new JTextField(null);
    JTextField malzemeBirimField = new JTextField(null);
    JTextField malzemeBirimFiyatField = new JTextField(null);

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

        panel.setLayout(null);
        frame.setContentPane(panel);

        geriButton.setBounds(15, 15, 150, 50);
        geriButton.setFont(new Font("Arial", Font.BOLD, 16));

        geriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AddRecipePage addRecipePage = new AddRecipePage();
                addRecipePage.createAndShowGUI();
            }
        });

        JLabel jLabelMalzemeEkleme = new JLabel("MALZEME EKLEME");
        jLabelMalzemeEkleme.setBounds(200, 90, 400, 40);
        jLabelMalzemeEkleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEkleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEkleme.setForeground(Color.BLACK);
        jLabelMalzemeEkleme.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeEkleme.setOpaque(true);
        jLabelMalzemeEkleme.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel jLabelMalzemeAdi = new JLabel("Malzeme Adı : ");
        jLabelMalzemeAdi.setBounds(200, 160, 150, 40);
        jLabelMalzemeAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeAdi.setForeground(Color.BLACK);
        jLabelMalzemeAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeAdi.setOpaque(true);
        jLabelMalzemeAdi.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeAdiField.setBounds(370, 160, 230, 40);
        malzemeAdiField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelToplamMiktar = new JLabel("Toplam Miktar : ");
        jLabelToplamMiktar.setBounds(200, 220, 150, 40);
        jLabelToplamMiktar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelToplamMiktar.setVerticalAlignment(SwingConstants.CENTER);
        jLabelToplamMiktar.setForeground(Color.BLACK);
        jLabelToplamMiktar.setBackground(new Color(255, 255, 255, 200));
        jLabelToplamMiktar.setOpaque(true);
        jLabelToplamMiktar.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeToplamMiktarField.setBounds(370, 220, 230, 40);
        malzemeToplamMiktarField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelMalzemeBirim = new JLabel("Malzeme Birim : ");
        jLabelMalzemeBirim.setBounds(200, 280, 150, 40);
        jLabelMalzemeBirim.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirim.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeBirim.setForeground(Color.BLACK);
        jLabelMalzemeBirim.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeBirim.setOpaque(true);
        jLabelMalzemeBirim.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeBirimField.setBounds(370, 280, 230, 40);
        malzemeBirimField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelBirimFiyat = new JLabel("Birim Fiyat : ");
        jLabelBirimFiyat.setBounds(200, 340, 150, 40);
        jLabelBirimFiyat.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelBirimFiyat.setVerticalAlignment(SwingConstants.CENTER);
        jLabelBirimFiyat.setForeground(Color.BLACK);
        jLabelBirimFiyat.setBackground(new Color(255, 255, 255, 200));
        jLabelBirimFiyat.setOpaque(true);
        jLabelBirimFiyat.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeBirimFiyatField.setBounds(370, 340, 230, 40);
        malzemeBirimFiyatField.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeyiEkleButton.setBounds(325, 410, 150, 50);
        malzemeyiEkleButton.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelBirimUyari = new JLabel("Birim ; mililitre, gram, adet, diş, çay kaşığı, tatlı kaşığı veya yemek kaşığı olmalıdır.");
        jLabelBirimUyari.setBounds(50, 500, 700, 50);
        jLabelBirimUyari.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelBirimUyari.setVerticalAlignment(SwingConstants.CENTER);
        jLabelBirimUyari.setForeground(Color.BLACK);
        jLabelBirimUyari.setBackground(new Color(255, 255, 255, 200));
        jLabelBirimUyari.setOpaque(true);
        jLabelBirimUyari.setFont(new Font("Arial", Font.BOLD, 16));

        malzemeyiEkleButton.addActionListener(new ActionListener() {
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

                malzemeAdlariAlma();

                if (malzemeler.contains(malzemeAdi) || malzemeler.contains(ilkHarfiBuyukYapmak(malzemeAdi))) {
                    JOptionPane.showMessageDialog(null, "Bu malzeme zaten var, İstersen ana sayfadan güncelleyebilirsin.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (malzemeAdi.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Malzeme adını boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Double malzemeToplamMiktarControl = Double.parseDouble(malzemeToplamMiktar);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Toplam malzeme miktarı bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (malzemeBirim.isEmpty() || !birimler.contains(malzemeBirim)) {
                    JOptionPane.showMessageDialog(null, "Malzeme birimini varsayılan birimlerden farklı giremezsiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double malzemeBirimFiyatControl = Double.parseDouble(malzemeBirimFiyat);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Malzeme birim fiyatı bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query = "INSERT INTO malzemeler (malzemeadi, toplammiktar, malzemebirim, birimfiyat) VALUES (?, ?, ?, ?)";

                try {
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, malzemeAdi);
                    stmt.setString(2, malzemeToplamMiktar);
                    stmt.setString(3, malzemeBirim);
                    stmt.setDouble(4, Double.parseDouble(malzemeBirimFiyat));

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Malzeme başarıyla kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        AddMaterialPage addMaterialPage = new AddMaterialPage();
                        addMaterialPage.createAndShowGUI();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Malzeme veritabanına kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        frame.add(geriButton);
        frame.add(jLabelMalzemeEkleme);
        frame.add(jLabelMalzemeAdi);
        frame.add(malzemeAdiField);
        frame.add(jLabelToplamMiktar);
        frame.add(malzemeToplamMiktarField);
        frame.add(jLabelMalzemeBirim);
        frame.add(malzemeBirimField);
        frame.add(jLabelBirimFiyat);
        frame.add(malzemeBirimFiyatField);
        frame.add(malzemeyiEkleButton);
        frame.add(jLabelBirimUyari);


        frame.setSize(800, 800);
        frame.setVisible(true);

    }

    public void malzemeAdlariAlma() {
        try {
            String query = "SELECT malzemeadi FROM malzemeler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tarifAdi = rs.getString("malzemeadi");
                malzemeler.add(tarifAdi);
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
