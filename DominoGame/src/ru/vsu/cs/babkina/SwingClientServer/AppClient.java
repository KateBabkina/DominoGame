package ru.vsu.cs.babkina.SwingClientServer;

import ru.vsu.cs.babkina.GameModel.DominoTile;
import ru.vsu.cs.babkina.GameModel.Round;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AppClient extends JPanel {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final int FIELD_WIDTH = 10;
    private final int FIELD_HEIGHT = 70 / FIELD_WIDTH;
    private final int PREFERRED_WIDTH_FOR_EXTRA_TILE_BUTTON = 300;
    private final int PREFERRED_HEIGHT_FOR_EXTRA_TILE_BUTTON = 100;

    private final DominoButton[][] buttonField = new DominoButton[FIELD_HEIGHT][FIELD_WIDTH];

    private final JButton takeExtraTilesButton = new JButton("Take extra tile");
    private final JButton connect = new JButton("Connect");
    private final JButton rightButton = new JButton("Right");
    private final JButton leftButton = new JButton("Left");
    private final JTextArea currGameState = new JTextArea("Start new game!\nChoose domino tile and make a move!");

    private DominoButton currBtn;

    public JButton getConnect() {
        return connect;
    }

    public JTextArea getCurrGameState() {
        return currGameState;
    }

    public JButton getTakeExtraTilesButton() {
        return takeExtraTilesButton;
    }

    public JButton getRightButton() {
        return rightButton;
    }

    public JButton getLeftButton() {
        return leftButton;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public static void main(String[] args) {
        AppClient client = new AppClient("localhost", 9999);
        client.start();
    }

    public AppClient(String host, int port) {
        this.host = host;
        this.port = port;

        setLayout(new GridLayout(FIELD_HEIGHT, FIELD_WIDTH));
        initButtonField();
        MouseAdapter positionMouseAdapter = new AppClient.PositionMouseAdapter();
        setPositionMouseAdapter(positionMouseAdapter);

        takeExtraTilesButton.setFont(Fonts.EXTRA_TILE_BUTTON_FONT.getFont());
        takeExtraTilesButton.setPreferredSize(new Dimension(PREFERRED_WIDTH_FOR_EXTRA_TILE_BUTTON, PREFERRED_HEIGHT_FOR_EXTRA_TILE_BUTTON));

        rightButton.setEnabled(false);
        rightButton.setFont(Fonts.EXTRA_TILE_BUTTON_FONT.getFont());

        leftButton.setEnabled(false);
        leftButton.setFont(Fonts.EXTRA_TILE_BUTTON_FONT.getFont());

        currGameState.setFont(Fonts.TEXT_FONT.getFont());

        takeExtraTilesButton.addActionListener(e -> {
            try {
                if (!socket.isClosed()) {
                    Command outputCommand = Command.EXTRA_TILE;
                    Request output = new Request(outputCommand, null);
                    out.writeObject(output);
                    System.out.println("To server " + output);
                    Response input = (Response) in.readObject();
                    Round round = input.getRound();
                    System.out.println("From server " + input);
                    currGameState.setText("It's your turn!\n\n");
                    currGameState.append(printQuantityOfTilesInfo(round));
                    refresh(round);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        connect.addActionListener(e -> {
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                Round currRound = (Round) in.readObject();
                refresh(currRound);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Can not connect to server");
            }
        });

        rightButton.addActionListener(e -> {
            try {
                if (!socket.isClosed()) {
                    Command outputCommand = Command.RIGHT;
                    DominoTile outputTile = currBtn.getDominoTile();
                    Request output = new Request(outputCommand, outputTile);
                    out.writeObject(output);
                    positionButton();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        leftButton.addActionListener(e -> {
            try {
                if (!socket.isClosed()) {
                    Command outputCommand = Command.LEFT;
                    DominoTile outputTile = currBtn.getDominoTile();
                    Request output = new Request(outputCommand, outputTile);
                    out.writeObject(output);
                    positionButton();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }


    public void start() {
        new ServerClientSwingVisualization(this);
    }

    private void positionButton() {
        try {
            System.out.println("To server ");
            Response input = (Response) in.readObject();
            Round round = input.getRound();
            System.out.println("From server " + input);
            currGameState.setText(round.getGameState().getStringOfState());
            currGameState.append(printQuantityOfTilesInfo(round));
            refresh(round);
            if (input.getCommand() != null && input.getCommand() == Command.END) {
                disableButtonField();
                System.out.println("Socked closed");
                socket.close();
            }
            rightButton.setEnabled(false);
            leftButton.setEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initButtonField() {
        for (int rowIndex = 0; rowIndex < FIELD_HEIGHT; rowIndex++) {
            for (int columnIndex = 0; columnIndex < FIELD_WIDTH; columnIndex++) {
                DominoButton newButton = new DominoButton();
                buttonField[rowIndex][columnIndex] = newButton;
                add(newButton);
            }
        }
    }

    private void disableButtonField() {
        for (int rowIndex = 0; rowIndex < FIELD_HEIGHT; rowIndex++) {
            for (int columnIndex = 0; columnIndex < FIELD_WIDTH; columnIndex++) {
                buttonField[rowIndex][columnIndex].setEnabled(false);
            }
        }
    }

    private void printButtonField(Round currRound) {
        int currTileIndex = 0;
        for (DominoTile tile : currRound.getTilesOfHuman()) {
            buttonField[currTileIndex / FIELD_WIDTH][currTileIndex % FIELD_WIDTH].setDominoTile(tile);
            currTileIndex++;
        }
        buttonField[currTileIndex / FIELD_WIDTH][currTileIndex % FIELD_WIDTH].clear();
        currTileIndex = 0;
        for (DominoTile tile : currRound.getCurrentGameProcess()) {
            buttonField[4 + currTileIndex / FIELD_WIDTH][currTileIndex % FIELD_WIDTH].setDominoTile(tile);
            currTileIndex++;
        }
    }

    private void refresh(Round currRound) {
        printButtonField(currRound);
        if (currRound.getQuantityOfExtraTiles() == 0 || currRound.getQuantityOfComputerTiles() == 0 || currRound.getQuantityOfHumanTiles() == 0) {
            takeExtraTilesButton.setEnabled(false);
        }
    }

    private void setPositionMouseAdapter(MouseAdapter positionMouseAdapter) {
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            for (int columnIndex = 0; columnIndex < FIELD_WIDTH; columnIndex++) {
                buttonField[rowIndex][columnIndex].addMouseListener(positionMouseAdapter);
            }
        }
    }

    private class PositionMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            try {
                if (!socket.isClosed()) {
                    currBtn = (DominoButton) event.getSource();
                    DominoTile outputTile = currBtn.getDominoTile();
                    Command outputCommand = Command.MOVE;
                    Request output = new Request(outputCommand, outputTile);
                    out.writeObject(output);
                    System.out.println("To server " + output);
                    Response input = (Response) in.readObject();
                    Round round = input.getRound();
                    System.out.println("From server " + input);

                    refresh(round);

                    currGameState.setText(round.getGameState().getStringOfState());
                    currGameState.append(printQuantityOfTilesInfo(round));

                    if (input.getCommand() != null && input.getCommand() == Command.POSITION_CHOICE) {
                        rightButton.setEnabled(true);
                        leftButton.setEnabled(true);
                    } else if (input.getCommand() != null && input.getCommand() == Command.END) {
                        System.out.println("Socked closed");
                        disableButtonField();
                        socket.close();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String printQuantityOfTilesInfo(Round currRound) {
        return ("Extra tiles left: " + currRound.getQuantityOfExtraTiles() +
                ";\nYou have " +
                currRound.getQuantityOfHumanTiles() +
                " tiles;\nComputer has " +
                currRound.getQuantityOfComputerTiles() +
                " tiles.");
    }

}