-- ----------------------------------------------------------------------------- 
-- SEMANA 6 - PRY2205_SEMANA6
-- Base de datos: PRY2205_ACTIVIDAD_S6 (Oracle Cloud) 
-- Estudiante: René Alfaro 
-- -----------------------------------------------------------------------------

-- Proyecto "Servicio de Asesoria de Empresas - Comercial Manager Consultants (CMC)"

-- -----------------------------------------------------------------------------   

-- =============================================================================
-- Informe CASO 1: "Reportería de Asesorías"
-- =============================================================================

SELECT 
    p.id_profesional AS "ID",
    INITCAP(p.appaterno || ' ' || p.apmaterno || ' ' || p.nombre) AS "PROFESIONAL",
    
    -- Cálculos para BANCA (Sector 3)
    COUNT(CASE WHEN e.cod_sector = 3 THEN 1 END) AS "NRO ASESORIA BANCA",
    TO_CHAR(ROUND(SUM(CASE WHEN e.cod_sector = 3 THEN a.honorario ELSE 0 END)), '$99G999G999') AS "MONTO_TOTAL_BANCA",
    
    -- Cálculos para RETAIL (Sector 4)
    COUNT(CASE WHEN e.cod_sector = 4 THEN 1 END) AS "NRO ASESORIA RETAIL",
    TO_CHAR(ROUND(SUM(CASE WHEN e.cod_sector = 4 THEN a.honorario ELSE 0 END)), '$99G999G999') AS "MONTO_TOTAL_RETAIL",
    
    -- Totales (Suma de ambas áreas)
    COUNT(*) AS "TOTAL ASESORIAS",
    TO_CHAR(ROUND(SUM(a.honorario)), '$99G999G999') AS "TOTAL HONORARIOS"

FROM profesional p
INNER JOIN asesoria a ON p.id_profesional = a.id_profesional -- Agregamos INNER para claridad
INNER JOIN empresa e ON a.cod_empresa = e.cod_empresa      -- Agregamos INNER para claridad

WHERE p.id_profesional IN (
    -- Subconsulta para filtrar Profesionales Versátiles (Banca + Retail)
    SELECT a_banca.id_profesional
    FROM asesoria a_banca
    INNER JOIN empresa e_banca ON a_banca.cod_empresa = e_banca.cod_empresa
    WHERE e_banca.cod_sector = 3
    
    INTERSECT
    
    SELECT a_retail.id_profesional
    FROM asesoria a_retail
    INNER JOIN empresa e_retail ON a_retail.cod_empresa = e_retail.cod_empresa
    WHERE e_retail.cod_sector = 4
)
AND e.cod_sector IN (3, 4) 

GROUP BY p.id_profesional, p.appaterno, p.apmaterno, p.nombre
ORDER BY p.id_profesional ASC;

-- =============================================================================
-- Informe CASO 2: "Resumen de Honorarios"
-- =============================================================================

-- Paso 1: Borramos la tabla por si ya existe
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE REPORTE_MES';
EXCEPTION
    WHEN OTHERS THEN NULL; -- Si no existe, no hace nada y sigue
END;
/

-- Paso 2: Creamos la tabla con los resultados
CREATE TABLE REPORTE_MES AS
SELECT 
    p.id_profesional AS "ID_PROF",
    
    -- Nombre completo formateado (Apellido Paterno + Materno + Nombre)
    INITCAP(p.appaterno || ' ' || p.apmaterno || ' ' || p.nombre) AS "NOMBRE_COMPLETO",
    
    -- Cruce con tabla Profesión
    pr.nombre_profesion AS "NOMBRE_PROFESION",
    
    -- Cruce con tabla Comuna
    c.nom_comuna AS "NOM_COMUNA",
    
    -- Estadísticas solicitadas (con valores redondeados)
    COUNT(a.honorario) AS "NRO_ASESORIAS",
    ROUND(SUM(a.honorario)) AS "MONTO_TOTAL_HONORARIOS",
    ROUND(AVG(a.honorario)) AS "PROMEDIO_HONORARIO",
    ROUND(MIN(a.honorario)) AS "HONORARIO_MINIMO",
    ROUND(MAX(a.honorario)) AS "HONORARIO_MAXIMO"

FROM profesional p
INNER JOIN profesion pr ON p.cod_profesion = pr.cod_profesion
INNER JOIN comuna c ON p.cod_comuna = c.cod_comuna
INNER JOIN asesoria a ON p.id_profesional = a.id_profesional

WHERE 
    -- REQUISITO: Abril del año pasado (Dinámico)
    EXTRACT(MONTH FROM a.fin_asesoria) = 4 
    AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1

GROUP BY 
    p.id_profesional, 
    p.appaterno, 
    p.apmaterno, 
    p.nombre, 
    pr.nombre_profesion, 
    c.nom_comuna

ORDER BY p.id_profesional ASC;

-- Paso 3: Verificamos que la tabla se creó y tiene datos (Solo para visualización)
SELECT * FROM REPORTE_MES;

-- =============================================================================
-- Informe CASO 3: "Modificación de Honorarios"
-- =============================================================================

-- /* 1. REPORTE DE VERIFICACIÓN "ANTES" DE LA ACTUALIZACIÓN */

-- (Para ver los sueldos originales)
SELECT 
    SUM(a.honorario) AS "HONORARIO",
    p.id_profesional AS "ID_PROFESIONAL",
    p.numrun_prof AS "NUMRUN_PROF",
    p.sueldo AS "SUELDO"
FROM profesional p
JOIN asesoria a ON p.id_profesional = a.id_profesional
WHERE EXTRACT(MONTH FROM a.fin_asesoria) = 3 -- Solo Marzo
  AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1 -- Año pasado
GROUP BY p.id_profesional, p.numrun_prof, p.sueldo
ORDER BY p.id_profesional;


-- /* 2. PROCESO DE ACTUALIZACIÓN (UPDATE)*/

UPDATE profesional p
SET sueldo = sueldo * (
    -- Subconsulta para calcular el factor de aumento según el total de honorarios
    SELECT CASE 
        WHEN SUM(a.honorario) < 1000000 THEN 1.10 -- Aumento del 10%
        ELSE 1.15                                 -- Aumento del 15%
    END
    FROM asesoria a
    WHERE a.id_profesional = p.id_profesional
      AND EXTRACT(MONTH FROM a.fin_asesoria) = 3
      AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
)
WHERE EXISTS (
    -- Condición: Solo actualizamos a quienes TRABAJARON en Marzo del año pasado
    SELECT 1
    FROM asesoria a
    WHERE a.id_profesional = p.id_profesional
      AND EXTRACT(MONTH FROM a.fin_asesoria) = 3
      AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
);

-- confirmación de cambios:
COMMIT; 

-- /* 3. REPORTE DE VERIFICACIÓN "DESPUÉS" DE LA ACTUALIZACIÓN         */

SELECT 
    SUM(a.honorario) AS "HONORARIO",
    p.id_profesional AS "ID_PROFESIONAL",
    p.numrun_prof AS "NUMRUN_PROF",
    p.sueldo AS "SUELDO"
FROM profesional p
JOIN asesoria a ON p.id_profesional = a.id_profesional
WHERE EXTRACT(MONTH FROM a.fin_asesoria) = 3
  AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
GROUP BY p.id_profesional, p.numrun_prof, p.sueldo
ORDER BY p.id_profesional;

-- fin del script
