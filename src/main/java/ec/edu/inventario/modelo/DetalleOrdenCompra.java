package ec.edu.inventario.modelo;

public class DetalleOrdenCompra extends DetalleTransaccion {

    public DetalleOrdenCompra(int numeroLinea, int cantidad, double precioUnitario, Producto producto) {
        super(numeroLinea, cantidad, precioUnitario, producto);
    }

    @Override
    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
