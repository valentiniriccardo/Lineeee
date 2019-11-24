package com.company;

public class GraphicsController implements Runnable{

    private Graphical s;
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            this.init(s);
        }
    }

    public void init(Graphical s){
        s.repaint();
    }
}
