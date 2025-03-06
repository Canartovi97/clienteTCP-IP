package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MovimientosVista extends JFrame {
    private JTextArea areaMovimientos;
    private JComboBox<String> comboCuentas;
    private JButton btnConsultar;
    private Controlador controlador;

    public MovimientosVista(Controlador controlador, List<String> cuentas) {
        this.controlador = controlador;

        this.setTitle("Movimientos de Cuenta");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Seleccione una cuenta:"));
        comboCuentas = new JComboBox<>(cuentas.toArray(new String[0]));
        panelSuperior.add(comboCuentas);

        btnConsultar = new JButton("Consultar");
        panelSuperior.add(btnConsultar);

        this.add(panelSuperior, BorderLayout.NORTH);


        areaMovimientos = new JTextArea();
        areaMovimientos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaMovimientos);
        this.add(scrollPane, BorderLayout.CENTER);

        // Evento del botón consultar
        btnConsultar.addActionListener(e -> {
            String cuentaSeleccionada = (String) comboCuentas.getSelectedItem();
            if (cuentaSeleccionada != null) {
                controlador.consultarMovimientos(cuentaSeleccionada);
            }
        });

        this.setVisible(true);
    }

   /* public void actualizarMovimientos(String movimientos) {
        System.out.println("[MovimientosVista] Recibiendo movimientos:\n" + movimientos); // ✅ Verifica la recepción
        areaMovimientos.setText(movimientos); // 🔹 Mostrar los movimientos en el JTextArea
    }*/

    public void actualizarMovimientos(String movimientos) {
        System.out.println("[MovimientosVista] Recibiendo movimientos:\n" + movimientos); // ✅ Verifica la recepción

        SwingUtilities.invokeLater(() -> {
            areaMovimientos.setText(movimientos); // 🔹 Mostrar los movimientos en el JTextArea
        });
    }
}
