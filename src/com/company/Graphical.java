package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Hashtable;
import java.util.Random;

class Graphical extends javax.swing.JFrame {

    private static final int TOTAL_DEGREES = 360;
    private final double screenX0;
    private final double screenY0;
    private static Integer lineNumber = 10;
    private static Integer maxLineNumber = 60;
    private double spaceBetweenLines = maximumSpace / lineNumber;
    private static double maximumSpace;
    private Color defaultPlotAreaBackgroundColor = new Color(0, 128, 128);
    private JPanel plotArea;
    private JPanel sliderArea;
    private JPanel dataInfoArea;
    private JSlider linesSlider;
    private JLabel linesLabel;
    private JLabel rightFreeArea;
    private JFrame colorPalette;
    private JButton colorPaletteButton;
    private JButton bgPaletteButton;
    private static Color gridColor = Color.green;

    private EventHandlerFormChanger ehfc;

    public Graphical() {
        this.setBackground(defaultPlotAreaBackgroundColor);                         //setto la menu bar del colore sopra
        this.getContentPane().setBackground(defaultPlotAreaBackgroundColor);        //setto lo sfondo del frame di quel colore

        ehfc = new EventHandlerFormChanger();

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        javax.swing.JMenuBar jmb = new javax.swing.JMenuBar();
        JMenu jmFile = new JMenu("Help");
        JMenuItem jItem = new JMenuItem("Meme");
        jItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(jItem, "Meme", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        jmFile.add(jItem);
        jmb.add(jmFile);
        this.setJMenuBar(jmb);

        plotArea = new JPanel();
        sliderArea = new JPanel();
        rightFreeArea = new JLabel();
        colorPaletteButton = new JButton("Click me for colors");
        colorPalette = new JFrame("Double click on a tile to select color");

        rightFreeArea.setVisible(true);
        linesSlider = initSlider();
        setDataInfoArea(defaultPlotAreaBackgroundColor);
        setColorPaletteButton();
        plotArea.setDoubleBuffered(true);
        plotArea.setLayout(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1280, 720);

        plotArea.setBounds(0, 60, 1280, 660);
        rightFreeArea.setBounds(960, 0, 320, 60);
        sliderArea.setBounds(480, 0, 320, 60);

        this.screenX0 = (double) this.getWidth() / 2;
        this.screenY0 = (double) this.getHeight() / 2 + (double) sliderArea.getHeight() / 2;
        setTitle("Memedetta drawing");

        maximumSpace = (int) (plotArea.getHeight() / 2.3);

        sliderArea.add(linesSlider);
        rightFreeArea.setBackground(Color.red);
        sliderArea.setBackground(defaultPlotAreaBackgroundColor);
        getContentPane().add(sliderArea);
        getContentPane().add(plotArea);
        getContentPane().add(dataInfoArea);
        getContentPane().add(rightFreeArea);
        getContentPane().add(colorPaletteButton);

        setResizable(false);
        plotArea.setBackground(defaultPlotAreaBackgroundColor);
    }


    private void setColorPaletteButton() {
        // colorPaletteButton.setBorderPainted(true);
        colorPaletteButton.setFont(new Font("Futura", 0, 15));
        colorPaletteButton.setBackground(new Color(9, 67, 56));
        colorPaletteButton.setBounds(960, 20, 200, 20);
        colorPaletteButton.setVisible(true);
        colorPaletteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectionButtonPressed();
            }
        });
    }

    private void selectionButtonPressed() {
        colorPalette.setBackground(defaultPlotAreaBackgroundColor);
        colorPalette.setDefaultCloseOperation(HIDE_ON_CLOSE);
        colorPalette.setSize(500, 500);
        colorPalette.setResizable(false);
        Random r = new Random();
        for (int i = 0; i < colorPalette.getSize().width; i += 10) {
            for (int j = 0; j < colorPalette.getSize().height; j += 10) {
                JPanel temp = new JPanel();
                temp.setBounds(i, j, 10, 10);
                temp.setBackground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                temp.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ehfc.ColorSelected(temp);
                    }

                    //Roba obbligatoria inutile
                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {

                    }

                    public void mouseExited(MouseEvent e) {
                    }

                });
                temp.setVisible(true);
                colorPalette.getContentPane().add(temp);
            }
        }
        colorPalette.setVisible(true);
    }

    private void setDataInfoArea(Color color) {
        dataInfoArea = new JPanel();
        dataInfoArea.setLayout(null);
        linesLabel = new JLabel("Linee: " + lineNumber);
        linesLabel.setVisible(true);
        linesLabel.setForeground(Color.white);
        linesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        linesLabel.setVerticalAlignment(SwingConstants.CENTER);
        linesLabel.setFont(new Font("Futura", 0, 20));
        linesLabel.setBounds(10, 0, 120, 60);
        dataInfoArea.setForeground(Color.white);
        dataInfoArea.setBounds(0, 0, 480, 60);
        dataInfoArea.add(linesLabel);
        dataInfoArea.setBackground(color);
    }

    private void drawGrid(@org.jetbrains.annotations.NotNull Graphics2D g2, Color c) {
        int angleStep = 4;
        Color temp = g2.getColor();
        g2.setColor(c);
        spaceBetweenLines = maximumSpace / lineNumber;
        Line2D l;

        //Blocco funzionale 14.47 non toccare
        /*for(int i = 0; i < lineNumber; i++){
            l = new Line2D.Double(screenX0 - i * spaceBetweenLines, screenY0, screenX0, screenY0 - lineNumber * spaceBetweenLines + i * spaceBetweenLines);
            g2.draw(l);
        }
        for(int i = 0; i < lineNumber; i++){
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 - i * spaceBetweenLines);
            g2.draw(l);
        }
        for(int i = 0; i < lineNumber; i++){
            l = new Line2D.Double(screenX0 - i * spaceBetweenLines, screenY0, screenX0, screenY0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines);
            g2.draw(l);
        }
        for(int i = 0; i < lineNumber; i++){
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 + i * spaceBetweenLines);
            g2.draw(l);
        }*/


        //Blocco test 14.56
/*
        double testRadiants = Math.toRadians(45);

        double shiftX = Math.cos(testRadiants), shiftY = Math.sin(testRadiants);

        for(int i = 0; i < lineNumber; i++){
            g2.setColor(c);
            l = new Line2D.Double(screenX0 - i * (spaceBetweenLines), screenY0, screenX0, screenY0 - (lineNumber - i) * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 - i * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 - i * spaceBetweenLines, screenY0, screenX0, screenY0 + (lineNumber - i) * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 + i * spaceBetweenLines);
            g2.draw(l);
        }*/

        //Blocco test 16.01

        /*double testRadiants = Math.toRadians(45);

        double shiftX = Math.cos(testRadiants), shiftY = Math.sin(testRadiants);
        double offsetX = (1 - shiftX) * maximumSpace;
        double offsetY = (1 - shiftY) * maximumSpace;

        System.out.println(offsetX);
        System.out.println(offsetY);*/

        for (int i = 0; i < lineNumber; i++) {
            g2.setColor(c);
            l = new Line2D.Double(screenX0 - i * (spaceBetweenLines), screenY0, screenX0, screenY0 - (lineNumber - i) * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 - i * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 - i * spaceBetweenLines, screenY0, screenX0, screenY0 + (lineNumber - i) * spaceBetweenLines);
            g2.draw(l);
            l = new Line2D.Double(screenX0 + lineNumber * spaceBetweenLines - i * spaceBetweenLines, screenY0, screenX0, screenY0 + i * spaceBetweenLines);
            g2.draw(l);
        }
        g2.setFont(new Font("Futura", 0, 15));
        for (int i = 0; i <= lineNumber; i += angleStep) {
            double slice = 0;
            if (lineNumber != 0) {
                slice = (double) TOTAL_DEGREES / (maxLineNumber + angleStep);
                g2.setColor(Color.white);
                g2.drawString(String.valueOf(i), (int) (Math.cos(Math.toRadians(slice * i + 180)) * 300 + screenX0), (int) (Math.sin(Math.toRadians(slice * i + 180)) * 300 + screenY0) + 3);
            }
        }

        CrossHair crossHair = new CrossHair(screenX0, screenY0, lineNumber, spaceBetweenLines, gridColor);

    }

    private void setLineNumber(int i) {
        if (i >= 0)
            lineNumber = i;
    }

    private void setMaximumSpace(int i) {
        if (i > 0)
            maximumSpace = i;
    }

    private JSlider initSlider() {
        DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(lineNumber, 0, 0, maxLineNumber);
        JSlider slider = new JSlider(model);
        setPreferredSize(new Dimension(220, 60));
        slider.setFont(new Font("Futura", 0, 10));
        slider.setForeground(Color.white);
        slider.setPaintLabels(true);
        slider.setLabelTable(getSliderHashtableDone());
        slider.setMajorTickSpacing(maxLineNumber / 6);
        slider.setMinorTickSpacing(maxLineNumber / 12);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setLineNumber(slider.getValue());
                Graphical.super.repaint();
                linesLabel.setText("Linee: " + lineNumber);
            }
        });
        return slider;
    }

    private Hashtable<Integer, JLabel> getSliderHashtableDone() {
        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>(); //Dunno what the hell that does but whatever
        JLabel lbl;
        for (int i = 0; i <= maxLineNumber; i += 20) {
            lbl = new JLabel(String.valueOf(i));
            lbl.setFont(new Font("Futura", 0, 10));
            lbl.setForeground(Color.white);
            position.put(i, lbl);
        }
        return position;
    }

    protected class CrossHair {
        Line2D horizontal, vertical;
        Ellipse2D e;

        CrossHair(double screenX0, double screenY0, int lineNumber, double spaceBetweenLines, Color color) {
            vertical = new Line2D.Double(screenX0, screenY0 - lineNumber * spaceBetweenLines, screenX0, screenY0 + lineNumber * spaceBetweenLines);
            horizontal = new Line2D.Double(screenX0 - lineNumber * spaceBetweenLines, screenY0, screenX0 + lineNumber * spaceBetweenLines, screenY0);
            //  e = new Ellipse2D.Double(screenX0  - 8.6 * lineNumber / 2, screenY0 - 8.6 * lineNumber / 2, 8.6 * lineNumber, 8.6 * lineNumber);
            drawMe((Graphics2D) getGraphics(), color);
        }

        private void drawMe(Graphics2D g, Color c) {
            Color temp = g.getColor();
            g.setColor(c);
            g.draw(horizontal);
            g.draw(vertical);
            g.setColor(temp);
        }
    }

    public void paint(Graphics g) {
        //Sezione inizializzazione grafica
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g; //Tavolozza dove colorare
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setColor(Color.green);
        //Fine sezione inizializzazione grafica

        //  c.drawMe(g2, Color.ORANGE); //Ricordarsi di resettare il colore dopo aver disegnto l'oggetto --> cazzata già fixato e non serve
        drawGrid(g2, gridColor);
    }


    private class EventHandlerFormChanger {
        private void ColorSelected(JPanel temp) {
            gridColor = temp.getBackground();
            colorPalette.setVisible(false);
            Graphical.super.repaint();
        }
    }
}


