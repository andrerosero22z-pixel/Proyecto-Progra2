from pathlib import Path

import pandas as pd
import streamlit as st


RUTA_PROYECTO = Path(__file__).resolve().parent.parent
RUTA_DATOS = RUTA_PROYECTO / "datos"


def cargar_csv(nombre_archivo, columnas):
    ruta = RUTA_DATOS / nombre_archivo
    if not ruta.exists() or ruta.stat().st_size == 0:
        return pd.DataFrame(columns=columnas)

    try:
        try:
            datos = pd.read_csv(
                ruta, sep=";", header=None, engine="python", on_bad_lines="skip"
            )
        except pd.errors.ParserError:
            datos = pd.DataFrame()

        # Los CSV actuales usan coma. Este segundo intento permite leerlos sin modificarlos.
        if len(datos.columns) != len(columnas):
            datos = pd.read_csv(
                ruta, sep=",", header=None, engine="python", on_bad_lines="skip"
            )

        if len(datos.columns) != len(columnas):
            st.warning(f"No se pudo reconocer la estructura de {nombre_archivo}.")
            return pd.DataFrame(columns=columnas)

        datos.columns = columnas

        # Algunos archivos actuales tienen encabezado y otros no.
        if not datos.empty:
            primer_valor = str(datos.iloc[0, 0]).strip().lower()
            encabezados = {
                "id",
                "id_item",
                "id_producto",
                "id_cliente",
                "id_proveedor",
                "numero",
            }
            if primer_valor in encabezados:
                datos = datos.iloc[1:]

        return datos.reset_index(drop=True)
    except (pd.errors.EmptyDataError, FileNotFoundError):
        return pd.DataFrame(columns=columnas)
    except Exception as error:
        st.warning(f"No se pudo leer {nombre_archivo}: {error}")
        return pd.DataFrame(columns=columnas)


st.set_page_config(
    page_title="Dashboard de Inventario",
    page_icon="📊",
    layout="wide",
)

st.title("Dashboard de Inventario y Ventas")
st.write(
    "Análisis de la información registrada por el sistema Java mediante archivos CSV."
)

productos = cargar_csv(
    "productos.csv", ["id_producto", "producto", "precio_compra", "precio_venta"]
)
inventario = cargar_csv(
    "inventario.csv",
    ["id_item", "id_producto", "stock_actual", "stock_minimo"],
)
pedidos = cargar_csv(
    "pedidos.csv",
    [
        "numero",
        "id_cliente",
        "id_producto",
        "cantidad",
        "precio",
        "descuento",
        "total",
        "estado",
        "fecha",
    ],
)
clientes = cargar_csv(
    "clientes.csv",
    ["id_cliente", "codigo", "cliente", "telefono", "correo", "direccion"],
)
proveedores = cargar_csv(
    "proveedores.csv",
    ["id_proveedor", "ruc", "proveedor", "telefono", "correo", "direccion"],
)
ordenes = cargar_csv(
    "ordenesCompra.csv",
    [
        "numero",
        "id_proveedor",
        "id_producto",
        "cantidad",
        "precio",
        "total",
        "estado",
        "fecha",
    ],
)

for columna in ["id_producto", "precio_compra", "precio_venta"]:
    productos[columna] = pd.to_numeric(productos[columna], errors="coerce")

for columna in ["id_item", "id_producto", "stock_actual", "stock_minimo"]:
    inventario[columna] = pd.to_numeric(inventario[columna], errors="coerce")

for columna in [
    "numero",
    "id_cliente",
    "id_producto",
    "cantidad",
    "precio",
    "descuento",
    "total",
]:
    pedidos[columna] = pd.to_numeric(pedidos[columna], errors="coerce")

pedidos["fecha"] = pd.to_datetime(pedidos["fecha"], errors="coerce")

productos_para_unir = productos[["id_producto", "producto"]].dropna(
    subset=["id_producto"]
)
productos_para_unir = productos_para_unir.drop_duplicates(subset=["id_producto"])

ventas = pedidos.merge(productos_para_unir, on="id_producto", how="left")
ventas["producto"] = ventas["producto"].fillna("Producto sin nombre")

inventario_productos = inventario.merge(
    productos_para_unir, on="id_producto", how="left"
)
inventario_productos["producto"] = inventario_productos["producto"].fillna(
    "Producto sin nombre"
)

opciones_productos = sorted(
    productos["producto"].dropna().astype(str).unique().tolist()
)
producto_elegido = st.sidebar.selectbox(
    "Producto", ["Todos"] + opciones_productos
)

opciones_estados = sorted(pedidos["estado"].dropna().astype(str).unique().tolist())
estado_elegido = st.sidebar.selectbox(
    "Estado del pedido", ["Todos"] + opciones_estados
)

pestana_resumen, pestana_ventas, pestana_inventario = st.tabs(
    ["Resumen", "Ventas", "Inventario"]
)

with pestana_resumen:
    pedidos_validos = pedidos.dropna(subset=["numero"])
    ingresos = pedidos["total"].dropna().sum()
    unidades = pedidos["cantidad"].dropna().sum()
    inventario_valido = inventario.dropna(subset=["stock_actual", "stock_minimo"])
    cantidad_stock_bajo = len(
        inventario_valido[
            inventario_valido["stock_actual"] <= inventario_valido["stock_minimo"]
        ]
    )

    columna_1, columna_2, columna_3, columna_4 = st.columns(4)
    columna_1.metric("Pedidos registrados", len(pedidos_validos))
    columna_2.metric("Ingresos totales", f"${ingresos:,.2f}")
    columna_3.metric("Unidades vendidas", f"{unidades:,.0f}")
    columna_4.metric("Productos con stock bajo", cantidad_stock_bajo)

with pestana_ventas:
    if ventas.dropna(subset=["numero"]).empty:
        st.info("No existen pedidos para analizar.")
    else:
        ventas_filtradas = ventas.copy()

        if producto_elegido != "Todos":
            ventas_filtradas = ventas_filtradas[
                ventas_filtradas["producto"] == producto_elegido
            ]
        if estado_elegido != "Todos":
            ventas_filtradas = ventas_filtradas[
                ventas_filtradas["estado"].astype(str) == estado_elegido
            ]

        fechas_validas = ventas_filtradas["fecha"].dropna()
        if not fechas_validas.empty:
            rango_fechas = st.date_input(
                "Rango de fechas",
                value=(fechas_validas.min().date(), fechas_validas.max().date()),
                min_value=fechas_validas.min().date(),
                max_value=fechas_validas.max().date(),
            )
            if isinstance(rango_fechas, (list, tuple)) and len(rango_fechas) == 2:
                fecha_inicio, fecha_fin = rango_fechas
                ventas_filtradas = ventas_filtradas[
                    (ventas_filtradas["fecha"].dt.date >= fecha_inicio)
                    & (ventas_filtradas["fecha"].dt.date <= fecha_fin)
                ]

        if ventas_filtradas.empty:
            st.info("No existen pedidos dentro de los filtros seleccionados.")
        else:
            st.subheader("Pedidos filtrados")
            tabla_ventas = ventas_filtradas[
                [
                    "numero",
                    "producto",
                    "cantidad",
                    "precio",
                    "descuento",
                    "total",
                    "estado",
                    "fecha",
                ]
            ].copy()
            st.dataframe(tabla_ventas, width="stretch", hide_index=True)

            columna_grafico_1, columna_grafico_2 = st.columns(2)
            with columna_grafico_1:
                st.subheader("10 productos más vendidos")
                productos_vendidos = (
                    ventas_filtradas.dropna(subset=["cantidad"])
                    .groupby("producto")["cantidad"]
                    .sum()
                    .sort_values(ascending=False)
                    .head(10)
                )
                st.bar_chart(productos_vendidos)

            with columna_grafico_2:
                st.subheader("Ventas totales por fecha")
                ventas_por_fecha = (
                    ventas_filtradas.dropna(subset=["fecha", "total"])
                    .groupby("fecha")["total"]
                    .sum()
                    .sort_index()
                )
                st.line_chart(ventas_por_fecha)

            st.subheader("Cantidad de pedidos por estado")
            pedidos_por_estado = ventas_filtradas["estado"].value_counts()
            st.bar_chart(pedidos_por_estado)

with pestana_inventario:
    inventario_filtrado = inventario_productos.dropna(
        subset=["stock_actual", "stock_minimo"]
    ).copy()

    if producto_elegido != "Todos":
        inventario_filtrado = inventario_filtrado[
            inventario_filtrado["producto"] == producto_elegido
        ]

    if inventario_filtrado.empty:
        st.info("No existen datos de inventario.")
    else:
        inventario_filtrado["estado"] = "Stock suficiente"
        condicion_stock_bajo = (
            inventario_filtrado["stock_actual"]
            <= inventario_filtrado["stock_minimo"]
        )
        inventario_filtrado.loc[condicion_stock_bajo, "estado"] = "Stock bajo"
        inventario_filtrado["faltante_para_minimo"] = (
            inventario_filtrado["stock_minimo"]
            - inventario_filtrado["stock_actual"]
        ).clip(lower=0)

        st.subheader("Estado general del inventario")
        st.dataframe(
            inventario_filtrado[
                ["producto", "stock_actual", "stock_minimo", "estado"]
            ],
            width="stretch",
            hide_index=True,
        )

        productos_stock_bajo = inventario_filtrado[condicion_stock_bajo]
        if not productos_stock_bajo.empty:
            st.warning("Existen productos que requieren una orden de compra.")
            st.dataframe(
                productos_stock_bajo[
                    [
                        "producto",
                        "stock_actual",
                        "stock_minimo",
                        "faltante_para_minimo",
                    ]
                ],
                width="stretch",
                hide_index=True,
            )

        st.subheader("Stock actual y stock mínimo")
        comparacion_stock = inventario_filtrado.set_index("producto")[[
            "stock_actual",
            "stock_minimo",
        ]]
        st.bar_chart(comparacion_stock)
