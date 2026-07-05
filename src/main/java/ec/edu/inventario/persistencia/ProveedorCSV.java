package ec.edu.inventario.persistencia;

import ec.edu.inventario.modelo.Proveedor;
import ec.edu.inventario.util.ArchivoUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ProveedorCSV {

    private static final String ARCHIVO = "proveedores.csv";

    public ArrayList<Proveedor> listar() throws FileNotFoundException, IOException {
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 6) {
                    int id = Integer.parseInt(datos[0]);
                    proveedores.add(new Proveedor(id, datos[1], datos[2], datos[3], datos[4], datos[5]));
                }
            } catch (NumberFormatException e) {
                // Se omiten registros incorrectos.
            }
        }
        return proveedores;
    }

    public void guardar(ArrayList<Proveedor> proveedores) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            lineas.add(proveedor.getIdEntidad() + ";"
                    + ArchivoUtil.limpiarTexto(proveedor.getRuc()) + ";"
                    + ArchivoUtil.limpiarTexto(proveedor.getNombreEmpresa()) + ";"
                    + ArchivoUtil.limpiarTexto(proveedor.getTelefono()) + ";"
                    + ArchivoUtil.limpiarTexto(proveedor.getCorreo()) + ";"
                    + ArchivoUtil.limpiarTexto(proveedor.getDireccion()));
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public Proveedor buscarPorId(int idProveedor) throws FileNotFoundException, IOException {
        ArrayList<Proveedor> proveedores = listar();
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            if (proveedor.getIdEntidad() == idProveedor) {
                return proveedor;
            }
        }
        return null;
    }

    public boolean existeId(int idProveedor) throws FileNotFoundException, IOException {
        return buscarPorId(idProveedor) != null;
    }

    public void agregar(Proveedor proveedor) throws Exception {
        ArrayList<Proveedor> proveedores = listar();
        if (existeId(proveedor.getIdEntidad())) {
            throw new Exception("Ya existe un proveedor con ese ID.");
        }
        proveedores.add(proveedor);
        guardar(proveedores);
    }

    public void editar(Proveedor proveedorEditado) throws Exception {
        ArrayList<Proveedor> proveedores = listar();
        boolean encontrado = false;

        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdEntidad() == proveedorEditado.getIdEntidad()) {
                proveedores.set(i, proveedorEditado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro el proveedor.");
        }
        guardar(proveedores);
    }

    public void eliminar(int idProveedor) throws Exception {
        ArrayList<Proveedor> proveedores = listar();
        boolean eliminado = false;

        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdEntidad() == idProveedor) {
                proveedores.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro el proveedor.");
        }
        guardar(proveedores);
    }
}
