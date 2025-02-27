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

    public boolean conectar() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            listener.mostrarMensaje("Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            return true;
        } catch (IOException e) {
            listener.mostrarMensaje("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }

    public String recibirMensaje() throws IOException {
        if (in != null) {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (!mensaje.trim().isEmpty()) {
                    return mensaje;
                }
            }
        }
        return "ERROR: No se recibió respuesta del servidor.";
    }
}

