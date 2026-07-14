package ec.edu.inventario.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import ec.edu.inventario.modelo.ItemInventario;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.persistencia.InventarioCSV;
import ec.edu.inventario.persistencia.ProductoCSV;
import ec.edu.inventario.util.Validaciones;

public class VentanaInventario extends JFrame {

    private Integer idItemSeleccionado = null;
    private JComboBox<Producto> cmbProducto;
    private JTextField txtStockActual;
    private JTextField txtStockMinimo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ProductoCSV productoCSV;
    private InventarioCSV inventarioCSV;
    private ArrayList<Producto> productos;

    public VentanaInventario() {
        productoCSV = new ProductoCSV();
        inventarioCSV = new InventarioCSV();
        productos = new ArrayList<>();

        setTitle("Inventario");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarProductos();
        cargarTabla();
    }

    private void iniciarComponentes() {
        cmbProducto = new JComboBox<>();
        txtStockActual = new JTextField();
        txtStockMinimo = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(cmbProducto);
        panelFormulario.add(new JLabel("Stock actual:"));
        panelFormulario.add(txtStockActual);
        panelFormulario.add(new JLabel("Stock minimo:"));
        panelFormulario.add(txtStockMinimo);

        modeloTabla = new DefaultTableModel(new Object[]{"ID item", "ID producto", "Producto", "Stock actual", "Stock minimo"}, 0) {
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

        // JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        // // btnAgregar.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         agregarItem();
        //     }
        // });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarItem();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarItem();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarItem();
            }
        });
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 5, 5));
        // panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarProductos() {
        try {
            productos = productoCSV.listar();
            cmbProducto.removeAllItems();
            for (int i = 0; i < productos.size(); i++) {
                cmbProducto.addItem(productos.get(i));
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de productos.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer productos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            productos = productoCSV.listar();
            modeloTabla.setRowCount(0);
            ArrayList<ItemInventario> items = inventarioCSV.listar(productos);

            for (int i = 0; i < items.size(); i++) {
                ItemInventario item = items.get(i);
                modeloTabla.addRow(new Object[]{
                        item.getIdItemInventario(),
                        item.getProducto().getIdProducto(),
                        item.getProducto().getNombre(),
                        item.getStockActual(),
                        item.getStockMinimo()
                });
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de inventario.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer inventario: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarItem() {
        if (!validarCampos()) {
            return;
        }

        try {
            int id = inventarioCSV.generarId(productos);
            ItemInventario item = crearItemDesdeCampos(id);
            inventarioCSV.agregar(item, productos);
            JOptionPane.showMessageDialog(this, "Item agregado correctamente.");
            cargarProductos();
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

    private void editarItem() {
        if (idItemSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de inventario para editar.");
            return;
        }
        if (!validarCampos()) {
            return;
        }

        try {
            ItemInventario item = crearItemDesdeCampos(idItemSeleccionado);
            inventarioCSV.editar(item, productos);
            JOptionPane.showMessageDialog(this, "Item editado correctamente.");
            cargarProductos();
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

    private void eliminarItem() {
        if (idItemSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de inventario para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este item?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            inventarioCSV.eliminar(idItemSeleccionado, productos);
            JOptionPane.showMessageDialog(this, "Item eliminado correctamente.");
            cargarProductos();
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

    private void buscarItem() {
        String textoId = JOptionPane.showInputDialog(this, "Ingrese el ID del item de inventario:");
        if (textoId == null) {
            return;
        }
        if (!Validaciones.esEnteroPositivo(textoId)) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un entero mayor que cero.");
            return;
        }

        try {
            int id = Integer.parseInt(textoId.trim());
            ItemInventario item = inventarioCSV.buscarPorId(id, productos);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el item.");
            } else {
                mostrarItem(item);
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

    private ItemInventario crearItemDesdeCampos(int id) {
        int stockActual = Integer.parseInt(txtStockActual.getText().trim());
        int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());
        Producto producto = (Producto) cmbProducto.getSelectedItem();
        return new ItemInventario(id, producto, stockActual, stockMinimo);
    }

    private boolean validarCampos() {
        if (cmbProducto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.");
            return false;
        }
        if (Validaciones.estaVacio(txtStockActual.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el stock actual.");
            return false;
        }
        if (!Validaciones.esEnteroNoNegativo(txtStockActual.getText())) {
            JOptionPane.showMessageDialog(this, "El stock actual debe ser un entero no negativo.");
            return false;
        }
        if (Validaciones.estaVacio(txtStockMinimo.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el stock minimo.");
            return false;
        }
        if (!Validaciones.esEnteroNoNegativo(txtStockMinimo.getText())) {
            JOptionPane.showMessageDialog(this, "El stock minimo debe ser un entero no negativo.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idItemSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            seleccionarProductoPorId(Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString()));
            txtStockActual.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtStockMinimo.setText(modeloTabla.getValueAt(fila, 4).toString());
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

    private void mostrarItem(ItemInventario item) {
        idItemSeleccionado = item.getIdItemInventario();
        seleccionarProductoPorId(item.getProducto().getIdProducto());
        txtStockActual.setText(String.valueOf(item.getStockActual()));
        txtStockMinimo.setText(String.valueOf(item.getStockMinimo()));
    }

    private void limpiarCampos() {
        txtStockActual.setText("");
        txtStockMinimo.setText("");
        if (cmbProducto.getItemCount() > 0) {
            cmbProducto.setSelectedIndex(0);
        }
        tabla.clearSelection();
        idItemSeleccionado = null;
    }
}
