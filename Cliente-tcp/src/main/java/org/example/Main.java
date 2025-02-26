package org.example;

import org.example.Controlador.Controlador;
import org.example.Modelo.Cliente;
import org.example.Vista.Vista;

public class Main {
    public static void main(String[] args) {

        Vista vista = new Vista();


        Cliente cliente = new Cliente(vista);


        Controlador controlador = new Controlador(vista, cliente);


        vista.setControlador(controlador);


        java.awt.EventQueue.invokeLater(() -> vista.setVisible(true));



    }
}