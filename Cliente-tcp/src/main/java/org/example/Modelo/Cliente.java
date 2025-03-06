package org.example.Modelo;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ListenerC listener;
    private PrintWriter pingOut;
    private BufferedReader pingIn;
    private Socket pingSocket;

    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 12345;
    private final int PING_PORT = 12346;
    private boolean conectado = false;
    private boolean monitoreando = false;


    private String ultimoUsuario;
    private String ultimaClave;

    private static final int MAX_REINTENTOS = 5;
    private static final int TIEMPO_ESPERA = 5000;





    public Cliente(ListenerC listener) {
        this.listener = listener;
    }


   /* public boolean conectar() {
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("[Cliente] Conexión establecida con el servidor.");
            conectado = true;

            if (!monitoreando) {
                iniciarMonitoreoServidor();
            }
            return true;
        } catch (IOException e) {
            System.out.println("[Cliente] No se pudo conectar al servidor: " + e.getMessage());
            return false;
        }
    }*/


    public boolean conectar() {
        try {
            System.out.println("[Cliente] Intentando conectar al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // **Conectar el socket de monitoreo (PING)**
            System.out.println("[Cliente] Conectando socket de monitoreo en " + SERVER_HOST + ":" + PING_PORT);
            pingSocket = new Socket(SERVER_HOST, PING_PORT);
            pingIn = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
            pingOut = new PrintWriter(pingSocket.getOutputStream(), true);

            listener.mostrarMensaje(" Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            conectado = true;

            // **Iniciar el monitoreo solo si no está corriendo**
            if (!monitoreando) {
                iniciarMonitoreoServidor();
            }

            return true;
        } catch (IOException e) {
            listener.mostrarMensaje("Error al conectar: " + e.getMessage());
            System.out.println("[Cliente] No se pudo conectar al servidor: " + e.getMessage());
            return false;
        }
    }





    public void enviarMensaje(String mensaje) {
        if (out != null && conectado) {
            System.out.println("[Cliente] Enviando: " + mensaje);
            out.println(mensaje);
        } else {
            System.out.println("[Cliente] No se pudo enviar. Conexión no establecida.");
        }
    }



    public String recibirMensaje() throws IOException {
        if (!conectado || in == null) {
            System.out.println("[Cliente] Error: 'recibirMensaje()' llamado sin conexión.");
            return "ERROR: Sin conexión.";
        }

        String linea = in.readLine();
        if (linea == null || linea.trim().isEmpty()) {
            System.out.println("[Cliente] No se recibió ninguna respuesta del servidor.");
            return "ERROR: No se recibió respuesta del servidor.";
        }

        System.out.println("[Cliente] Mensaje completo recibido: " + linea);
        return linea.replace("\\n", "\n");  // Convertir los saltos de línea en texto real
    }


    private void iniciarMonitoreoServidor() {
        if (monitoreando) return;
        monitoreando = true;

        new Thread(() -> {
            while (conectado) {
                try {
                    pingOut.println("PING");
                    String respuesta = pingIn.readLine();

                    if (respuesta == null || !respuesta.equals("PONG")) {
                        throw new IOException("No se recibió PONG del servidor.");
                    }

                    System.out.println("[Cliente] PING exitoso.");
                    Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.println("[Cliente] ERROR: Conexión perdida.");
                    reconectar();
                    monitoreando = false;
                    break;
                }
            }
        }).start();
    }



    private synchronized void reconectar() {
        if (conectado) return;

        conectado = false;
        for (int i = 1; i <= MAX_REINTENTOS; i++) {
            System.out.println("[Cliente] Intento de reconexión " + i + " de " + MAX_REINTENTOS);
            if (conectar()) {
                System.out.println("[Cliente] Reconexión exitosa.");
                return;
            }
            try {
                Thread.sleep(TIEMPO_ESPERA);
            } catch (InterruptedException ignored) {}
        }
        System.out.println("[Cliente] No se pudo reconectar después de " + MAX_REINTENTOS + " intentos.");
    }

    public void guardarCredenciales(String usuario, String clave) {
        this.ultimoUsuario = usuario;
        this.ultimaClave = clave;
    }

    private void reenviarCredenciales() {
        if (ultimoUsuario != null && ultimaClave != null) {
            enviarMensaje("LOGIN " + ultimoUsuario + " " + ultimaClave);
        }
    }


}
