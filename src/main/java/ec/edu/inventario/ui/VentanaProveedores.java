package ec.edu.inventario.ui;

import ec.edu.inventario.modelo.Proveedor;
import ec.edu.inventario.persistencia.ProveedorCSV;
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

public class VentanaProveedores extends JFrame {

    private JTextField txtId;
    private JTextField txtRuc;
    private JTextField txtNombreEmpresa;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ProveedorCSV proveedorCSV;

    public VentanaProveedores() {
        proveedorCSV = new ProveedorCSV();

        setTitle("Gestion de proveedores");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        txtId = new JTextField();
        txtRuc = new JTextField();
        txtNombreEmpresa = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);
        panelFormulario.add(new JLabel("RUC:"));
        panelFormulario.add(txtRuc);
        panelFormulario.add(new JLabel("Empresa:"));
        panelFormulario.add(txtNombreEmpresa);
        panelFormulario.add(new JLabel("Telefono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Direccion:"));
        panelFormulario.add(txtDireccion);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "RUC", "Empresa", "Telefono", "Correo", "Direccion"}, 0) {
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
                agregarProveedor();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProveedor();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProveedor();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProveedor();
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
            ArrayList<Proveedor> proveedores = proveedorCSV.listar();
            for (int i = 0; i < proveedores.size(); i++) {
                Proveedor proveedor = proveedores.get(i);
                modeloTabla.addRow(new Object[]{
                        proveedor.getIdEntidad(),
                        proveedor.getRuc(),
                        proveedor.getNombreEmpresa(),
                        proveedor.getTelefono(),
                        proveedor.getCorreo(),
                        proveedor.getDireccion()
                });
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de proveedores.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer proveedores: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarProveedor() {
        if (!validarCampos()) {
            return;
        }

        try {
            Proveedor proveedor = crearProveedorDesdeCampos();
            proveedorCSV.agregar(proveedor);
            JOptionPane.showMessageDialog(this, "Proveedor agregado correctamente.");
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

    private void editarProveedor() {
        if (!validarCampos()) {
            return;
        }

        try {
            Proveedor proveedor = crearProveedorDesdeCampos();
            proveedorCSV.editar(proveedor);
            JOptionPane.showMessageDialog(this, "Proveedor editado correctamente.");
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

    private void eliminarProveedor() {
        if (Validaciones.estaVacio(txtId.getText()) || !Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID valido para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            proveedorCSV.eliminar(id);
            JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.");
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

    private void buscarProveedor() {
        if (Validaciones.estaVacio(txtId.getText()) || !Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID valido para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            Proveedor proveedor = proveedorCSV.buscarPorId(id);
            if (proveedor == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el proveedor.");
            } else {
                mostrarProveedor(proveedor);
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

    private Proveedor crearProveedorDesdeCampos() {
        int id = Integer.parseInt(txtId.getText().trim());
        return new Proveedor(
                id,
                txtRuc.getText().trim(),
                txtNombreEmpresa.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDireccion.getText().trim());
    }

    private boolean validarCampos() {
        if (Validaciones.estaVacio(txtId.getText())
                || Validaciones.estaVacio(txtRuc.getText())
                || Validaciones.estaVacio(txtNombreEmpresa.getText())
                || Validaciones.estaVacio(txtTelefono.getText())
                || Validaciones.estaVacio(txtCorreo.getText())
                || Validaciones.estaVacio(txtDireccion.getText())) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        if (!Validaciones.esEntero(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numerico.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtRuc.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombreEmpresa.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }

    private void mostrarProveedor(Proveedor proveedor) {
        txtId.setText(String.valueOf(proveedor.getIdEntidad()));
        txtRuc.setText(proveedor.getRuc());
        txtNombreEmpresa.setText(proveedor.getNombreEmpresa());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
        txtDireccion.setText(proveedor.getDireccion());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtRuc.setText("");
        txtNombreEmpresa.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        tabla.clearSelection();
    }
}
