# Sistema de Gestión de Inventario

Aplicación de escritorio desarrollada en **Java** para administrar la información de una tienda mediante clientes, proveedores, productos, inventario, pedidos y órdenes de compra.

El proyecto fue realizado como parte de la asignatura **Programación 2**, aplicando conceptos de Programación Orientada a Objetos, manejo de archivos CSV e interfaces gráficas con Java Swing.

---

## Objetivo

Desarrollar un sistema que permita registrar, consultar, editar y eliminar información relacionada con el inventario de una tienda.

La aplicación también controla automáticamente las entradas y salidas de productos:

- Los **pedidos** disminuyen el stock.
- Las **órdenes de compra** aumentan el stock.
- El sistema muestra una advertencia cuando un producto alcanza o queda por debajo de su stock mínimo.

Como complemento, se incluye un dashboard desarrollado con **Python y Streamlit** para analizar los datos almacenados en los archivos CSV.

---

## Funcionalidades principales

### Gestión de clientes

- Registrar clientes.
- Editar información.
- Buscar clientes por ID.
- Eliminar clientes.
- Generar IDs automáticamente.
- Validar nombre, teléfono, correo y dirección.

### Gestión de proveedores

- Registrar proveedores.
- Editar información.
- Buscar proveedores por ID.
- Eliminar proveedores.
- Generar IDs automáticamente.
- Validar RUC, empresa, teléfono, correo y dirección.

### Gestión de productos

- Registrar productos.
- Editar productos.
- Buscar productos por ID.
- Eliminar productos.
- Generar IDs automáticamente.
- Validar precios y nombres repetidos.
- Crear automáticamente un registro de inventario para cada producto.

### Control de inventario

- Consultar el stock actual de cada producto.
- Configurar el stock mínimo.
- Editar cantidades disponibles.
- Identificar productos con stock bajo.
- Generar automáticamente el ID del ítem de inventario.

### Registro de pedidos

- Seleccionar cliente y producto.
- Registrar la cantidad solicitada.
- Aplicar descuentos.
- Calcular automáticamente el total.
- Validar que exista stock suficiente.
- Disminuir automáticamente el inventario.
- Generar automáticamente el número del pedido.
- Mostrar una alerta cuando el producto queda igual o por debajo del stock mínimo.

### Órdenes de compra

- Seleccionar proveedor y producto.
- Registrar la cantidad comprada.
- Calcular automáticamente el total.
- Aumentar el stock del producto.
- Generar automáticamente el número de la orden.

### Dashboard de análisis

El dashboard desarrollado con Streamlit permite visualizar:

- Total de pedidos registrados.
- Ingresos obtenidos.
- Unidades vendidas.
- Productos más vendidos.
- Ventas agrupadas por fecha.
- Cantidad de pedidos según su estado.
- Productos con stock bajo.
- Cantidad necesaria para alcanzar el stock mínimo.

---

## Tecnologías utilizadas

### Aplicación principal

- Java 17
- Java Swing
- Maven
- Programación Orientada a Objetos
- Archivos CSV

### Dashboard

- Python
- Pandas
- Streamlit

### Control de versiones

- Git
- GitHub

---

## Funcionamiento general

1. El usuario registra información desde las ventanas desarrolladas con Java Swing.
2. Los datos se almacenan en archivos CSV con encabezados y separados por comas dentro de la carpeta `datos`.
3. La primera fila de cada archivo contiene los nombres de sus columnas.
4. Cuando se registra un pedido, el sistema disminuye el stock.
5. Cuando se registra una orden de compra, el sistema aumenta el stock.
6. El dashboard de Streamlit lee los archivos CSV y genera estadísticas y gráficos.
7. El dashboard no modifica la información del sistema; solamente la analiza.

---

## Estructura del proyecto

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
│                       ├── app/
│                       ├── modelo/
│                       ├── persistencia/
│                       ├── ui/
│                       └── util/
│
├── pom.xml
└── README.md
```

---

## Archivos CSV

Los datos del sistema se guardan en la carpeta `datos`.

| Archivo | Información almacenada |
|---|---|
| `clientes.csv` | Información de los clientes |
| `proveedores.csv` | Información de los proveedores |
| `productos.csv` | Productos y precios |
| `inventario.csv` | Stock actual y stock mínimo |
| `pedidos.csv` | Pedidos realizados por los clientes |
| `ordenesCompra.csv` | Compras realizadas a proveedores |

Todos los archivos tienen encabezados en la primera fila y utilizan la coma como separador.

```text
,
```

Ejemplo:

```text
id,codigo,nombre,telefono,correo,direccion
1,0001,Nicole Salazar,0991234567,nicole@gmail.com,Quito
```

---

## Requisitos

Para ejecutar la aplicación principal se necesita:

- Java 17 o superior.
- Maven.
- NetBeans, IntelliJ IDEA, Eclipse o Visual Studio Code.

Para ejecutar el dashboard se necesita:

- Python 3.
- Pandas.
- Streamlit.

---

## Cómo ejecutar la aplicación Java

### Opción 1: desde un IDE

1. Abrir el proyecto en NetBeans, IntelliJ IDEA, Eclipse o Visual Studio Code.
2. Esperar que Maven cargue el proyecto.
3. Ejecutar la clase:

```text
ec.edu.inventario.app.Main
```

### Opción 2: desde la terminal

Desde la carpeta principal del proyecto, ejecutar:

```bash
mvn clean compile
```

Después:

```bash
java -cp target/classes ec.edu.inventario.app.Main
```

---

## Cómo ejecutar el dashboard

Desde la carpeta principal del proyecto, instalar las dependencias:

```bash
pip install -r dashboard/requirements.txt
```

Luego ejecutar:

```bash
streamlit run dashboard/app.py
```

Streamlit abrirá automáticamente el dashboard en el navegador.

También se puede abrir manualmente la dirección mostrada en la terminal, normalmente:

```text
http://localhost:8501
```

---

## Análisis de datos con Streamlit

El dashboard utiliza **Pandas** para leer y analizar los archivos CSV.

Algunas operaciones utilizadas son:

### Lectura de archivos

```python
pd.read_csv(ruta)
```

Pandas reconoce automáticamente los nombres de las columnas porque se encuentran en la primera fila de cada archivo CSV.

### Unión de información

`merge` permite relacionar los pedidos con los productos mediante su ID:

```python
pedidos.merge(productos, on="id_producto")
```

### Agrupación de datos

`groupby` permite sumar las cantidades vendidas de cada producto:

```python
ventas.groupby("producto")["cantidad"].sum()
```

### Detección de stock bajo

```python
stock_actual <= stock_minimo
```

Cuando esta condición se cumple, el producto necesita ser reabastecido mediante una orden de compra.

---

## Conceptos aplicados

Durante el desarrollo del proyecto se aplicaron los siguientes conceptos:

- Clases y objetos.
- Encapsulamiento.
- Herencia.
- Interfaces.
- Polimorfismo.
- Constructores.
- Métodos y atributos.
- Listas con `ArrayList`.
- Manejo de excepciones.
- Lectura y escritura de archivos.
- Persistencia mediante CSV.
- Interfaces gráficas con Java Swing.
- Validación de datos.
- Análisis de datos con Pandas.
- Creación de dashboards con Streamlit.
- Control de versiones con Git y GitHub.

---

## Validaciones implementadas

El sistema valida información antes de guardar los registros.

Entre las principales validaciones se encuentran:

- Campos obligatorios.
- IDs y números automáticos.
- Teléfonos con formato válido.
- Correos electrónicos válidos.
- RUC de 13 dígitos.
- Nombres no formados únicamente por números.
- Precios mayores que cero.
- Cantidades enteras positivas.
- Descuentos no negativos.
- Descuentos que no superen el subtotal.
- Productos, códigos y RUC no repetidos.
- Stock suficiente antes de registrar un pedido.

---

## Integrantes

1. **Andre Rosero**
2. **Pavel Ganchala**
3. **Melanie Guaman**
4. **Bryan Lozada**

---

## Proyecto académico

Proyecto desarrollado para la asignatura **Programación 2**.

El sistema integra una aplicación de escritorio en Java con un dashboard de análisis en Python, utilizando archivos CSV como medio de almacenamiento y comunicación entre ambas partes.
