# Dashboard de Inventario y Ventas

Este dashboard es un complemento de lectura para el sistema de inventario desarrollado en Java. El sistema Java guarda la información en archivos CSV y Streamlit la presenta mediante indicadores, tablas y gráficos sencillos.

## Requisitos

1. Instalar Python.
2. Abrir una terminal en la carpeta raíz del proyecto.
3. Instalar las dependencias:

```bash
pip install -r dashboard/requirements.txt
```

4. Iniciar el dashboard:

```bash
streamlit run dashboard/app.py
```

El dashboard solamente lee los archivos de la carpeta `datos`. No modifica, agrega ni elimina información de los CSV.

## Contenido

- **Resumen:** pedidos registrados, ingresos, unidades vendidas y productos con stock bajo.
- **Ventas:** filtros, tabla de pedidos, productos más vendidos, ventas por fecha y estados.
- **Inventario:** estado del stock, productos que requieren reabastecimiento y comparación con el stock mínimo.
