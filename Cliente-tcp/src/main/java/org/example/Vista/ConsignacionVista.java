package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;

public class ConsignacionVista extends JFrame {
    private JTextField txtCuentaDestino, txtMonto;
    private JButton btConfirmar;
    private Controlador controlador;

    public ConsignacionVista(Controlador controlador) {
        this.controlador = controlador;
        this.setTitle("Consignación");
        this.setSize(400, 250);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        JLabel lblCuentaDestino = new JLabel("Cuenta destino:");
        lblCuentaDestino.setBounds(50, 30, 120, 20);
        this.add(lblCuentaDestino);

        txtCuentaDestino = new JTextField();
        txtCuentaDestino.setBounds(170, 30, 150, 25);
        this.add(txtCuentaDestino);

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(50, 70, 120, 20);
        this.add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(170, 70, 150, 25);
        this.add(txtMonto);

        btConfirmar = new JButton("Confirmar");
        btConfirmar.setBounds(120, 120, 150, 30);
        this.add(btConfirmar);

        btConfirmar.addActionListener(e -> controlador.realizarConsignacion(txtCuentaDestino.getText(), txtMonto.getText()));

        this.setVisible(true);
    }
}
