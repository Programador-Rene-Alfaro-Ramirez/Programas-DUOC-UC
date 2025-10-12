-- ================================================
--   BASE DE DATOS: COMPUTEC
--   Autor: René Alfaro
--   Evaluación Final Transversal - DOO2
-- ================================================

DROP DATABASE IF EXISTS computec_db;
CREATE DATABASE computec_db;
USE computec_db;

-- ================================================
--   TABLA CLIENTE
-- ================================================
CREATE TABLE cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(100),
    correo VARCHAR(100),
    telefono VARCHAR(20)
);

-- ================================================
--   TABLA EQUIPO
-- ================================================
CREATE TABLE equipo (
    idEquipo INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    tipo ENUM('Desktop','Laptop') NOT NULL
);

-- ================================================
--   TABLA VENTA
-- ================================================
CREATE TABLE venta (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    idEquipo INT NOT NULL,
    precioFinal DECIMAL(10,2) NOT NULL,
    fechaVenta DATETIME NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente(idCliente),
    FOREIGN KEY (idEquipo) REFERENCES equipo(idEquipo)
);

-- ================================================
--   PROCEDIMIENTOS ALMACENADOS
-- ================================================

-- -------- CLIENTES --------
DELIMITER //
CREATE PROCEDURE insertar_cliente(
    IN p_rut VARCHAR(20),
    IN p_nombre VARCHAR(100),
    IN p_direccion VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    INSERT INTO cliente (rut, nombre, direccion, correo, telefono)
    VALUES (p_rut, p_nombre, p_direccion, p_correo, p_telefono);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE listar_clientes()
BEGIN
    SELECT * FROM cliente;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizar_cliente(
    IN p_idCliente INT,
    IN p_rut VARCHAR(20),
    IN p_nombre VARCHAR(100),
    IN p_direccion VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    UPDATE cliente
    SET rut = p_rut,
        nombre = p_nombre,
        direccion = p_direccion,
        correo = p_correo,
        telefono = p_telefono
    WHERE idCliente = p_idCliente;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminar_cliente(IN p_idCliente INT)
BEGIN
    DELETE FROM cliente WHERE idCliente = p_idCliente;
END //
DELIMITER ;

-- -------- EQUIPOS --------
DELIMITER //
CREATE PROCEDURE insertar_equipo(
    IN p_marca VARCHAR(50),
    IN p_modelo VARCHAR(50),
    IN p_precio DECIMAL(10,2),
    IN p_tipo VARCHAR(20)
)
BEGIN
    INSERT INTO equipo (marca, modelo, precio, tipo)
    VALUES (p_marca, p_modelo, p_precio, p_tipo);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE listar_equipos()
BEGIN
    SELECT * FROM equipo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizar_equipo(
    IN p_idEquipo INT,
    IN p_marca VARCHAR(50),
    IN p_modelo VARCHAR(50),
    IN p_precio DECIMAL(10,2),
    IN p_tipo VARCHAR(20)
)
BEGIN
    UPDATE equipo
    SET marca = p_marca,
        modelo = p_modelo,
        precio = p_precio,
        tipo = p_tipo
    WHERE idEquipo = p_idEquipo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminar_equipo(IN p_idEquipo INT)
BEGIN
    DELETE FROM equipo WHERE idEquipo = p_idEquipo;
END //
DELIMITER ;

-- -------- VENTAS --------
DELIMITER //
CREATE PROCEDURE insertar_venta(
    IN p_idCliente INT,
    IN p_idEquipo INT,
    IN p_precioFinal DECIMAL(10,2),
    IN p_fechaVenta DATE
)
BEGIN
    INSERT INTO venta (idCliente, idEquipo, precioFinal, fechaVenta)
    VALUES (p_idCliente, p_idEquipo, p_precioFinal, p_fechaVenta);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE listar_ventas()
BEGIN
    SELECT 
        v.idVenta,
        c.idCliente,
        c.nombre AS Cliente,
        e.idEquipo,
        CONCAT(e.marca, ' ', e.modelo) AS Equipo,
        e.tipo AS Tipo,
        v.precioFinal,
        v.fechaVenta
    FROM venta v
    INNER JOIN cliente c ON v.idCliente = c.idCliente
    INNER JOIN equipo e ON v.idEquipo = e.idEquipo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE listar_ventas_por_tipo(IN p_tipo VARCHAR(20))
BEGIN
    SELECT 
        v.idVenta,
        c.idCliente,
        c.nombre AS Cliente,
        e.idEquipo,
        CONCAT(e.marca, ' ', e.modelo) AS Equipo,
        e.tipo AS Tipo,
        v.precioFinal,
        v.fechaVenta
    FROM venta v
    INNER JOIN cliente c ON v.idCliente = c.idCliente
    INNER JOIN equipo e ON v.idEquipo = e.idEquipo
    WHERE e.tipo = p_tipo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizar_venta(
    IN p_idVenta INT,
    IN p_idCliente INT,
    IN p_idEquipo INT,
    IN p_precioFinal DECIMAL(10,2)
)
BEGIN
    UPDATE venta
    SET idCliente = p_idCliente,
        idEquipo = p_idEquipo,
        precioFinal = p_precioFinal
    WHERE idVenta = p_idVenta;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminar_venta(IN p_idVenta INT)
BEGIN
    DELETE FROM venta WHERE idVenta = p_idVenta;
END //
DELIMITER ;

-- ================================================
--   DATOS DE PRUEBA
-- ================================================
INSERT INTO cliente (rut, nombre, direccion, correo, telefono) VALUES
('20.123.456-7', 'René Alfaro', 'Av. Los Alerces 123', 'rene@computec.cl', '987654321'),
('19.987.654-3', 'Juan Pérez', 'Calle Los Robles 45', 'juan@correo.cl', '998877665');

INSERT INTO equipo (marca, modelo, precio, tipo) VALUES
('Asus', 'Vivobook 15', 650000, 'Laptop'),
('Apple', 'MacBook Air M1', 950000, 'Laptop'),
('HP', 'Pavilion 5000', 550000, 'Desktop'),
('Apple', 'iMac M3', 1250000, 'Desktop');

-- ================================================
--   FIN DEL SCRIPT
-- ================================================
