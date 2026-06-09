package ec.edu.inventario.modelo;

public class Producto {

    private int idProducto;
    private String nombre;
    private double precioCompra;
    private double precioVenta;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, double precioCompra, double precioVenta) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    public void actualizarPrecio(double precioCompra, double precioVenta) {
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    public String mostrarProducto() {
        return nombre + " | Compra: " + precioCompra + " | Venta: " + precioVenta;
    }

    public double obtenerPrecioVentaFinal() {
        return precioVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }
}
