package ec.edu.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private int idInventario;
    private List<ItemInventario> items;

    public Inventario() {
        this.items = new ArrayList<>();
    }

    public void agregarItem(ItemInventario item) {
        items.add(item);
    }

    public ItemInventario buscarItem(Producto producto) {
        for (ItemInventario item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                return item;
            }
        }
        return null;
    }

    public boolean verificarStock(Producto producto, int cantidad) {
        ItemInventario item = buscarItem(producto);
        return item != null && item.getStockActual() >= cantidad;
    }

    public void aumentarStock(Producto producto, int cantidad) {
        ItemInventario item = buscarItem(producto);
        if (item != null) {
            item.aumentarStock(cantidad);
        }
    }

    public void disminuirStock(Producto producto, int cantidad) {
        ItemInventario item = buscarItem(producto);
        if (item != null) {
            item.disminuirStock(cantidad);
        }
    }

    public List<ItemInventario> listarStockBajo() {
        List<ItemInventario> stockBajo = new ArrayList<>();
        for (ItemInventario item : items) {
            if (item.estaBajoStock()) {
                stockBajo.add(item);
            }
        }
        return stockBajo;
    }
}
