package org.example.Vista;

import org.example.Controlador.Controlador;
import org.example.Modelo.ListenerC;

import javax.swing.*;

public class PrincipalVista extends JFrame implements ListenerC {
    private JButton btConsultarSaldo, btConsignar, btConsultarMovimientos;
    private JTextArea mensajesTxt;
    private JLabel lblUsuario;
    private Controlador controlador;

    public PrincipalVista(String usuario, String numeroCuenta) {
        this.setTitle("Cliente TCP");
        this.setSize(600, 400);
        this.setLayout(null);

        lblUsuario = new JLabel("Bienvenido, " + usuario);
        lblUsuario.setBounds(200, 10, 300, 20);
        this.add(lblUsuario);

        // Botones organizados en 2 filas
        btConsultarSaldo = new JButton("Consultar Saldo");
        btConsultarSaldo.setBounds(100, 50, 180, 30);
        this.add(btConsultarSaldo);

        btConsultarMovimientos = new JButton("Consultar Movimientos");
        btConsultarMovimientos.setBounds(320, 50, 180, 30);
        this.add(btConsultarMovimientos);

        btConsignar = new JButton("Consignar");
        btConsignar.setBounds(210, 100, 180, 30);
        this.add(btConsignar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(30, 150, 520, 100);
        this.add(scrollPane);

        btConsultarSaldo.addActionListener(e -> controlador.consultarSaldo(numeroCuenta));
        btConsultarMovimientos.addActionListener(e -> mostrarPantallaMovimientos(numeroCuenta));
        btConsignar.addActionListener(e -> mostrarPantallaConsignacion());

        this.setVisible(true);
    }

    private void mostrarPantallaConsignacion() {
        new ConsignacionVista(controlador);
    }

    private void mostrarPantallaMovimientos(String numeroCuenta) {
        new MovimientosVista(controlador, numeroCuenta);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajesTxt.append(mensaje + "\n");
    }
}
