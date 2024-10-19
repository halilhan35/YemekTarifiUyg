package org.example.pages;

import org.example.classes.Malzeme;
import org.example.classes.Tarif;
import org.example.database.ConnectDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Favorites {

    private Connection connection;
    private JFrame frame;
    private JPanel AnaPanel = new JPanel();

    private ArrayList<Integer> favoriler = new ArrayList<Integer>();
    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();
    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private java.util.List<Integer> kullanilanMalzemeIdleri = new ArrayList<Integer>();
    private List<Double> kullanilanMalzemeMiktarlari = new ArrayList<Double>();
    private HashMap<Integer, Double> maliyetMap = new HashMap<Integer, Double>();

    private JTextField favoriEkleText = new JTextField(null);
    private JTextField favoriSilText = new JTextField(null);

    private JButton geriButton = new JButton("Geri");
    private JButton favorilereEkleButton = new JButton("Favorilere Ekle");
    private JButton favorilerdenSilButton = new JButton("Favorilerden Sil");

    public void createAndShowGUI() {

        connection = ConnectDB.getConnection();

        favorilerSeçme();
        tumTarifleriVeMalzemeleriListeyeEkleme();
        tariflerdeMaliyetiHashMapEkleme();

        frame = new JFrame("Food System App");
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

        frame.add(geriButton);

        JLabel jLabelFavoriler = new JLabel("FAVORİLER");
        jLabelFavoriler.setBounds(125, 80, 550, 40);
        jLabelFavoriler.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavoriler.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavoriler.setForeground(Color.BLACK);
        jLabelFavoriler.setBackground(new Color(255, 255, 255, 200));
        jLabelFavoriler.setOpaque(true);
        jLabelFavoriler.setFont(new Font("Arial", Font.BOLD, 32));

        frame.add(jLabelFavoriler);

        AnaPanel.setLayout(new GridLayout(0, 2, 10, 10));

        favorilerGosterim();

        JScrollPane scrollPane = new JScrollPane(AnaPanel);
        scrollPane.setBounds(125, 140, 550, 400);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);

        JLabel jLabelFavorilereEkleme = new JLabel("Favorilere Ekleme");
        jLabelFavorilereEkleme.setBounds(75, 560, 300, 40);
        jLabelFavorilereEkleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavorilereEkleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavorilereEkleme.setForeground(Color.BLACK);
        jLabelFavorilereEkleme.setBackground(new Color(255, 255, 255, 200));
        jLabelFavorilereEkleme.setOpaque(true);
        jLabelFavorilereEkleme.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(jLabelFavorilereEkleme);

        JLabel jLabelFavorilereEkleTarifAdi = new JLabel("Tarif Adı:");
        jLabelFavorilereEkleTarifAdi.setBounds(75, 620, 100, 40);
        jLabelFavorilereEkleTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavorilereEkleTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavorilereEkleTarifAdi.setForeground(Color.BLACK);
        jLabelFavorilereEkleTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelFavorilereEkleTarifAdi.setOpaque(true);
        jLabelFavorilereEkleTarifAdi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelFavorilereEkleTarifAdi);

        favoriEkleText.setBounds(190, 618, 185, 45);
        favoriEkleText.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(favoriEkleText);

        favorilereEkleButton.setBounds(140, 680, 175, 50);
        favorilereEkleButton.setFont(new Font("Arial", Font.BOLD, 16));

        favorilereEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                favorilereEkleme();
            }
        });

        frame.add(favorilereEkleButton);

        JLabel jLabelCizgi = new JLabel();
        jLabelCizgi.setBounds(398, 560, 20, 200);
        jLabelCizgi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCizgi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelCizgi.setForeground(Color.BLACK);
        jLabelCizgi.setBackground(new Color(255, 255, 255, 200));
        jLabelCizgi.setOpaque(true);

        frame.add(jLabelCizgi);

        JLabel jLabelFavorilerdenSilme = new JLabel("Favorilerden Silme");
        jLabelFavorilerdenSilme.setBounds(441, 560, 300, 40);
        jLabelFavorilerdenSilme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavorilerdenSilme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavorilerdenSilme.setForeground(Color.BLACK);
        jLabelFavorilerdenSilme.setBackground(new Color(255, 255, 255, 200));
        jLabelFavorilerdenSilme.setOpaque(true);
        jLabelFavorilerdenSilme.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(jLabelFavorilerdenSilme);

        JLabel jLabelFavorilereSilTarifAdi = new JLabel("Tarif Adı:");
        jLabelFavorilereSilTarifAdi.setBounds(441, 620, 100, 40);
        jLabelFavorilereSilTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavorilereSilTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavorilereSilTarifAdi.setForeground(Color.BLACK);
        jLabelFavorilereSilTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelFavorilereSilTarifAdi.setOpaque(true);
        jLabelFavorilereSilTarifAdi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelFavorilereSilTarifAdi);

        favoriSilText.setBounds(556, 618, 185, 45);
        favoriSilText.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(favoriSilText);

        favorilerdenSilButton.setBounds(506, 680, 175, 50);
        favorilerdenSilButton.setFont(new Font("Arial", Font.BOLD, 16));

        favorilerdenSilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                favorilerdenSilme();
            }
        });

        frame.add(favorilerdenSilButton);

        frame.setSize(800, 800);
        frame.setVisible(true);

    }

    public void favorilerSeçme() {
        String query = "SELECT tarifid FROM favoriler WHERE favoridurumu = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setBoolean(1, true);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int tarifID = rs.getInt("tarifid");
                favoriler.add(tarifID);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void tumTarifleriVeMalzemeleriListeyeEkleme() {

        try {
            String query = "SELECT * FROM tarifler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int tarifID = rs.getInt("tarifid");
                String tarifAdi = rs.getString("tarifadi");
                String tarifKategori = rs.getString("kategori");
                int tarifHazirlamaSuresi = rs.getInt("hazirlamasuresi");
                String tarifTalimatlar = rs.getString("talimatlar");

                tarifler.add(new Tarif(tarifID, tarifAdi, tarifKategori, tarifHazirlamaSuresi, tarifTalimatlar, null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "SELECT * FROM malzemeler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int malzemeID = rs.getInt("malzemeid");
                String malzemeAdi = rs.getString("malzemeadi");
                String malzemeToplamMiktar = rs.getString("toplammiktar");
                String malzemeBirim = rs.getString("malzemebirim");
                Double malzemeBirimFiyat = rs.getDouble("birimfiyat");

                malzemeler.add(new Malzeme(malzemeID, malzemeAdi, malzemeToplamMiktar, malzemeBirim, malzemeBirimFiyat, null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void tariflerdeMaliyetiHashMapEkleme() {

        for (int i = 0; i < tarifler.size(); i++) {

            double toplamMaliyet = 0;
            kullanilanMalzemeIdleri.clear();
            kullanilanMalzemeMiktarlari.clear();

            try {
                String query = "SELECT * FROM tarifmalzeme WHERE tarifid = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, tarifler.get(i).getTarifID());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int malzemeID = rs.getInt("malzemeid");
                    String malzemeToplamMiktar = rs.getString("malzememiktar");
                    double malzemeToplamMiktari;

                    if (malzemeToplamMiktar != null) {
                        malzemeToplamMiktari = Double.parseDouble(malzemeToplamMiktar);
                    } else {
                        malzemeToplamMiktari = 0.0;
                    }

                    kullanilanMalzemeIdleri.add(malzemeID);
                    kullanilanMalzemeMiktarlari.add(malzemeToplamMiktari);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < kullanilanMalzemeIdleri.size(); j++) {
                for (Malzeme malzeme : malzemeler) {
                    if (malzeme.getMalzemeID() == kullanilanMalzemeIdleri.get(j)) {
                        Double malzemeToplamMiktar;
                        if (malzeme.getToplamMiktar() != null) {
                            malzemeToplamMiktar = Double.parseDouble(malzeme.getToplamMiktar());
                        } else {
                            malzemeToplamMiktar = 0.0;
                        }
                        if (malzemeToplamMiktar < kullanilanMalzemeMiktarlari.get(j)) {
                            toplamMaliyet += (kullanilanMalzemeMiktarlari.get(j) - malzemeToplamMiktar) * malzeme.getMalzemeFiyat();
                        }
                    }
                }
            }

            maliyetMap.put(tarifler.get(i).getTarifID(), toplamMaliyet);

        }
    }

    public void favorilerGosterim() {
        for (int i = 0; i < favoriler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            for (int j = 0; j < tarifler.size(); j++) {
                if (favoriler.get(i) == tarifler.get(j).getTarifID()) {
                    double toplamMaliyet = maliyetMap.get(tarifler.get(j).getTarifID());

                    try {
                        File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifler.get(j).getTarifID() + ".jpeg");
                        BufferedImage image;
                        if (imageFile.exists()) {
                            image = ImageIO.read(imageFile);
                        } else {
                            image = ImageIO.read(new File("src/main/java/org/example/drawables/recipe_default.jpeg"));
                        }

                        int newWidth = 275;
                        int newHeight = 200;
                        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
                        BufferedImage lowQualityImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = lowQualityImage.createGraphics();
                        g2d.drawImage(scaledImage, 0, 0, null);
                        g2d.dispose();

                        JLabel imageLabel = new JLabel(new ImageIcon(lowQualityImage));
                        singleTarifPanel.add(imageLabel, BorderLayout.CENTER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new GridLayout(3, 1));

                    JLabel tarifAdiLabel = new JLabel(tarifler.get(j).getTarifAdi(), SwingConstants.CENTER);

                    JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(tarifler.get(j).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);

                    System.out.println("Tarif : " + tarifler.get(j).getTarifID() + " işleniyor ve getiriliyor.");

                    JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

                    if (toplamMaliyet > 0.0) {
                        tarifAdiLabel.setForeground(Color.RED);
                        tarifAdiLabel.setBackground(Color.BLACK);
                        tarifAdiLabel.setOpaque(true);
                        tarifHazirlamaSuresiLabel.setForeground(Color.RED);
                        tarifHazirlamaSuresiLabel.setBackground(Color.BLACK);
                        tarifHazirlamaSuresiLabel.setOpaque(true);
                        costLabel.setForeground(Color.RED);
                        costLabel.setBackground(Color.BLACK);
                        costLabel.setOpaque(true);
                    } else {
                        tarifAdiLabel.setForeground(Color.GREEN);
                        tarifAdiLabel.setBackground(Color.BLACK);
                        tarifAdiLabel.setOpaque(true);
                        tarifHazirlamaSuresiLabel.setForeground(Color.GREEN);
                        tarifHazirlamaSuresiLabel.setBackground(Color.BLACK);
                        tarifHazirlamaSuresiLabel.setOpaque(true);
                        costLabel.setForeground(Color.GREEN);
                        costLabel.setBackground(Color.BLACK);
                        costLabel.setOpaque(true);
                    }

                    infoPanel.add(tarifAdiLabel);
                    infoPanel.add(tarifHazirlamaSuresiLabel);
                    infoPanel.add(costLabel);

                    singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

                    AnaPanel.add(singleTarifPanel);
                }
            }
        }
    }

    public void favorilereEkleme() {

        String eklenecekTarifAdi = favoriEkleText.getText().toString();
        int eklenecekTarifId = -1;

        for (int i = 0; i < tarifler.size(); i++) {
            if (tarifler.get(i).getTarifAdi().equalsIgnoreCase(eklenecekTarifAdi)) {
                eklenecekTarifId = tarifler.get(i).getTarifID();
            }
        }

        if (eklenecekTarifAdi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Eklemek istediğiniz tarif adını boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (eklenecekTarifId == -1) {
            JOptionPane.showMessageDialog(null, "Eklemek istediğiniz tarif adı, tarif listesinde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (favoriler.contains(eklenecekTarifId)) {
            JOptionPane.showMessageDialog(null, "Favorilerinize eklemek istediğiniz tarif zaten favorilerinizde bulunuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String updateQuery = "UPDATE favoriler SET favoridurumu = ? WHERE tarifid = ?";

            PreparedStatement stmt = connection.prepareStatement(updateQuery);

            stmt.setBoolean(1, true);
            stmt.setInt(2, eklenecekTarifId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Tarif favorilere başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                Favorites favorites = new Favorites();
                favorites.createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(null, "Tarif favorilere eklenemedi. Lütfen tarif adını kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void favorilerdenSilme() {

        String silinecekTarifAdi = favoriSilText.getText().toString();
        int silinecekTarifId = -1;

        for (int i = 0; i < tarifler.size(); i++) {
            if (tarifler.get(i).getTarifAdi().equalsIgnoreCase(silinecekTarifAdi)) {
                silinecekTarifId = tarifler.get(i).getTarifID();
            }
        }

        if (silinecekTarifId == -1) {
            JOptionPane.showMessageDialog(null, "Silmek istediğiniz tarif adı, tarif listesinde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!favoriler.contains(silinecekTarifId)) {
            JOptionPane.showMessageDialog(null, "Silmek istediğiniz tarif, favorilerinizde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (silinecekTarifAdi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silmek istediğiniz tarif adını boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String updateQuery = "UPDATE favoriler SET favoridurumu = ? WHERE tarifid = ?";

            PreparedStatement stmt = connection.prepareStatement(updateQuery);

            stmt.setBoolean(1, false);
            stmt.setInt(2, silinecekTarifId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Tarif favorilerden başarıyla silindi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                Favorites favorites = new Favorites();
                favorites.createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(null, "Tarif favorilerden silinemedi. Lütfen tarif adını kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

    }

}
