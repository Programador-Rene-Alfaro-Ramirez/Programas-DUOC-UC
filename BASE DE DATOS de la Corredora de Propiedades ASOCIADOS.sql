-- -------------------------------------------------------- 
-- SEMANA 3 - PRY2205_SEMANA3 
-- Usuario: PRY2205_S3 
-- Base de datos: PRY2205_ACTIVIDAD_S3 (Oracle Cloud) 
-- Wallet: Wallet_PHUYBTJLPM8F9BY0
-- Contraseña: PRY2205.semana_3
-- -------------------------------------------------------- 
-- Proyecto "ASOCIADOS Corredora de Propiedades"
-- --------------------------------------------------------

-- LIMPIEZA GLOBAL COMPLETA (ORDEN SEGÚN DEPENDENCIAS)
BEGIN
   -- Primero las tablas hijas (dependen de otras)
   DELETE FROM propiedad;
   DELETE FROM empleado;
   DELETE FROM categoria_empleado;
   DELETE FROM tipo_propiedad;
   DELETE FROM propietario;
   DELETE FROM cliente;
   DELETE FROM sucursal;
   DELETE FROM comuna;

   COMMIT;
EXCEPTION
   WHEN OTHERS THEN
      NULL; -- Ignora errores si alguna tabla no existe o está vacía
END;
/
PROMPT Limpieza global completada correctamente.

-- CREACIÓN DE USUARIO
DECLARE
   v_count NUMBER;
BEGIN
   -- Verifica si el usuario ya existe en la base de datos
   SELECT COUNT(*) INTO v_count 
   FROM dba_users 
   WHERE username = 'PRY2205_S3';

   IF v_count = 0 THEN
      EXECUTE IMMEDIATE '
         CREATE USER PRY2205_S3 
         IDENTIFIED BY "PRY2205.semana_3"
         DEFAULT TABLESPACE DATA
         TEMPORARY TABLESPACE TEMP
         QUOTA UNLIMITED ON DATA';
   ELSE
      DBMS_OUTPUT.PUT_LINE('Usuario PRY2205_S3 ya existe, se omitió la creación.');
   END IF;
END;
/

PROMPT Validación de usuario completada.

-- ASIGNACIÓN DE PRIVILEGIOS
GRANT CREATE SESSION TO PRY2205_S3;
GRANT RESOURCE TO PRY2205_S3;
ALTER USER PRY2205_S3 DEFAULT ROLE RESOURCE;
PROMPT Privilegios asignados correctamente.

-- CONFIRMAR ESTADO DEL USUARIO
COLUMN USERNAME FORMAT A20
COLUMN ACCOUNT_STATUS FORMAT A20
SELECT username, account_status 
FROM dba_users 
WHERE username = 'PRY2205_S3';

-- ================================================================
-- CASO 1: LISTADO DE CLIENTES POR RANGO DE RENTA
-- ================================================================

-- CONFIGURACIÓN DE FORMATO EN FECHAS Y CARACTERES
ALTER SESSION SET NLS_DATE_FORMAT='DD/MM/YYYY';
ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.'; -- punto como separador de miles

-- LIMPIEZA
DELETE FROM propiedad;
DELETE FROM tipo_propiedad;
DELETE FROM propietario;
DELETE FROM empleado;
DELETE FROM sucursal;
DELETE FROM comuna;
DELETE FROM categoria_empleado;
DELETE FROM cliente;
COMMIT;

-- INSERCIÓN DE CLIENTES
INSERT INTO cliente (numrut_cli, dvrut_cli, appaterno_cli, apmaterno_cli, nombre_cli, direccion_cli, fonofijo_cli, celular_cli, renta_cli) VALUES 
(12415220, '5', 'Castro', 'Tobar', 'Alejandra', 'Rodr. De Araya 4651b B/11 Depto. 42', 22415678, 68546237, 500000);
INSERT INTO cliente VALUES (13057574, '1', 'Cornejo', 'Gonzalez', 'Alejandro', 'Cienclas #8442 P/Biaut', 22334455, 72950789, 400000);
INSERT INTO cliente VALUES (12302426, '1', 'Alvarado', 'Arauna', 'Alex', 'Pje. Chonchi 6678 V/Los Troncos', 22334455, 67783253, 1200000);
INSERT INTO cliente VALUES (13269751, '6', 'Valle', 'Diaz', 'Alexis', 'Cuatro Remos 580 V/Ant. Varas', 22556688, 57788381, 240000);
INSERT INTO cliente VALUES (12960006, '3', 'Marillanca', 'Carvajal', 'Antonio', 'Sitio 37b Pinte Laguna Aculeo', 22112233, 62562637, 320000);
INSERT INTO cliente VALUES (9964101, '2', 'Meneses', 'Rubio', 'Carlos', 'Santa Marta 0713', 22667788, 26438047, 200000);
INSERT INTO cliente VALUES (13050258, '0', 'Salas', 'Cornejo', 'Carlos', 'Valle Del Sol N°4028', 22667788, 25287698, 300000);
INSERT INTO cliente VALUES (12832359, '0', 'Salas', 'Toro', 'Carlos', 'Pje. Lleuque 0861 V/El Peral 3', 22445566, 77764463, 300000);
INSERT INTO cliente VALUES (13088742, '6', 'Cid', 'Padilla', 'Carolina', 'Av. Fraternal 3910', 22889900, 88721366, 260000);
INSERT INTO cliente VALUES (13074837, '1', 'Amengual', 'Saldias', 'Cesar', 'Pje. Codornices 1550 V/El Rodeo', 22889900, 55212406, 400000);
INSERT INTO cliente VALUES (12817700, 'K', 'Vargas', 'Garay', 'Claudia', 'Pje. Belen N°8 /Geraldine', 22331122, 98122441, 400000);
INSERT INTO cliente VALUES (11949670, '2', 'Contreras', 'Miranda', 'Claudio', 'Vista Hermosa 2126 /P.H.Alto', 22990011, 65582082, 500000);
INSERT INTO cliente VALUES (12947165, '6', 'Hisi', 'Diaz', 'Rosa', 'Icaro 3580 V/Santa Ines', 22223344, 52893123, 250000);
INSERT INTO cliente VALUES (14526736, '1', 'Valenzuela', 'Montoya', 'Rosa', 'Genaro Prieto 910 P/El Tranque', 22778899, 96469095, 400000);
INSERT INTO cliente VALUES (12459100, '8', 'Poblete', 'Fuentes', 'Sergio', 'Tinguiririca 3553 V/Foresta', 22334455, 75631102, 340000);
INSERT INTO cliente VALUES (10711069, '6', 'Cisternas', 'Saavedra', 'Victor', 'Lago Gris 4673 V/El Alba', 22001122, 67813075, 250000);
INSERT INTO cliente VALUES (13072796, '5', 'Medina', 'Camus', 'Wanda', 'Ictinos 1162 Villa Pedro Lagos', 22660077, 95586941, 300000);
INSERT INTO cliente VALUES (13043565, '7', 'Aguilera', 'Roman', 'Willibaldo', 'Andes 4717', 22880099, 55285702, 380000);
COMMIT;

SELECT COUNT(*) AS "Total clientes poblados" FROM cliente;

-- CONSULTA CASO 1
SELECT 
    TO_CHAR(numrut_cli, 'FM99G999G999', 'NLS_NUMERIC_CHARACTERS='',.''') || '-' || dvrut_cli AS "RUT Cliente",
    TRIM(nombre_cli || ' ' || appaterno_cli || ' ' || apmaterno_cli) AS "Nombre Completo Cliente",
    direccion_cli AS "Dirección Cliente",
    TO_CHAR(renta_cli, '$999G999G999', 'NLS_NUMERIC_CHARACTERS='',.''') AS "Renta Cliente",
    '0' || SUBSTR(celular_cli, 1, 1) || '-' || SUBSTR(celular_cli, 2, 3) || '-' || SUBSTR(celular_cli, 5, 4) AS "Celular Cliente",
    CASE 
        WHEN renta_cli > 500000 THEN 'TRAMO 1'
        WHEN renta_cli BETWEEN 400000 AND 500000 THEN 'TRAMO 2'
        WHEN renta_cli BETWEEN 200000 AND 399999 THEN 'TRAMO 3'
        ELSE 'TRAMO 4'
    END AS "Tramo Renta Cliente"
FROM cliente
WHERE celular_cli IS NOT NULL
  AND renta_cli BETWEEN &RENTA_MINIMA AND &RENTA_MAXIMA
ORDER BY TRIM(nombre_cli || ' ' || appaterno_cli || ' ' || apmaterno_cli);

-- ================================================================
-- CASO 2: SUELDO PROMEDIO POR CATEGORÍA DE EMPLEADO
-- ================================================================

-- LIMPIEZA 
DELETE FROM empleado;
DELETE FROM sucursal;
DELETE FROM comuna;
DELETE FROM categoria_empleado;
COMMIT;

-- INSERCIÓN DE COMUNAS
INSERT INTO comuna VALUES (80, 'Las Condes');
INSERT INTO comuna VALUES (81, 'Santiago Centro');
INSERT INTO comuna VALUES (82, 'Providencia');
INSERT INTO comuna VALUES (83, 'Vitacura');
COMMIT;

-- INSERCIÓN DE SUCURSALES
INSERT INTO sucursal VALUES (10, 'Sucursal Las Condes', 'Dir Las Condes', 80);
INSERT INTO sucursal VALUES (20, 'Sucursal Santiago Centro', 'Dir Santiago Centro', 81);
INSERT INTO sucursal VALUES (30, 'Sucursal Providencia', 'Dir Providencia', 82);
INSERT INTO sucursal VALUES (40, 'Sucursal Vitacura', 'Dir Vitacura', 83);
COMMIT;

-- INSERCIÓN DE CATEGORÍAS DE EMPLEADOS
INSERT INTO categoria_empleado VALUES (1, 'Gerente');
INSERT INTO categoria_empleado VALUES (2, 'Supervisor');
INSERT INTO categoria_empleado VALUES (3, 'Ejecutivo de Arriendo');
COMMIT;

-- INSERCIÓN DE EMPLEADOS EN SUCURSALES
/* Supervisor – Las Condes */
INSERT INTO empleado VALUES (20000001,'1','Supervisor','Ejemplo','Rodrigo',empty_blob(),'Dir1','20000001','920000001',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),2658577,2,10);

/* Supervisor – Providencia */
INSERT INTO empleado VALUES (20000002,'2','Supervisor','Ejemplo','Sofía',empty_blob(),'Dir2','20000002','920000002',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1144245,2,30);

/* Gerente – Las Condes */
INSERT INTO empleado VALUES (20000003,'3','Gerente','Ejemplo','Carlos',empty_blob(),'Dir3','20000003','920000003',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1515239,1,10);

/* Ejecutivos – Santiago Centro (2 empleados) */
INSERT INTO empleado VALUES (20000004,'4','Ejecutivo','Arriendo','Juan',empty_blob(),'Dir4','20000004','920000004',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),2303154,3,20);
INSERT INTO empleado VALUES (20000005,'5','Ejecutivo','Arriendo','Pedro',empty_blob(),'Dir5','20000005','920000005',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),2303154,3,20);

/* Ejecutivos – Providencia (5 empleados) */
INSERT INTO empleado VALUES (30000001,'1','Ejecutivo1','Arriendo1','Luis1',empty_blob(),'Dir1','30000001','930000001',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1855874,3,30);
INSERT INTO empleado VALUES (30000002,'2','Ejecutivo2','Arriendo2','Luis2',empty_blob(),'Dir2','30000002','930000002',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1855874,3,30);
INSERT INTO empleado VALUES (30000003,'3','Ejecutivo3','Arriendo3','Luis3',empty_blob(),'Dir3','30000003','930000003',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1855874,3,30);
INSERT INTO empleado VALUES (30000004,'4','Ejecutivo4','Arriendo4','Luis4',empty_blob(),'Dir4','30000004','930000004',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1855874,3,30);
INSERT INTO empleado VALUES (30000005,'5','Ejecutivo5','Arriendo5','Luis5',empty_blob(),'Dir5','30000005','930000005',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1855874,3,30);

/* Ejecutivos – Vitacura (5 empleados) */
INSERT INTO empleado VALUES (40000001,'1','Ejecutivo1','Arriendo1','Ana1',empty_blob(),'Dir1','40000001','940000001',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1703355,3,40);
INSERT INTO empleado VALUES (40000002,'2','Ejecutivo2','Arriendo2','Ana2',empty_blob(),'Dir2','40000002','940000002',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1703355,3,40);
INSERT INTO empleado VALUES (40000003,'3','Ejecutivo3','Arriendo3','Ana3',empty_blob(),'Dir3','40000003','940000003',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1703355,3,40);
INSERT INTO empleado VALUES (40000004,'4','Ejecutivo4','Arriendo4','Ana4',empty_blob(),'Dir4','40000004','940000004',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1703355,3,40);
INSERT INTO empleado VALUES (40000005,'5','Ejecutivo5','Arriendo5','Ana5',empty_blob(),'Dir5','40000005','940000005',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1703355,3,40);

/* Ejecutivos – Las Condes (5 empleados) */
INSERT INTO empleado VALUES (50000001,'1','Ejecutivo1','Arriendo1','Marcelo1',empty_blob(),'Dir1','50000001','950000001',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1535729,3,10);
INSERT INTO empleado VALUES (50000002,'2','Ejecutivo2','Arriendo2','Marcelo2',empty_blob(),'Dir2','50000002','950000002',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1535729,3,10);
INSERT INTO empleado VALUES (50000003,'3','Ejecutivo3','Arriendo3','Marcelo3',empty_blob(),'Dir3','50000003','950000003',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1535729,3,10);
INSERT INTO empleado VALUES (50000004,'4','Ejecutivo4','Arriendo4','Marcelo4',empty_blob(),'Dir4','50000004','950000004',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1535729,3,10);
INSERT INTO empleado VALUES (50000005,'5','Ejecutivo5','Arriendo5','Marcelo5',empty_blob(),'Dir5','50000005','950000005',
TO_DATE('01/01/1990','DD/MM/YYYY'),TO_DATE('01/01/2020','DD/MM/YYYY'),1535729,3,10);

COMMIT;

-- CONSULTA CASO 2
SELECT
    e.id_categoria_emp AS "CÓDIGO_CATEGORÍA",
    INITCAP(
        CASE e.id_categoria_emp
            WHEN 1 THEN 'Gerente'
            WHEN 2 THEN 'Supervisor'
            WHEN 3 THEN 'Ejecutivo de Arriendo'
            WHEN 4 THEN 'Auxiliar'
            ELSE NVL(TO_CHAR(e.id_categoria_emp), 'Sin Categoría')
        END
    ) AS "DESCRIPCIÓN_CATEGORÍA",
    COUNT(e.numrut_emp) AS "CANTIDAD_EMPLEADOS",
    INITCAP(
        CASE e.id_sucursal
            WHEN 10 THEN 'Sucursal Las Condes'
            WHEN 20 THEN 'Sucursal Santiago Centro'
            WHEN 30 THEN 'Sucursal Providencia'
            WHEN 40 THEN 'Sucursal Vitacura'
            ELSE NVL(TO_CHAR(e.id_sucursal), 'Sin Sucursal')
        END
    ) AS "SUCURSAL",
    TO_CHAR(AVG(NVL(e.sueldo_emp,0)), '$999G999G999') AS "SUELDO_PROMEDIO"
FROM empleado e
GROUP BY e.id_categoria_emp, e.id_sucursal
HAVING AVG(NVL(e.sueldo_emp,0)) > &SUELDO_PROMEDIO_MINIMO
ORDER BY AVG(e.sueldo_emp) DESC;

-- =============================================================================
-- CASO 3: ARRIENDO PROMEDIO POR TIPO DE PROPIEDAD
-- =============================================================================

-- LIMPIEZA
BEGIN
   DELETE FROM propiedad;
   DELETE FROM tipo_propiedad;
   DELETE FROM propietario;
   COMMIT;
EXCEPTION
   WHEN OTHERS THEN
      NULL;
END;
/

-- INSERCIÓN TIPO DE PROPIEDAD
INSERT INTO tipo_propiedad VALUES ('A', 'CASA');
INSERT INTO tipo_propiedad VALUES ('B', 'DEPARTAMENTO');
INSERT INTO tipo_propiedad VALUES ('C', 'LOCAL');
INSERT INTO tipo_propiedad VALUES ('D', 'PARCELA SIN CASA');
INSERT INTO tipo_propiedad VALUES ('E', 'PARCELA CON CASA');
COMMIT;

-- INSERCIÓN DE PROPIETARIOS
INSERT INTO propietario VALUES (11111111, '1', 'Pérez', 'Gómez', 'Juan', 'Dir Prop 1', '222345001', '912345001');
INSERT INTO propietario VALUES (11111112, '2', 'Soto', 'López', 'Pedro', 'Dir Prop 2', '222345002', '912345002');
INSERT INTO propietario VALUES (11111113, '3', 'Lagos', 'Fuentes', 'Luis', 'Dir Prop 3', '222345003', '912345003');
INSERT INTO propietario VALUES (11111114, '4', 'Morales', 'Pérez', 'Sofía', 'Dir Prop 4', '222345004', '912345004');
INSERT INTO propietario VALUES (11111115, '5', 'Araya', 'Fuentes', 'María', 'Dir Prop 5', '222345005', '912345005');
INSERT INTO propietario VALUES (11111116, '6', 'Díaz', 'Mena', 'Claudio', 'Dir Prop 6', '222345006', '912345006');
INSERT INTO propietario VALUES (11111117, '7', 'Muñoz', 'Reyes', 'Jorge', 'Dir Prop 7', '222345007', '912345007');
INSERT INTO propietario VALUES (11111118, '8', 'Rojas', 'Navarro', 'Camila', 'Dir Prop 8', '222345008', '912345008');
INSERT INTO propietario VALUES (11111119, '9', 'Díaz', 'Caro', 'Felipe', 'Dir Prop 9', '222345009', '912345009');
INSERT INTO propietario VALUES (11111120, '0', 'Soto', 'Aravena', 'Ana', 'Dir Prop 10', '222345010', '912345010');
COMMIT;

-- INSERCIÓN DE PROPIEDADES
INSERT INTO propiedad VALUES (1, 'Parcela San Pedro', 66, 0, 0, 1300000, NULL, 'D', 80, 11111111, NULL);
INSERT INTO propiedad VALUES (2, 'Parcela con Casa 1', 60, 3, 1, 1200000, NULL, 'E', 81, 11111112, NULL);
INSERT INTO propiedad VALUES (3, 'Parcela con Casa 2', 67, 3, 2, 1200000, NULL, 'E', 81, 11111113, NULL);
INSERT INTO propiedad VALUES (4,  'Depto 1', 58, 2, 1, 400000, 50000, 'B', 82, 11111114, NULL);
INSERT INTO propiedad VALUES (5,  'Depto 2', 60, 2, 1, 380000, 50000, 'B', 82, 11111115, NULL);
INSERT INTO propiedad VALUES (6,  'Depto 3', 62, 2, 2, 390000, 55000, 'B', 82, 11111116, NULL);
INSERT INTO propiedad VALUES (7,  'Depto 4', 59, 2, 1, 395000, 50000, 'B', 82, 11111117, NULL);
INSERT INTO propiedad VALUES (8,  'Depto 5', 57, 1, 1, 400000, 40000, 'B', 82, 11111118, NULL);
INSERT INTO propiedad VALUES (9,  'Depto 6', 61, 2, 1, 380000, 45000, 'B', 82, 11111119, NULL);
INSERT INTO propiedad VALUES (10, 'Depto 7', 60, 2, 1, 370000, 50000, 'B', 82, 11111120, NULL);
INSERT INTO propiedad VALUES (11, 'Depto 8', 61, 2, 1, 400000, 50000, 'B', 82, 11111111, NULL);
INSERT INTO propiedad VALUES (12, 'Depto 9', 58, 2, 1, 385000, 45000, 'B', 82, 11111112, NULL);
INSERT INTO propiedad VALUES (13, 'Depto 10', 60, 2, 1, 390000, 50000, 'B', 82, 11111113, NULL);
INSERT INTO propiedad VALUES (14, 'Depto 11', 59, 2, 1, 385000, 45000, 'B', 82, 11111114, NULL);
INSERT INTO propiedad VALUES (15, 'Casa 1', 80, 3, 2, 500000, 60000, 'A', 80, 11111115, NULL);
INSERT INTO propiedad VALUES (16, 'Casa 2', 78, 3, 2, 510000, 60000, 'A', 80, 11111116, NULL);
INSERT INTO propiedad VALUES (17, 'Casa 3', 82, 4, 3, 490000, 60000, 'A', 80, 11111117, NULL);
INSERT INTO propiedad VALUES (18, 'Casa 4', 77, 3, 2, 520000, 60000, 'A', 80, 11111118, NULL);
INSERT INTO propiedad VALUES (19, 'Casa 5', 79, 3, 2, 505000, 60000, 'A', 80, 11111119, NULL);
INSERT INTO propiedad VALUES (20, 'Casa 6', 80, 3, 2, 515000, 60000, 'A', 80, 11111120, NULL);
INSERT INTO propiedad VALUES (21, 'Casa 7', 78, 3, 2, 510000, 60000, 'A', 80, 11111111, NULL);
INSERT INTO propiedad VALUES (22, 'Casa 8', 81, 4, 3, 500000, 60000, 'A', 80, 11111112, NULL);
INSERT INTO propiedad VALUES (23, 'Casa 9', 80, 3, 2, 515000, 60000, 'A', 80, 11111113, NULL);
INSERT INTO propiedad VALUES (24, 'Local 1', 50, 0, 1, 210000, NULL, 'C', 83, 11111114, NULL);
INSERT INTO propiedad VALUES (25, 'Local 2', 48, 0, 1, 200000, NULL, 'C', 83, 11111115, NULL);
INSERT INTO propiedad VALUES (26, 'Local 3', 50, 0, 1, 210000, NULL, 'C', 83, 11111116, NULL);
COMMIT;

-- CONSULTA CASO 3
SELECT
    tp.ID_TIPO_PROPIEDAD AS "CÓDIGO_TIPO",
    tp.DESC_TIPO_PROPIEDAD AS "DESCRIPCIÓN_TIPO",
    COUNT(p.NRO_PROPIEDAD) AS "TOTAL PROPIEDADES",
    TO_CHAR(AVG(p.VALOR_ARRIENDO), '$999G999G999', 'NLS_NUMERIC_CHARACTERS='',.''') AS "PROMEDIO_ARRIENDO",
    TO_CHAR(ROUND(AVG(p.SUPERFICIE), 2), '999G990D00', 'NLS_NUMERIC_CHARACTERS='',.''') AS "PROMEDIO_SUPERFICIE",
    TO_CHAR(ROUND(AVG(p.VALOR_ARRIENDO / p.SUPERFICIE)), '$999G999', 'NLS_NUMERIC_CHARACTERS='',.''') AS "VALOR_ARRIENDO_M2",
    CASE 
        WHEN AVG(p.VALOR_ARRIENDO / p.SUPERFICIE) < 5000 THEN 'Económico'
        WHEN AVG(p.VALOR_ARRIENDO / p.SUPERFICIE) BETWEEN 5000 AND 10000 THEN 'Medio'
        ELSE 'Alto'
    END AS "CLASIFICACIÓN"
FROM propiedad p
JOIN tipo_propiedad tp ON p.ID_TIPO_PROPIEDAD = tp.ID_TIPO_PROPIEDAD
WHERE p.SUPERFICIE > 0
GROUP BY tp.ID_TIPO_PROPIEDAD, tp.DESC_TIPO_PROPIEDAD
HAVING AVG(p.VALOR_ARRIENDO / p.SUPERFICIE) > 1000
ORDER BY AVG(p.VALOR_ARRIENDO / p.SUPERFICIE) DESC;

-- ================================================================
-- FIN DEL SCRIPT
-- ================================================================
