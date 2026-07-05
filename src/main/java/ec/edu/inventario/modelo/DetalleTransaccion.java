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

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
