package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MovimientosVista extends JFrame {
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    private JLabel lblNumeroCuenta;
    private Controlador controlador;
    private String numeroCuenta;

    public MovimientosVista(Controlador controlador, String numeroCuenta) {
        this.controlador = controlador;
        this.numeroCuenta = numeroCuenta;

        this.setTitle("Movimientos de Cuenta");
        this.setSize(600, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        lblNumeroCuenta = new JLabel("Número de cuenta: " + numeroCuenta);
        lblNumeroCuenta.setBounds(20, 10, 300, 20);
        this.add(lblNumeroCuenta);

        String[] columnas = {"Movimiento", "No Cuenta", "Valor", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaMovimientos = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaMovimientos);
        scrollPane.setBounds(20, 40, 540, 180);
        this.add(scrollPane);

        this.setVisible(true);
    }

    public void agregarMovimiento(String movimiento, String noCuenta, String valor, String descripcion) {
        modeloTabla.addRow(new Object[]{movimiento, noCuenta, valor, descripcion});
    }
}
