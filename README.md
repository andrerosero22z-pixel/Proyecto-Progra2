# Sistema de Gestión de Inventario

Aplicación de escritorio desarrollada para la asignatura **Programación 2**, orientada a la administración de productos, clientes, proveedores, pedidos, órdenes de compra e inventario.

El sistema utiliza una interfaz gráfica creada con **Java Swing** y almacena la información en archivos **CSV**. También incluye un dashboard desarrollado con **Python y Streamlit** para visualizar datos importantes del negocio.

---

## Objetivo del proyecto

Desarrollar un sistema que facilite el registro y control de las operaciones principales de un inventario, aplicando conceptos de Programación Orientada a Objetos, interfaces gráficas, validaciones, manejo de excepciones y persistencia de datos mediante archivos CSV.

---

## Funcionalidades principales

### Clientes

- Registrar nuevos clientes.
- Editar información existente.
- Buscar clientes.
- Eliminar registros.
- Generar identificadores automáticamente.

### Proveedores

- Registrar proveedores.
- Modificar sus datos.
- Buscar y eliminar registros.
- Validar información como RUC, teléfono y correo.

### Productos

- Registrar productos.
- Administrar precios de compra y venta.
- Editar y eliminar productos.
- Generar automáticamente un registro de inventario.

### Inventario

- Consultar el stock disponible.
- Modificar el stock actual.
- Establecer un stock mínimo.
- Identificar productos que necesitan reabastecimiento.

### Pedidos

- Seleccionar un cliente y un producto.
- Registrar cantidades y descuentos.
- Calcular automáticamente el total.
- Validar que exista stock suficiente.
- Disminuir el inventario después de una venta.
- Mostrar una alerta cuando el producto alcanza el stock mínimo.

### Órdenes de compra

- Seleccionar un proveedor y un producto.
- Registrar la cantidad comprada.
- Calcular el valor total de la orden.
- Aumentar automáticamente el stock del producto.

### Dashboard

El dashboard permite visualizar:

- Total de pedidos.
- Ingresos registrados.
- Unidades vendidas.
- Productos más vendidos.
- Ventas por fecha.
- Estado de los pedidos.
- Productos con stock bajo.
- Cantidad necesaria para alcanzar el stock mínimo.

---

## Tecnologías utilizadas

- Java 17
- Java Swing
- Maven
- Programación Orientada a Objetos
- Archivos CSV
- Python
- Pandas
- Streamlit
- Git
- GitHub

---

## Almacenamiento de datos

La información se guarda en archivos CSV dentro de la carpeta `datos`.

```text
datos/
├── clientes.csv
├── proveedores.csv
├── productos.csv
├── inventario.csv
├── pedidos.csv
└── ordenesCompra.csv
```

Los archivos contienen encabezados en la primera fila y utilizan comas para separar los valores.

Ejemplo:

```csv
id,codigo,nombre,telefono,correo,direccion
1,C001,Juan Perez,0999999999,juan@gmail.com,Quito
```

---

## Estructura general

```text
Proyecto-Progra2/
├── dashboard/
│   ├── app.py
│   ├── requirements.txt
│   └── README.md
│
├── datos/
│   ├── clientes.csv
│   ├── proveedores.csv
│   ├── productos.csv
│   ├── inventario.csv
│   ├── pedidos.csv
│   └── ordenesCompra.csv
│
├── src/
│   └── main/
│       └── java/
│           └── ec/
│               └── edu/
│                   └── inventario/
│
├── pom.xml
└── README.md
```

---

## Ejecutar el dashboard

Desde la carpeta principal del proyecto, instalar las dependencias:

```bash
pip install -r dashboard/requirements.txt
```

Después, ejecutar:

```bash
streamlit run dashboard/app.py
```

El dashboard se abrirá en el navegador, normalmente en:

```text
http://localhost:8501
```

---

## Funcionamiento general

1. El usuario registra información desde la aplicación Java.
2. Los datos se guardan en los archivos CSV.
3. Los pedidos disminuyen el stock disponible.
4. Las órdenes de compra aumentan el stock.
5. El sistema alerta cuando un producto alcanza su cantidad mínima.
6. Streamlit lee los archivos CSV y presenta los resultados mediante métricas, tablas y gráficos.

El dashboard funciona únicamente como herramienta de consulta y análisis; no modifica los archivos del sistema.

---

## Conceptos aplicados

Durante el desarrollo se aplicaron los siguientes conocimientos:

- Clases y objetos.
- Encapsulamiento.
- Constructores.
- Métodos y atributos.
- Colecciones dinámicas.
- Manejo de excepciones.
- Validación de datos.
- Lectura y escritura de archivos.
- Persistencia mediante CSV.
- Interfaces gráficas con Java Swing.
- Análisis de datos con Pandas.
- Visualización de información con Streamlit.
- Control de versiones con Git y GitHub.

---

## Integrantes

1. **Andre Rosero**
2. **Pavel Ganchala**
3. **Melanie Guaman**
4. **Bryan Lozada**

---

## Información académica

Proyecto desarrollado como parte de la asignatura **Programación 2**.

Su finalidad es demostrar la integración de una aplicación de escritorio en Java con almacenamiento en archivos CSV y un dashboard de análisis desarrollado en Python.