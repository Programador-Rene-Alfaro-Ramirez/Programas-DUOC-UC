CREATE DATABASE IF NOT EXISTS Cine_DB;
USE Cine_DB;

CREATE TABLE IF NOT EXISTS peliculas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    director VARCHAR(100) NOT NULL,
    anio INT NOT NULL,
    duracion INT NOT NULL,
    genero VARCHAR(50) NOT NULL
);

INSERT INTO peliculas (titulo, director, anio, duracion, genero) VALUES
('Kill Bill', 'Quentin Tarantino', 2003, 116, 'Accion'),
('Star Wars', 'George Lucas', 1977, 125, 'Ciencia Ficcion'),
('El Señor de los Anillos: Las dos torres', 'Peter Jackson', 2002, 179, 'Fantasia'),
('Forrest Gump', 'Robert Zemeckis', 1994, 142, 'Drama'),
('The Pursuit of Happyness', 'Gabriele Muccino', 2006, 117, 'Drama'),
('The Social Network', 'David Fincher', 2010, 120, 'Drama'),
('Toy Story', 'John Lasseter', 1995, 81, 'Animacion');
