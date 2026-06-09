package ec.edu.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends EntidadComercial {

    private String codigoCliente;
    private List<Pedido> historialPedidos;

    public Cliente() {
        this.historialPedidos = new ArrayList<>();
    }

    public Cliente(int idEntidad, String codigoCliente, String nombre, String telefono, String correo, String direccion) {
        super(idEntidad, nombre, telefono, correo, direccion);
        this.codigoCliente = codigoCliente;
        this.historialPedidos = new ArrayList<>();
    }

    public void realizarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public List<Pedido> consultarHistorialPedidos() {
        return historialPedidos;
    }

    @Override
    public String mostrarInformacion() {
        return "Cliente: " + nombre + " | Código: " + codigoCliente;
    }
}
