package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsignacionVista extends JFrame {
    private JRadioButton[] opcionesCuentas;
    private JTextField txtCuentaDestino, txtMonto;
    private JButton btConfirmar;
    private Controlador controlador;
    private ButtonGroup grupoCuentas;

    public ConsignacionVista(Controlador controlador, List<String> cuentas) {
        this.controlador = controlador;
        this.setTitle("Consignación");
        this.setSize(400, 350);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        JLabel lblCuentaOrigen = new JLabel("Seleccione cuenta origen:");
        lblCuentaOrigen.setBounds(50, 20, 200, 20);
        this.add(lblCuentaOrigen);

        // Crear un panel para los RadioButtons
        JPanel panelCuentas = new JPanel();
        panelCuentas.setLayout(new GridLayout(cuentas.size(), 1));
        panelCuentas.setBounds(50, 45, 300, cuentas.size() * 30);
        this.add(panelCuentas);

        // Crear radio buttons dinámicamente para cada cuenta del usuario
        grupoCuentas = new ButtonGroup();
        opcionesCuentas = new JRadioButton[cuentas.size()];
        for (int i = 0; i < cuentas.size(); i++) {
            opcionesCuentas[i] = new JRadioButton(cuentas.get(i));
            grupoCuentas.add(opcionesCuentas[i]);
            panelCuentas.add(opcionesCuentas[i]);
        }

        // Seleccionar la primera cuenta por defecto
        if (opcionesCuentas.length > 0) {
            opcionesCuentas[0].setSelected(true);
        }

        JLabel lblCuentaDestino = new JLabel("Cuenta destino:");
        lblCuentaDestino.setBounds(50, 120, 120, 20);
        this.add(lblCuentaDestino);

        txtCuentaDestino = new JTextField();
        txtCuentaDestino.setBounds(170, 120, 150, 25);
        this.add(txtCuentaDestino);

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(50, 160, 120, 20);
        this.add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(170, 160, 150, 25);
        this.add(txtMonto);

        btConfirmar = new JButton("Confirmar");
        btConfirmar.setBounds(120, 210, 150, 30);
        this.add(btConfirmar);

        btConfirmar.addActionListener(e -> {
            String cuentaDestino = txtCuentaDestino.getText().trim();
            String montoText = txtMonto.getText().trim();

            if (cuentaDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la cuenta destino", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (montoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El monto no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cuentaOrigenSeleccionada = null;
            for (JRadioButton radioButton : opcionesCuentas) {
                if (radioButton.isSelected()) {
                    cuentaOrigenSeleccionada = radioButton.getText();
                    break;
                }
            }

            if (cuentaOrigenSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una cuenta de origen", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double monto = Double.parseDouble(montoText);
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor que 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controlador.realizarConsignacion(cuentaOrigenSeleccionada, cuentaDestino, montoText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.setVisible(true);
    }
}
