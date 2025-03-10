package com.mycompany.cliente;

public class Controlador {
    private Vista vista;
    private Cliente cliente;

    public Controlador(Vista vista, Cliente cliente) {
        this.vista = vista;
        this.cliente = cliente;
        this.vista.setControlador(this);
    }

    public void conectarServidor() {
        vista.mostrarMensaje("Conectando al servidor...");
        cliente.conectar();
    }

    public void enviarMensaje(String mensaje) {
        if (mensaje.trim().isEmpty()) {
            vista.mostrarMensaje("No se puede enviar un mensaje vacío.");
            return;
        }
        cliente.enviarMensaje(mensaje);
        vista.limpiarCampoMensaje();
    }
}
