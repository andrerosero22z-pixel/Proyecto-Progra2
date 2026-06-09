package ec.edu.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class Proveedor extends EntidadComercial {

    private String ruc;
    private String nombreEmpresa;
    private List<Producto> productosOfrecidos;

    public Proveedor() {
        this.productosOfrecidos = new ArrayList<>();
    }

    public Proveedor(int idEntidad, String ruc, String nombreEmpresa, String telefono, String correo, String direccion) {
        super(idEntidad, nombreEmpresa, telefono, correo, direccion);
        this.ruc = ruc;
        this.nombreEmpresa = nombreEmpresa;
        this.productosOfrecidos = new ArrayList<>();
    }

    public void agregarProductoOfrecido(Producto producto) {
        productosOfrecidos.add(producto);
    }

    public List<Producto> consultarProductosOfrecidos() {
        return productosOfrecidos;
    }

    @Override
    public String mostrarInformacion() {
        return "Proveedor: " + nombreEmpresa + " | RUC: " + ruc;
    }
}
