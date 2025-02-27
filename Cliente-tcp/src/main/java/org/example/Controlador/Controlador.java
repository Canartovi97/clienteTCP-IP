package org.example.Controlador;

import org.example.Modelo.Cliente;
import org.example.Vista.LoginVista;
import org.example.Vista.PrincipalVista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Controlador {
    private LoginVista loginVista;
    private PrincipalVista principalVista;
    private final Cliente cliente;
    private PrintWriter out;
    private BufferedReader in;
    private String usuarioAutenticado;

    public Controlador(LoginVista loginVista, Cliente cliente) {
        this.loginVista = loginVista;
        this.cliente = cliente;
        this.loginVista.setControlador(this);
    }

    public void conectarServidor() {
        loginVista.mostrarMensaje("Conectando al servidor...");
        boolean conectado = cliente.conectar();
        loginVista.actualizarEstadoServidor(conectado);
        if (conectado) {
            loginVista.mostrarMensaje("Conexión establecida con el servidor.");
        } else {
            loginVista.mostrarMensaje("Error: No se pudo conectar al servidor.");
        }
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
            Thread.sleep(500); // Pequeña espera para asegurarse de que el servidor responda
            String respuesta = cliente.recibirMensaje();
            System.out.println("Cliente recibió: " + respuesta);

            if (respuesta != null && respuesta.startsWith("LOGIN_EXITO")) {
                usuarioAutenticado = username;
                loginVista.setVisible(false);
                principalVista = new PrincipalVista(username);
                principalVista.setControlador(this);
            } else {
                loginVista.mostrarMensaje("Credenciales incorrectas");
            }
        } catch (IOException | InterruptedException e) {
            loginVista.mostrarMensaje("Error en la comunicación con el servidor: " + e.getMessage());
        }
    }


    public void consultarSaldo(String numeroCuenta) {
        principalVista.mostrarMensaje("Consultando saldo...");

        String mensaje = "CONSULTAR_SALDO " + numeroCuenta;
        System.out.println("Cliente enviando: [" + mensaje + "]");

        cliente.enviarMensaje(mensaje);

        try {
            String respuesta = cliente.recibirMensaje();
            System.out.println("Cliente recibió: [" + respuesta + "]");

            // 🔥 Asegurar que procesamos correctamente "SALDO_OK"
            if (respuesta.startsWith("SALDO_OK")) {
                String saldo = respuesta.replace("SALDO_OK ", ""); // Elimina "SALDO_OK "
                principalVista.mostrarMensaje(saldo);
            } else {
                principalVista.mostrarMensaje("Error en la respuesta del servidor: " + respuesta);
            }
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
        cliente.enviarMensaje("CONSIGNAR:" + cuentaDestino + "," + monto);
    }
}
