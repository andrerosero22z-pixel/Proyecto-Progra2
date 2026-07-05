package ec.edu.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class OrdenCompra extends Transaccion implements AfectaInventario {

    private String numeroOrden;
    private Proveedor proveedor;
    private List<DetalleOrdenCompra> detalles;

    public OrdenCompra(String numeroOrden, Proveedor proveedor) {
        super();
        this.numeroOrden = numeroOrden;
        this.proveedor = proveedor;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleOrdenCompra detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    public void recibirOrden(Inventario inventario) {
        aplicarMovimiento(inventario);
        cambiarEstado("RECIBIDA");
    }

    public void cancelarOrden() {
        cambiarEstado("CANCELADA");
    }

    @Override
    public double calcularTotal() {
        total = 0;
        for (DetalleOrdenCompra detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    @Override
    public void aplicarMovimiento(Inventario inventario) {
        for (DetalleOrdenCompra detalle : detalles) {
            inventario.aumentarStock(detalle.getProducto(), detalle.getCantidad());
        }
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleOrdenCompra> getDetalles() {
        return detalles;
    }
}
