package ec.edu.inventario.modelo;

public abstract class DetalleTransaccion {

    protected int numeroLinea;
    protected int cantidad;
    protected double precioUnitario;
    protected double subtotal;
    protected Producto producto;

    public DetalleTransaccion() {
    }

    public DetalleTransaccion(int numeroLinea, int cantidad, double precioUnitario, Producto producto) {
        this.numeroLinea = numeroLinea;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    public abstract double calcularSubtotal();

    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
