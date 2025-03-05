package org.example.Vista;

import org.example.Controlador.Controlador;

import javax.swing.*;
import java.awt.*;



public class SaldoVista extends JFrame {
    private JRadioButton rbNumeroCuenta;
    private JRadioButton rbCedula;
    private JTextField txtNumero;
    private JTextArea txtResultado;
    private JButton btnConsultar;
    private JButton btnCerrar;
    private ButtonGroup grupoOpciones;

    private Controlador controlador;
    private String username;

    public SaldoVista(Controlador controlador, String username) {
        this.controlador = controlador;
        this.username = username;

        setTitle("Consulta Saldo");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel lblTitulo = new JLabel("Consulta saldo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(80, 10, 220, 30);
        panel.add(lblTitulo);

        JLabel lblSeleccion = new JLabel("Selecciona por qué opción quieres consultar:");
        lblSeleccion.setBounds(40, 50, 300, 20);
        panel.add(lblSeleccion);

        rbNumeroCuenta = new JRadioButton("Número de cuenta", true);
        rbNumeroCuenta.setBounds(60, 75, 200, 20);
        panel.add(rbNumeroCuenta);

        rbCedula = new JRadioButton("Número de cédula");
        rbCedula.setBounds(60, 100, 200, 20);
        panel.add(rbCedula);

        grupoOpciones = new ButtonGroup();
        grupoOpciones.add(rbNumeroCuenta);
        grupoOpciones.add(rbCedula);

        JLabel lblNumero = new JLabel("Ingrese el número:");
        lblNumero.setBounds(40, 130, 200, 20);
        panel.add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(40, 155, 300, 25);
        panel.add(txtNumero);

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(40, 190, 130, 30);
        panel.add(btnConsultar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(210, 190, 130, 30);
        panel.add(btnCerrar);

        JLabel lblResultado = new JLabel("Resultado de la consulta:");
        lblResultado.setBounds(40, 230, 200, 20);
        panel.add(lblResultado);

        txtResultado = new JTextArea();
        txtResultado.setBounds(40, 250, 300, 250);
        txtResultado.setEditable(false);
        txtResultado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(txtResultado);

        add(panel);

        btnCerrar.addActionListener(e -> dispose());


        btnConsultar.addActionListener(e -> {
            String numero = txtNumero.getText().trim();
            String tipo = rbNumeroCuenta.isSelected() ? "CUENTA" : "CEDULA";

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controlador.consultarSaldoAvanzado(username, tipo, numero, this);
        });
    }

    public void mostrarMensaje(String mensaje) {
        txtResultado.append(mensaje + "\n");
    }

}
