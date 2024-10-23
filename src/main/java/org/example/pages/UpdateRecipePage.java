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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateRecipePage {

    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();
    private ArrayList<Malzeme> malzemeler = new ArrayList<Malzeme>();
    private ArrayList<String> tarifAdlari = new ArrayList<String>();
    private List<Integer> kullanilanMalzemeIdleri = new ArrayList<>();
    private ArrayList<String> kullanilanMalzemeMiktarlari = new ArrayList<String>();

    private Connection connection;
    private JFrame frame = new JFrame("Food System App");

    private JButton geriButton = new JButton("Geri");
    private JButton bilgiGetirButton = new JButton("Bilgileri Getir");
    private JButton degisiklikleriKaydetButton = new JButton("Değişiklikleri Kaydet");
    private JButton malzemeyiEkleButton = new JButton("Malzemeyi Ekle");
    private JButton malzemeyiSilButton = new JButton("Malzemeyi Sil");

    private JTextArea tarifListeAlani = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(tarifListeAlani);

    private JTextField tarifAdiGetiriciField = new JTextField(null);
    private JTextField tarifAramaField = new JTextField(null);
    private JTextField tarifAdiField = new JTextField(null);
    private JTextField tarifKategoriField = new JTextField(null);
    private JTextArea malzemeText = new JTextArea();
    private JScrollPane scrollPaneYatay = new JScrollPane(malzemeText);
    private JTextField tarifHazirlamaSuresiField = new JTextField(null);
    private JTextField tarifTalimatlarField = new JTextField(null);
    private JTextField malzemeEklemeAdiField = new JTextField(null);
    private JTextField malzemeEklemeMiktarField = new JTextField(null);
    private JTextField malzemeSilmeAdiField = new JTextField(null);

    private int girilenTarifID;

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

        JLabel jLabelTarifGuncelleme = new JLabel("TARİF GÜNCELLEME");
        jLabelTarifGuncelleme.setBounds(200, 50, 400, 40);
        jLabelTarifGuncelleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifGuncelleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifGuncelleme.setForeground(Color.BLACK);
        jLabelTarifGuncelleme.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifGuncelleme.setOpaque(true);
        jLabelTarifGuncelleme.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel jLabelTarifAdiBilgisi = new JLabel("Tarif Adı : ");
        jLabelTarifAdiBilgisi.setBounds(200, 110, 150, 40);
        jLabelTarifAdiBilgisi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifAdiBilgisi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifAdiBilgisi.setForeground(Color.BLACK);
        jLabelTarifAdiBilgisi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifAdiBilgisi.setOpaque(true);
        jLabelTarifAdiBilgisi.setFont(new Font("Arial", Font.BOLD, 15));

        tarifAdiGetiriciField.setBounds(370, 110, 230, 40);
        tarifAdiGetiriciField.setFont(new Font("Arial", Font.BOLD, 15));

        bilgiGetirButton.setBounds(620, 105, 150, 50);
        bilgiGetirButton.setFont(new Font("Arial", Font.BOLD, 15));

        bilgiGetirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String girilenTarifAdi = tarifAdiGetiriciField.getText().toString();

                boolean tarifBulundu = false;

                for (Tarif tarif : tarifler) {
                    if (tarif.getTarifAdi().equalsIgnoreCase(girilenTarifAdi)) {
                        tarifBulundu = true;
                        girilenTarifID = tarif.getTarifID();

                        tarifAdiField.setText(tarif.getTarifAdi());
                        tarifKategoriField.setText(tarif.getKategori());
                        kullanilanMalzemeIdleriListesiYapma(tarif.getTarifID());
                        kullanilanMalzemelerListesiYapma();
                        tarifHazirlamaSuresiField.setText(Integer.toString(tarif.getHazirlamaSuresi()));
                        tarifTalimatlarField.setText(tarif.getTalimatlar());
                    }
                }

                if (!tarifBulundu) {
                    JOptionPane.showMessageDialog(null, "Yazdığınız tarif, tarif listesinde bulunmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel jLabelTarifListesi = new JLabel("TARİF LİSTESİ");
        jLabelTarifListesi.setBounds(200, 175, 400, 40);
        jLabelTarifListesi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifListesi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifListesi.setForeground(Color.BLACK);
        jLabelTarifListesi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifListesi.setOpaque(true);
        jLabelTarifListesi.setFont(new Font("Arial", Font.BOLD, 24));

        tarifAramaField.setBounds(200, 225, 400, 40);
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

        tarifListeAlani.setBounds(200, 275, 400, 90);
        tarifListeAlani.setLineWrap(true);
        tarifListeAlani.setWrapStyleWord(true);
        tarifListeAlani.setEditable(false);
        tarifListeAlani.setFont(new Font("Arial", Font.BOLD, 15));

        for (Tarif tarif : tarifler) {
            tarifListeAlani.append(tarif.getTarifAdi() + "\n\n");
        }

        scrollPane.setBounds(200, 275, 400, 90);
        scrollPane.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelTarifBilgileri = new JLabel("TARİF BİLGİLERİ");
        jLabelTarifBilgileri.setBounds(30, 375, 740, 40);
        jLabelTarifBilgileri.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifBilgileri.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifBilgileri.setForeground(Color.BLACK);
        jLabelTarifBilgileri.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifBilgileri.setOpaque(true);
        jLabelTarifBilgileri.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel jLabelTarifAdi = new JLabel("Tarif Adı : ");
        jLabelTarifAdi.setBounds(30, 425, 150, 40);
        jLabelTarifAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifAdi.setForeground(Color.BLACK);
        jLabelTarifAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifAdi.setOpaque(true);
        jLabelTarifAdi.setFont(new Font("Arial", Font.BOLD, 15));

        tarifAdiField.setBounds(200, 425, 175, 40);
        tarifAdiField.setFont(new Font("Arial", Font.BOLD, 15));
        tarifAdiField.setEditable(false);

        JLabel jLabelTarifKategori = new JLabel("Kategori : ");
        jLabelTarifKategori.setBounds(30, 485, 150, 40);
        jLabelTarifKategori.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifKategori.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifKategori.setForeground(Color.BLACK);
        jLabelTarifKategori.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifKategori.setOpaque(true);
        jLabelTarifKategori.setFont(new Font("Arial", Font.BOLD, 15));

        tarifKategoriField.setBounds(200, 485, 175, 40);
        tarifKategoriField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelmalzeme = new JLabel("Malzemeler : ");
        jLabelmalzeme.setBounds(30, 545, 150, 40);
        jLabelmalzeme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelmalzeme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelmalzeme.setForeground(Color.BLACK);
        jLabelmalzeme.setBackground(new Color(255, 255, 255, 200));
        jLabelmalzeme.setOpaque(true);
        jLabelmalzeme.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeText.setBounds(200, 540, 180, 50);
        malzemeText.setFont(new Font("Arial", Font.BOLD, 15));
        malzemeText.setEditable(false);

        scrollPaneYatay.setBounds(200, 540, 180, 50);
        scrollPaneYatay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneYatay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel jLabelTarifHazirlamaSuresi = new JLabel("Hazırlama Süresi : ");
        jLabelTarifHazirlamaSuresi.setBounds(30, 610, 150, 40);
        jLabelTarifHazirlamaSuresi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifHazirlamaSuresi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifHazirlamaSuresi.setForeground(Color.BLACK);
        jLabelTarifHazirlamaSuresi.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifHazirlamaSuresi.setOpaque(true);
        jLabelTarifHazirlamaSuresi.setFont(new Font("Arial", Font.BOLD, 15));

        tarifHazirlamaSuresiField.setBounds(200, 610, 175, 40);
        tarifHazirlamaSuresiField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelTarifTalimatlar = new JLabel("Talimatlar : ");
        jLabelTarifTalimatlar.setBounds(30, 670, 150, 40);
        jLabelTarifTalimatlar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTarifTalimatlar.setVerticalAlignment(SwingConstants.CENTER);
        jLabelTarifTalimatlar.setForeground(Color.BLACK);
        jLabelTarifTalimatlar.setBackground(new Color(255, 255, 255, 200));
        jLabelTarifTalimatlar.setOpaque(true);
        jLabelTarifTalimatlar.setFont(new Font("Arial", Font.BOLD, 15));

        tarifTalimatlarField.setBounds(200, 670, 175, 40);
        tarifTalimatlarField.setFont(new Font("Arial", Font.BOLD, 15));

        degisiklikleriKaydetButton.setBounds(100, 720, 200, 50);
        degisiklikleriKaydetButton.setFont(new Font("Arial", Font.BOLD, 15));

        degisiklikleriKaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String yeniKategori = tarifKategoriField.getText().toString();
                String yeniTalimatlar = tarifTalimatlarField.getText().toString();
                int yeniHazirlamaSuresi;

                if (yeniKategori.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kategori adını boş bırakamazsın.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (yeniTalimatlar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Talimatları boş bırakamazsın.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    yeniHazirlamaSuresi = Integer.parseInt(tarifHazirlamaSuresiField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Tarif hazırlama süresi bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateQuery = "UPDATE tarifler SET kategori = ?, hazirlamasuresi = ?, talimatlar = ? WHERE tarifid = ?";

                try {

                    PreparedStatement stmt = connection.prepareStatement(updateQuery);

                    stmt.setString(1, yeniKategori);
                    stmt.setInt(2, yeniHazirlamaSuresi);
                    stmt.setString(3, yeniTalimatlar);
                    stmt.setInt(4, girilenTarifID);

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Tarif başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        UpdateRecipePage updateRecipePage = new UpdateRecipePage();
                        updateRecipePage.createAndShowGUI();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JLabel jLabelMalzemeEkleme = new JLabel("TARİFE MALZEME EKLEME");
        jLabelMalzemeEkleme.setBounds(420, 425, 350, 40);
        jLabelMalzemeEkleme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEkleme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeEkleme.setForeground(Color.BLACK);
        jLabelMalzemeEkleme.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeEkleme.setOpaque(true);
        jLabelMalzemeEkleme.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelEklenecekMalzemeAdi = new JLabel("Malzeme Adı : ");
        jLabelEklenecekMalzemeAdi.setBounds(420, 475, 150, 40);
        jLabelEklenecekMalzemeAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelEklenecekMalzemeAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelEklenecekMalzemeAdi.setForeground(Color.BLACK);
        jLabelEklenecekMalzemeAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelEklenecekMalzemeAdi.setOpaque(true);
        jLabelEklenecekMalzemeAdi.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeEklemeAdiField.setBounds(590, 475, 175, 40);
        malzemeEklemeAdiField.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelEklenecekMalzemeMiktari = new JLabel("Kullanılacak Miktar : ");
        jLabelEklenecekMalzemeMiktari.setBounds(420, 525, 150, 40);
        jLabelEklenecekMalzemeMiktari.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelEklenecekMalzemeMiktari.setVerticalAlignment(SwingConstants.CENTER);
        jLabelEklenecekMalzemeMiktari.setForeground(Color.BLACK);
        jLabelEklenecekMalzemeMiktari.setBackground(new Color(255, 255, 255, 200));
        jLabelEklenecekMalzemeMiktari.setOpaque(true);
        jLabelEklenecekMalzemeMiktari.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeEklemeMiktarField.setBounds(590, 525, 175, 40);
        malzemeEklemeMiktarField.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeyiEkleButton.setBounds(505, 570, 175, 50);
        malzemeyiEkleButton.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeyiEkleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tarifAdiField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Önce bir tarif bilgisi göstermelisiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean malzemeKontrol = false;
                boolean malzemeKontrol2 = false;
                int malzemeId = -1;

                for (Malzeme malzeme : malzemeler) {
                    if (malzeme.getMalzemeAdi().equalsIgnoreCase(malzemeEklemeAdiField.getText().toString())) {
                        malzemeKontrol = true;
                        malzemeId = malzeme.getMalzemeID();
                    } else if (kullanilanMalzemeIdleri.contains(malzemeId)) {
                        malzemeKontrol2 = true;
                    }
                }

                if (malzemeKontrol2) {
                    JOptionPane.showMessageDialog(null, "Girdiğiniz malzeme, zaten tarifte bulunuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!malzemeKontrol) {
                    JOptionPane.showMessageDialog(null, "Girdiğiniz malzeme, malzeme listesinde bulunmuyor. Malzeme listesine göz atın.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double malzemeEklemeMiktar = Integer.parseInt(malzemeEklemeMiktarField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Malzemenin miktarı bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query = "INSERT INTO tarifmalzeme (tarifid, malzemeid, malzememiktar) VALUES (?, ?, ?)";

                try {
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, girilenTarifID);
                    stmt.setInt(2, malzemeId);
                    stmt.setDouble(3, Double.parseDouble(malzemeEklemeMiktarField.getText().toString()));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Malzeme tarife başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    UpdateRecipePage updateRecipePage = new UpdateRecipePage();
                    updateRecipePage.createAndShowGUI();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanına kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JLabel jLabelMalzemeSilme = new JLabel("TARİFTEN MALZEME SİLME");
        jLabelMalzemeSilme.setBounds(420, 625, 350, 40);
        jLabelMalzemeSilme.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMalzemeSilme.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMalzemeSilme.setForeground(Color.BLACK);
        jLabelMalzemeSilme.setBackground(new Color(255, 255, 255, 200));
        jLabelMalzemeSilme.setOpaque(true);
        jLabelMalzemeSilme.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel jLabelSilinecekMalzemeAdi = new JLabel("Malzeme Adı : ");
        jLabelSilinecekMalzemeAdi.setBounds(420, 675, 150, 40);
        jLabelSilinecekMalzemeAdi.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelSilinecekMalzemeAdi.setVerticalAlignment(SwingConstants.CENTER);
        jLabelSilinecekMalzemeAdi.setForeground(Color.BLACK);
        jLabelSilinecekMalzemeAdi.setBackground(new Color(255, 255, 255, 200));
        jLabelSilinecekMalzemeAdi.setOpaque(true);
        jLabelSilinecekMalzemeAdi.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeSilmeAdiField.setBounds(590, 675, 175, 40);
        malzemeSilmeAdiField.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeyiSilButton.setBounds(505, 720, 175, 50);
        malzemeyiSilButton.setFont(new Font("Arial", Font.BOLD, 15));

        malzemeyiSilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String silinecekMalzemeAdi = malzemeSilmeAdiField.getText().toString();

                int silinecekMalzemeID = -1;

                for (Malzeme malzeme : malzemeler) {
                    if (malzeme.getMalzemeAdi().equalsIgnoreCase(silinecekMalzemeAdi)) {
                        silinecekMalzemeID = malzeme.getMalzemeID();
                    }
                }

                if (silinecekMalzemeID == -1) {
                    JOptionPane.showMessageDialog(null, "Girdiğiniz malzeme adı tarifte bulunmuyor!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String query = "DELETE FROM tarifmalzeme WHERE tarifid = ? AND malzemeid = ?";

                try {
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, girilenTarifID);
                    stmt.setInt(2, silinecekMalzemeID);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Malzeme tariften başarıyla silindi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        UpdateRecipePage updateRecipePage = new UpdateRecipePage();
                        updateRecipePage.createAndShowGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "Malzeme tarif içinde bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Malzeme tariften silinirken bir veritabanı hatası oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        frame.add(geriButton);
        frame.add(jLabelTarifGuncelleme);
        frame.add(jLabelTarifAdiBilgisi);
        frame.add(tarifAdiGetiriciField);
        frame.add(bilgiGetirButton);
        frame.add(tarifAramaField);
        frame.add(jLabelTarifListesi);
        frame.add(scrollPane);
        frame.add(jLabelTarifBilgileri);
        frame.add(jLabelTarifAdi);
        frame.add(tarifAdiField);
        frame.add(jLabelTarifKategori);
        frame.add(tarifKategoriField);
        frame.add(jLabelmalzeme);
        frame.add(scrollPaneYatay);
        frame.add(jLabelTarifHazirlamaSuresi);
        frame.add(tarifHazirlamaSuresiField);
        frame.add(jLabelTarifTalimatlar);
        frame.add(tarifTalimatlarField);
        frame.add(degisiklikleriKaydetButton);
        frame.add(jLabelMalzemeEkleme);
        frame.add(jLabelEklenecekMalzemeAdi);
        frame.add(malzemeEklemeAdiField);
        frame.add(jLabelEklenecekMalzemeMiktari);
        frame.add(malzemeEklemeMiktarField);
        frame.add(malzemeyiEkleButton);
        frame.add(jLabelMalzemeSilme);
        frame.add(jLabelSilinecekMalzemeAdi);
        frame.add(malzemeSilmeAdiField);
        frame.add(malzemeyiSilButton);

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
                tarifAdlari.add(tarifAdi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void malzemelerListesiYapma() {
        try {
            String query = "SELECT * FROM malzemeler";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int malzemeId = rs.getInt("malzemeid");
                String malzemeAdi = rs.getString("malzemeadi");
                String malzemeToplamMiktar = rs.getString("toplammiktar");
                String malzemeBirim = rs.getString("malzemebirim");
                Double malzemeBirimFiyat = rs.getDouble("birimfiyat");

                Malzeme malzeme = new Malzeme(malzemeId, malzemeAdi, malzemeToplamMiktar, malzemeBirim, malzemeBirimFiyat, null);
                malzemeler.add(malzeme);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kullanilanMalzemeIdleriListesiYapma(int tarifId) {
        String query = "SELECT * FROM tarifmalzeme WHERE tarifid = ?";

        kullanilanMalzemeIdleri.clear();
        kullanilanMalzemeMiktarlari.clear();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, tarifId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int malzemeId = rs.getInt("malzemeid");
                String malzemeMiktari = rs.getString("malzememiktar");
                kullanilanMalzemeIdleri.add(malzemeId);
                kullanilanMalzemeMiktarlari.add(malzemeMiktari);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void kullanilanMalzemelerListesiYapma() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < kullanilanMalzemeIdleri.size(); i++) {
            for (Malzeme malzeme : malzemeler) {
                if (malzeme.getMalzemeID() == kullanilanMalzemeIdleri.get(i)) {
                    stringBuilder.append(kullanilanMalzemeMiktarlari.get(i))
                            .append(" ")
                            .append(malzeme.getMalzemeBirim())
                            .append(" ")
                            .append(malzeme.getMalzemeAdi())
                            .append(", ");
                }
            }
        }

        if (stringBuilder.length() > 2) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

        malzemeText.setText(stringBuilder.toString());
    }

    private void tarifArama() {
        String searchTerm = tarifAramaField.getText().toLowerCase();
        StringBuilder results = new StringBuilder();

        for (Tarif tarif : tarifler) {
            if (tarif.getTarifAdi().toLowerCase().contains(searchTerm)) {
                results.append(tarif.getTarifAdi()).append("\n\n");
            }
        }

        tarifListeAlani.setText(results.toString());
    }

}
