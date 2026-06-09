package ec.edu.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido extends Transaccion implements AfectaInventario {

    private String numeroPedido;
    private Cliente cliente;
    private List<DetallePedido> detalles;

    public Pedido(String numeroPedido, Cliente cliente) {
        super();
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    public boolean verificarDisponibilidad(Inventario inventario) {
        for (DetallePedido detalle : detalles) {
            if (!inventario.verificarStock(detalle.getProducto(), detalle.getCantidad())) {
                return false;
            }
        }
        return true;
    }

    public void confirmarPedido(Inventario inventario) {
        if (verificarDisponibilidad(inventario)) {
            aplicarMovimiento(inventario);
            cambiarEstado("CONFIRMADO");
        }
    }

    public void cancelarPedido() {
        cambiarEstado("CANCELADO");
    }

    @Override
    public double calcularTotal() {
        total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    @Override
    public void aplicarMovimiento(Inventario inventario) {
        for (DetallePedido detalle : detalles) {
            inventario.disminuirStock(detalle.getProducto(), detalle.getCantidad());
        }
    }
}
