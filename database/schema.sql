CREATE DATABASE IF NOT EXISTS sistema_inventario_tienda;
USE sistema_inventario_tienda;

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    codigo_cliente VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150)
);

CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    nombre_empresa VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150)
);

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio_compra DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    CHECK (precio_compra >= 0),
    CHECK (precio_venta >= 0)
);

CREATE TABLE proveedor_producto (
    id_proveedor INT NOT NULL,
    id_producto INT NOT NULL,
    PRIMARY KEY (id_proveedor, id_producto),
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE inventario (
    id_inventario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL DEFAULT 'Inventario Principal'
);

CREATE TABLE item_inventario (
    id_item_inventario INT AUTO_INCREMENT PRIMARY KEY,
    id_inventario INT NOT NULL,
    id_producto INT NOT NULL,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_inventario) REFERENCES inventario(id_inventario),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    UNIQUE (id_inventario, id_producto),
    CHECK (stock_actual >= 0),
    CHECK (stock_minimo >= 0)
);

CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(30) NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado ENUM('PENDIENTE', 'CONFIRMADO', 'CANCELADO', 'ENTREGADO') NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CHECK (total >= 0)
);

CREATE TABLE detalle_pedido (
    id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    numero_linea INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    UNIQUE (id_pedido, numero_linea),
    CHECK (cantidad > 0),
    CHECK (precio_unitario >= 0),
    CHECK (descuento >= 0),
    CHECK (subtotal >= 0)
);

CREATE TABLE orden_compra (
    id_orden_compra INT AUTO_INCREMENT PRIMARY KEY,
    numero_orden VARCHAR(30) NOT NULL UNIQUE,
    id_proveedor INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado ENUM('PENDIENTE', 'RECIBIDA', 'CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
    CHECK (total >= 0)
);

CREATE TABLE detalle_orden_compra (
    id_detalle_orden_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_orden_compra INT NOT NULL,
    id_producto INT NOT NULL,
    numero_linea INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_orden_compra) REFERENCES orden_compra(id_orden_compra),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    UNIQUE (id_orden_compra, numero_linea),
    CHECK (cantidad > 0),
    CHECK (precio_unitario >= 0),
    CHECK (subtotal >= 0)
);

INSERT INTO inventario (nombre)
VALUES ('Inventario Principal');
