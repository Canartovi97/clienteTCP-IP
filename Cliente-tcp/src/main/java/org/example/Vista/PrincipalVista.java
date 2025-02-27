package org.example.Vista;

import org.example.Controlador.Controlador;
import org.example.Modelo.ListenerC;

import javax.swing.*;

public class PrincipalVista extends JFrame implements ListenerC {
    private JButton btConsultarSaldo, btConsignar;
    private JTextArea mensajesTxt;
    private JLabel lblUsuario;
    private Controlador controlador;

    public PrincipalVista(String usuario) {
        this.setTitle("Cliente TCP");
        this.setSize(500, 400);
        this.setLayout(null);

        lblUsuario = new JLabel("Bienvenido, " + usuario);
        lblUsuario.setBounds(180, 10, 200, 20);
        this.add(lblUsuario);

        btConsultarSaldo = new JButton("Consultar Saldo");
        btConsultarSaldo.setBounds(50, 50, 150, 30);
        this.add(btConsultarSaldo);

        btConsignar = new JButton("Consignar");
        btConsignar.setBounds(250, 50, 150, 30);
        this.add(btConsignar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(50, 100, 350, 100);
        this.add(scrollPane);

        btConsultarSaldo.addActionListener(e -> {
            String numeroCuenta = JOptionPane.showInputDialog(this, "Ingrese el número de cuenta:");
            if (numeroCuenta != null && !numeroCuenta.trim().isEmpty()) {
                controlador.consultarSaldo(numeroCuenta);
            } else {
                mostrarMensaje("Debe ingresar un número de cuenta.");
            }
        });

        btConsignar.addActionListener(e -> mostrarPantallaConsignacion());

        this.setVisible(true);
    }

    private void mostrarPantallaConsignacion() {
        new ConsignacionVista(controlador);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajesTxt.append(mensaje + "\n");
    }
}
