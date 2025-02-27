package org.example.Vista;

import org.example.Controlador.Controlador;
import org.example.Modelo.ListenerC;

import javax.swing.*;

public class LoginVista extends JFrame implements ListenerC {
    private JTextField txtUsername, txtPassword;
    private JButton btLogin, btConectar;
    private JTextArea mensajesTxt;
    private Controlador controlador;
    private boolean servidorConectado = false;

    public LoginVista() {
        this.setTitle("Login - Cliente TCP");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(50, 30, 100, 20);
        this.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 30, 180, 25);
        txtUsername.setEnabled(false);
        this.add(txtUsername);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(50, 70, 100, 20);
        this.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 70, 180, 25);
        txtPassword.setEnabled(false);
        this.add(txtPassword);

        btLogin = new JButton("Iniciar sesión");
        btLogin.setBounds(120, 110, 150, 30);
        btLogin.setEnabled(false);
        this.add(btLogin);

        btConectar = new JButton("Conectar Servidor");
        btConectar.setBounds(120, 150, 150, 30);
        this.add(btConectar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(50, 190, 300, 50);
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
