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

    // Opciones para reintentos de reconexión
    private final int numeroIntentos = 5;
    private final int tiempoIntento = 5000;

    public Cliente(ListenerC listener) {
        this.listener = listener;
    }

    /**
     * Intenta conectar al servidor, y si lo logra, queda listo para
     * enviar/recibir con enviarMensaje(...) y recibirMensaje(...).
     */
    public boolean conectar() {
        try {
            System.out.println("[Cliente] Intentando conectar al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            socket = new Socket(SERVER_HOST, SERVER_PORT);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            conectado = true;
            listener.mostrarMensaje("✅ Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            System.out.println("[Cliente] Conexión establecida con el servidor.");

            // Si quisieras un hilo que ESCUCHE mensajes continuamente,
            // podrías implementarlo aquí. Pero ojo, no mezclar con recibirMensaje().
            return true;

        } catch (IOException e) {
            listener.mostrarMensaje("❌ Error al conectar: " + e.getMessage());
            System.out.println("[Cliente] Error al conectar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Envía un mensaje al servidor si existe la conexión.
     */
    public void enviarMensaje(String mensaje) {
        if (out != null && conectado) {
            System.out.println("[Cliente] Enviando: " + mensaje);
            out.println(mensaje);
        } else {
            System.out.println("[Cliente] No se pudo enviar. Conexión no establecida.");
        }
    }

    /**
     * Lee UNA línea (respuesta) del servidor.
     * Si el servidor se cae, readLine() puede devolver null o lanzar IOException.
     */
    public String recibirMensaje() throws IOException {
        if (!conectado || in == null) {
            System.out.println("[Cliente] Error: 'recibirMensaje()' llamado sin conexión.");
            return "ERROR: Sin conexión.";
        }
        String linea = in.readLine();
        if (linea == null) {
            // Significa que el servidor cerró la conexión
            System.out.println("[Cliente] Servidor cerró la conexión (readLine() = null).");
            throw new IOException("Conexión cerrada por el servidor.");
        }
        System.out.println("[Cliente] Recibido: " + linea);
        return linea;
    }

    /**
     * Opción de reconectar en caso de que perder la conexión (IOException).
     * Intenta 'numeroIntentos' veces cada 'tiempoIntento' milisegundos.
     */
    public void reconectar() {
        desconectar(); // cierra el socket anterior
        for (int i = 1; i <= numeroIntentos; i++) {
            listener.mostrarMensaje("Intento de reconexión " + i + " de " + numeroIntentos + "...");
            System.out.println("[Cliente] Intento de reconexión " + i + " de " + numeroIntentos + "...");

            boolean reintento = conectar();
            if (reintento) {
                listener.mostrarMensaje("✅ Re-conexión exitosa con el servidor.");
                System.out.println("[Cliente] Re-conexión exitosa.");
                return;
            }
            try {
                Thread.sleep(tiempoIntento);
            } catch (InterruptedException ignored) {
            }
        }
        listener.mostrarMensaje("❌ No se pudo reconectar al servidor después de " + numeroIntentos + " intentos.");
        System.out.println("[Cliente] No se pudo reconectar después de " + numeroIntentos + " intentos.");
    }

    /**
     * Cierra la conexión explícitamente.
     */
    public void desconectar() {
        conectado = false;
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
