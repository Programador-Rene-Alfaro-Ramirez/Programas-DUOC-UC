-- ----------------------------------------------------------------------------- 
-- SEMANA 4 - PRY2206_SEMANA4
-- Base de datos: PRACT4_PRY2206 (Oracle Cloud) 
-- Estudiante: René Alfaro & Verioska Ramírez
-- -----------------------------------------------------------------------------

-- Proyecto " Tarjeta CATB - Tienda Retail ALL THE BEST"

-- =============================================================================
-- BLOQUE ADMIN (ADMIN_PRY2206): Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER PRY2206_P4 IDENTIFIED BY "PRY2206.practica_4"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER PRY2206_P4  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO PRY2206_P4;
GRANT "RESOURCE" TO PRY2206_P4;
ALTER USER PRY2206_P4 DEFAULT ROLE "RESOURCE";

/* ==========================================================================
   DESARROLLO CASO 1: "Procesamiento de puntos por transacciones de tarjeta"
   ========================================================================== */
-- Variables BIND
VARIABLE b_tramo1 NUMBER;
VARIABLE b_tramo2 NUMBER;
VARIABLE b_tramo3 NUMBER;

EXEC :b_tramo1 := 500000;
EXEC :b_tramo2 := 700001;
EXEC :b_tramo3 := 900001;

DECLARE
    -- VARRAY para puntos (250, 300, 550, 700)
    TYPE t_array_puntos IS VARRAY(4) OF NUMBER;
    v_puntos t_array_puntos := t_array_puntos(250, 300, 550, 700);
    
    -- Declaración MANUAL del Registro
    TYPE t_registro_cliente IS RECORD (
        v_run          NUMBER(10),
        v_dv           VARCHAR2(1),
        v_tipo_cliente NUMBER(3)
    );
    v_reg_cte t_registro_cliente;
    
    -- Variable de Cursor (REF CURSOR) sin parámetro
    TYPE t_cur_clientes IS REF CURSOR;
    v_cur_ctes t_cur_clientes;
    
    -- Cursor Explícito con parámetro
    CURSOR c_transacciones(p_run NUMBER, p_anno NUMBER) IS
        SELECT t.nro_tarjeta, 
               t.nro_transaccion, 
               t.fecha_transaccion, 
               t.monto_transaccion, 
               t.cod_tptran_tarjeta
        FROM TRANSACCION_TARJETA_CLIENTE t
        JOIN TARJETA_CLIENTE tc ON t.nro_tarjeta = tc.nro_tarjeta
        WHERE tc.numrun = p_run 
        AND EXTRACT(YEAR FROM t.fecha_transaccion) = p_anno
        ORDER BY t.fecha_transaccion ASC;
        
    v_anno_proceso NUMBER;
    v_puntos_base NUMBER;
    v_puntos_extra NUMBER;
    v_total_puntos NUMBER;
    v_monto_anual NUMBER;
    v_nom_tipo_trans VARCHAR2(100); 

BEGIN
    -- TRUNCAR Tablas (Limpieza inicial)
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_PUNTOS_TARJETA_CATB';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE RESUMEN_PUNTOS_TARJETA_CATB';

    -- Obtener año anterior dinámicamente (no se usan fechas fijas)
    v_anno_proceso := EXTRACT(YEAR FROM SYSDATE) - 1;
    
    -- Abrir cursor seleccionando SOLO las columnas que se necesitan
    OPEN v_cur_ctes FOR SELECT numrun, dvrun, cod_tipo_cliente FROM CLIENTE;
    
    LOOP
        FETCH v_cur_ctes INTO v_reg_cte;
        EXIT WHEN v_cur_ctes%NOTFOUND;
        
        -- Calcular monto anual total del cliente
        SELECT NVL(SUM(t.monto_transaccion), 0) INTO v_monto_anual
        FROM TRANSACCION_TARJETA_CLIENTE t
        JOIN TARJETA_CLIENTE tc ON t.nro_tarjeta = tc.nro_tarjeta
        WHERE tc.numrun = v_reg_cte.v_run 
        AND EXTRACT(YEAR FROM t.fecha_transaccion) = v_anno_proceso;
        
        -- Recorrer transacciones del cliente
        FOR reg_trans IN c_transacciones(v_reg_cte.v_run, v_anno_proceso) LOOP
            
            -- Puntos base
            v_puntos_base := FLOOR(reg_trans.monto_transaccion / 100000) * v_puntos(1);
            
            v_puntos_extra := 0;
            
            -- Lógica Puntos Extras (Dueña de Casa=3, Pensionado=4)
            IF v_reg_cte.v_tipo_cliente IN (3, 4) THEN
                IF v_monto_anual >= :b_tramo1 AND v_monto_anual < :b_tramo2 THEN
                    v_puntos_extra := FLOOR(reg_trans.monto_transaccion / 100000) * v_puntos(2);
                ELSIF v_monto_anual >= :b_tramo2 AND v_monto_anual < :b_tramo3 THEN
                    v_puntos_extra := FLOOR(reg_trans.monto_transaccion / 100000) * v_puntos(3);
                ELSIF v_monto_anual >= :b_tramo3 THEN
                    v_puntos_extra := FLOOR(reg_trans.monto_transaccion / 100000) * v_puntos(4);
                END IF;
            END IF;
            
            v_total_puntos := v_puntos_base + v_puntos_extra;
            
            -- Obtener nombre del tipo de transacción
            SELECT nombre_tptran_tarjeta INTO v_nom_tipo_trans 
            FROM TIPO_TRANSACCION_TARJETA 
            WHERE cod_tptran_tarjeta = reg_trans.cod_tptran_tarjeta;
            
            -- Insertar en Detalle de puntos de tarjeta CATB
            INSERT INTO DETALLE_PUNTOS_TARJETA_CATB (
                numrun, dvrun, nro_tarjeta, nro_transaccion, fecha_transaccion, 
                tipo_transaccion, monto_transaccion, puntos_allthebest
            ) VALUES (
                v_reg_cte.v_run, 
                v_reg_cte.v_dv, 
                reg_trans.nro_tarjeta, 
                reg_trans.nro_transaccion, 
                reg_trans.fecha_transaccion,
                v_nom_tipo_trans, 
                reg_trans.monto_transaccion, 
                v_total_puntos
            );
        END LOOP;
    END LOOP;
    CLOSE v_cur_ctes;
    
    -- Insertar Resumen de puntos de Tarjeta CATB
    INSERT INTO RESUMEN_PUNTOS_TARJETA_CATB (
        mes_anno, monto_total_compras, total_puntos_compras, 
        monto_total_avances, total_puntos_avances, 
        monto_total_savances, total_puntos_savances
    )
    SELECT 
        TO_CHAR(fecha_transaccion, 'MMYYYY'),
        SUM(CASE WHEN tipo_transaccion LIKE 'Compra%' THEN monto_transaccion ELSE 0 END),
        SUM(CASE WHEN tipo_transaccion LIKE 'Compra%' THEN puntos_allthebest ELSE 0 END),
        SUM(CASE WHEN tipo_transaccion LIKE 'Avance%' THEN monto_transaccion ELSE 0 END),
        SUM(CASE WHEN tipo_transaccion LIKE 'Avance%' THEN puntos_allthebest ELSE 0 END),
        SUM(CASE WHEN tipo_transaccion LIKE 'Súper%' THEN monto_transaccion ELSE 0 END),
        SUM(CASE WHEN tipo_transaccion LIKE 'Súper%' THEN puntos_allthebest ELSE 0 END)
    FROM DETALLE_PUNTOS_TARJETA_CATB
    GROUP BY TO_CHAR(fecha_transaccion, 'MMYYYY')
    ORDER BY 1 ASC;

    COMMIT;
END;
/

-- Verificación de Tabla DETALLE (mostrando transacciones y puntos calculados)
SELECT numrun, 
       fecha_transaccion, 
       tipo_transaccion, 
       monto_transaccion, 
       puntos_allthebest 
FROM DETALLE_PUNTOS_TARJETA_CATB
ORDER BY fecha_transaccion ASC;

-- Verificación de Tabla RESUMEN (mostrando los puntos totales por mes)  
SELECT * FROM RESUMEN_PUNTOS_TARJETA_CATB
ORDER BY mes_anno ASC;

-- Verificación de la cantidad total de registros procesados
SELECT COUNT(*) as TOTAL_REGISTROS_PROCESADOS 
FROM DETALLE_PUNTOS_TARJETA_CATB;
/* ==========================================================================
   DESARROLLO CASO 2: "Cálculo de aportes por avances en efectivo (SBIF)"
   ========================================================================== */
   
  DECLARE
   v_anio            NUMBER := EXTRACT(YEAR FROM SYSDATE);
   v_aporte          NUMBER;
   v_monto_total     NUMBER;
   v_aporte_total    NUMBER;

   CURSOR c_detalle IS
      SELECT
         c.numrun,
         c.dvrun,
         tc.nro_tarjeta,
         ttc.nro_transaccion,
         ttc.fecha_transaccion,
         ttt.nombre_tptran_tarjeta AS tipo_transaccion,
         ttc.monto_total_transaccion
      FROM transaccion_tarjeta_cliente ttc
           JOIN tarjeta_cliente tc ON tc.nro_tarjeta = ttc.nro_tarjeta
           JOIN cliente c ON c.numrun = tc.numrun
           JOIN tipo_transaccion_tarjeta ttt 
                ON ttt.cod_tptran_tarjeta = ttc.cod_tptran_tarjeta
      WHERE ttt.nombre_tptran_tarjeta IN 
           ('Avance en Efectivo','Súper Avance en Efectivo')
        AND EXTRACT(YEAR FROM ttc.fecha_transaccion) = v_anio
      ORDER BY ttc.fecha_transaccion, c.numrun;

   CURSOR c_resumen (p_mes VARCHAR2, p_tipo VARCHAR2) IS
      SELECT
         SUM(monto_transaccion),
         SUM(aporte_sbif)
      FROM detalle_aporte_sbif
      WHERE TO_CHAR(fecha_transaccion,'MMYYYY') = p_mes
        AND tipo_transaccion = p_tipo;

BEGIN
   EXECUTE IMMEDIATE 'TRUNCATE TABLE detalle_aporte_sbif';
   EXECUTE IMMEDIATE 'TRUNCATE TABLE resumen_aporte_sbif';

   -- DETALLE
   FOR r IN c_detalle LOOP
      SELECT porc_aporte_sbif
      INTO v_aporte
      FROM tramo_aporte_sbif
      WHERE r.monto_total_transaccion 
            BETWEEN tramo_inf_av_sav AND tramo_sup_av_sav;

      v_aporte := r.monto_total_transaccion * (v_aporte / 100);

      INSERT INTO detalle_aporte_sbif (
         numrun,
         dvrun,
         nro_tarjeta,
         nro_transaccion,
         fecha_transaccion,
         tipo_transaccion,
         monto_transaccion,
         aporte_sbif
      ) VALUES (
         r.numrun,
         r.dvrun,
         r.nro_tarjeta,
         r.nro_transaccion,
         r.fecha_transaccion,
         r.tipo_transaccion,
         r.monto_total_transaccion,
         v_aporte
      );
   END LOOP;

   -- RESUMEN
   FOR x IN (
      SELECT DISTINCT
             TO_CHAR(fecha_transaccion,'MMYYYY') AS mes,
             tipo_transaccion
      FROM detalle_aporte_sbif
   ) LOOP
      OPEN c_resumen(x.mes, x.tipo_transaccion);
      FETCH c_resumen INTO v_monto_total, v_aporte_total;
      CLOSE c_resumen;

      INSERT INTO resumen_aporte_sbif (
         mes_anno,
         tipo_transaccion,
         monto_total_transacciones,
         aporte_total_abif
      ) VALUES (
         x.mes,
         x.tipo_transaccion,
         v_monto_total,
         v_aporte_total
      );
   END LOOP;

   COMMIT;
END;
/

SELECT
   numrun,
   dvrun,
   nro_tarjeta,
   nro_transaccion,
   TO_CHAR(fecha_transaccion,'DD/MM/YYYY') AS fecha_transaccion,
   tipo_transaccion,
   monto_transaccion,
   aporte_sbif
FROM detalle_aporte_sbif
ORDER BY fecha_transaccion, numrun;

SELECT
   mes_anno,
   tipo_transaccion,
   monto_total_transacciones,
   aporte_total_abif
FROM resumen_aporte_sbif
ORDER BY mes_anno, tipo_transaccion;


