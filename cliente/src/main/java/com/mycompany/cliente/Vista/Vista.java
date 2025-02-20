package com.mycompany.cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Vista extends JFrame implements ListenerC {
    private JButton bConectar, btEnviar;
    private JTextArea mensajesTxt;
    private JTextField mensajeTxt;
    private Controlador controlador;

    public Vista() {
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Cliente TCP");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        JLabel label = new JLabel("Cliente TCP");
        label.setBounds(200, 10, 100, 20);
        this.add(label);

        bConectar = new JButton("Conectar");
        bConectar.setBounds(50, 50, 150, 30);
        this.add(bConectar);

        btEnviar = new JButton("Enviar");
        btEnviar.setBounds(250, 50, 150, 30);
        this.add(btEnviar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(50, 100, 350, 100);
        this.add(scrollPane);

        mensajeTxt = new JTextField();
        mensajeTxt.setBounds(50, 210, 250, 30);
        this.add(mensajeTxt);

        bConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador != null) controlador.conectarServidor();
            }
        });

        btEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador != null) controlador.enviarMensaje(mensajeTxt.getText());
            }
        });
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajesTxt.append(mensaje + "\n");
    }

    public void limpiarCampoMensaje() {
        mensajeTxt.setText("");
    }
}
