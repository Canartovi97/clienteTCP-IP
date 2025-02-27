package org.example;

import org.example.Controlador.Controlador;
import org.example.Modelo.Cliente;
import org.example.Vista.LoginVista;

public class Main {
    public static void main(String[] args) {
        LoginVista loginVista = new LoginVista();
        Cliente cliente = new Cliente(loginVista);
        Controlador controlador = new Controlador(loginVista, cliente);
        loginVista.setControlador(controlador);

        java.awt.EventQueue.invokeLater(() -> loginVista.setVisible(true));
    }
}
