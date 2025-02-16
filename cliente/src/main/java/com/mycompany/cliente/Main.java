package com.mycompany.cliente;

import com.mycompany.cliente.*;



public class Main {
    public static void main(String[] args) {
        // Crear instancia de la Vista
        Vista vista = new Vista();

        // Crear instancia del Modelo (Cliente TCP)
        Cliente cliente = new Cliente(vista);

        // Crear instancia del Controlador
        Controlador controlador = new Controlador(vista, cliente);

        // Asignar el controlador a la vista
        vista.setControlador(controlador);

        // Mostrar la ventana del cliente
        java.awt.EventQueue.invokeLater(() -> vista.setVisible(true));
    }
}
