package com.company;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class Drawer {
    private static final Taskbar taskbar = Taskbar.getTaskbar();
    public static void main(String[] args) {

        ImageIcon i = new ImageIcon("Resources/appicon.png");
        taskbar.setIconImage(i.getImage());

        Graphical s = new Graphical();
        //s.run();
        s.setVisible(true);
    }
}