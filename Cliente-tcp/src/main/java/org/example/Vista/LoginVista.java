package org.example.Vista;

import org.example.Controlador.Controlador;
import org.example.Modelo.ListenerC;

import javax.swing.*;
import java.awt.*;

public class LoginVista extends JFrame implements ListenerC {
    private JTextField txtUsername, txtPassword;
    private JButton btLogin, btConectar;
    private JTextArea mensajesTxt;
    private Controlador controlador;
    private boolean servidorConectado = false;

    public LoginVista() {
        this.setTitle("Login - Cliente TCP");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        JLabel lblTitulo = new JLabel("Login Banco TCP", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(80, 10, 220, 30);
        this.add(lblTitulo);


        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(50, 50, 100, 20);
        this.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 50, 180, 25);
        txtUsername.setEnabled(false);
        this.add(txtUsername);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(50, 90, 100, 20);
        this.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 90, 180, 25);
        txtPassword.setEnabled(false);
        this.add(txtPassword);

        btLogin = new JButton("Iniciar sesión");
        btLogin.setBounds(120, 130, 150, 30);
        btLogin.setEnabled(false);
        this.add(btLogin);

        btConectar = new JButton("Conectar Servidor");
        btConectar.setBounds(120, 170, 150, 30);
        this.add(btConectar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(50, 210, 300, 150);
        this.add(scrollPane);

        btLogin.addActionListener(e -> {
            if (controlador != null) controlador.validarLogin(txtUsername.getText(), txtPassword.getText());
        });

        btConectar.addActionListener(e -> {
            if (controlador != null) controlador.conectarServidor();
        });

        this.setVisible(true);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajesTxt.append(mensaje + "\n");
    }


    public void setBotonesHabilitados(boolean habilitado) {
        btLogin.setEnabled(habilitado);
        btConectar.setEnabled(habilitado);
        txtUsername.setEnabled(habilitado);
        txtPassword.setEnabled(habilitado);
    }

    public void actualizarEstadoServidor(boolean conectado) {
        servidorConectado = conectado;
        txtUsername.setEnabled(conectado);
        txtPassword.setEnabled(conectado);
        btLogin.setEnabled(conectado);
        if (!conectado) {
            mostrarMensaje("Error: No se pudo conectar al servidor.");
        }
    }
}
