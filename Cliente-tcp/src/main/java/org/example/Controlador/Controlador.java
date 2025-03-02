package org.example.Controlador;

import org.example.Modelo.Cliente;
import org.example.Modelo.ListenerC;
import org.example.Vista.LoginVista;
import org.example.Vista.PrincipalVista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controlador {
    private LoginVista loginVista;
    private PrincipalVista principalVista;
    private final Cliente cliente;
    private PrintWriter out;
    private BufferedReader in;
    private String usuarioAutenticado;

    private String idUsuario;
    private String numeroCuenta;

    private Socket socket;

    private boolean conectado = false;


    private ListenerC listener;

    int numeroIntentos = 5;
    int tiempoIntento = 5000;


    public Controlador(LoginVista loginVista, Cliente cliente) {
        this.loginVista = loginVista;
        this.cliente = cliente;
        this.loginVista.setControlador(this);
    }

    public void conectarServidor() {
        int numeroIntentos = 5;
        int tiempoIntento = 5000;

        loginVista.mostrarMensaje("Conectando al servidor...");

        for (int i = 1; i <= numeroIntentos; i++) {
            boolean conectado = cliente.conectar();
            loginVista.actualizarEstadoServidor(conectado);

            if (conectado) {
                loginVista.mostrarMensaje("Conexión establecida con el servidor.");
                return;
            } else {
                loginVista.mostrarMensaje("Intento " + i + " de " + numeroIntentos + " fallido. Reintentando...");

                if (i < numeroIntentos) {
                    try {
                        Thread.sleep(tiempoIntento);
                    } catch (InterruptedException ignored) { }
                }
            }
        }

        loginVista.mostrarMensaje("Error: No se pudo conectar después de " + numeroIntentos + " intentos.");
    }


    public boolean conectar() {
        try {
            String SERVER_HOST = "localhost";
            int SERVER_PORT = 12345;
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            conectado = true;
            listener.mostrarMensaje("Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);

            new Thread(this::monitorearConexion).start();
            return true;
        } catch (IOException e) {
            listener.mostrarMensaje("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    private void monitorearConexion() {
        while (conectado) {
            try {
                if (socket.isClosed() || !socket.isConnected()) {
                    throw new IOException("Conexión perdida.");
                }
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                listener.mostrarMensaje("Servidor desconectado. Intentando reconectar...");
                reconectar();
                return;
            }
        }
    }

    private void reconectar() {
        for (int i = 1; i <= numeroIntentos; i++) {
            listener.mostrarMensaje("Intento de reconexión " + i + " de " + numeroIntentos + "...");
            boolean reintento = conectar();
            if (reintento) {
                listener.mostrarMensaje("Re-conexión exitosa con el servidor.");
                return;
            }

            try {
                Thread.sleep(tiempoIntento);
            } catch (InterruptedException ignored) { }
        }
        listener.mostrarMensaje("No se pudo reconectar al servidor después de " + numeroIntentos + " intentos.");
    }







    public void validarLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginVista.mostrarMensaje("Los campos no pueden estar vacíos");
            return;
        }

        String mensaje = username + " " + password;
        System.out.println("Cliente enviando: " + mensaje);
        cliente.enviarMensaje(mensaje);

        try {
            Thread.sleep(500);
            String respuesta = cliente.recibirMensaje();
            System.out.println("Cliente recibió: " + respuesta);

            if (respuesta.startsWith("LOGIN_EXITO")) {
                String[] partes = respuesta.split("\\s+");
                if (partes.length == 3) {
                    usuarioAutenticado = username;
                    idUsuario = partes[1];
                    numeroCuenta = partes[2];

                    loginVista.setVisible(false);
                    principalVista = new PrincipalVista(usuarioAutenticado, numeroCuenta);
                    principalVista.setControlador(this);
                } else {
                    loginVista.mostrarMensaje("Error en la respuesta del servidor.");
                }
            }
        } catch (IOException | InterruptedException e) {
            loginVista.mostrarMensaje("Error en la comunicación con el servidor: " + e.getMessage());
        }
    }


    public void consultarSaldo(String numeroCuenta) {
        principalVista.mostrarMensaje("Consultando saldo...");
        String mensaje = "CONSULTAR_SALDO " + numeroCuenta;

        cliente.enviarMensaje(mensaje);

        try {
            String respuesta;
            do {
                respuesta = cliente.recibirMensaje();
            } while (respuesta.startsWith("ERROR"));

            System.out.println("Cliente recibió: [" + respuesta + "]");
            principalVista.mostrarMensaje(respuesta);
        } catch (IOException e) {
            principalVista.mostrarMensaje("Error al recibir la respuesta del servidor: " + e.getMessage());
        }
    }


    public void realizarConsignacion(String cuentaDestino, String monto) {
        if (cuentaDestino.isEmpty() || monto.isEmpty()) {
            principalVista.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        principalVista.mostrarMensaje("Consignando " + monto + " a la cuenta " + cuentaDestino);
        String mensaje = "CONSIGNAR " + cuentaDestino + " " + monto;
        System.out.println("Cliente enviando: [" + mensaje + "]");

        cliente.enviarMensaje(mensaje);

        try {
            String respuesta = cliente.recibirMensaje();
            System.out.println("Cliente recibió: [" + respuesta + "]");
            principalVista.mostrarMensaje(respuesta);
        } catch (IOException e) {
            principalVista.mostrarMensaje("Error al recibir la respuesta del servidor: " + e.getMessage());
        }
    }
}
