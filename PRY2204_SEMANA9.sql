-- --------------------------------------------
-- SEMANA 9 - PRY2204_SEMANA9
-- Usuario: PRY2204_S9
-- Base de datos: EFTS9ReneAlfaro (Oracle Cloud)
--> Wallet Wallet_EFTS9ReneAlfaro Contraseña: Reneduoc1234
-- --------------------------------------------

--     Proyecto "Cristalería Andina S.A."  

-- --------------------------------------------

SET DEFINE OFF

--      ===== LIMPIEZA =====
DECLARE
  v_sql VARCHAR2(4000);
BEGIN
  FOR r IN (
    SELECT table_name
    FROM user_tables
    WHERE table_name IN (
      'ORDEN_MANTENCION','ASIGNACION_TURNO',
      'DETALLE_EMPLEADO','EMPLEADO',
      'MAQUINA','TIPO_MAQUINA','TURNO','ROL_TURNO',
      'SISTEMA_SALUD','AFP','PLANTA','COMUNA','REGION'
    )
  ) LOOP
    v_sql := 'DROP TABLE "' || r.table_name || '" CASCADE CONSTRAINTS';
    BEGIN
      EXECUTE IMMEDIATE v_sql;
    EXCEPTION WHEN OTHERS THEN NULL;
    END;
  END LOOP;
END;
/
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_REGION';
EXCEPTION
  WHEN OTHERS THEN NULL; -- si no existe, seguimos
END;
/
BEGIN
  EXECUTE IMMEDIATE 'DROP TRIGGER TRG_ORDEN_TECNICO_CHECK';
EXCEPTION
  WHEN OTHERS THEN NULL; -- si no existe, seguimos
END;
/

-- --------------------------------------------
--          Tablas y Secuencias
-- --------------------------------------------

-- SECUENCIA PARA REGION
CREATE SEQUENCE SEQ_REGION START WITH 21 INCREMENT BY 1 NOCACHE;

-- TABLA REGION
CREATE TABLE REGION (
  id_region      INTEGER,
  nombre_region  VARCHAR2(50) NOT NULL,
  CONSTRAINT PK_REGION PRIMARY KEY (id_region),
  CONSTRAINT UN_REGION_NOMBRE UNIQUE (nombre_region)
);

-- TABLA COMUNA (IDENTITY: inicia en 1050, incrementa de 5 en 5)
CREATE TABLE COMUNA (
  id_comuna     INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1050 INCREMENT BY 5),
  nombre_comuna VARCHAR2(50) NOT NULL,
  id_region     INTEGER NOT NULL,
  CONSTRAINT PK_COMUNA PRIMARY KEY (id_comuna),
  CONSTRAINT FK_COMUNA_REGION FOREIGN KEY (id_region)
    REFERENCES REGION (id_region)
);

-- TABLA PLANTA
CREATE TABLE PLANTA (
  id_planta      INTEGER,
  nombre_planta  VARCHAR2(50) NOT NULL,
  direccion      VARCHAR2(100) NOT NULL,
  id_comuna      INTEGER NOT NULL,
  CONSTRAINT PK_PLANTA PRIMARY KEY (id_planta),
  CONSTRAINT FK_PLANTA_COMUNA FOREIGN KEY (id_comuna)
    REFERENCES COMUNA (id_comuna)
);

-- Tabla ROL_TURNO 
CREATE TABLE ROL_TURNO (
  id_rol_turno   INTEGER,
  nombre_rol     VARCHAR2(30) NOT NULL,
  CONSTRAINT PK_ROL_TURNO PRIMARY KEY (id_rol_turno),
  CONSTRAINT UN_ROL_TURNO_NOMBRE UNIQUE (nombre_rol)
);

-- TABLA TURNO
CREATE TABLE TURNO (
  id_turno      INTEGER,
  nombre_turno  VARCHAR2(30) NOT NULL,
  hora_inicio   CHAR(5) NOT NULL,
  hora_termino  CHAR(5) NOT NULL,
  CONSTRAINT PK_TURNO PRIMARY KEY (id_turno),
  CONSTRAINT UN_TURNO_NOMBRE UNIQUE (nombre_turno)
);

-- AFP
CREATE TABLE AFP (
  id_afp        INTEGER,
  nombre_afp    VARCHAR2(50) NOT NULL,
  CONSTRAINT PK_AFP PRIMARY KEY (id_afp),
  CONSTRAINT UN_AFP_NOMBRE UNIQUE (nombre_afp)
);

-- SISTEMA_SALUD
CREATE TABLE SISTEMA_SALUD (
  id_salud        INTEGER,
  nombre_salud    VARCHAR2(50) NOT NULL,
  CONSTRAINT PK_SALUD PRIMARY KEY (id_salud),
  CONSTRAINT UN_SALUD_NOMBRE UNIQUE (nombre_salud)
);

-- TIPO_MAQUINA
CREATE TABLE TIPO_MAQUINA (
  id_tipo_maquina  INTEGER,
  nombre_tipo      VARCHAR2(50) NOT NULL,
  CONSTRAINT PK_TIPO_MAQ PRIMARY KEY (id_tipo_maquina),
  CONSTRAINT UN_TIPO_MAQ_NOMBRE UNIQUE (nombre_tipo)
);

-- MAQUINA (PK compuesta: num_maquina, id_planta)
CREATE TABLE MAQUINA (
  num_maquina      INTEGER,
  id_planta        INTEGER,
  nombre_maquina   VARCHAR2(50) NOT NULL,
  estado_activo    CHAR(1) DEFAULT 'S' NOT NULL,
  id_tipo_maquina  INTEGER NOT NULL,
  CONSTRAINT PK_MAQUINA PRIMARY KEY (num_maquina, id_planta),
  CONSTRAINT CK_MAQ_ESTADO CHECK (estado_activo IN ('S','N')),
  CONSTRAINT FK_MAQ_PLANTA FOREIGN KEY (id_planta)
    REFERENCES PLANTA (id_planta),
  CONSTRAINT FK_MAQ_TIPO FOREIGN KEY (id_tipo_maquina)
    REFERENCES TIPO_MAQUINA (id_tipo_maquina)
);

-- EMPLEADO (supertipo)
CREATE TABLE EMPLEADO (
  id_empleado          INTEGER,
  rut                  VARCHAR2(12) NOT NULL,
  nombres              VARCHAR2(50) NOT NULL,
  apellidos            VARCHAR2(50) NOT NULL,
  fecha_contratacion   DATE NOT NULL,
  sueldo_base          NUMBER(10,2),
  estado_activo        CHAR(1) DEFAULT 'S' NOT NULL,
  id_planta            INTEGER NOT NULL,
  id_afp               INTEGER NOT NULL,
  id_salud             INTEGER NOT NULL,
  id_jefe_directo      INTEGER,
  CONSTRAINT PK_EMPLEADO PRIMARY KEY (id_empleado),
  CONSTRAINT UN_EMP_RUT UNIQUE (rut),
  CONSTRAINT CK_EMP_ESTADO CHECK (estado_activo IN ('S','N')),
  CONSTRAINT FK_EMP_PLANTA FOREIGN KEY (id_planta)
    REFERENCES PLANTA (id_planta),
  CONSTRAINT FK_EMP_AFP FOREIGN KEY (id_afp)
    REFERENCES AFP (id_afp),
  CONSTRAINT FK_EMP_SALUD FOREIGN KEY (id_salud)
    REFERENCES SISTEMA_SALUD (id_salud),
  CONSTRAINT FK_EMP_JEFE FOREIGN KEY (id_jefe_directo)
    REFERENCES EMPLEADO (id_empleado)
);

-- DETALLE_EMPLEADO (FIX: sin NBSP en CHECK >= 0)
CREATE TABLE DETALLE_EMPLEADO (
  id_empleado                INTEGER PRIMARY KEY,
  tipo_empleado              VARCHAR2(20) NOT NULL,
  -- Atributos del antiguo JEFE_TURNO
  area_responsabilidad       VARCHAR2(50),
  max_operarios              INTEGER,
  -- Atributos del antiguo OPERARIO
  categoria_proceso          VARCHAR2(20),
  certificacion              VARCHAR2(50),
  horas_estandar             INTEGER DEFAULT 8,
  -- Atributos del antiguo TECNICO_MANTENCION
  especialidad               VARCHAR2(30),
  nivel_certificacion        VARCHAR2(30),
  tiempo_respuesta_estandar  INTEGER,
  -- Restricciones y relaciones
  CONSTRAINT FK_DET_EMP FOREIGN KEY (id_empleado)
    REFERENCES EMPLEADO (id_empleado),
  CONSTRAINT CK_TIPO_EMP CHECK (tipo_empleado IN ('JEFE_TURNO','OPERARIO','TECNICO')),
  CONSTRAINT CK_OPER_CAT CHECK (categoria_proceso IS NULL OR categoria_proceso IN ('caliente','frio','inspeccion')),
  CONSTRAINT CK_OPER_HORAS CHECK (horas_estandar IS NULL OR horas_estandar > 0),
  CONSTRAINT CK_TEC_ESPEC CHECK (especialidad IS NULL OR especialidad IN ('electrica','mecanica','instrumentacion')),
  CONSTRAINT CK_TEC_TIEMPO CHECK (tiempo_respuesta_estandar IS NULL OR tiempo_respuesta_estandar >= 0)
);

-- ASIGNACION_TURNO
CREATE TABLE ASIGNACION_TURNO (
  id_asignacion     INTEGER,
  id_empleado       INTEGER NOT NULL,
  id_turno          INTEGER NOT NULL,
  num_maquina       INTEGER NOT NULL,
  id_planta         INTEGER NOT NULL,
  fecha_asignacion  DATE NOT NULL,
  id_rol_turno      INTEGER NOT NULL,
  CONSTRAINT PK_ASIGNACION PRIMARY KEY (id_asignacion),
  CONSTRAINT UNQ_ASIG_EMP_FECHA UNIQUE (id_empleado, fecha_asignacion),
  CONSTRAINT FK_ASIG_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO(id_empleado),
  CONSTRAINT FK_ASIG_TURNO FOREIGN KEY (id_turno) REFERENCES TURNO(id_turno),
  CONSTRAINT FK_ASIG_MAQ FOREIGN KEY (num_maquina, id_planta) REFERENCES MAQUINA(num_maquina, id_planta),
  CONSTRAINT FK_ASIG_ROL FOREIGN KEY (id_rol_turno) REFERENCES ROL_TURNO(id_rol_turno)
);

-- ORDEN_MANTENCION (FK_ORDEN_TEC -> EMPLEADO)
CREATE TABLE ORDEN_MANTENCION (
  id_orden           INTEGER,
  num_maquina        INTEGER NOT NULL,
  id_planta          INTEGER NOT NULL,
  id_tecnico         INTEGER NOT NULL,
  fecha_programada   DATE NOT NULL,
  fecha_ejecucion    DATE,
  descripcion        VARCHAR2(200) NOT NULL,
  CONSTRAINT PK_ORDEN PRIMARY KEY (id_orden),
  CONSTRAINT FK_ORDEN_MAQ FOREIGN KEY (num_maquina, id_planta)
    REFERENCES MAQUINA (num_maquina, id_planta),
  CONSTRAINT FK_ORDEN_TEC FOREIGN KEY (id_tecnico)
    REFERENCES EMPLEADO (id_empleado),
  CONSTRAINT CK_ORDEN_FECHAS CHECK (fecha_ejecucion IS NULL OR fecha_ejecucion >= fecha_programada)
);

-- Trigger para asegurar que id_tecnico sea un EMPLEADO de tipo 'TECNICO'
CREATE OR REPLACE TRIGGER TRG_ORDEN_TECNICO_CHECK
BEFORE INSERT OR UPDATE OF id_tecnico ON ORDEN_MANTENCION
FOR EACH ROW
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
    FROM DETALLE_EMPLEADO
   WHERE id_empleado = :NEW.id_tecnico
     AND tipo_empleado = 'TECNICO';
  IF v_count = 0 THEN
    RAISE_APPLICATION_ERROR(-20001, 'id_tecnico no corresponde a un EMPLEADO de tipo TECNICO');
  END IF;
END;
/
SHOW ERRORS

-- --------------------------------------------------------
--  Poblamiento inicial
-- --------------------------------------------------------

--                INSERTANDO DATOS BASE

-- REGION
INSERT INTO REGION (id_region, nombre_region) VALUES (SEQ_REGION.NEXTVAL, 'Región de Valparaíso');
INSERT INTO REGION (id_region, nombre_region) VALUES (SEQ_REGION.NEXTVAL, 'Región Metropolitana');

-- COMUNA
INSERT INTO COMUNA (nombre_comuna, id_region) VALUES ('Quilpué', 21);
INSERT INTO COMUNA (nombre_comuna, id_region) VALUES ('Maipú', 22);

-- PLANTA
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, id_comuna)
VALUES (45, 'Planta Oriente Camino Industrial 1234', 'Camino Industrial 1234', 1050);
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, id_comuna)
VALUES (46, 'Planta Costa Av. Vicuña Mackenna 890', 'Av. Vicuña Mackenna 890', 1055);

-- AFP y SALUD (necesarios para FKs de EMPLEADO)
INSERT INTO AFP (id_afp, nombre_afp) VALUES (1, 'Modelo');
INSERT INTO SISTEMA_SALUD (id_salud, nombre_salud) VALUES (1, 'FONASA');

-- TURNO
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_termino) VALUES (1, 'Mañana', '07:00', '15:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_termino) VALUES (2, 'Noche',  '23:00', '07:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_termino) VALUES (3, 'Tarde',  '15:00', '23:00');

-- ROL_TURNO
INSERT INTO ROL_TURNO (id_rol_turno, nombre_rol) VALUES (1, 'Moldeador');
INSERT INTO ROL_TURNO (id_rol_turno, nombre_rol) VALUES (2, 'Inspector');

-- DATOS DE EMPLEADOS 
INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (103, '15.111.222-3', 'Sebastián', 'Izquierdo', SYSDATE, 1300000, 45, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, especialidad, nivel_certificacion, tiempo_respuesta_estandar)
VALUES (103, 'TECNICO', 'mecanica', 'Nivel 3', 20);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (104, '16.222.333-4', 'María', 'Torres', SYSDATE, 950000, 46, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, categoria_proceso, certificacion, horas_estandar)
VALUES (104, 'OPERARIO', 'caliente', 'Certificación Clase A', 8);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (105, '17.333.444-5', 'Jorge', 'Ramírez', SYSDATE, 1600000, 45, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, area_responsabilidad, max_operarios)
VALUES (105, 'JEFE_TURNO', 'Área Ensamble', 12);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (106, '18.444.555-6', 'Daniela', 'Castro', SYSDATE, 980000, 46, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, categoria_proceso, certificacion, horas_estandar)
VALUES (106, 'OPERARIO', 'frio', 'Certificación Clase B', 8);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (107, '19.555.666-7', 'Felipe', 'Morales', SYSDATE, 1250000, 45, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, especialidad, nivel_certificacion, tiempo_respuesta_estandar)
VALUES (107, 'TECNICO', 'instrumentacion', 'Nivel 1', 18);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (108, '20.666.777-8', 'Camila', 'González', SYSDATE, 890000, 46, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, categoria_proceso, certificacion, horas_estandar)
VALUES (108, 'OPERARIO', 'inspeccion', 'Certificación Clase C', 8);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (109, '21.777.888-9', 'Rodrigo', 'Pérez', SYSDATE, 1550000, 45, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, area_responsabilidad, max_operarios)
VALUES (109, 'JEFE_TURNO', 'Área Control de Calidad', 8);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (110, '22.888.999-0', 'Valentina', 'Muñoz', SYSDATE, 1150000, 46, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, especialidad, nivel_certificacion, tiempo_respuesta_estandar)
VALUES (110, 'TECNICO', 'electrica', 'Nivel 2', 12);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (111, '23.999.000-1', 'Cristian', 'Fuentes', SYSDATE, 950000, 46, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, categoria_proceso, certificacion, horas_estandar)
VALUES (111, 'OPERARIO', 'caliente', 'Certificación Clase A', 8);

INSERT INTO EMPLEADO (id_empleado, rut, nombres, apellidos, fecha_contratacion, sueldo_base, id_planta, id_afp, id_salud)
VALUES (112, '24.000.111-2', 'Isabel', 'Reyes', SYSDATE, 1600000, 45, 1, 1);
INSERT INTO DETALLE_EMPLEADO (id_empleado, tipo_empleado, area_responsabilidad, max_operarios)
VALUES (112, 'JEFE_TURNO', 'Área Mantenimiento', 9);

-- INFORME 1: Turnos con horario de inicio posterior a 20:00
SELECT 
    nombre_turno     AS "TURNO",
    hora_inicio      AS "ENTRADA",
    hora_termino     AS "SALIDA"
FROM TURNO
WHERE hora_inicio > '20:00'
ORDER BY hora_inicio DESC;

-- INFORME 2 (igual que original): Turnos diurnos (inicio entre 06:00 y 14:59)
SELECT 
    nombre_turno     AS "TURNO",
    hora_inicio      AS "ENTRADA",
    hora_termino     AS "SALIDA"
FROM TURNO
WHERE hora_inicio BETWEEN '06:00' AND '14:59'
ORDER BY hora_inicio ASC;

-- 1) COMUNA -> REGION
SELECT COUNT(*) AS comunas_sin_region
FROM COMUNA c
WHERE c.id_region NOT IN (SELECT id_region FROM REGION);

-- 2) PLANTA -> COMUNA
SELECT COUNT(*) AS plantas_sin_comuna
FROM PLANTA p
WHERE p.id_comuna NOT IN (SELECT id_comuna FROM COMUNA);

-- 3) EMPLEADO -> AFP/SALUD/PLANTA
SELECT COUNT(*) AS empleados_sin_afp
FROM EMPLEADO e
WHERE e.id_afp NOT IN (SELECT id_afp FROM AFP);

SELECT COUNT(*) AS empleados_sin_salud
FROM EMPLEADO e
WHERE e.id_salud NOT IN (SELECT id_salud FROM SISTEMA_SALUD);

SELECT COUNT(*) AS empleados_sin_planta
FROM EMPLEADO e
WHERE e.id_planta NOT IN (SELECT id_planta FROM PLANTA);

-- 4) MAQUINA -> PLANTA / TIPO_MAQUINA
SELECT COUNT(*) AS maquinas_sin_planta
FROM MAQUINA m
WHERE m.id_planta NOT IN (SELECT id_planta FROM PLANTA);

SELECT COUNT(*) AS maquinas_sin_tipo
FROM MAQUINA m
WHERE m.id_tipo_maquina NOT IN (SELECT id_tipo_maquina FROM TIPO_MAQUINA);

-- 5) ASIGNACION_TURNO -> EMPLEADO / TURNO / MAQUINA / ROL_TURNO (nuevo)
SELECT COUNT(*) AS asignaciones_sin_empleado
FROM ASIGNACION_TURNO a
WHERE a.id_empleado NOT IN (SELECT id_empleado FROM EMPLEADO);

SELECT COUNT(*) AS asignaciones_sin_turno
FROM ASIGNACION_TURNO a
WHERE a.id_turno NOT IN (SELECT id_turno FROM TURNO);

SELECT COUNT(*) AS asignaciones_sin_maquina
FROM ASIGNACION_TURNO a
WHERE (a.num_maquina, a.id_planta) NOT IN (SELECT num_maquina, id_planta FROM MAQUINA);

SELECT COUNT(*) AS asignaciones_sin_rol
FROM ASIGNACION_TURNO a
WHERE a.id_rol_turno NOT IN (SELECT id_rol_turno FROM ROL_TURNO);

-- 6) ORDEN_MANTENCION: técnico válido y máquina válida

SELECT COUNT(*) AS ordenes_sin_tecnico_valido
FROM ORDEN_MANTENCION o
LEFT JOIN DETALLE_EMPLEADO d
       ON d.id_empleado = o.id_tecnico
      AND d.tipo_empleado = 'TECNICO'
WHERE d.id_empleado IS NULL;

SELECT COUNT(*) AS ordenes_sin_maquina
FROM ORDEN_MANTENCION o
WHERE (o.num_maquina, o.id_planta) NOT IN (SELECT num_maquina, id_planta FROM MAQUINA);

-- Vista que "emula" TECNICO_MANTENCION
SELECT 
  e.id_empleado,
  e.rut, e.nombres, e.apellidos,
  d.especialidad, d.nivel_certificacion, d.tiempo_respuesta_estandar
FROM EMPLEADO e
JOIN DETALLE_EMPLEADO d ON d.id_empleado = e.id_empleado
WHERE d.tipo_empleado = 'TECNICO';

-- Vista que "emula" OPERARIO
SELECT 
  e.id_empleado,
  e.rut, e.nombres, e.apellidos,
  d.categoria_proceso, d.certificacion, d.horas_estandar
FROM EMPLEADO e
JOIN DETALLE_EMPLEADO d ON d.id_empleado = e.id_empleado
WHERE d.tipo_empleado = 'OPERARIO';

-- Vista que "emula" JEFE_TURNO
SELECT 
    e.id_empleado,
    e.rut,
    e.nombres,
    e.apellidos,
    d.area_responsabilidad,
    d.max_operarios
FROM EMPLEADO e
INNER JOIN DETALLE_EMPLEADO d 
    ON d.id_empleado = e.id_empleado
WHERE d.tipo_empleado = 'JEFE_TURNO'
ORDER BY e.id_empleado;

COMMIT;

-- ------------------------------------------------------------
-- FIN DEL SCRIPT PRY2204_SEMANA9
-- ------------------------------------------------------------