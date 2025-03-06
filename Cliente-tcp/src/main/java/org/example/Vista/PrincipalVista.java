package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import java.util.List;

public class PrincipalVista extends JFrame {
    private JButton btConsultarSaldo, btConsignar, btConsultarMovimientos;
    private JTextArea mensajesTxt;
    private JLabel lblUsuario;
    private Controlador controlador;
    private String usuario;

    public PrincipalVista(String usuario) {
        this.setTitle("Cliente TCP");
        this.setSize(600, 400);
        this.setLayout(null);
        this.usuario = usuario;

        lblUsuario = new JLabel("Bienvenido, " + usuario);
        lblUsuario.setBounds(200, 10, 300, 20);
        this.add(lblUsuario);

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

        btConsultarSaldo.addActionListener(e -> abrirSaldoVista());
        btConsignar.addActionListener(e -> mostrarPantallaConsignacion());
        btConsultarMovimientos.addActionListener(e -> mostrarPantallaMovimientos());

        this.setVisible(true);
    }


    private void mostrarPantallaMovimientos() {
        if (controlador == null) {
            System.out.println("[PrincipalVista] Error: Controlador no asignado.");
            return;
        }

        String username = controlador.getUsuarioActual();
        List<String> cuentasUsuario = controlador.obtenerCuentasDelUsuario(username);

        if (cuentasUsuario.isEmpty()) {
            mostrarMensaje("No se encontraron cuentas asociadas al usuario.");
            return;
        }

        new MovimientosVista(controlador, cuentasUsuario);
    }

    private void mostrarPantallaConsignacion() {
        String username = controlador.getUsuarioActual();
        List<String> cuentasUsuario = controlador.obtenerCuentasDelUsuario(username);
        new ConsignacionVista(controlador, cuentasUsuario);
    }

    private void abrirSaldoVista() {
        SaldoVista saldoVista = new SaldoVista(controlador, usuario);
        saldoVista.setVisible(true);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarMensaje(String mensaje) {
        mensajesTxt.append(mensaje + "\n");
    }
}
