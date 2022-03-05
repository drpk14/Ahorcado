CREATE DATABASE ahorcado;

CREATE TABLE usuario(
	nombre VARCHAR(30),
    contrasenia VARCHAR(15),
    partidasJugadas INT,
    partidasGanadas INT,
    tiempoJugado FLOAT,
    rol VARCHAR(20),
    CONSTRAINT usuario_pk PRIMARY KEY (nombre)
)

INSERT INTO `usuario`(`nombre`, `contrasenia`, `partidasJugadas`, `partidasGanadas`, `tiempoJugado`, `rol`) VALUES ('root','root',0,0,0,'administrador');