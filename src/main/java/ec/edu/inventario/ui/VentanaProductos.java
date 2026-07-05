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

    private JTextField txtId;
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
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtPrecioCompra = new JTextField();
        txtPrecioVenta = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);
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
        if (!validarCampos()) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
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
        if (!validarCampos()) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
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
        if (Validaciones.estaVacio(txtId.getText()) || !Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID valido para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
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
        if (Validaciones.estaVacio(txtId.getText()) || !Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID valido para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
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

    private boolean validarCampos() {
        if (Validaciones.estaVacio(txtId.getText())
                || Validaciones.estaVacio(txtNombre.getText())
                || Validaciones.estaVacio(txtPrecioCompra.getText())
                || Validaciones.estaVacio(txtPrecioVenta.getText())) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        if (!Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numerico.");
            return false;
        }
        if (!Validaciones.esDecimalNoNegativo(txtPrecioCompra.getText())) {
            JOptionPane.showMessageDialog(this, "El precio de compra no puede ser negativo.");
            return false;
        }
        if (!Validaciones.esDecimalNoNegativo(txtPrecioVenta.getText())) {
            JOptionPane.showMessageDialog(this, "El precio de venta no puede ser negativo.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtPrecioCompra.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtPrecioVenta.setText(modeloTabla.getValueAt(fila, 3).toString());
        }
    }

    private void mostrarProducto(Producto producto) {
        txtId.setText(String.valueOf(producto.getIdProducto()));
        txtNombre.setText(producto.getNombre());
        txtPrecioCompra.setText(String.valueOf(producto.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        tabla.clearSelection();
    }
}
