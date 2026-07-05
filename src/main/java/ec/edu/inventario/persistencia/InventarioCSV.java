package ec.edu.inventario.persistencia;

import ec.edu.inventario.modelo.ItemInventario;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.util.ArchivoUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InventarioCSV {

    private static final String ARCHIVO = "inventario.csv";

    public ArrayList<ItemInventario> listar(ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<ItemInventario> items = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 4) {
                    int idItem = Integer.parseInt(datos[0]);
                    int idProducto = Integer.parseInt(datos[1]);
                    int stockActual = Integer.parseInt(datos[2]);
                    int stockMinimo = Integer.parseInt(datos[3]);
                    Producto producto = buscarProductoEnLista(productos, idProducto);

                    if (producto != null) {
                        items.add(new ItemInventario(idItem, producto, stockActual, stockMinimo));
                    }
                }
            } catch (NumberFormatException e) {
                // Se omiten registros incorrectos.
            }
        }
        return items;
    }

    public void guardar(ArrayList<ItemInventario> items) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            ItemInventario item = items.get(i);
            if (item.getProducto() != null) {
                lineas.add(item.getIdItemInventario() + ";"
                        + item.getProducto().getIdProducto() + ";"
                        + item.getStockActual() + ";"
                        + item.getStockMinimo());
            }
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public ItemInventario buscarPorId(int idItem, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<ItemInventario> items = listar(productos);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIdItemInventario() == idItem) {
                return items.get(i);
            }
        }
        return null;
    }

    public ItemInventario buscarPorProducto(int idProducto, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<ItemInventario> items = listar(productos);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getIdProducto() == idProducto) {
                return items.get(i);
            }
        }
        return null;
    }

    public boolean existeId(int idItem, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        return buscarPorId(idItem, productos) != null;
    }

    public boolean existeProducto(int idProducto, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        return buscarPorProducto(idProducto, productos) != null;
    }

    public int generarId(ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<ItemInventario> items = listar(productos);
        int mayor = 0;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIdItemInventario() > mayor) {
                mayor = items.get(i).getIdItemInventario();
            }
        }
        return mayor + 1;
    }

    public void agregar(ItemInventario item, ArrayList<Producto> productos) throws Exception {
        ArrayList<ItemInventario> items = listar(productos);

        if (existeId(item.getIdItemInventario(), productos)) {
            throw new Exception("Ya existe un item de inventario con ese ID.");
        }
        if (existeProducto(item.getProducto().getIdProducto(), productos)) {
            throw new Exception("Ese producto ya esta registrado en inventario.");
        }

        items.add(item);
        guardar(items);
    }

    public void editar(ItemInventario itemEditado, ArrayList<Producto> productos) throws Exception {
        ArrayList<ItemInventario> items = listar(productos);
        boolean encontrado = false;

        for (int i = 0; i < items.size(); i++) {
            ItemInventario item = items.get(i);
            if (item.getIdItemInventario() != itemEditado.getIdItemInventario()
                    && item.getProducto().getIdProducto() == itemEditado.getProducto().getIdProducto()) {
                throw new Exception("Ese producto ya esta registrado en otro item.");
            }

            if (item.getIdItemInventario() == itemEditado.getIdItemInventario()) {
                items.set(i, itemEditado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro el item de inventario.");
        }
        guardar(items);
    }

    public void eliminar(int idItem, ArrayList<Producto> productos) throws Exception {
        ArrayList<ItemInventario> items = listar(productos);
        boolean eliminado = false;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIdItemInventario() == idItem) {
                items.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro el item de inventario.");
        }
        guardar(items);
    }

    public void eliminarPorProducto(int idProducto, ArrayList<Producto> productos) throws IOException {
        ArrayList<ItemInventario> items = listar(productos);

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getIdProducto() == idProducto) {
                items.remove(i);
                break;
            }
        }
        guardar(items);
    }

    public void crearItemParaProducto(Producto producto, ArrayList<Producto> productos) throws Exception {
        if (producto == null) {
            return;
        }
        if (!existeProducto(producto.getIdProducto(), productos)) {
            ItemInventario item = new ItemInventario(generarId(productos), producto, 0, 0);
            ArrayList<ItemInventario> items = listar(productos);
            items.add(item);
            guardar(items);
        }
    }

    public int obtenerStock(int idProducto, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ItemInventario item = buscarPorProducto(idProducto, productos);
        if (item == null) {
            return 0;
        }
        return item.getStockActual();
    }

    public void aumentarStock(int idProducto, int cantidad, ArrayList<Producto> productos) throws Exception {
        ArrayList<ItemInventario> items = listar(productos);
        ItemInventario itemEncontrado = null;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getIdProducto() == idProducto) {
                itemEncontrado = items.get(i);
            }
        }

        if (itemEncontrado == null) {
            Producto producto = buscarProductoEnLista(productos, idProducto);
            if (producto == null) {
                throw new Exception("No se encontro el producto para actualizar stock.");
            }
            itemEncontrado = new ItemInventario(generarId(productos), producto, 0, 0);
            items.add(itemEncontrado);
        }

        itemEncontrado.setStockActual(itemEncontrado.getStockActual() + cantidad);
        guardar(items);
    }

    public void disminuirStock(int idProducto, int cantidad, ArrayList<Producto> productos) throws Exception {
        ArrayList<ItemInventario> items = listar(productos);
        ItemInventario itemEncontrado = null;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getIdProducto() == idProducto) {
                itemEncontrado = items.get(i);
            }
        }

        if (itemEncontrado == null) {
            throw new Exception("El producto no existe en inventario.");
        }
        if (itemEncontrado.getStockActual() < cantidad) {
            throw new Exception("No hay stock suficiente.");
        }

        itemEncontrado.setStockActual(itemEncontrado.getStockActual() - cantidad);
        guardar(items);
    }

    private Producto buscarProductoEnLista(ArrayList<Producto> productos, int idProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == idProducto) {
                return productos.get(i);
            }
        }
        return null;
    }
}
