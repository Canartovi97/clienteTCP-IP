package org.example.Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ListenerC listener;
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 12345;

    public Cliente(ListenerC listener) {
        this.listener = listener;
    }

    public void conectar() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            listener.mostrarMensaje("Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            recibirMensajes();
        } catch (IOException e) {
            listener.mostrarMensaje("Error al conectar: " + e.getMessage());
        }
    }

    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }

    private void recibirMensajes() {
        new Thread(() -> {
            try {
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    listener.mostrarMensaje("Servidor: " + mensaje);
                }
            } catch (IOException e) {
                listener.mostrarMensaje("Error recibiendo mensajes: " + e.getMessage());
            }
        }).start();
    }
}

