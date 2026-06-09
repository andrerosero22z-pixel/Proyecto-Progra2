package ec.edu.inventario.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Inventario para Tienda");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JLabel titulo = new JLabel("Sistema de Inventario", SwingConstants.CENTER);

        JButton btnClientes = new JButton("Gestionar clientes");
        JButton btnProveedores = new JButton("Gestionar proveedores");
        JButton btnProductos = new JButton("Gestionar productos");
        JButton btnInventario = new JButton("Ver inventario");
        JButton btnPedidos = new JButton("Registrar pedido");
        JButton btnOrdenesCompra = new JButton("Registrar orden de compra");

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 10, 10));
        panelBotones.add(btnClientes);
        panelBotones.add(btnProveedores);
        panelBotones.add(btnProductos);
        panelBotones.add(btnInventario);
        panelBotones.add(btnPedidos);
        panelBotones.add(btnOrdenesCompra);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }
}
