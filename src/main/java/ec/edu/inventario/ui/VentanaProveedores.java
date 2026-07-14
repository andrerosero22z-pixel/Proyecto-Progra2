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

    private Integer idProveedorSeleccionado = null;
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
        txtRuc = new JTextField();
        txtNombreEmpresa = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
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
        if (!validarCampos(-1)) {
            return;
        }

        try {
            int id = proveedorCSV.generarId();
            Proveedor proveedor = crearProveedorDesdeCampos(id);
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
        if (idProveedorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para editar.");
            return;
        }
        if (!validarCampos(idProveedorSeleccionado)) {
            return;
        }

        try {
            Proveedor proveedor = crearProveedorDesdeCampos(idProveedorSeleccionado);
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
        if (idProveedorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            proveedorCSV.eliminar(idProveedorSeleccionado);
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
        String textoId = JOptionPane.showInputDialog(this, "Ingrese el ID del proveedor:");
        if (textoId == null) {
            return;
        }
        if (!Validaciones.esEnteroPositivo(textoId)) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un entero mayor que cero.");
            return;
        }

        try {
            int id = Integer.parseInt(textoId.trim());
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

    private Proveedor crearProveedorDesdeCampos(int id) {
        return new Proveedor(
                id,
                txtRuc.getText().trim(),
                txtNombreEmpresa.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDireccion.getText().trim());
    }

    private boolean validarCampos(int idExcluir) {
        if (Validaciones.estaVacio(txtRuc.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el RUC.");
            return false;
        }
        if (!Validaciones.esRucValido(txtRuc.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un RUC valido de 13 digitos.");
            return false;
        }
        if (Validaciones.estaVacio(txtNombreEmpresa.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la empresa.");
            return false;
        }
        if (!Validaciones.esTextoValido(txtNombreEmpresa.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de empresa valido.");
            return false;
        }
        if (Validaciones.estaVacio(txtTelefono.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el telefono.");
            return false;
        }
        if (!Validaciones.esTelefonoValido(txtTelefono.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un telefono valido de 9 o 10 digitos.");
            return false;
        }
        if (Validaciones.estaVacio(txtCorreo.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el correo.");
            return false;
        }
        if (!Validaciones.esCorreoValido(txtCorreo.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo valido.");
            return false;
        }
        if (Validaciones.estaVacio(txtDireccion.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese la direccion.");
            return false;
        }

        try {
            if (proveedorCSV.existeRuc(txtRuc.getText(), idExcluir)) {
                JOptionPane.showMessageDialog(this, "Ya existe un proveedor con ese RUC.");
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo validar el RUC del proveedor.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idProveedorSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            txtRuc.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombreEmpresa.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }

    private void mostrarProveedor(Proveedor proveedor) {
        idProveedorSeleccionado = proveedor.getIdEntidad();
        txtRuc.setText(proveedor.getRuc());
        txtNombreEmpresa.setText(proveedor.getNombreEmpresa());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
        txtDireccion.setText(proveedor.getDireccion());
    }

    private void limpiarCampos() {
        txtRuc.setText("");
        txtNombreEmpresa.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        tabla.clearSelection();
        idProveedorSeleccionado = null;
    }
}
