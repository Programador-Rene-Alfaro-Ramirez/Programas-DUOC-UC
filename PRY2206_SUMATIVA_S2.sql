-- ----------------------------------------------------------------------------- 
-- SEMANA 5 - PRY2206_SUMATIVA_2
-- Base de datos: PRY2206S5_S2 (Oracle Cloud) 
-- Estudiante: René Alfaro 
-- -----------------------------------------------------------------------------

-- Proyecto "Transacciones de Avances y Super Avances - Tienda Retail ALL THE BEST"

-- =============================================================================
-- BLOQUE ADMIN: Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER SUMATIVA_2206_P2 IDENTIFIED BY "PRY2206.sumativa_2"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER SUMATIVA_2206_P2  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO SUMATIVA_2206_P2;
GRANT "RESOURCE" TO SUMATIVA_2206_P2;
ALTER USER SUMATIVA_2206_P2 DEFAULT ROLE "RESOURCE";

/* ==========================================================================
   DESARROLLO DEL CASO
   ========================================================================== */

-- 1. VARIABLE BIND 
VARIABLE b_periodo NUMBER;
EXEC :b_periodo := EXTRACT(YEAR FROM SYSDATE);

DECLARE
    -- Variables de Entorno
    v_anno_proceso    NUMBER := :b_periodo; 
    v_anno_actual     NUMBER := EXTRACT(YEAR FROM SYSDATE);
    v_diferencia      NUMBER;
    
    -- Variables de Control y Contadores
    v_total_leidos    NUMBER := 0;
    v_total_ok        NUMBER := 0;
    v_errores         NUMBER := 0;
    v_fecha_proy      DATE;
    
    -- Variables de Cálculo
    v_aporte          NUMBER(10);
    v_pct             NUMBER(3);

    -- VARIABLES ESCALARES 
    v_run           CLIENTE.numrun%TYPE;
    v_dv            CLIENTE.dvrun%TYPE;
    v_nro_tarjeta   TARJETA_CLIENTE.nro_tarjeta%TYPE;
    v_nro_trx       TRANSACCION_TARJETA_CLIENTE.nro_transaccion%TYPE;
    v_fec_trx       TRANSACCION_TARJETA_CLIENTE.fecha_transaccion%TYPE;
    v_nom_tipo      TIPO_TRANSACCION_TARJETA.NOMBRE_TPTRAN_TARJETA%TYPE;
    v_monto         TRANSACCION_TARJETA_CLIENTE.monto_transaccion%TYPE;

    -- CURSOR EXPLÍCITO CON PARÁMETRO 
    CURSOR cur_datos(p_agno NUMBER) IS
        SELECT c.numrun, 
               c.dvrun, 
               tc.nro_tarjeta, 
               tr.nro_transaccion, 
               tr.fecha_transaccion, 
               tt.NOMBRE_TPTRAN_TARJETA, 
               tr.monto_transaccion
        FROM transaccion_tarjeta_cliente tr
        JOIN tarjeta_cliente tc ON tr.nro_tarjeta = tc.nro_tarjeta
        JOIN cliente c ON tc.numrun = c.numrun
        JOIN tipo_transaccion_tarjeta tt ON tr.cod_tptran_tarjeta = tt.cod_tptran_tarjeta
        WHERE EXTRACT(YEAR FROM tr.fecha_transaccion) = p_agno
        ORDER BY tr.fecha_transaccion ASC;

    -- VARIABLES PARA REGLAS
    v_min NUMBER; v_max NUMBER; v_pct_regla NUMBER;

    CURSOR cur_reglas IS
        SELECT tramo_inf_av_sav, tramo_sup_av_sav, porc_aporte_sbif
        FROM tramo_aporte_sbif;

    -- EXCEPCIONES 
    e_sin_datos EXCEPTION; 
    e_error_valor EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_error_valor, -01438);

BEGIN
    -- TRUNCADO DE TABLAS 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_APORTE_SBIF';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE RESUMEN_APORTE_SBIF';

    v_diferencia := v_anno_actual - v_anno_proceso;

    DBMS_OUTPUT.PUT_LINE('--- INICIO PROCESO (PAUTA ESTRICTA) ---');
    DBMS_OUTPUT.PUT_LINE('Procesando periodo: ' || v_anno_proceso);

    OPEN cur_datos(v_anno_proceso); 
    LOOP
        FETCH cur_datos INTO v_run, v_dv, v_nro_tarjeta, v_nro_trx, v_fec_trx, v_nom_tipo, v_monto;
        EXIT WHEN cur_datos%NOTFOUND;
        
        -- LÓGICA DE NEGOCIO
        IF (v_nom_tipo LIKE '%Avance%' OR v_nom_tipo LIKE '%Súper%') THEN
            
            v_total_leidos := v_total_leidos + 1;
            v_aporte := 0;
            
            -- Proyección 
            v_fecha_proy := ADD_MONTHS(v_fec_trx, (v_diferencia * 12));

            -- CÁLCULO
            OPEN cur_reglas;
            LOOP
                FETCH cur_reglas INTO v_min, v_max, v_pct_regla;
                EXIT WHEN cur_reglas%NOTFOUND;
                
                IF v_monto BETWEEN v_min AND v_max THEN
                    v_pct := v_pct_regla;
                    v_aporte := ROUND(v_monto * v_pct / 100);
                    EXIT;
                END IF;
            END LOOP;
            CLOSE cur_reglas;

            -- INSERCIÓN Y MANEJO DE ERRORES (Bloque anidado)
            BEGIN
                INSERT INTO DETALLE_APORTE_SBIF (
                    numrun, dvrun, nro_tarjeta, nro_transaccion, 
                    fecha_transaccion, tipo_transaccion, 
                    monto_transaccion, aporte_sbif
                ) VALUES (
                    v_run, v_dv, v_nro_tarjeta, v_nro_trx,
                    v_fecha_proy, v_nom_tipo, v_monto, v_aporte
                );
                
                v_total_ok := v_total_ok + 1;
                
            EXCEPTION
                -- Uso de Excepciones solicitadas
                WHEN DUP_VAL_ON_INDEX THEN
                    v_errores := v_errores + 1;
                    DBMS_OUTPUT.PUT_LINE('Error Duplicado ID: ' || v_nro_trx);
                
                WHEN e_error_valor THEN
                    v_errores := v_errores + 1;
                    DBMS_OUTPUT.PUT_LINE('Error de Valor Numérico ID: ' || v_nro_trx);
                    
                WHEN OTHERS THEN
                    v_errores := v_errores + 1;
                    DBMS_OUTPUT.PUT_LINE('Error General ID ' || v_nro_trx || ': ' || SQLERRM);
            END;

        END IF;

    END LOOP;
    CLOSE cur_datos;

    DBMS_OUTPUT.PUT_LINE('------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('Resumen Final:');
    DBMS_OUTPUT.PUT_LINE('Leidos (Filtrados): ' || v_total_leidos);
    DBMS_OUTPUT.PUT_LINE('Insertados OK: ' || v_total_ok);

    -- VALIDACIÓN FINAL Y RESUMEN (Confirmación de transacción)
    IF v_total_leidos > 0 AND v_total_ok > 0 THEN
        INSERT INTO RESUMEN_APORTE_SBIF (mes_anno, tipo_transaccion, monto_total_transacciones, aporte_total_abif)
        SELECT TO_CHAR(fecha_transaccion, 'MMYYYY'), 
               tipo_transaccion, 
               SUM(monto_transaccion), 
               SUM(aporte_sbif)
        FROM DETALLE_APORTE_SBIF
        GROUP BY TO_CHAR(fecha_transaccion, 'MMYYYY'), tipo_transaccion
        ORDER BY 1, 2;
        
        COMMIT; -- se confirma la transacción
        DBMS_OUTPUT.PUT_LINE('PROCESO EXITOSO! Tablas llenas y confirmadas.');
    ELSE
        ROLLBACK;
        RAISE e_sin_datos; -- Usamos excepción de usuario
    END IF;

EXCEPTION
    WHEN e_sin_datos THEN
        DBMS_OUTPUT.PUT_LINE('ALERTA: No se encontraron datos válidos para el proceso.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR CRITICO DEL SISTEMA: ' || SQLERRM);
END;
/

-- Visualizacion de la tabla "DETALLE_APORTE_SBIF"
SELECT * FROM DETALLE_APORTE_SBIF;

--  Visualizacion RESUMEN FINAL
SELECT * FROM RESUMEN_APORTE_SBIF;
