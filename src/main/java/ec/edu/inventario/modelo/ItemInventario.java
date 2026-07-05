package ec.edu.inventario.modelo;

public class ItemInventario {

    private int idItemInventario;
    private Producto producto;
    private int stockActual;
    private int stockMinimo;

    public ItemInventario(int idItemInventario, Producto producto, int stockActual, int stockMinimo) {
        this.idItemInventario = idItemInventario;
        this.producto = producto;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            stockActual += cantidad;
        }
    }

    public void disminuirStock(int cantidad) {
        if (cantidad > 0 && cantidad <= stockActual) {
            stockActual -= cantidad;
        }
    }

    public boolean estaBajoStock() {
        return stockActual <= stockMinimo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getIdItemInventario() {
        return idItemInventario;
    }

    public void setIdItemInventario(int idItemInventario) {
        this.idItemInventario = idItemInventario;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
