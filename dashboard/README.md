# Dashboard de análisis

Este dashboard complementa el sistema de inventario desarrollado en Java. Utiliza Streamlit para presentar indicadores, tablas y gráficos a partir de los archivos CSV del proyecto.

## Archivos analizados

El dashboard lee los siguientes archivos de la carpeta `datos`:

- `clientes.csv`.
- `proveedores.csv`.
- `productos.csv`.
- `inventario.csv`.
- `pedidos.csv`.
- `ordenesCompra.csv`.

El análisis de ventas e inventario utiliza principalmente la información de productos, pedidos e inventario. Streamlit solamente lee estos archivos; no agrega, modifica ni elimina datos.

## Indicadores y gráficos

- Total de pedidos registrados.
- Ingresos obtenidos.
- Unidades vendidas.
- Productos con stock bajo.
- Productos más vendidos.
- Ventas por fecha.
- Cantidad de pedidos por estado.
- Comparación entre el stock actual y el stock mínimo.

## Instalar dependencias

Desde la carpeta raíz `sistema inventario`, ejecutar:

```bash
pip install -r dashboard/requirements.txt
```

## Ejecutar el dashboard

Desde la misma carpeta raíz, ejecutar:

```bash
streamlit run dashboard/app.py
```

Streamlit mostrará en la terminal la dirección local para abrir el dashboard en el navegador.

## Conceptos básicos de pandas

- **pandas:** biblioteca de Python que permite leer, ordenar y analizar datos en forma de tablas.
- **merge:** une dos tablas mediante una columna común. En este proyecto permite relacionar los IDs de productos con sus nombres.
- **groupby:** agrupa filas que tienen un dato en común y permite calcular totales. Se utiliza para obtener las ventas por producto y por fecha.
