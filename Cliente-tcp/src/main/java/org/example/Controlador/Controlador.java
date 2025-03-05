package org.example.Controlador;

import org.example.Modelo.Cliente;
import org.example.Modelo.ListenerC;
import org.example.Vista.LoginVista;
import org.example.Vista.PrincipalVista;
import org.example.Vista.SaldoVista;

import java.io.IOException;

public class Controlador {
    private final LoginVista loginVista;
    private PrincipalVista principalVista;
    private SaldoVista saldoVista;
    private final Cliente cliente;


    private String idUsuario;
    private String numeroCuenta;

    public Controlador(LoginVista loginVista, Cliente cliente) {
        this.loginVista = loginVista;
        this.cliente = cliente;
        this.loginVista.setControlador(this);
    }

    /**
     * Conexión con reintentos
     */
    public void conectarServidor() {
        int numeroIntentos = 5;
        int tiempoIntento = 5000;

        loginVista.mostrarMensaje("Conectando al servidor...");


        for (int i = 1; i <= numeroIntentos; i++) {
            boolean ok = cliente.conectar();
            loginVista.actualizarEstadoServidor(ok);

            if (ok) {
                loginVista.mostrarMensaje(" Conexión establecida con el servidor.");
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

    /**
     * Envía el comando "LOGIN <user> <pass>", recibe "LOGIN_EXITO" o "LOGIN_FALLIDO".
     * Si es éxito, manda "OBTENER_INFO_USUARIO" y procesa la respuesta "idUsuario numeroCuenta".
     */
    public void validarLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginVista.mostrarMensaje("Los campos no pueden estar vacíos.");
            return;
        }

        String comando = "LOGIN " + username + " " + password;
        System.out.println("[Controlador] Enviando: " + comando);
        cliente.enviarMensaje(comando);

        try {
            String respuesta = cliente.recibirMensaje().trim();
            System.out.println("[Controlador] Respuesta login: [" + respuesta + "]");


            if (respuesta.startsWith("LOGIN_EXITO")) {
                System.out.println("Esta es la respuesta " + respuesta);
                String[] partes = respuesta.split("\\s+");
                System.out.println("Esta es la respuesta " + respuesta);

                loginVista.setVisible(false);
                principalVista = new PrincipalVista(username);
                principalVista.setControlador(this);


            } else if (respuesta.equalsIgnoreCase("LOGIN_FALLIDO")) {
                loginVista.mostrarMensaje("Credenciales incorrectas.");
            } else {

                loginVista.mostrarMensaje("Error en la respuesta del servidor: " + respuesta);
            }
        } catch (IOException e) {
            loginVista.mostrarMensaje("Error en la comunicación con el servidor: " + e.getMessage());
        }
    }



    public void consultarSaldoAvanzado(String username, String numero, String tipo, SaldoVista saldoVista) {
        if (principalVista == null) return;

        saldoVista.mostrarMensaje("Consultando saldo...");


        String comando = "CONSULTAR_SALDO " + username + " " + tipo + " " + numero;

        System.out.println("[Controlador] Enviando: " + comando);

        cliente.enviarMensaje(comando);

        try {
            String respuesta = cliente.recibirMensaje();
            System.out.println("[Controlador] Respuesta recibida: " + respuesta);
            saldoVista.mostrarMensaje(respuesta);
        } catch (IOException e) {
            saldoVista.mostrarMensaje("Error al recibir la respuesta: " + e.getMessage());
        }
    }


    public void realizarConsignacion(String cuentaDestino, String monto) {
        if (principalVista == null) return;

        if (cuentaDestino.isEmpty() || monto.isEmpty()) {
            principalVista.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        principalVista.mostrarMensaje("Consignando $" + monto + " a la cuenta " + cuentaDestino);
        String comando = "CONSIGNAR " + cuentaDestino + " " + monto;
        System.out.println("[Controlador] Enviando: " + comando);

        cliente.enviarMensaje(comando);
        try {
            String respuesta = cliente.recibirMensaje();
            System.out.println("[Controlador] Respuesta consignación: [" + respuesta + "]");
            principalVista.mostrarMensaje(respuesta);
        } catch (IOException e) {
            principalVista.mostrarMensaje("Error al recibir respuesta: " + e.getMessage());
        }
    }
}
