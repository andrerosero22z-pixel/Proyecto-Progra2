package ec.edu.inventario.ui;

import ec.edu.inventario.modelo.DetalleOrdenCompra;
import ec.edu.inventario.modelo.OrdenCompra;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.modelo.Proveedor;
import ec.edu.inventario.persistencia.InventarioCSV;
import ec.edu.inventario.persistencia.OrdenCompraCSV;
import ec.edu.inventario.persistencia.ProductoCSV;
import ec.edu.inventario.persistencia.ProveedorCSV;
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

public class VentanaOrdenesCompra extends JFrame {

    private String numeroOrdenSeleccionada = null;
    private JComboBox<Proveedor> cmbProveedor;
    private JComboBox<Producto> cmbProducto;
    private JTextField txtCantidad;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private ProveedorCSV proveedorCSV;
    private ProductoCSV productoCSV;
    private OrdenCompraCSV ordenCompraCSV;
    private InventarioCSV inventarioCSV;
    private ArrayList<Proveedor> proveedores;
    private ArrayList<Producto> productos;

    public VentanaOrdenesCompra() {
        proveedorCSV = new ProveedorCSV();
        productoCSV = new ProductoCSV();
        ordenCompraCSV = new OrdenCompraCSV();
        inventarioCSV = new InventarioCSV();
        proveedores = new ArrayList<>();
        productos = new ArrayList<>();

        setTitle("Ordenes de compra");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarCombos();
        cargarTabla();
    }

    private void iniciarComponentes() {
        cmbProveedor = new JComboBox<>();
        cmbProducto = new JComboBox<>();
        txtCantidad = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        panelFormulario.add(new JLabel("Proveedor:"));
        panelFormulario.add(cmbProveedor);
        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(cmbProducto);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);

        modeloTabla = new DefaultTableModel(new Object[]{
                "Numero", "ID proveedor", "Proveedor", "ID producto", "Producto",
                "Cantidad", "Precio", "Total", "Estado", "Fecha"
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
                agregarOrden();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarOrden();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarOrden();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarOrden();
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
            proveedores = proveedorCSV.listar();
            productos = productoCSV.listar();

            cmbProveedor.removeAllItems();
            for (int i = 0; i < proveedores.size(); i++) {
                cmbProveedor.addItem(proveedores.get(i));
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
            proveedores = proveedorCSV.listar();
            productos = productoCSV.listar();
            modeloTabla.setRowCount(0);

            ArrayList<OrdenCompra> ordenes = ordenCompraCSV.listar(proveedores, productos);
            for (int i = 0; i < ordenes.size(); i++) {
                OrdenCompra orden = ordenes.get(i);
                if (orden.getDetalles().size() > 0) {
                    DetalleOrdenCompra detalle = orden.getDetalles().get(0);
                    String precioFormateado = String.format("%.2f", detalle.getPrecioUnitario());
                    String totalFormateado = String.format("%.2f", orden.getTotal());

                    modeloTabla.addRow(new Object[]{
                            orden.getNumeroOrden(),
                            orden.getProveedor().getIdEntidad(),
                            orden.getProveedor().getNombreEmpresa(),
                            detalle.getProducto().getIdProducto(),
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            precioFormateado,
                            totalFormateado,
                            orden.getEstado(),
                            orden.getFecha()
                    });
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de ordenes.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer ordenes: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarOrden() {
        if (!validarCampos()) {
            return;
        }

        try {
            String numero = ordenCompraCSV.generarNumero();
            OrdenCompra orden = crearOrdenDesdeCampos(numero);
            DetalleOrdenCompra detalle = orden.getDetalles().get(0);

            ordenCompraCSV.agregar(orden, proveedores, productos);
            inventarioCSV.aumentarStock(detalle.getProducto().getIdProducto(), detalle.getCantidad(), productos);

            JOptionPane.showMessageDialog(this, "Orden registrada correctamente.");
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

    private void editarOrden() {
        if (numeroOrdenSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de compra para editar.");
            return;
        }
        if (!validarCampos()) {
            return;
        }

        try {
            OrdenCompra ordenAnterior = ordenCompraCSV.buscarPorNumero(numeroOrdenSeleccionada, proveedores, productos);
            if (ordenAnterior == null || ordenAnterior.getDetalles().size() == 0) {
                JOptionPane.showMessageDialog(this, "No se encontro la orden.");
                return;
            }

            OrdenCompra ordenNueva = crearOrdenDesdeCampos(numeroOrdenSeleccionada);
            DetalleOrdenCompra detalleAnterior = ordenAnterior.getDetalles().get(0);
            DetalleOrdenCompra detalleNuevo = ordenNueva.getDetalles().get(0);

            ajustarStockAlEditar(detalleAnterior, detalleNuevo);
            ordenCompraCSV.editar(ordenNueva, proveedores, productos);

            JOptionPane.showMessageDialog(this, "Orden editada correctamente.");
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

    private void eliminarOrden() {
        if (numeroOrdenSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de compra para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar esta orden?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            OrdenCompra orden = ordenCompraCSV.buscarPorNumero(numeroOrdenSeleccionada, proveedores, productos);
            if (orden == null || orden.getDetalles().size() == 0) {
                JOptionPane.showMessageDialog(this, "No se encontro la orden.");
                return;
            }

            DetalleOrdenCompra detalle = orden.getDetalles().get(0);
            inventarioCSV.disminuirStock(detalle.getProducto().getIdProducto(), detalle.getCantidad(), productos);
            ordenCompraCSV.eliminar(numeroOrdenSeleccionada, proveedores, productos);

            JOptionPane.showMessageDialog(this, "Orden eliminada correctamente.");
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

    private void buscarOrden() {
        String textoNumero = JOptionPane.showInputDialog(this, "Ingrese el numero de la orden:");
        if (textoNumero == null) {
            return;
        }
        if (!Validaciones.esEnteroPositivo(textoNumero)) {
            JOptionPane.showMessageDialog(this, "El numero de la orden debe ser un entero mayor que cero.");
            return;
        }

        try {
            OrdenCompra orden = ordenCompraCSV.buscarPorNumero(textoNumero.trim(), proveedores, productos);
            if (orden == null) {
                JOptionPane.showMessageDialog(this, "No se encontro la orden.");
            } else {
                mostrarOrden(orden);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private OrdenCompra crearOrdenDesdeCampos(String numero) {
        Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
        Producto producto = (Producto) cmbProducto.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        OrdenCompra orden = new OrdenCompra(numero, proveedor);
        orden.agregarDetalle(new DetalleOrdenCompra(1, cantidad, producto.getPrecioCompra(), producto));
        orden.cambiarEstado("RECIBIDA");
        orden.setFecha(LocalDate.now());
        return orden;
    }

    private void ajustarStockAlEditar(DetalleOrdenCompra detalleAnterior, DetalleOrdenCompra detalleNuevo) throws Exception {
        int idAnterior = detalleAnterior.getProducto().getIdProducto();
        int idNuevo = detalleNuevo.getProducto().getIdProducto();

        if (idAnterior == idNuevo) {
            int diferencia = detalleNuevo.getCantidad() - detalleAnterior.getCantidad();
            if (diferencia > 0) {
                inventarioCSV.aumentarStock(idNuevo, diferencia, productos);
            } else if (diferencia < 0) {
                inventarioCSV.disminuirStock(idNuevo, diferencia * -1, productos);
            }
        } else {
            inventarioCSV.disminuirStock(idAnterior, detalleAnterior.getCantidad(), productos);
            inventarioCSV.aumentarStock(idNuevo, detalleNuevo.getCantidad(), productos);
        }
    }

    private boolean validarCampos() {
        if (cmbProveedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.");
            return false;
        }
        if (cmbProducto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.");
            return false;
        }
        if (Validaciones.estaVacio(txtCantidad.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad.");
            return false;
        }
        if (!Validaciones.esEnteroPositivo(txtCantidad.getText())) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un entero mayor que cero.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            numeroOrdenSeleccionada = modeloTabla.getValueAt(fila, 0).toString();
            seleccionarProveedorPorId(Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString()));
            seleccionarProductoPorId(Integer.parseInt(modeloTabla.getValueAt(fila, 3).toString()));
            txtCantidad.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }

    private void seleccionarProveedorPorId(int idProveedor) {
        for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
            Proveedor proveedor = cmbProveedor.getItemAt(i);
            if (proveedor.getIdEntidad() == idProveedor) {
                cmbProveedor.setSelectedIndex(i);
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

    private void mostrarOrden(OrdenCompra orden) {
        numeroOrdenSeleccionada = orden.getNumeroOrden();
        seleccionarProveedorPorId(orden.getProveedor().getIdEntidad());
        if (orden.getDetalles().size() > 0) {
            DetalleOrdenCompra detalle = orden.getDetalles().get(0);
            seleccionarProductoPorId(detalle.getProducto().getIdProducto());
            txtCantidad.setText(String.valueOf(detalle.getCantidad()));
        }
    }

    private void limpiarCampos() {
        txtCantidad.setText("");
        if (cmbProveedor.getItemCount() > 0) {
            cmbProveedor.setSelectedIndex(0);
        }
        if (cmbProducto.getItemCount() > 0) {
            cmbProducto.setSelectedIndex(0);
        }
        tabla.clearSelection();
        numeroOrdenSeleccionada = null;
    }
}
