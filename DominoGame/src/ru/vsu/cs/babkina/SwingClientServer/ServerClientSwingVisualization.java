package ru.vsu.cs.babkina.SwingClientServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerClientSwingVisualization extends JFrame {

    public ServerClientSwingVisualization(AppClient field) throws HeadlessException {
        final int FRAME_WIDTH = 1500;
        final int FRAME_HEIGHT = 800;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);

        Container cp = this.getContentPane();
        this.setLayout(new BorderLayout());

        cp.add(field, BorderLayout.CENTER);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(field.getLeftButton(), BorderLayout.EAST);
        jPanel.add(field.getTakeExtraTilesButton(), BorderLayout.EAST);
        jPanel.add(field.getCurrGameState(), BorderLayout.WEST);
        jPanel.add(field.getRightButton(), BorderLayout.WEST);
        jPanel.add(field.getConnect(), BorderLayout.WEST);

        cp.add(jPanel, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (field.getSocket() != null) {
                        if (!field.getSocket().isClosed()) {
                            field.getOut().writeObject(new Request(Command.END, null));
                            field.getSocket().close();
                            System.out.println("Socked closed");
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.setVisible(true);
    }
}
