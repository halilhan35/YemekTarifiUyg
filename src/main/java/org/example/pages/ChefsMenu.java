package org.example.pages;

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
import java.util.ArrayList;
import java.util.HashMap;

public class ChefsMenu {

    private ArrayList<Tarif> tarifler = new ArrayList<Tarif>();
    private HashMap<Integer, Double> maliyetMap = new HashMap<Integer, Double>();


    private Connection connection;
    private JFrame frame;

    private JButton geriButton = new JButton("Geri");



    public void createAndShowGUI() {

        connection = ConnectDB.getConnection();

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
        jLabelFavoriler.setBounds(200, 80, 400, 40);
        jLabelFavoriler.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFavoriler.setVerticalAlignment(SwingConstants.CENTER);
        jLabelFavoriler.setForeground(Color.BLACK);
        jLabelFavoriler.setBackground(new Color(255, 255, 255, 200));
        jLabelFavoriler.setOpaque(true);
        jLabelFavoriler.setFont(new Font("Arial", Font.BOLD, 24));

        frame.add(jLabelFavoriler);

        frame.setSize(800, 800);
        frame.setVisible(true);

    }

}
