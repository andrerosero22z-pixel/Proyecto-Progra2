package ec.edu.inventario.ui;

import ec.edu.inventario.modelo.Cliente;
import ec.edu.inventario.modelo.DetallePedido;
import ec.edu.inventario.modelo.Pedido;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.persistencia.ClienteCSV;
import ec.edu.inventario.persistencia.InventarioCSV;
import ec.edu.inventario.persistencia.PedidoCSV;
import ec.edu.inventario.persistencia.ProductoCSV;
import ec.edu.inventario.util.Validaciones;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class VentanaPedidos extends JFrame {

    private JTextField txtNumero;
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<Producto> cmbProducto;
    private JTextField txtCantidad;
    private JTextField txtDescuento;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private ClienteCSV clienteCSV;
    private ProductoCSV productoCSV;
    private PedidoCSV pedidoCSV;
    private InventarioCSV inventarioCSV;
    private ArrayList<Cliente> clientes;
    private ArrayList<Producto> productos;

    public VentanaPedidos() {
        clienteCSV = new ClienteCSV();
        productoCSV = new ProductoCSV();
        pedidoCSV = new PedidoCSV();
        inventarioCSV = new InventarioCSV();
        clientes = new ArrayList<>();
        productos = new ArrayList<>();

        setTitle("Registro de pedidos");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarCombos();
        cargarTabla();
    }

    private void iniciarComponentes() {
        txtNumero = new JTextField();
        cmbCliente = new JComboBox<>();
        cmbProducto = new JComboBox<>();
        txtCantidad = new JTextField();
        txtDescuento = new JTextField("0");

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.add(new JLabel("Numero pedido:"));
        panelFormulario.add(txtNumero);
        panelFormulario.add(new JLabel("Cliente:"));
        panelFormulario.add(cmbCliente);
        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(cmbProducto);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel("Descuento:"));
        panelFormulario.add(txtDescuento);

        modeloTabla = new DefaultTableModel(new Object[]{
                "Numero", "ID cliente", "Cliente", "ID producto", "Producto",
                "Cantidad", "Precio", "Descuento", "Total", "Estado", "Fecha"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarFila();
                }
            }
        });

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPedido();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarPedido();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPedido();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPedido();
            }
        });
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 5, 5));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        try {
            clientes = clienteCSV.listar();
            productos = productoCSV.listar();

            cmbCliente.removeAllItems();
            for (int i = 0; i < clientes.size(); i++) {
                cmbCliente.addItem(clientes.get(i));
            }

            cmbProducto.removeAllItems();
            for (int i = 0; i < productos.size(); i++) {
                cmbProducto.addItem(productos.get(i));
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro un archivo necesario.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer datos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            clientes = clienteCSV.listar();
            productos = productoCSV.listar();
            modeloTabla.setRowCount(0);

            ArrayList<Pedido> pedidos = pedidoCSV.listar(clientes, productos);
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido pedido = pedidos.get(i);
                if (pedido.getDetalles().size() > 0) {
                    DetallePedido detalle = pedido.getDetalles().get(0);
                    modeloTabla.addRow(new Object[]{
                            pedido.getNumeroPedido(),
                            pedido.getCliente().getIdEntidad(),
                            pedido.getCliente().getNombre(),
                            detalle.getProducto().getIdProducto(),
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.getDescuento(),
                            pedido.getTotal(),
                            pedido.getEstado(),
                            pedido.getFecha()
                    });
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de pedidos.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer pedidos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarPedido() {
        if (!validarCampos()) {
            return;
        }

        try {
            Pedido pedido = crearPedidoDesdeCampos();
            DetallePedido detalle = pedido.getDetalles().get(0);
            int idProducto = detalle.getProducto().getIdProducto();
            int stockDisponible = inventarioCSV.obtenerStock(idProducto, productos);

            if (stockDisponible < detalle.getCantidad()) {
                JOptionPane.showMessageDialog(this, "No hay stock suficiente para este pedido.");
                return;
            }

            pedidoCSV.agregar(pedido, clientes, productos);
            inventarioCSV.disminuirStock(idProducto, detalle.getCantidad(), productos);

            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.");
            cargarCombos();
            cargarTabla();
            limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Revise los valores numericos.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editarPedido() {
        if (!validarCampos()) {
            return;
        }

        try {
            String numero = txtNumero.getText().trim();
            Pedido pedidoAnterior = pedidoCSV.buscarPorNumero(numero, clientes, productos);
            if (pedidoAnterior == null || pedidoAnterior.getDetalles().size() == 0) {
                JOptionPane.showMessageDialog(this, "No se encontro el pedido.");
                return;
            }

            Pedido pedidoNuevo = crearPedidoDesdeCampos();
            DetallePedido detalleAnterior = pedidoAnterior.getDetalles().get(0);
            DetallePedido detalleNuevo = pedidoNuevo.getDetalles().get(0);

            int idProductoAnterior = detalleAnterior.getProducto().getIdProducto();
            int idProductoNuevo = detalleNuevo.getProducto().getIdProducto();
            int stockDisponible = inventarioCSV.obtenerStock(idProductoNuevo, productos);

            if (idProductoAnterior == idProductoNuevo) {
                stockDisponible = stockDisponible + detalleAnterior.getCantidad();
            }

            if (stockDisponible < detalleNuevo.getCantidad()) {
                JOptionPane.showMessageDialog(this, "No hay stock suficiente para editar el pedido.");
                return;
            }

            ajustarStockAlEditar(detalleAnterior, detalleNuevo);
            pedidoCSV.editar(pedidoNuevo, clientes, productos);

            JOptionPane.showMessageDialog(this, "Pedido editado correctamente.");
            cargarCombos();
            cargarTabla();
            limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Revise los valores numericos.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void eliminarPedido() {
        if (Validaciones.estaVacio(txtNumero.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el numero del pedido.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este pedido?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String numero = txtNumero.getText().trim();
            Pedido pedido = pedidoCSV.buscarPorNumero(numero, clientes, productos);
            if (pedido == null || pedido.getDetalles().size() == 0) {
                JOptionPane.showMessageDialog(this, "No se encontro el pedido.");
                return;
            }

            DetallePedido detalle = pedido.getDetalles().get(0);
            inventarioCSV.aumentarStock(detalle.getProducto().getIdProducto(), detalle.getCantidad(), productos);
            pedidoCSV.eliminar(numero, clientes, productos);

            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
            cargarCombos();
            cargarTabla();
            limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Revise los valores numericos.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void buscarPedido() {
        if (Validaciones.estaVacio(txtNumero.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el numero del pedido.");
            return;
        }

        try {
            Pedido pedido = pedidoCSV.buscarPorNumero(txtNumero.getText().trim(), clientes, productos);
            if (pedido == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el pedido.");
            } else {
                mostrarPedido(pedido);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private Pedido crearPedidoDesdeCampos() {
        String numero = txtNumero.getText().trim();
        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
        Producto producto = (Producto) cmbProducto.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());
        double descuento = Double.parseDouble(txtDescuento.getText().trim());

        Pedido pedido = new Pedido(numero, cliente);
        pedido.agregarDetalle(new DetallePedido(1, cantidad, producto.getPrecioVenta(), producto, descuento));
        pedido.cambiarEstado("CONFIRMADO");
        pedido.setFecha(LocalDate.now());
        return pedido;
    }

    private void ajustarStockAlEditar(DetallePedido detalleAnterior, DetallePedido detalleNuevo) throws Exception {
        int idAnterior = detalleAnterior.getProducto().getIdProducto();
        int idNuevo = detalleNuevo.getProducto().getIdProducto();

        if (idAnterior == idNuevo) {
            int diferencia = detalleNuevo.getCantidad() - detalleAnterior.getCantidad();
            if (diferencia > 0) {
                inventarioCSV.disminuirStock(idNuevo, diferencia, productos);
            } else if (diferencia < 0) {
                inventarioCSV.aumentarStock(idNuevo, diferencia * -1, productos);
            }
        } else {
            inventarioCSV.aumentarStock(idAnterior, detalleAnterior.getCantidad(), productos);
            inventarioCSV.disminuirStock(idNuevo, detalleNuevo.getCantidad(), productos);
        }
    }

    private boolean validarCampos() {
        if (cmbCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.");
            return false;
        }
        if (cmbProducto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.");
            return false;
        }
        if (Validaciones.estaVacio(txtNumero.getText())
                || Validaciones.estaVacio(txtCantidad.getText())
                || Validaciones.estaVacio(txtDescuento.getText())) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        if (!Validaciones.esEnteroPositivo(txtCantidad.getText())) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
            return false;
        }
        if (!Validaciones.esDecimalNoNegativo(txtDescuento.getText())) {
            JOptionPane.showMessageDialog(this, "El descuento no puede ser negativo.");
            return false;
        }

        Producto producto = (Producto) cmbProducto.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());
        double descuento = Double.parseDouble(txtDescuento.getText().trim());
        double subtotal = cantidad * producto.getPrecioVenta();

        if (descuento > subtotal) {
            JOptionPane.showMessageDialog(this, "El descuento no puede ser mayor al subtotal.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtNumero.setText(modeloTabla.getValueAt(fila, 0).toString());
            seleccionarClientePorId(Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString()));
            seleccionarProductoPorId(Integer.parseInt(modeloTabla.getValueAt(fila, 3).toString()));
            txtCantidad.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtDescuento.setText(modeloTabla.getValueAt(fila, 7).toString());
        }
    }

    private void seleccionarClientePorId(int idCliente) {
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            Cliente cliente = cmbCliente.getItemAt(i);
            if (cliente.getIdEntidad() == idCliente) {
                cmbCliente.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarProductoPorId(int idProducto) {
        for (int i = 0; i < cmbProducto.getItemCount(); i++) {
            Producto producto = cmbProducto.getItemAt(i);
            if (producto.getIdProducto() == idProducto) {
                cmbProducto.setSelectedIndex(i);
                return;
            }
        }
    }

    private void mostrarPedido(Pedido pedido) {
        txtNumero.setText(pedido.getNumeroPedido());
        seleccionarClientePorId(pedido.getCliente().getIdEntidad());
        if (pedido.getDetalles().size() > 0) {
            DetallePedido detalle = pedido.getDetalles().get(0);
            seleccionarProductoPorId(detalle.getProducto().getIdProducto());
            txtCantidad.setText(String.valueOf(detalle.getCantidad()));
            txtDescuento.setText(String.valueOf(detalle.getDescuento()));
        }
    }

    private void limpiarCampos() {
        txtNumero.setText("");
        txtCantidad.setText("");
        txtDescuento.setText("0");
        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
        }
        if (cmbProducto.getItemCount() > 0) {
            cmbProducto.setSelectedIndex(0);
        }
        tabla.clearSelection();
    }
}
