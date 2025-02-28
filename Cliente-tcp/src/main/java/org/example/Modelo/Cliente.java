package org.example.Modelo;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ListenerC listener;
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 12345;
    private boolean conectado = false;
    private final int numeroIntentos = 5;
    private final int tiempoIntento = 5000;

    public Cliente(ListenerC listener) {
        this.listener = listener;
    }

    public boolean conectar() {
        try {
            System.out.println("Intentando conectar al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            conectado = true;
            listener.mostrarMensaje("Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            System.out.println("Conexión establecida con el servidor.");

            // Iniciar escucha de conexión en un hilo separado
            new Thread(this::escucharConexion).start();

            return true;
        } catch (IOException e) {
            listener.mostrarMensaje("Error al conectar: " + e.getMessage());
            System.out.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    private void escucharConexion() {
        try {
            
            InputStream input = socket.getInputStream();
            while (conectado) {
                if (input.read() == -1) {
                    throw new IOException("Conexión cerrada por el servidor.");
                }
            }
        } catch (IOException e) {
            listener.mostrarMensaje("Servidor desconectado. Intentando reconectar...");
            System.out.println("Servidor desconectado. Iniciando reconexión...");
            reconectar();
        }
    }

    private void reconectar() {
        for (int i = 1; i <= numeroIntentos; i++) {
            listener.mostrarMensaje("Intento de reconexión " + i + " de " + numeroIntentos + "...");
            System.out.println("Intento de reconexión " + i + " de " + numeroIntentos + "...");

            boolean reintento = conectar();
            if (reintento) {
                listener.mostrarMensaje("Re-conexión exitosa con el servidor.");
                System.out.println("Re-conexión exitosa.");
                return;
            }

            try {
                Thread.sleep(tiempoIntento);
            } catch (InterruptedException ignored) { }
        }
        listener.mostrarMensaje("No se pudo reconectar después de " + numeroIntentos + " intentos.");
        System.out.println("No se pudo reconectar después de " + numeroIntentos + " intentos.");
    }

    public void enviarMensaje(String mensaje) {
        if (out != null && conectado) {
            System.out.println("Enviando mensaje: " + mensaje);
            out.println(mensaje);
        } else {
            System.out.println("No se pudo enviar el mensaje. Conexión no establecida.");
        }
    }

    public String recibirMensaje() throws IOException {
        if (in != null) {
            String mensaje = in.readLine();
            if (mensaje != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                return mensaje;
            }
        }
        System.out.println("ERROR: No se recibió respuesta del servidor.");
        return "ERROR: No se recibió respuesta del servidor.";
    }
}
