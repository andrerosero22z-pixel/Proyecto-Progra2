package ec.edu.inventario.persistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.util.ArchivoUtil;

public class ProductoCSV {

    private static final String ARCHIVO = "productos.csv";

    public ArrayList<Producto> listar() throws FileNotFoundException, IOException {
        ArrayList<Producto> productos = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 4) {
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    double precioCompra = Double.parseDouble(datos[2]);
                    double precioVenta = Double.parseDouble(datos[3]);
                    productos.add(new Producto(id, nombre, precioCompra, precioVenta));
                }
            } catch (NumberFormatException e) {
                // Si una linea esta danada, se omite para no cerrar el programa.
            }
        }
        return productos;
    }

    public void guardar(ArrayList<Producto> productos) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        lineas.add("id,nombre,precioCompra,precioVenta");
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            lineas.add(producto.getIdProducto() + ","
                    + ArchivoUtil.limpiarTexto(producto.getNombre()) + ","
                    + producto.getPrecioCompra() + ","
                    + producto.getPrecioVenta());
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public Producto buscarPorId(int idProducto) throws FileNotFoundException, IOException {
        ArrayList<Producto> productos = listar();
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            if (producto.getIdProducto() == idProducto) {
                return producto;
            }
        }
        return null;
    }

    public boolean existeId(int idProducto) throws FileNotFoundException, IOException {
        return buscarPorId(idProducto) != null;
    }

    public int generarId() throws FileNotFoundException, IOException {
        ArrayList<Producto> productos = listar();
        int mayor = 0;

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() > mayor) {
                mayor = productos.get(i).getIdProducto();
            }
        }
        return mayor + 1;
    }

    public boolean existeNombre(String nombre, int idExcluir) throws FileNotFoundException, IOException {
        ArrayList<Producto> productos = listar();
        String nombreBuscado = nombre.trim();

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            if (producto.getIdProducto() != idExcluir
                    && producto.getNombre().trim().equalsIgnoreCase(nombreBuscado)) {
                return true;
            }
        }
        return false;
    }

    public void agregar(Producto producto) throws Exception {
        ArrayList<Producto> productos = listar();
        if (existeId(producto.getIdProducto())) {
            throw new Exception("Ya existe un producto con ese ID.");
        }
        productos.add(producto);
        guardar(productos);
    }

    public void editar(Producto productoEditado) throws Exception {
        ArrayList<Producto> productos = listar();
        boolean encontrado = false;

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == productoEditado.getIdProducto()) {
                productos.set(i, productoEditado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro el producto.");
        }
        guardar(productos);
    }

    public void eliminar(int idProducto) throws Exception {
        ArrayList<Producto> productos = listar();
        boolean eliminado = false;

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == idProducto) {
                productos.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro el producto.");
        }
        guardar(productos);
    }
}
