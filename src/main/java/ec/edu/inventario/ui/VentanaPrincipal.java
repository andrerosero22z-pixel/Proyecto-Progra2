package ec.edu.inventario.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestion de Inventario para Tienda");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN DE INVENTARIO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 10, 30, 10));

        JButton btnClientes = new JButton("Gestionar clientes");
        btnClientes.setFocusPainted(false);

        JButton btnProveedores = new JButton("Gestionar proveedores");
        btnProveedores.setFocusPainted(false);

        JButton btnProductos = new JButton("Gestionar productos");
        btnProductos.setFocusPainted(false);

        JButton btnInventario = new JButton("Ver inventario");
        btnInventario.setFocusPainted(false);

        JButton btnPedidos = new JButton("Registrar pedido");
        btnPedidos.setFocusPainted(false);

        JButton btnOrdenesCompra = new JButton("Registrar orden de compra");
        btnOrdenesCompra.setFocusPainted(false);

        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 17);

        btnClientes.setFont(fuenteBoton);
        btnProveedores.setFont(fuenteBoton);
        btnProductos.setFont(fuenteBoton);
        btnInventario.setFont(fuenteBoton);
        btnPedidos.setFont(fuenteBoton);
        btnOrdenesCompra.setFont(fuenteBoton);

        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaClientes().setVisible(true);
            }
        });

        btnProveedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaProveedores().setVisible(true);
            }
        });

        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaProductos().setVisible(true);
            }
        });

        btnInventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaInventario().setVisible(true);
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaPedidos().setVisible(true);
            }
        });

        btnOrdenesCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaOrdenesCompra().setVisible(true);
            }
        });

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnClientes);
        panelBotones.add(btnProveedores);
        panelBotones.add(btnProductos);
        panelBotones.add(btnInventario);
        panelBotones.add(btnPedidos);
        panelBotones.add(btnOrdenesCompra);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
    }
}
