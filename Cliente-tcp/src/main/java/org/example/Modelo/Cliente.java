package org.example.Modelo;

import org.example.Vista.LoginVista;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

    private String ultimaTransaccion = "";


    private int maxReintentos;
    private int tiempoEspera;





    public Cliente(ListenerC listener) {
        this.listener = listener;
        cargarConfiguracion();
    }



    private void cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("configuracion.properties")) {
            propiedades.load(input);
            maxReintentos = Integer.parseInt(propiedades.getProperty("max_reintentos", "5"));
            tiempoEspera = Integer.parseInt(propiedades.getProperty("tiempo_espera", "5000"));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Archivo configuracion no cargo");

        }
    }


    public boolean conectar() {
        try {
            System.out.println("[Cliente] Intentando conectar al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);


            System.out.println("[Cliente] Conectando socket de monitoreo en " + SERVER_HOST + ":" + PING_PORT);
            pingSocket = new Socket(SERVER_HOST, PING_PORT);
            pingIn = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
            pingOut = new PrintWriter(pingSocket.getOutputStream(), true);

            listener.mostrarMensaje(" Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            conectado = true;


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
            if (mensaje.startsWith("LOGIN")){
                System.out.println("No guarda el ultimo mensaje es un login ");
                out.println(mensaje);
            } else {
                ultimaTransaccion = mensaje;
                System.out.println("Mensaje guardado en ultima transaccion "+ ultimaTransaccion);
                out.println(mensaje);
            }

        } else {
            System.out.println("[Cliente] No se pudo enviar. Conexión no establecida.");
        }
    }

    public void enviarUltimaTransaccion (){

            enviarMensaje(ultimaTransaccion);

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
        return linea.replace("\\n", "\n");
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
                    conectado = false;
                    reconectar();
                    monitoreando = false;
                    break;
                }
            }
        }).start();
    }



    private synchronized void reconectar() {
        if (conectado) return;

        if (listener instanceof LoginVista) {
            ((LoginVista) listener).setBotonesHabilitados(false);
        }

        System.out.println("[Cliente] Intentando reconectar...");

        for (int i = 1; i <= maxReintentos; i++) {
            System.out.println("[Cliente] Intento de reconexión " + i + " de " + maxReintentos);
            if (conectar()) {
                System.out.println("[Cliente] Reconexión exitosa.");
                reenviarCredenciales();
                enviarUltimaTransaccion();
                iniciarMonitoreoServidor();

                if (listener instanceof LoginVista) {
                    ((LoginVista) listener).setBotonesHabilitados(true);
                }

                return;
            }
            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException ignored) {}
        }

        System.out.println("La reconexion fallo desdepues de " + maxReintentos + " intentos.");

        if (listener instanceof LoginVista) {
            ((LoginVista) listener).setBotonesHabilitados(true);
        }
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

    public List<String> obtenerCuentas(String username) {
        enviarMensaje("OBTENER_CUENTAS " + username);
        try {
            String respuesta = recibirMensaje();
            return Arrays.asList(respuesta.split("\\s+"));
        } catch (IOException e) {
            System.out.println("[Cliente] Error al obtener cuentas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String getUsuarioActual() {
        return this.ultimoUsuario;
    }


}
