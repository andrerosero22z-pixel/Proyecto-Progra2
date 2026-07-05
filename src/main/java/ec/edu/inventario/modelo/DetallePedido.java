package ec.edu.inventario.modelo;

public class DetallePedido extends DetalleTransaccion {

    private double descuento;

    public DetallePedido(int numeroLinea, int cantidad, double precioUnitario, Producto producto, double descuento) {
        super(numeroLinea, cantidad, precioUnitario, producto);
        this.descuento = descuento;
        this.subtotal = calcularSubtotal();
    }

    public void aplicarDescuento(double descuento) {
        this.descuento = descuento;
        this.subtotal = calcularSubtotal();
    }

    @Override
    public double calcularSubtotal() {
        return (cantidad * precioUnitario) - descuento;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
