package org.example.Controlador;

import org.example.Modelo.Cliente;
import org.example.Modelo.ListenerC;
import org.example.Vista.LoginVista;
import org.example.Vista.MovimientosVista;
import org.example.Vista.PrincipalVista;
import org.example.Vista.SaldoVista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private final LoginVista loginVista;
    private PrincipalVista principalVista;
    private final Cliente cliente;

    private MovimientosVista movimientosVista;





    public Controlador(LoginVista loginVista, Cliente cliente) {
        this.loginVista = loginVista;
        this.cliente = cliente;
        this.loginVista.setControlador(this);
    }


    public void conectarServidor() {
        new Thread(() -> {
            loginVista.mostrarMensaje("Conectando al servidor...");
            boolean ok = cliente.conectar();
            loginVista.actualizarEstadoServidor(ok);

            if (ok) {
                loginVista.mostrarMensaje("Conexión establecida con el servidor.");
            } else {
                loginVista.mostrarMensaje("Error: No se pudo conectar al servidor.");
            }
        }).start();
    }



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

                String[] partes = respuesta.split("\\s+");
                System.out.println("Esta es la respuesta " + respuesta);

                cliente.guardarCredenciales(username,password);
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



    public void realizarConsignacion(String cuentaOrigen, String cuentaDestino, String monto) {
        if (principalVista == null) return;

        if (cuentaOrigen.isEmpty() || cuentaDestino.isEmpty() || monto.isEmpty()) {
            principalVista.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        principalVista.mostrarMensaje("Consignando $" + monto + " de la cuenta " + cuentaOrigen + " a la cuenta " + cuentaDestino);
        String comando = "CONSIGNAR " + cuentaOrigen + " " + cuentaDestino + " " + monto;
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

    public List<String> obtenerCuentasDelUsuario(String username) {
        return cliente.obtenerCuentas(username); // Implementado en Cliente
    }

    public String getUsuarioActual() {
        return cliente.getUsuarioActual();
    }


    public void mostrarMovimientosVista() {
        String usuario = cliente.getUsuarioActual(); // Obtener usuario autenticado
        List<String> cuentas = cliente.obtenerCuentas(usuario); // Obtener cuentas desde el servidor

        if (cuentas.isEmpty()) {
            System.out.println("[Controlador] No se encontraron cuentas asociadas.");
            return;
        }


        if (movimientosVista == null) {
            movimientosVista = new MovimientosVista(this, cuentas);
        } else {
            System.out.println("[Controlador] MovimientosVista ya estaba inicializada.");
        }
    }

    public void consultarMovimientos(String numeroCuenta) {
        System.out.println("[Controlador] Consultando movimientos de la cuenta: " + numeroCuenta);

        // ✅ Verifica que la ventana está creada antes de continuar
        if (movimientosVista == null) {
            System.out.println("[Controlador] ERROR: MovimientosVista no inicializada. Creándola...");
            mostrarMovimientosVista(); // Crearla antes de continuar
        }

        cliente.enviarMensaje("CONSULTAR_MOVIMIENTOS " + numeroCuenta);

        try {
            String respuesta = cliente.recibirMensaje();
            System.out.println("[Controlador] Respuesta movimientos: " + respuesta);

            if (movimientosVista != null) {
                movimientosVista.actualizarMovimientos(respuesta); 
            } else {
                System.out.println("[Controlador] ERROR: MovimientosVista sigue sin estar inicializada.");
            }
        } catch (IOException e) {
            System.out.println("[Controlador] Error al recibir movimientos: " + e.getMessage());
        }
    }





    private List<String[]> parsearMovimientos(String respuesta) {
        List<String[]> movimientos = new ArrayList<>();

        String[] filas = respuesta.split(";");
        for (String fila : filas) {
            String[] datos = fila.split(",");
            if (datos.length == 4) { // Asegurar que tenga los datos correctos
                movimientos.add(datos);
            }
        }
        return movimientos;
    }



}
