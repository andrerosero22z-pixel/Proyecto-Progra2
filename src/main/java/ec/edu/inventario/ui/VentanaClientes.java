package ec.edu.inventario.ui;

import ec.edu.inventario.modelo.Cliente;
import ec.edu.inventario.persistencia.ClienteCSV;
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

public class VentanaClientes extends JFrame {

    private Integer idClienteSeleccionado = null;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ClienteCSV clienteCSV;

    public VentanaClientes() {
        clienteCSV = new ClienteCSV();

        setTitle("Gestion de clientes");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.add(new JLabel("Codigo:"));
        panelFormulario.add(txtCodigo);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Telefono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Direccion:"));
        panelFormulario.add(txtDireccion);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Codigo", "Nombre", "Telefono", "Correo", "Direccion"}, 0) {
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
                agregarCliente();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarCliente();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
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
            ArrayList<Cliente> clientes = clienteCSV.listar();
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                modeloTabla.addRow(new Object[]{
                        cliente.getIdEntidad(),
                        cliente.getCodigoCliente(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getCorreo(),
                        cliente.getDireccion()
                });
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontro el archivo de clientes.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer clientes: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void agregarCliente() {
        if (!validarCampos(-1)) {
            return;
        }

        try {
            int id = clienteCSV.generarId();
            Cliente cliente = crearClienteDesdeCampos(id);
            clienteCSV.agregar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente agregado correctamente.");
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

    private void editarCliente() {
        if (idClienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar.");
            return;
        }
        if (!validarCampos(idClienteSeleccionado)) {
            return;
        }

        try {
            Cliente cliente = crearClienteDesdeCampos(idClienteSeleccionado);
            clienteCSV.editar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente editado correctamente.");
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

    private void eliminarCliente() {
        if (idClienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Desea eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            clienteCSV.eliminar(idClienteSeleccionado);
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
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

    private void buscarCliente() {
        String textoId = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente:");
        if (textoId == null) {
            return;
        }
        if (!Validaciones.esEnteroPositivo(textoId)) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un entero mayor que cero.");
            return;
        }

        try {
            int id = Integer.parseInt(textoId.trim());
            Cliente cliente = clienteCSV.buscarPorId(id);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el cliente.");
            } else {
                mostrarCliente(cliente);
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

    private Cliente crearClienteDesdeCampos(int id) {
        return new Cliente(
                id,
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDireccion.getText().trim());
    }

    private boolean validarCampos(int idExcluir) {
        if (Validaciones.estaVacio(txtCodigo.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el codigo del cliente.");
            return false;
        }
        if (Validaciones.estaVacio(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.");
            return false;
        }
        if (!Validaciones.esTextoValido(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre valido.");
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
            if (clienteCSV.existeCodigo(txtCodigo.getText(), idExcluir)) {
                JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese codigo.");
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo validar el codigo del cliente.");
            return false;
        }
        return true;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idClienteSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            txtCodigo.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }

    private void mostrarCliente(Cliente cliente) {
        idClienteSeleccionado = cliente.getIdEntidad();
        txtCodigo.setText(cliente.getCodigoCliente());
        txtNombre.setText(cliente.getNombre());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        tabla.clearSelection();
        idClienteSeleccionado = null;
    }
}
