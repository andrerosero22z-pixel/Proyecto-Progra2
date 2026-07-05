package ec.edu.inventario.persistencia;

import ec.edu.inventario.modelo.Cliente;
import ec.edu.inventario.util.ArchivoUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ClienteCSV {

    private static final String ARCHIVO = "clientes.csv";

    public ArrayList<Cliente> listar() throws FileNotFoundException, IOException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 6) {
                    int id = Integer.parseInt(datos[0]);
                    clientes.add(new Cliente(id, datos[1], datos[2], datos[3], datos[4], datos[5]));
                }
            } catch (NumberFormatException e) {
                // Se omiten registros incorrectos.
            }
        }
        return clientes;
    }

    public void guardar(ArrayList<Cliente> clientes) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            lineas.add(cliente.getIdEntidad() + ";"
                    + ArchivoUtil.limpiarTexto(cliente.getCodigoCliente()) + ";"
                    + ArchivoUtil.limpiarTexto(cliente.getNombre()) + ";"
                    + ArchivoUtil.limpiarTexto(cliente.getTelefono()) + ";"
                    + ArchivoUtil.limpiarTexto(cliente.getCorreo()) + ";"
                    + ArchivoUtil.limpiarTexto(cliente.getDireccion()));
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public Cliente buscarPorId(int idCliente) throws FileNotFoundException, IOException {
        ArrayList<Cliente> clientes = listar();
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            if (cliente.getIdEntidad() == idCliente) {
                return cliente;
            }
        }
        return null;
    }

    public boolean existeId(int idCliente) throws FileNotFoundException, IOException {
        return buscarPorId(idCliente) != null;
    }

    public void agregar(Cliente cliente) throws Exception {
        ArrayList<Cliente> clientes = listar();
        if (existeId(cliente.getIdEntidad())) {
            throw new Exception("Ya existe un cliente con ese ID.");
        }
        clientes.add(cliente);
        guardar(clientes);
    }

    public void editar(Cliente clienteEditado) throws Exception {
        ArrayList<Cliente> clientes = listar();
        boolean encontrado = false;

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdEntidad() == clienteEditado.getIdEntidad()) {
                clientes.set(i, clienteEditado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro el cliente.");
        }
        guardar(clientes);
    }

    public void eliminar(int idCliente) throws Exception {
        ArrayList<Cliente> clientes = listar();
        boolean eliminado = false;

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdEntidad() == idCliente) {
                clientes.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro el cliente.");
        }
        guardar(clientes);
    }
}
