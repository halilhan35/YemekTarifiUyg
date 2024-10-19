package org.example.pages;

import org.example.classes.Malzeme;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPage {

    private Connection connection;
    private JFrame frame;

    private JPanel AnaPanel = new JPanel();

    private ArrayList<Tarif> tarifFiltreleyiciler = new ArrayList<>();
    private ArrayList<Malzeme> malzemeFiltreleyiciler = new ArrayList<>();

    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();
    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private List<Integer> kullanilanMalzemeIdleri = new ArrayList<Integer>();
    private List<Double> kullanilanMalzemeMiktarlari = new ArrayList<Double>();
    private HashMap<Integer, Double> maliyetMap = new HashMap<Integer, Double>();

    private JButton tarifEkleButton = new JButton("Tarif Ekle");
    private JButton tarifGuncelleButton = new JButton("Tarif Güncelle");
    private JButton tarifSilButton = new JButton("Tarif Sil");
    private JButton malzemeGuncelleButton = new JButton("Malzeme Güncelle");

    private JLabel ListeAciklama = new JLabel();
    private JTextField dinamikAramaField = new JTextField(null);

    public void createAndShowGUI() {

        connection = ConnectDB.getConnection();

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

        tarifEkleButton.setBounds(1025, 25, 150, 50);
        tarifEkleButton.setFont(new Font("Arial", Font.BOLD, 15));

        tarifGuncelleButton.setBounds(1025, 95, 150, 50);
        tarifGuncelleButton.setFont(new Font("Arial", Font.BOLD, 15));

        tarifSilButton.setBounds(1025, 165, 150, 50);
        tarifSilButton.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeGuncelleButton.setBounds(1025, 235, 150, 50);
        malzemeGuncelleButton.setFont(new Font("Arial", Font.BOLD, 15));

        tarifEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.dispose();
                        AddRecipePage addRecipePage = new AddRecipePage();
                        addRecipePage.createAndShowGUI();
                    }
                });
            }
        });

        tarifSilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                DeleteRecipePage deleteRecipePage = new DeleteRecipePage();
                deleteRecipePage.createAndShowGUI();
            }
        });

        malzemeGuncelleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UpdateMaterialPage updateMaterialPage = new UpdateMaterialPage();
                updateMaterialPage.createAndShowGUI();
            }
        });

        tarifGuncelleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UpdateRecipePage updateRecipePage = new UpdateRecipePage();
                updateRecipePage.createAndShowGUI();
            }
        });

        frame.add(tarifEkleButton);
        frame.add(tarifGuncelleButton);
        frame.add(tarifSilButton);
        frame.add(malzemeGuncelleButton);

        JLabel filtrelemeSecenekleri = new JLabel("FİLTRELEME SEÇENEKLERİ");
        filtrelemeSecenekleri.setBounds(10, 20, 975, 40);
        filtrelemeSecenekleri.setHorizontalAlignment(SwingConstants.CENTER);
        filtrelemeSecenekleri.setVerticalAlignment(SwingConstants.CENTER);
        filtrelemeSecenekleri.setForeground(Color.BLACK);
        filtrelemeSecenekleri.setBackground(new Color(255, 255, 255, 200));
        filtrelemeSecenekleri.setOpaque(true);
        filtrelemeSecenekleri.setFont(new Font("Arial", Font.BOLD, 24));

        JButton varsayilanTarifListesiButton = new JButton("Varsayılan Tarifler Listesi");
        varsayilanTarifListesiButton.setBounds(10, 88, 250, 50);
        varsayilanTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        varsayilanTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                varsayilanTarifGosterim();
            }
        });

        frame.add(varsayilanTarifListesiButton);

        JButton varsayilanMalzemeListesiButton = new JButton("Varsayılan Malzemeler Listesi");
        varsayilanMalzemeListesiButton.setBounds(10, 158, 250, 50);
        varsayilanMalzemeListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        varsayilanMalzemeListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                varsayilanMalzemeGosterim();
            }
        });

        frame.add(varsayilanMalzemeListesiButton);

        JButton maliyetsizTariflerListesiButton = new JButton("Maliyetsiz Tarifler Listesi");
        maliyetsizTariflerListesiButton.setBounds(10, 228, 250, 50);
        maliyetsizTariflerListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        maliyetsizTariflerListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                maliyetsizTarifleriGosterim();
            }
        });

        frame.add(maliyetsizTariflerListesiButton);

        JButton maliyetliTarifListesiButton = new JButton("Maliyetli Tarifler Listesi");
        maliyetliTarifListesiButton.setBounds(270, 88, 250, 50);
        maliyetliTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        maliyetliTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                maliyetliTarifleriGosterim();
            }
        });

        frame.add(maliyetliTarifListesiButton);

        JButton maliyetiAzalanTarifListesiButton = new JButton("Azalan Maliyet Tarifler Listesi");
        maliyetiAzalanTarifListesiButton.setBounds(270, 158, 250, 50);
        maliyetiAzalanTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        maliyetiAzalanTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                maliyetAzalanTarifGosterim();
            }
        });

        frame.add(maliyetiAzalanTarifListesiButton);

        JButton maliyetiArtanTarifListesiButton = new JButton("Artan Maliyet Tarifler Listesi");
        maliyetiArtanTarifListesiButton.setBounds(270, 228, 250, 50);
        maliyetiArtanTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        maliyetiArtanTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                maliyetArtanTarifGosterim();
            }
        });

        frame.add(maliyetiArtanTarifListesiButton);

        JButton enHizlidanEnYavasaTarifListesiButton = new JButton("En Hızlıdan En Yavaşa Tarifler Listesi");
        enHizlidanEnYavasaTarifListesiButton.setBounds(530, 88, 300, 50);
        enHizlidanEnYavasaTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        enHizlidanEnYavasaTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                EnHizlidanEnYavasaTarifGosterim();
            }
        });

        frame.add(enHizlidanEnYavasaTarifListesiButton);

        JButton EnYavastanEnHizliyaTarifListesiButton = new JButton("En Yavaştan En Hızlıya Tarifler Listesi");
        EnYavastanEnHizliyaTarifListesiButton.setBounds(530, 158, 300, 50);
        EnYavastanEnHizliyaTarifListesiButton.setFont(new Font("Arial", Font.BOLD, 15));

        EnYavastanEnHizliyaTarifListesiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                EnYavastanEnHizliyaTarifGosterim();
            }
        });

        frame.add(EnYavastanEnHizliyaTarifListesiButton);

        JButton sefinOnerisiButton = new JButton("Favoriler");
        sefinOnerisiButton.setBounds(530, 228, 300, 50);
        sefinOnerisiButton.setFont(new Font("Arial", Font.BOLD, 15));

        sefinOnerisiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Favorites favorites = new Favorites();
                favorites.createAndShowGUI();
            }
        });

        frame.add(sefinOnerisiButton);

        JButton anaYemekButton = new JButton("Ana Yemekler");
        anaYemekButton.setBounds(835, 70, 150, 40);
        anaYemekButton.setFont(new Font("Arial", Font.BOLD, 15));

        anaYemekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                kategoriyeGoreGosterim("Ana Yemek");
            }
        });

        frame.add(anaYemekButton);

        JButton tatliButton = new JButton("Tatlılar");
        tatliButton.setBounds(835, 115, 150, 40);
        tatliButton.setFont(new Font("Arial", Font.BOLD, 15));

        tatliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                kategoriyeGoreGosterim("Tatlı");
            }
        });

        frame.add(tatliButton);

        JButton salataButton = new JButton("Salatalar");
        salataButton.setBounds(835, 160, 150, 40);
        salataButton.setFont(new Font("Arial", Font.BOLD, 15));

        salataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                kategoriyeGoreGosterim("Salata");
            }
        });

        frame.add(salataButton);

        JButton atistirmalikButton = new JButton("Atıştırmalıklar");
        atistirmalikButton.setBounds(835, 205, 150, 40);
        atistirmalikButton.setFont(new Font("Arial", Font.BOLD, 15));

        atistirmalikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                kategoriyeGoreGosterim("Atıştırmalık");
            }
        });

        frame.add(atistirmalikButton);

        JButton iceceklerButton = new JButton("İçecekler");
        iceceklerButton.setBounds(835, 250, 150, 40);
        iceceklerButton.setFont(new Font("Arial", Font.BOLD, 15));

        iceceklerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnaPanel.removeAll();
                kategoriyeGoreGosterim("İçecek");
            }
        });

        frame.add(iceceklerButton);

        JLabel cizgiCekme = new JLabel();
        cizgiCekme.setBounds(995, 20, 20, 270);
        cizgiCekme.setHorizontalAlignment(SwingConstants.CENTER);
        cizgiCekme.setVerticalAlignment(SwingConstants.CENTER);
        cizgiCekme.setForeground(Color.BLACK);
        cizgiCekme.setBackground(new Color(255, 255, 255, 200));
        cizgiCekme.setOpaque(true);
        cizgiCekme.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(filtrelemeSecenekleri);
        frame.add(cizgiCekme);

        ListeAciklama.setBounds(10, 300, 1180, 40);
        ListeAciklama.setHorizontalAlignment(SwingConstants.CENTER);
        ListeAciklama.setVerticalAlignment(SwingConstants.CENTER);
        ListeAciklama.setForeground(Color.BLACK);
        ListeAciklama.setBackground(new Color(255, 255, 255, 200));
        ListeAciklama.setOpaque(true);
        ListeAciklama.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(ListeAciklama);

        AnaPanel.setLayout(new GridLayout(0, 4, 10, 10));

        varsayilanTarifGosterim();

        JLabel listedeAramakIstediginizTarifAdi = new JLabel("Liste İçinde Aramak İstediğiniz Tarif Adı : ");
        listedeAramakIstediginizTarifAdi.setBounds(150, 350, 370, 40);
        listedeAramakIstediginizTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        listedeAramakIstediginizTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        listedeAramakIstediginizTarifAdi.setForeground(Color.BLACK);
        listedeAramakIstediginizTarifAdi.setBackground(new Color(255, 255, 255, 200));
        listedeAramakIstediginizTarifAdi.setOpaque(true);
        listedeAramakIstediginizTarifAdi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(listedeAramakIstediginizTarifAdi);

        dinamikAramaField.setBounds(540, 350, 500, 40);
        dinamikAramaField.setFont(new Font("Arial", Font.BOLD, 16));
        dinamikAramaField.setHorizontalAlignment(SwingConstants.CENTER);

        dinamikAramaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtreleyici();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtreleyici();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtreleyici();
            }
        });

        frame.add(dinamikAramaField);

        JScrollPane scrollPane = new JScrollPane(AnaPanel);
        scrollPane.setBounds(25, 400, 1150, 370);
        frame.add(scrollPane);

        frame.setSize(1200, 820);
        frame.setVisible(true);
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

    private void filtreleyici() {
        String searchText = dinamikAramaField.getText().toLowerCase();

        AnaPanel.removeAll();

        if (!tarifFiltreleyiciler.isEmpty()) {
            for (int i = 0; i < tarifFiltreleyiciler.size(); i++) {
                String tarifAdi = tarifFiltreleyiciler.get(i).getTarifAdi().toLowerCase();

                if (tarifAdi.contains(searchText)) {
                    JPanel singleTarifPanel = new JPanel();
                    singleTarifPanel.setLayout(new BorderLayout());

                    double toplamMaliyet = maliyetMap.get(tarifFiltreleyiciler.get(i).getTarifID());

                    try {
                        File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifFiltreleyiciler.get(i).getTarifID() + ".jpeg");
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

                    JLabel tarifAdiLabel = new JLabel(tarifFiltreleyiciler.get(i).getTarifAdi(), SwingConstants.CENTER);
                    JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + tarifFiltreleyiciler.get(i).getHazirlamaSuresi() + " dakika", SwingConstants.CENTER);
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

                    System.out.println("Tarif : " + tarifFiltreleyiciler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

                    infoPanel.add(tarifAdiLabel);
                    infoPanel.add(tarifHazirlamaSuresiLabel);
                    infoPanel.add(costLabel);

                    singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

                    AnaPanel.add(singleTarifPanel);
                }
            }
        }

        if (!malzemeFiltreleyiciler.isEmpty()) {
            for (int i = 0; i < malzemeFiltreleyiciler.size(); i++) {
                String malzemeAdi = malzemeFiltreleyiciler.get(i).getMalzemeAdi().toLowerCase();

                if (malzemeAdi.contains(searchText)) {
                    JPanel singleTarifPanel = new JPanel();
                    singleTarifPanel.setLayout(new BorderLayout());

                    try {
                        File imageFile = new File("src/main/java/org/example/drawables/material_" + malzemeFiltreleyiciler.get(i).getMalzemeID() + ".jpeg");
                        BufferedImage image;
                        if (imageFile.exists()) {
                            image = ImageIO.read(imageFile);
                        } else {
                            image = ImageIO.read(new File("src/main/java/org/example/drawables/material_default.jpeg"));
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

                    double malzemeToplamMiktari;

                    if (malzemeFiltreleyiciler.get(i).getToplamMiktar() != null) {
                        malzemeToplamMiktari = Double.parseDouble(malzemeFiltreleyiciler.get(i).getToplamMiktar());
                    } else {
                        malzemeToplamMiktari = 0;
                    }

                    JLabel malzemeAdiLabel = new JLabel(malzemeFiltreleyiciler.get(i).getMalzemeAdi(), SwingConstants.CENTER);

                    JLabel malzemeToplamMiktar = new JLabel("Toplam Miktar : " + malzemeToplamMiktari + " " + malzemeFiltreleyiciler.get(i).getMalzemeBirim(), SwingConstants.CENTER);

                    JLabel malzemeBirimFiyat = new JLabel("Malzeme Birim Fiyatı : " + malzemeFiltreleyiciler.get(i).getMalzemeFiyat() + " TL", SwingConstants.CENTER);

                    malzemeAdiLabel.setForeground(Color.WHITE);
                    malzemeAdiLabel.setBackground(Color.BLACK);
                    malzemeAdiLabel.setOpaque(true);
                    malzemeToplamMiktar.setForeground(Color.WHITE);
                    malzemeToplamMiktar.setBackground(Color.BLACK);
                    malzemeToplamMiktar.setOpaque(true);
                    malzemeBirimFiyat.setForeground(Color.WHITE);
                    malzemeBirimFiyat.setBackground(Color.BLACK);
                    malzemeBirimFiyat.setOpaque(true);

                    System.out.println("Malzeme : " + malzemeFiltreleyiciler.get(i).getMalzemeID() + " işleniyor ve getiriliyor.");

                    infoPanel.add(malzemeAdiLabel);
                    infoPanel.add(malzemeToplamMiktar);
                    infoPanel.add(malzemeBirimFiyat);

                    singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

                    AnaPanel.add(singleTarifPanel);
                }
            }
        }

        AnaPanel.revalidate();
        AnaPanel.repaint();
    }

    public void varsayilanTarifGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        ListeAciklama.setText("VARSAYILAN TARİFLER LİSTESİ");
        tarifFiltreleyiciler.addAll(tarifler);

        AnaPanel.removeAll();

        for (int i = 0; i < tarifler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(tarifler.get(i).getTarifID());

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(tarifler.get(i).getTarifAdi(), SwingConstants.CENTER);
            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + tarifler.get(i).getHazirlamaSuresi() + " dakika", SwingConstants.CENTER);
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

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

            infoPanel.add(tarifAdiLabel);
            infoPanel.add(tarifHazirlamaSuresiLabel);
            infoPanel.add(costLabel);

            singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

            AnaPanel.add(singleTarifPanel);
        }

    }

    public void varsayilanMalzemeGosterim() {
        malzemeFiltreleyiciler.clear();
        tarifFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        ListeAciklama.setText("VARSAYILAN MALZEMELER LİSTESİ");
        malzemeFiltreleyiciler.addAll(malzemeler);

        for (int i = 0; i < malzemeler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            try {
                File imageFile = new File("src/main/java/org/example/drawables/material_" + malzemeler.get(i).getMalzemeID() + ".jpeg");
                BufferedImage image;
                if (imageFile.exists()) {
                    image = ImageIO.read(imageFile);
                } else {
                    image = ImageIO.read(new File("src/main/java/org/example/drawables/material_default.jpeg"));
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

            double malzemeToplamMiktari;

            if (malzemeler.get(i).getToplamMiktar() != null) {
                malzemeToplamMiktari = Double.parseDouble(malzemeler.get(i).getToplamMiktar());
            } else {
                malzemeToplamMiktari = 0;
            }

            JLabel malzemeAdiLabel = new JLabel(malzemeler.get(i).getMalzemeAdi(), SwingConstants.CENTER);

            JLabel malzemeToplamMiktar = new JLabel("Toplam Miktar : " + malzemeToplamMiktari + " " + malzemeler.get(i).getMalzemeBirim(), SwingConstants.CENTER);

            JLabel malzemeBirimFiyat = new JLabel("Malzeme Birim Fiyatı : " + malzemeler.get(i).getMalzemeFiyat() + " TL", SwingConstants.CENTER);

            malzemeAdiLabel.setForeground(Color.WHITE);
            malzemeAdiLabel.setBackground(Color.BLACK);
            malzemeAdiLabel.setOpaque(true);
            malzemeToplamMiktar.setForeground(Color.WHITE);
            malzemeToplamMiktar.setBackground(Color.BLACK);
            malzemeToplamMiktar.setOpaque(true);
            malzemeBirimFiyat.setForeground(Color.WHITE);
            malzemeBirimFiyat.setBackground(Color.BLACK);
            malzemeBirimFiyat.setOpaque(true);

            System.out.println("Malzeme : " + malzemeler.get(i).getMalzemeID() + " işleniyor ve getiriliyor.");

            infoPanel.add(malzemeAdiLabel);
            infoPanel.add(malzemeToplamMiktar);
            infoPanel.add(malzemeBirimFiyat);

            singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

            AnaPanel.add(singleTarifPanel);
        }

    }

    public void maliyetsizTarifleriGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        for (int i = 0; i < tarifler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(tarifler.get(i).getTarifID());

            ListeAciklama.setText("MALİYETSİZ TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(tarifler.get(i).getTarifAdi(), SwingConstants.CENTER);

            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(tarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            tarifAdiLabel.setForeground(Color.GREEN);
            tarifAdiLabel.setBackground(Color.BLACK);
            tarifAdiLabel.setOpaque(true);
            tarifHazirlamaSuresiLabel.setForeground(Color.GREEN);
            tarifHazirlamaSuresiLabel.setBackground(Color.BLACK);
            tarifHazirlamaSuresiLabel.setOpaque(true);
            costLabel.setForeground(Color.GREEN);
            costLabel.setBackground(Color.BLACK);
            costLabel.setOpaque(true);

            infoPanel.add(tarifAdiLabel);
            infoPanel.add(tarifHazirlamaSuresiLabel);
            infoPanel.add(costLabel);

            singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

            if (!(maliyetMap.get(tarifler.get(i).getTarifID()) > 0.0)) {
                AnaPanel.add(singleTarifPanel);
                tarifFiltreleyiciler.add(tarifler.get(i));
            }
        }

    }

    public void maliyetliTarifleriGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        for (int i = 0; i < tarifler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(tarifler.get(i).getTarifID());

            ListeAciklama.setText("MALİYETLİ TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(tarifler.get(i).getTarifAdi(), SwingConstants.CENTER);

            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(tarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            tarifAdiLabel.setForeground(Color.RED);
            tarifAdiLabel.setBackground(Color.BLACK);
            tarifAdiLabel.setOpaque(true);
            tarifHazirlamaSuresiLabel.setForeground(Color.RED);
            tarifHazirlamaSuresiLabel.setBackground(Color.BLACK);
            tarifHazirlamaSuresiLabel.setOpaque(true);
            costLabel.setForeground(Color.RED);
            costLabel.setBackground(Color.BLACK);
            costLabel.setOpaque(true);

            infoPanel.add(tarifAdiLabel);
            infoPanel.add(tarifHazirlamaSuresiLabel);
            infoPanel.add(costLabel);

            singleTarifPanel.add(infoPanel, BorderLayout.SOUTH);

            if ((maliyetMap.get(tarifler.get(i).getTarifID()) > 0.0)) {
                AnaPanel.add(singleTarifPanel);
                tarifFiltreleyiciler.add(tarifler.get(i));
            }
        }
    }

    public void maliyetAzalanTarifGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        List<Tarif> sortedTarifler = new ArrayList<>(tarifler);

        sortedTarifler.sort((tarif1, tarif2) -> {
            double maliyet1 = maliyetMap.get(tarif1.getTarifID());
            double maliyet2 = maliyetMap.get(tarif2.getTarifID());
            return Double.compare(maliyet2, maliyet1);
        });

        AnaPanel.removeAll();

        for (int i = 0; i < sortedTarifler.size(); i++) {

            tarifFiltreleyiciler.add(sortedTarifler.get(i));

            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(sortedTarifler.get(i).getTarifID());

            ListeAciklama.setText("AZALAN MALİYETLİ TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + sortedTarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(sortedTarifler.get(i).getTarifAdi(), SwingConstants.CENTER);
            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(sortedTarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);
            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

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
        AnaPanel.revalidate();
        AnaPanel.repaint();
    }

    public void maliyetArtanTarifGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        List<Tarif> sortedTarifler = new ArrayList<>(tarifler);

        sortedTarifler.sort((tarif1, tarif2) -> {
            double maliyet1 = maliyetMap.get(tarif1.getTarifID());
            double maliyet2 = maliyetMap.get(tarif2.getTarifID());
            return Double.compare(maliyet1, maliyet2);
        });

        AnaPanel.removeAll();

        for (int i = 0; i < sortedTarifler.size(); i++) {
            tarifFiltreleyiciler.add(sortedTarifler.get(i));

            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(sortedTarifler.get(i).getTarifID());

            ListeAciklama.setText("ARTAN MALİYETLİ TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + sortedTarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(sortedTarifler.get(i).getTarifAdi(), SwingConstants.CENTER);
            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(sortedTarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);
            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

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
        AnaPanel.revalidate();
        AnaPanel.repaint();
    }

    public void EnHizlidanEnYavasaTarifGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        List<Tarif> sortedTarifler = new ArrayList<>(tarifler);

        sortedTarifler.sort((tarif1, tarif2) -> {
            int hazirlamaSuresi1 = tarif1.getHazirlamaSuresi();
            int hazirlamaSuresi2 = tarif2.getHazirlamaSuresi();
            return Integer.compare(hazirlamaSuresi1, hazirlamaSuresi2);
        });

        AnaPanel.removeAll();

        for (int i = 0; i < sortedTarifler.size(); i++) {

            tarifFiltreleyiciler.add(sortedTarifler.get(i));

            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(sortedTarifler.get(i).getTarifID());

            ListeAciklama.setText("EN HIZLIDAN EN YAVAŞA TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + sortedTarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(sortedTarifler.get(i).getTarifAdi(), SwingConstants.CENTER);
            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(sortedTarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);
            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

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
        AnaPanel.revalidate();
        AnaPanel.repaint();
    }

    public void EnYavastanEnHizliyaTarifGosterim() {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        List<Tarif> sortedTarifler = new ArrayList<>(tarifler);

        sortedTarifler.sort((tarif1, tarif2) -> {
            int hazirlamaSuresi1 = tarif1.getHazirlamaSuresi();
            int hazirlamaSuresi2 = tarif2.getHazirlamaSuresi();
            return Integer.compare(hazirlamaSuresi2, hazirlamaSuresi1);
        });

        AnaPanel.removeAll();

        for (int i = 0; i < sortedTarifler.size(); i++) {

            tarifFiltreleyiciler.add(sortedTarifler.get(i));

            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(sortedTarifler.get(i).getTarifID());

            ListeAciklama.setText("EN YAVAŞTAN EN HIZLIYA TARİFLER LİSTESİ");

            try {
                File imageFile = new File("src/main/java/org/example/drawables/recipe_" + sortedTarifler.get(i).getTarifID() + ".jpeg");
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

            JLabel tarifAdiLabel = new JLabel(sortedTarifler.get(i).getTarifAdi(), SwingConstants.CENTER);
            JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(sortedTarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);
            JLabel costLabel = new JLabel("Maliyet : " + String.format("%.2f", toplamMaliyet) + " TL", SwingConstants.CENTER);

            System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

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
        AnaPanel.revalidate();
        AnaPanel.repaint();
    }

    public void kategoriyeGoreGosterim(String kategori) {
        tarifFiltreleyiciler.clear();
        malzemeFiltreleyiciler.clear();
        dinamikAramaField.setText("");

        for (int i = 0; i < tarifler.size(); i++) {
            JPanel singleTarifPanel = new JPanel();
            singleTarifPanel.setLayout(new BorderLayout());

            double toplamMaliyet = maliyetMap.get(tarifler.get(i).getTarifID());

            ListeAciklama.setText(kategori.toUpperCase() + "LİSTESİ");

            if (tarifler.get(i).getKategori().equalsIgnoreCase(kategori)) {
                tarifFiltreleyiciler.add(tarifler.get(i));
                try {
                    File imageFile = new File("src/main/java/org/example/drawables/recipe_" + tarifler.get(i).getTarifID() + ".jpeg");
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

                JLabel tarifAdiLabel = new JLabel(tarifler.get(i).getTarifAdi(), SwingConstants.CENTER);

                JLabel tarifHazirlamaSuresiLabel = new JLabel("Hazırlama Süresi : " + String.valueOf(tarifler.get(i).getHazirlamaSuresi()) + " dakika", SwingConstants.CENTER);

                System.out.println("Tarif : " + tarifler.get(i).getTarifID() + " işleniyor ve getiriliyor.");

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
