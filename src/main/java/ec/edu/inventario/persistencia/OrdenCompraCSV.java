package ec.edu.inventario.persistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import ec.edu.inventario.modelo.DetalleOrdenCompra;
import ec.edu.inventario.modelo.OrdenCompra;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.modelo.Proveedor;
import ec.edu.inventario.util.ArchivoUtil;

public class OrdenCompraCSV {

    private static final String ARCHIVO = "ordenesCompra.csv";

    public ArrayList<OrdenCompra> listar(ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<OrdenCompra> ordenes = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 8) {
                    Proveedor proveedor = buscarProveedor(proveedores, Integer.parseInt(datos[1]));
                    Producto producto = buscarProducto(productos, Integer.parseInt(datos[2]));

                    if (proveedor != null && producto != null) {
                        OrdenCompra orden = new OrdenCompra(datos[0], proveedor);
                        int cantidad = Integer.parseInt(datos[3]);
                        double precioUnitario = Double.parseDouble(datos[4]);
                        double total = Double.parseDouble(datos[5]);
                        String estado = datos[6];
                        LocalDate fecha = LocalDate.parse(datos[7]);

                        orden.agregarDetalle(new DetalleOrdenCompra(1, cantidad, precioUnitario, producto));
                        orden.setTotal(total);
                        orden.cambiarEstado(estado);
                        orden.setFecha(fecha);
                        ordenes.add(orden);
                    }
                }
            } catch (Exception e) {
                // Se omiten registros incorrectos.
            }
        }
        return ordenes;
    }

    public void guardar(ArrayList<OrdenCompra> ordenes) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (int i = 0; i < ordenes.size(); i++) {
            OrdenCompra orden = ordenes.get(i);
            if (orden.getDetalles().size() > 0 && orden.getProveedor() != null) {
                DetalleOrdenCompra detalle = orden.getDetalles().get(0);
                String fecha = LocalDate.now().toString();
                if (orden.getFecha() != null) {
                    fecha = orden.getFecha().toString();
                }

                lineas.add(ArchivoUtil.limpiarTexto(orden.getNumeroOrden()) + ","
                        + orden.getProveedor().getIdEntidad() + ","
                        + detalle.getProducto().getIdProducto() + ","
                        + detalle.getCantidad() + ","
                        + detalle.getPrecioUnitario() + ","
                        + orden.getTotal() + ","
                        + ArchivoUtil.limpiarTexto(orden.getEstado()) + ","
                        + fecha);
            }
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public OrdenCompra buscarPorNumero(String numero, ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<OrdenCompra> ordenes = listar(proveedores, productos);
        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getNumeroOrden().equals(numero)) {
                return ordenes.get(i);
            }
        }
        return null;
    }

    public boolean existeNumero(String numero, ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        return buscarPorNumero(numero, proveedores, productos) != null;
    }

    public String generarNumero() throws FileNotFoundException, IOException {
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);
        int mayor = 0;

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                int numero = Integer.parseInt(datos[0].trim());
                if (numero > mayor) {
                    mayor = numero;
                }
            } catch (Exception e) {
                // Se omiten encabezados o numeros incorrectos.
            }
        }
        return String.valueOf(mayor + 1);
    }

    public void agregar(OrdenCompra orden, ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws Exception {
        ArrayList<OrdenCompra> ordenes = listar(proveedores, productos);
        if (existeNumero(orden.getNumeroOrden(), proveedores, productos)) {
            throw new Exception("Ya existe una orden de compra con ese numero.");
        }
        ordenes.add(orden);
        guardar(ordenes);
    }

    public void editar(OrdenCompra ordenEditada, ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws Exception {
        ArrayList<OrdenCompra> ordenes = listar(proveedores, productos);
        boolean encontrado = false;

        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getNumeroOrden().equals(ordenEditada.getNumeroOrden())) {
                ordenes.set(i, ordenEditada);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro la orden de compra.");
        }
        guardar(ordenes);
    }

    public void eliminar(String numero, ArrayList<Proveedor> proveedores, ArrayList<Producto> productos) throws Exception {
        ArrayList<OrdenCompra> ordenes = listar(proveedores, productos);
        boolean eliminado = false;

        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getNumeroOrden().equals(numero)) {
                ordenes.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro la orden de compra.");
        }
        guardar(ordenes);
    }

    private Proveedor buscarProveedor(ArrayList<Proveedor> proveedores, int idProveedor) {
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdEntidad() == idProveedor) {
                return proveedores.get(i);
            }
        }
        return null;
    }

    private Producto buscarProducto(ArrayList<Producto> productos, int idProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == idProducto) {
                return productos.get(i);
            }
        }
        return null;
    }
}
