package org.example.pages;

import org.example.classes.Tarif;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteRecipePage {

    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();

    private Connection connection;
    private JFrame frame = new JFrame("Food System App");

    private JTextArea tarifListeAlani = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(tarifListeAlani);

    private JButton geriButton = new JButton("Geri");
    private JButton tarifSilButton = new JButton("Tarifi Sil");
    private JTextField tarifAdiField = new JTextField(null);
    private JTextField tarifAramaField = new JTextField(null);

    public void createAndShowGUI() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        connection = ConnectDB.getConnection();

        tariflerListesiYapma();

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

        JLabel jLabelTarifSilme = new JLabel("TARİF SİLME");
        jLabelTarifSilme.setBounds(200, 70, 400, 40);
        jLabelTarifSilme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifSilme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifSilme.setForeground(Color.BLACK);
        jLabelTarifSilme.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifSilme.setOpaque(true);
        jLabelTarifSilme.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel jLabelTarifAdi = new JLabel("Silmek istediğiniz Tarif Adı : ");
        jLabelTarifAdi.setBounds(150, 130, 250, 40);
        jLabelTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setForeground(Color.BLACK);
        jLabelTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifAdi.setOpaque(true);
        jLabelTarifAdi.setFont(new Font("Arial", Font.BOLD, 15));

        tarifAdiField.setBounds(420, 130, 250, 40);
        tarifAdiField.setFont(new Font("Arial", Font.BOLD, 15));

        tarifSilButton.setBounds(330, 190, 150, 50);
        tarifSilButton.setFont(new Font("Arial", Font.BOLD, 15));

        tarifSilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tarifAdi = tarifAdiField.getText().toString();
                int tarifID = -1;

                for (int i = 0; i < tarifler.size(); i++) {
                    if (tarifler.get(i).getTarifAdi().equalsIgnoreCase(tarifAdi)) {
                        tarifID = tarifler.get(i).getTarifID();
                    }
                }

                if (tarifAdi.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Önce silmek istediğiniz tarif adını yazmalısınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tarifID == -1) {
                    JOptionPane.showMessageDialog(null, "Yazdığınız tarif, tarif listesinde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query1 = "DELETE FROM favoriler WHERE tarifid = ?";
                String query2 = "DELETE FROM tarifler WHERE tarifid = ?";

                try {
                    PreparedStatement stmt1 = connection.prepareStatement(query1);
                    stmt1.setInt(1, tarifID);

                    int rowsAffected1 = stmt1.executeUpdate();
                    if (rowsAffected1 > 0) {
                        PreparedStatement stmt2 = connection.prepareStatement(query2);
                        stmt2.setInt(1, tarifID);

                        int rowsAffected2 = stmt2.executeUpdate();
                        if (rowsAffected2 > 0) {
                            JOptionPane.showMessageDialog(null, "Tarif başarıyla silindi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose();
                            DeleteRecipePage deleteRecipePage = new DeleteRecipePage();
                            deleteRecipePage.createAndShowGUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "Tarif bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Favorilerde bu tarif bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Tarif veritabanından silinirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JLabel jLabelTarifListesi = new JLabel("TARİF LİSTESİ");
        jLabelTarifListesi.setBounds(200, 300, 400, 40);
        jLabelTarifListesi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifListesi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifListesi.setForeground(Color.BLACK);
        jLabelTarifListesi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifListesi.setOpaque(true);
        jLabelTarifListesi.setFont(new Font("Arial", Font.BOLD, 24));

        tarifAramaField.setBounds(200, 360, 400, 40);
        tarifAramaField.setFont(new Font("Arial", Font.BOLD, 15));

        tarifAramaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tarifArama();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tarifArama();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tarifArama();
            }
        });

        tarifListeAlani.setBounds(200, 420, 400, 270);
        tarifListeAlani.setLineWrap(true);
        tarifListeAlani.setWrapStyleWord(true);
        tarifListeAlani.setEditable(false);
        tarifListeAlani.setFont(new Font("Arial", Font.BOLD, 15));

        for (Tarif tarifAdi : tarifler) {
            tarifListeAlani.append(tarifAdi.getTarifAdi() + "\n\n");
        }

        scrollPane.setBounds(200, 420, 400, 270);
        scrollPane.setFont(new Font("Arial", Font.BOLD, 15));

        frame.add(geriButton);
        frame.add(jLabelTarifSilme);
        frame.add(jLabelTarifAdi);
        frame.add(tarifAdiField);
        frame.add(tarifSilButton);
        frame.add(jLabelTarifListesi);
        frame.add(tarifAramaField);
        frame.add(scrollPane);

        frame.setSize(800, 800);
        frame.setVisible(true);

    }

    public void tariflerListesiYapma() {
        try {
            String query = "SELECT * FROM tarifler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int tarifId = rs.getInt("tarifid");
                String tarifAdi = rs.getString("tarifadi");
                String tarifKategori = rs.getString("kategori");
                int tarifHazirlamaSuresi = rs.getInt("hazirlamasuresi");
                String tarifTalimatlar = rs.getString("talimatlar");

                Tarif tarif = new Tarif(tarifId, tarifAdi, tarifKategori, tarifHazirlamaSuresi, tarifTalimatlar, null);
                tarifler.add(tarif);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void tarifArama() {
        String searchTerm = tarifAramaField.getText().toLowerCase();
        StringBuilder results = new StringBuilder();

        for (Tarif tarifAdi : tarifler) {
            if (tarifAdi.getTarifAdi().toLowerCase().contains(searchTerm)) {
                results.append(tarifAdi).append("\n\n");
            }
        }

        tarifListeAlani.setText(results.toString());
    }

}
