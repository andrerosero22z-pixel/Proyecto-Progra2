package ec.edu.inventario.ui;

import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.persistencia.InventarioCSV;
import ec.edu.inventario.persistencia.ProductoCSV;
import ec.edu.inventario.util.Validaciones;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
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

public class VentanaProductos extends JFrame {

    private Integer idProductoSeleccionado = null;
    private JTextField txtNombre;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ProductoCSV productoCSV;
    private InventarioCSV inventarioCSV;

    public VentanaProductos() {
        productoCSV = new ProductoCSV();
        inventarioCSV = new InventarioCSV();

        setTitle("Gestion de productos");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        txtNombre = new JTextField();
        txtPrecioCompra = new JTextField();
        txtPrecioVenta = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Precio compra:"));
        panelFormulario.add(txtPrecioCompra);
        panelFormulario.add(new JLabel("Precio venta:"));
        panelFormulario.add(txtPrecioVenta);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio compra", "Precio venta"}, 0) {
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
                agregarProducto();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProducto();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
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

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            ArrayList<Producto> productos = productoCSV.listar();
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                modeloTabla.addRow(new Object[]{
                        producto.getIdProducto(),
                        producto.getNombre(),
                        producto.getPrecioCompra(),
                        producto.getPrecioVenta()
                });
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de productos.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer productos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarProducto() {
        if (!validarCampos(-1)) {
            return;
        }

        try {
            int id = productoCSV.generarId();
            String nombre = txtNombre.getText().trim();
            double precioCompra = Double.parseDouble(txtPrecioCompra.getText().trim());
            double precioVenta = Double.parseDouble(txtPrecioVenta.getText().trim());

            Producto producto = new Producto(id, nombre, precioCompra, precioVenta);
            productoCSV.agregar(producto);

            ArrayList<Producto> productosActualizados = productoCSV.listar();
            inventarioCSV.crearItemParaProducto(producto, productosActualizados);

            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
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

    private void editarProducto() {
        if (idProductoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            return;
        }
        if (!validarCampos(idProductoSeleccionado)) {
            return;
        }

        try {
            int id = idProductoSeleccionado;
            String nombre = txtNombre.getText().trim();
            double precioCompra = Double.parseDouble(txtPrecioCompra.getText().trim());
            double precioVenta = Double.parseDouble(txtPrecioVenta.getText().trim());

            Producto producto = new Producto(id, nombre, precioCompra, precioVenta);
            productoCSV.editar(producto);
            JOptionPane.showMessageDialog(this, "Producto editado correctamente.");
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

    private void eliminarProducto() {
        if (idProductoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = idProductoSeleccionado;
            ArrayList<Producto> productos = productoCSV.listar();
            inventarioCSV.eliminarPorProducto(id, productos);
            productoCSV.eliminar(id);

            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            cargarTabla();
            limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numerico.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void buscarProducto() {
        String textoId = JOptionPane.showInputDialog(this, "Ingrese el ID del producto:");
        if (textoId == null) {
            return;
        }
        if (!Validaciones.esEnteroPositivo(textoId)) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un entero mayor que cero.");
            return;
        }

        try {
            int id = Integer.parseInt(textoId.trim());
            Producto producto = productoCSV.buscarPorId(id);

            if (producto == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el producto.");
            } else {
                mostrarProducto(producto);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numerico.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private boolean validarCampos(int idExcluir) {
        if (Validaciones.estaVacio(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto.");
            return false;
        }
        if (!Validaciones.esTextoValido(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de producto valido.");
            return false;
        }
        if (Validaciones.estaVacio(txtPrecioCompra.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el precio de compra.");
            return false;
        }
        if (!Validaciones.esDecimalPositivo(txtPrecioCompra.getText())) {
            JOptionPane.showMessageDialog(this, "El precio de compra debe ser mayor que cero.");
            return false;
        }
        if (Validaciones.estaVacio(txtPrecioVenta.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el precio de venta.");
            return false;
        }
        if (!Validaciones.esDecimalPositivo(txtPrecioVenta.getText())) {
            JOptionPane.showMessageDialog(this, "El precio de venta debe ser mayor que cero.");
            return false;
        }

        try {
            if (productoCSV.existeNombre(txtNombre.getText(), idExcluir)) {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con ese nombre.");
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo validar el nombre del producto.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idProductoSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtPrecioCompra.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtPrecioVenta.setText(modeloTabla.getValueAt(fila, 3).toString());
        }
    }

    private void mostrarProducto(Producto producto) {
        idProductoSeleccionado = producto.getIdProducto();
        txtNombre.setText(producto.getNombre());
        txtPrecioCompra.setText(String.valueOf(producto.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        tabla.clearSelection();
        idProductoSeleccionado = null;
    }
}
