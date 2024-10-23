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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RecipeInfoPage {

    private Connection connection;
    private JFrame frame;

    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();
    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private List<Integer> secilenMalzemeIdleri = new ArrayList<Integer>();
    private List<Double> secilenMalzemeMiktarlari = new ArrayList<Double>();
    private List<Integer> kullanilanMalzemeIdleri = new ArrayList<Integer>();
    private List<Double> kullanilanMalzemeMiktarlari = new ArrayList<Double>();
    private HashMap<Integer, Double> maliyetMap = new HashMap<Integer, Double>();
    private HashMap<Integer, Double> toplamMaliyetMap = new HashMap<Integer, Double>();

    private JButton geriButton = new JButton("Geri");
    private JButton detaylariGosterButton = new JButton("Detayları Göster");

    private JTextField tarifDetayiGetirici = new JTextField(null);
    JPanel resimPanel = new JPanel();
    private JTextField tarifAdiField = new JTextField(null);
    private JTextField tarifKategoriField = new JTextField(null);
    private JTextField tarifMaliyetField = new JTextField(null);
    private JTextField gerekenlerinMaliyetiField = new JTextField(null);
    private JTextArea tarifMalzemelerArea = new JTextArea();
    private JTextField tarifHazirlamaSuresiField = new JTextField(null);
    private JTextArea tarifTalimatlarArea = new JTextArea();
    private JScrollPane jScrollPaneMalzemeler = new JScrollPane(null);
    private JScrollPane jScrollPaneTalimatlar = new JScrollPane(null);

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

        JLabel jLabelTarifDetayiGorme = new JLabel("TARİF DETAYI GÖRME");
        jLabelTarifDetayiGorme.setBounds(200, 70, 400, 40);
        jLabelTarifDetayiGorme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayiGorme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayiGorme.setForeground(Color.BLACK);
        jLabelTarifDetayiGorme.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifDetayiGorme.setOpaque(true);
        jLabelTarifDetayiGorme.setFont(new Font("Arial", Font.BOLD, 32));

        frame.add(jLabelTarifDetayiGorme);

        JLabel jLabelTarifDetayiAdi = new JLabel("Tarif Adı : ");
        jLabelTarifDetayiAdi.setBounds(200, 130, 190, 40);
        jLabelTarifDetayiAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayiAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayiAdi.setForeground(Color.BLACK);
        jLabelTarifDetayiAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifDetayiAdi.setOpaque(true);
        jLabelTarifDetayiAdi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelTarifDetayiAdi);

        tarifDetayiGetirici.setBounds(400,130,200,40);
        tarifDetayiGetirici.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(tarifDetayiGetirici);

        detaylariGosterButton.setBounds(300,180,200,45);
        detaylariGosterButton.setFont(new Font("Arial",Font.BOLD,16));

        resimPanel.setBounds(50, 305, 250, 200);
        frame.add(resimPanel);

        detaylariGosterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int getirilecekTarifID = -1;
                String tarifAdi = "";
                String kategori = "";
                double maliyet = 0.0;
                double gerekenMaliyet = 0.0;
                int hazirlamaSuresi = 0;
                String talimatlar = "";

                if(tarifDetayiGetirici.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Tarif adını boş bırakamazsınız.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for(Tarif tarif : tarifler){
                    if(tarif.getTarifAdi().toLowerCase(Locale.ROOT).equals(tarifDetayiGetirici.getText().toLowerCase())){
                       getirilecekTarifID = tarif.getTarifID();
                       tarifAdi = tarif.getTarifAdi();
                       kategori = tarif.getKategori();
                       maliyet = toplamMaliyetMap.get(tarif.getTarifID());
                       gerekenMaliyet = maliyetMap.get(tarif.getTarifID());
                       hazirlamaSuresi = tarif.getHazirlamaSuresi();
                       talimatlar = tarif.getTalimatlar();
                       secilenTarifeGoreMalzemeler(tarif.getTarifID());
                    }
                }

                if(getirilecekTarifID == -1){
                    JOptionPane.showMessageDialog(null, "Girdiğiniz tarif adı tarif listesinde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    File imageFile = new File("src/main/java/org/example/drawables/recipe_" + getirilecekTarifID +".jpeg");
                    BufferedImage image;
                    if (imageFile.exists()) {
                        image = ImageIO.read(imageFile);
                    } else {
                        image = ImageIO.read(new File("src/main/java/org/example/drawables/recipe_default.jpeg"));
                    }

                    int newWidth = 250;
                    int newHeight = 200;
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
                    BufferedImage lowQualityImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = lowQualityImage.createGraphics();
                    g2d.drawImage(scaledImage, 0, 0, null);
                    g2d.dispose();

                    resimPanel.removeAll();
                    JLabel imageLabel = new JLabel(new ImageIcon(lowQualityImage));
                    resimPanel.add(imageLabel, BorderLayout.CENTER);

                    resimPanel.revalidate();
                    resimPanel.repaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                tarifAdiField.setText(tarifAdi);
                tarifKategoriField.setText(kategori);
                tarifMaliyetField.setText(String.format("%.2f", maliyet) + " TL");
                gerekenlerinMaliyetiField.setText(String.format("%.2f", gerekenMaliyet) + " TL");
                tarifHazirlamaSuresiField.setText(hazirlamaSuresi + " dakika");
                tarifTalimatlarArea.setText("");
                tarifTalimatlarArea.setText(talimatlar);

                StringBuilder builder = new StringBuilder();

                for(int i = 0; i < secilenMalzemeIdleri.size() ; i++){
                    for(Malzeme malzeme : malzemeler){
                        if(malzeme.getMalzemeID() == secilenMalzemeIdleri.get(i)){
                            builder.append(secilenMalzemeMiktarlari.get(i)).append(" ").append(malzeme.getMalzemeBirim())
                                    .append(" ").append(malzeme.getMalzemeAdi()).append("\n");
                        }
                    }
                }

                tarifMalzemelerArea.setText(builder.toString());
            }
        });

        frame.add(detaylariGosterButton);

        JLabel jLabelTarifDetayi = new JLabel("TARİF DETAYI");
        jLabelTarifDetayi.setBounds(50, 245, 700, 40);
        jLabelTarifDetayi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifDetayi.setForeground(Color.BLACK);
        jLabelTarifDetayi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifDetayi.setOpaque(true);
        jLabelTarifDetayi.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(jLabelTarifDetayi);

        JLabel jLabelTarifAdi = new JLabel("Tarif Adı : ");
        jLabelTarifAdi.setBounds(320, 308, 150, 40);
        jLabelTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setForeground(Color.BLACK);
        jLabelTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifAdi.setOpaque(true);
        jLabelTarifAdi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelTarifAdi);

        tarifAdiField.setBounds(480,306,270,45);
        tarifAdiField.setFont(new Font("Arial",Font.BOLD,16));
        tarifAdiField.setEditable(false);

        frame.add(tarifAdiField);

        JLabel jLabelKategori = new JLabel("Kategori : ");
        jLabelKategori.setBounds(320, 358, 150, 40);
        jLabelKategori.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelKategori.setVerticalAlignment(SwingConstants.CENTER);
        jLabelKategori.setForeground(Color.BLACK);
        jLabelKategori.setBackground(new Color(255, 255, 255, 200));
        jLabelKategori.setOpaque(true);
        jLabelKategori.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelKategori);

        tarifKategoriField.setBounds(480,356,270,45);
        tarifKategoriField.setFont(new Font("Arial",Font.BOLD,16));
        tarifKategoriField.setEditable(false);

        frame.add(tarifKategoriField);

        JLabel jLabelMaliyet = new JLabel("Maliyet : ");
        jLabelMaliyet.setBounds(320, 408, 150, 40);
        jLabelMaliyet.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMaliyet.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMaliyet.setForeground(Color.BLACK);
        jLabelMaliyet.setBackground(new Color(255, 255, 255, 200));
        jLabelMaliyet.setOpaque(true);
        jLabelMaliyet.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelMaliyet);

        tarifMaliyetField.setBounds(480,406,270,45);
        tarifMaliyetField.setFont(new Font("Arial",Font.BOLD,16));
        tarifMaliyetField.setEditable(false);

        frame.add(tarifMaliyetField);

        JLabel jLabelGerekenlerinMaliyeti = new JLabel("Gereken Maliyet : ");
        jLabelGerekenlerinMaliyeti.setBounds(320, 460, 150, 40);
        jLabelGerekenlerinMaliyeti.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelGerekenlerinMaliyeti.setVerticalAlignment(SwingConstants.CENTER);
        jLabelGerekenlerinMaliyeti.setForeground(Color.BLACK);
        jLabelGerekenlerinMaliyeti.setBackground(new Color(255, 255, 255, 200));
        jLabelGerekenlerinMaliyeti.setOpaque(true);
        jLabelGerekenlerinMaliyeti.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelGerekenlerinMaliyeti);

        gerekenlerinMaliyetiField.setBounds(480,460,270,45);
        gerekenlerinMaliyetiField.setFont(new Font("Arial",Font.BOLD,16));
        gerekenlerinMaliyetiField.setEditable(false);

        frame.add(gerekenlerinMaliyetiField);

        JLabel jLabelHazirlamaSuresi = new JLabel("Hazırlama Süresi : ");
        jLabelHazirlamaSuresi.setBounds(50, 520, 150, 40);
        jLabelHazirlamaSuresi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelHazirlamaSuresi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelHazirlamaSuresi.setForeground(Color.BLACK);
        jLabelHazirlamaSuresi.setBackground(new Color(255, 255, 255, 200));
        jLabelHazirlamaSuresi.setOpaque(true);
        jLabelHazirlamaSuresi.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelHazirlamaSuresi);

        tarifHazirlamaSuresiField.setBounds(208,517,265,45);
        tarifHazirlamaSuresiField.setFont(new Font("Arial",Font.BOLD,16));
        tarifHazirlamaSuresiField.setEditable(false);

        frame.add(tarifHazirlamaSuresiField);

        JLabel jLabelMalzemeler = new JLabel("Malzemeler : ");
        jLabelMalzemeler.setBounds(50, 570, 150, 40);
        jLabelMalzemeler.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeler.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeler.setForeground(Color.BLACK);
        jLabelMalzemeler.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeler.setOpaque(true);
        jLabelMalzemeler.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelMalzemeler);

        tarifMalzemelerArea.setEditable(false);
        jScrollPaneMalzemeler = new JScrollPane(tarifMalzemelerArea);
        jScrollPaneMalzemeler.setBounds(210,570,540,80);
        jScrollPaneMalzemeler.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneMalzemeler.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneMalzemeler.setFont(new Font("Arial",Font.BOLD,16));

        frame.add(jScrollPaneMalzemeler);

        JLabel jLabelTalimatlar = new JLabel("Talimatlar : ");
        jLabelTalimatlar.setBounds(50, 680, 150, 40);
        jLabelTalimatlar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTalimatlar.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTalimatlar.setForeground(Color.BLACK);
        jLabelTalimatlar.setBackground(new Color(255, 255, 255, 200));
        jLabelTalimatlar.setOpaque(true);
        jLabelTalimatlar.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(jLabelTalimatlar);

        tarifTalimatlarArea.setEditable(false);
        jScrollPaneTalimatlar = new JScrollPane(tarifTalimatlarArea);
        jScrollPaneTalimatlar.setBounds(210,678,540,80);
        jScrollPaneTalimatlar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneTalimatlar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneTalimatlar.setFont(new Font("Arial",Font.BOLD,16));

        frame.add(jScrollPaneTalimatlar);

        frame.setSize(800, 820);
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

            double gerekenMaliyet = 0;
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
                            gerekenMaliyet += (kullanilanMalzemeMiktarlari.get(j) - malzemeToplamMiktar) * malzeme.getMalzemeFiyat();
                        }

                        toplamMaliyet += kullanilanMalzemeMiktarlari.get(j)*malzeme.getMalzemeFiyat();

                    }
                }
            }

            maliyetMap.put(tarifler.get(i).getTarifID(), gerekenMaliyet);
            toplamMaliyetMap.put(tarifler.get(i).getTarifID(), toplamMaliyet);

        }
    }

    public void secilenTarifeGoreMalzemeler(int tarifID){

        secilenMalzemeIdleri.clear();
        secilenMalzemeMiktarlari.clear();

        try {
            String query = "SELECT * FROM tarifmalzeme WHERE tarifid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1,tarifID);
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

                secilenMalzemeIdleri.add(malzemeID);
                secilenMalzemeMiktarlari.add(malzemeToplamMiktari);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
