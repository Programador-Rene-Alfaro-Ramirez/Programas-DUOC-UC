-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS Cine_DB;

-- Usar la base de datos
USE Cine_DB;

-- Crear la tabla Cartelera
CREATE TABLE Cartelera (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    director VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    duracion INT NOT NULL,
    genero ENUM('Comedia', 'Drama', 'Acción', 'Terror', 'Romance', 'Ciencia Ficción') NOT NULL
);
