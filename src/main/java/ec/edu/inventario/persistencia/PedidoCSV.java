package ec.edu.inventario.persistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import ec.edu.inventario.modelo.Cliente;
import ec.edu.inventario.modelo.DetallePedido;
import ec.edu.inventario.modelo.Pedido;
import ec.edu.inventario.modelo.Producto;
import ec.edu.inventario.util.ArchivoUtil;

public class PedidoCSV {

    private static final String ARCHIVO = "pedidos.csv";

    public ArrayList<Pedido> listar(ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        ArrayList<String> lineas = ArchivoUtil.leerLineas(ARCHIVO);

        for (int i = 0; i < lineas.size(); i++) {
            try {
                String[] datos = ArchivoUtil.separar(lineas.get(i));
                if (datos.length >= 9) {
                    Cliente cliente = buscarCliente(clientes, Integer.parseInt(datos[1]));
                    Producto producto = buscarProducto(productos, Integer.parseInt(datos[2]));

                    if (cliente != null && producto != null) {
                        Pedido pedido = new Pedido(datos[0], cliente);
                        int cantidad = Integer.parseInt(datos[3]);
                        double precioUnitario = Double.parseDouble(datos[4]);
                        double descuento = Double.parseDouble(datos[5]);
                        double total = Double.parseDouble(datos[6]);
                        String estado = datos[7];
                        LocalDate fecha = LocalDate.parse(datos[8]);

                        pedido.agregarDetalle(new DetallePedido(1, cantidad, precioUnitario, producto, descuento));
                        pedido.setTotal(total);
                        pedido.cambiarEstado(estado);
                        pedido.setFecha(fecha);
                        pedidos.add(pedido);
                    }
                }
            } catch (Exception e) {
                // Se omiten registros incorrectos.
            }
        }
        return pedidos;
    }

    public void guardar(ArrayList<Pedido> pedidos) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        lineas.add("numero,idCliente,idProducto,cantidad,precio,descuento,total,estado,fecha");
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido pedido = pedidos.get(i);
            if (pedido.getDetalles().size() > 0 && pedido.getCliente() != null) {
                DetallePedido detalle = pedido.getDetalles().get(0);
                String fecha = LocalDate.now().toString();
                if (pedido.getFecha() != null) {
                    fecha = pedido.getFecha().toString();
                }

                lineas.add(ArchivoUtil.limpiarTexto(pedido.getNumeroPedido()) + ","
                        + pedido.getCliente().getIdEntidad() + ","
                        + detalle.getProducto().getIdProducto() + ","
                        + detalle.getCantidad() + ","
                        + detalle.getPrecioUnitario() + ","
                        + detalle.getDescuento() + ","
                        + pedido.getTotal() + ","
                        + ArchivoUtil.limpiarTexto(pedido.getEstado()) + ","
                        + fecha);
            }
        }
        ArchivoUtil.escribirLineas(ARCHIVO, lineas);
    }

    public Pedido buscarPorNumero(String numero, ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        ArrayList<Pedido> pedidos = listar(clientes, productos);
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getNumeroPedido().equals(numero)) {
                return pedidos.get(i);
            }
        }
        return null;
    }

    public boolean existeNumero(String numero, ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws FileNotFoundException, IOException {
        return buscarPorNumero(numero, clientes, productos) != null;
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

    public void agregar(Pedido pedido, ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws Exception {
        ArrayList<Pedido> pedidos = listar(clientes, productos);
        if (existeNumero(pedido.getNumeroPedido(), clientes, productos)) {
            throw new Exception("Ya existe un pedido con ese numero.");
        }
        pedidos.add(pedido);
        guardar(pedidos);
    }

    public void editar(Pedido pedidoEditado, ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws Exception {
        ArrayList<Pedido> pedidos = listar(clientes, productos);
        boolean encontrado = false;

        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getNumeroPedido().equals(pedidoEditado.getNumeroPedido())) {
                pedidos.set(i, pedidoEditado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new Exception("No se encontro el pedido.");
        }
        guardar(pedidos);
    }

    public void eliminar(String numero, ArrayList<Cliente> clientes, ArrayList<Producto> productos) throws Exception {
        ArrayList<Pedido> pedidos = listar(clientes, productos);
        boolean eliminado = false;

        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getNumeroPedido().equals(numero)) {
                pedidos.remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new Exception("No se encontro el pedido.");
        }
        guardar(pedidos);
    }

    private Cliente buscarCliente(ArrayList<Cliente> clientes, int idCliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdEntidad() == idCliente) {
                return clientes.get(i);
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
