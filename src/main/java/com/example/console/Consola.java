package com.example.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class Consola extends JFrame implements KeyListener{
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private JLabel labelTitulo = new JLabel("Consola de Comandos en Java");
    private JTextField texto = new JTextField();
    private JLabel label2 = new JLabel("Ingresa el comando: ");
    private JTextArea areaTerminal = new JTextArea("");
    private boolean metalera =  false;
    private Process p;
    private String textoTerminal = "";
    private String path = "";
    public Consola(){
        initComponents();
        setTitle("Console");
        setLayout(new GridLayout(2,1));
        setBounds(200,50,600,350);
        //setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void initComponents(){
        panel.setLayout(null);
        panel.setBackground(Color.black);
        //label.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("terminal.jpg"))));
        label.setBackground(Color.black);
        label.setBounds(0, 0, 100, 90);
        label2.setBackground(Color.black);
        label2.setForeground(Color.green);
        label2.setFont(new Font("consolas",1,12));
        label2.setBounds(0, 50, 150, 100);
        labelTitulo.setBounds(200,10,300,20);
        labelTitulo.setBackground(Color.black);
        labelTitulo.setForeground(Color.red);
        labelTitulo.setFont(new Font("consolas",1,20));
        texto.setBounds(150,90,400,35);
        texto.setForeground(Color.green);
        texto.setBackground(Color.black);
        texto.setSelectionColor(Color.red);
        texto.setFont(new Font("consolas",1,12));
        areaTerminal.setBounds(0,150,300,500);
        areaTerminal.setBackground(Color.black);
        areaTerminal.setForeground(Color.green);
        areaTerminal.setSelectionColor(Color.red);
        areaTerminal.setFont(new Font("consolas",1,10));
        areaTerminal.setEnabled(false);
        path += currentDirectory.getAbsolutePath() + " : "+ "\n";
        areaTerminal.setText(path + textoTerminal);
        panel.add(labelTitulo);
        panel.add(label);
        panel.add(label2);
        panel.add(texto);
        add(panel,BorderLayout.NORTH);
        add(new JScrollPane(areaTerminal),BorderLayout.SOUTH);
        texto.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    if(texto.getText().equals(""))
                        metalera = false;
                    else
                        metalera = true;
                    if(metalera){
                        String cadena = texto.getText();
                        mensageSalida(cadena);
                    }
                    texto.setText("");
                }
            }
        });
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    private File currentDirectory = new File(System.getProperty("user.dir")); // directorio actual inicial

    public void mensageSalida(final String cadena){
        try {
            if (cadena.equals("cls") || cadena.equals("clear")) {
                textoTerminal = currentDirectory.getAbsolutePath() + " : ";
                areaTerminal.setText(textoTerminal);
                return;
            }

            if (cadena.startsWith("cd ")) {
                String newDir = cadena.substring(3).trim();
                File newDirectory = new File(currentDirectory, newDir);
                if (newDirectory.exists() && newDirectory.isDirectory()) {
                    currentDirectory = newDirectory;
                    textoTerminal = currentDirectory.getAbsolutePath() + " : " + "\n";
                    areaTerminal.setText(textoTerminal);
                    return;
                } else {
                    textoTerminal += "Error: el directorio no existe.\n";
                    areaTerminal.setText(textoTerminal);
                    return;
                }
            }

            ProcessBuilder pb = new ProcessBuilder(cadena.split(" "));
            pb.directory(currentDirectory);

            try {
                p = pb.start();
            } catch (IOException iOException) {
                // Tratando de ejecutar comandos específicos de shell como aquellos que usan pipes, redirections, etc.
                pb = new ProcessBuilder("/bin/sh", "-c", cadena);
                pb.directory(currentDirectory);
                p = pb.start();
                System.out.println(iOException);
            }

            InputStream salida = p.getInputStream();
            BufferedReader leer = new BufferedReader(new InputStreamReader(salida));
            String lectura;
            while ((lectura = leer.readLine()) != null) {
                System.out.println(lectura);
                textoTerminal += lectura + "\n";
            }
            textoTerminal = currentDirectory.getAbsolutePath() + " : " + "\n" + textoTerminal;
            areaTerminal.setText(textoTerminal);
        } catch (Exception e) {
            textoTerminal += "Error: " + e.getMessage() + "\n";
            areaTerminal.setText(textoTerminal);
        }
    }



    public Image getIconImage() {
        Image iconoVentana = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("terminal.jpg"));
        return iconoVentana;
    }
}