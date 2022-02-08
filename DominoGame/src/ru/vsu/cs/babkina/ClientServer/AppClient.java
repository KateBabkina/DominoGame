package ru.vsu.cs.babkina.ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AppClient {
    private final String host;
    private final int port;

    public static void main(String[] args) throws IOException {
        AppClient client = new AppClient("localhost", 9999);
        client.start();
    }

    public AppClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String command;
        while (!socket.isClosed() && (command = in.readLine()) != null) {
            String[] parsed = command.split(Command.SEPARATOR);
            System.out.println("From server: " + parsed[0]);
            if (Command.END.getCommandString().equals(parsed[0])) {
                System.out.println(parsed[1]);
                socket.close();
            }
            if (Command.MOVE.getCommandString().equals(parsed[0])) {
                System.out.println(parsed[1]);
                int nextInt = new Scanner(System.in).nextInt();
                String resp = Command.RESP.getCommandString() + Command.SEPARATOR + nextInt;
                System.out.println("To server: " + resp);
                out.println(resp);
            }
        }
    }

}