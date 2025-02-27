package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;

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


        ((AbstractDocument) txtMonto.getDocument()).setDocumentFilter(new NumericFilter());

        btConfirmar = new JButton("Confirmar");
        btConfirmar.setBounds(120, 120, 150, 30);
        this.add(btConfirmar);

        btConfirmar.addActionListener(e -> {
            String montoText = txtMonto.getText();


            if (montoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El monto no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double monto = Double.parseDouble(montoText);
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor que 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controlador.realizarConsignacion(txtCuentaDestino.getText(), montoText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.setVisible(true);
    }


    static class NumericFilter extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
