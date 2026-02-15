-- ----------------------------------------------------------------------------- 
-- SEMANA 6 - PRY2206_SEMANA6
-- Base de datos: PRACT6_PRY2206 (Oracle Cloud) 
-- Estudiante: René Alfaro & Verioska Ramírez
-- -----------------------------------------------------------------------------

-- Proyecto "Administración Integral de Edificios AINTEGRAEDI "

-- =============================================================================
-- BLOQUE ADMIN (ADMIN_PRY2206): Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER PRY2206_P6 IDENTIFIED BY "PRY2206.practica_6"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER PRY2206_P6  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO PRY2206_P6;
GRANT "RESOURCE" TO PRY2206_P6;
ALTER USER PRY2206_P6 DEFAULT ROLE "RESOURCE";

/* ==========================================================================
   DESARROLLO CASO 1: "Detalle de deudores con fecha de corte de servicios"
   ========================================================================== */
SET SERVEROUTPUT ON SIZE UNLIMITED;
CREATE OR REPLACE PROCEDURE INSERTAR_DEUDOR_GASTO_COMUN(
    p_anno_mes_pcgc         IN NUMBER,
    p_id_edif               IN NUMBER,
    p_nombre_edif           IN VARCHAR2,
    p_run_administrador     IN VARCHAR2,
    p_nombre_administrador  IN VARCHAR2,
    p_nro_depto             IN NUMBER,
    p_run_responsable       IN VARCHAR2,
    p_nombre_responsable    IN VARCHAR2,
    p_valor_multa           IN NUMBER,
    p_observacion           IN VARCHAR2
) AS
BEGIN
    INSERT INTO GASTO_COMUN_PAGO_CERO (
        ANNO_MES_PCGC,
        ID_EDIF,
        NOMBRE_EDIF,
        RUN_ADMINISTRADOR,
        NOMBRE_ADMNISTRADOR,
        NRO_DEPTO,
        RUN_RESPONSABLE_PAGO_GC,
        NOMBRE_RESPONSABLE_PAGO_GC,
        VALOR_MULTA_PAGO_CERO,
        OBSERVACION
    ) VALUES (
        p_anno_mes_pcgc,
        p_id_edif,
        p_nombre_edif,
        p_run_administrador,
        p_nombre_administrador,
        p_nro_depto,
        p_run_responsable,
        p_nombre_responsable,
        p_valor_multa,
        p_observacion
    );
END INSERTAR_DEUDOR_GASTO_COMUN;
/

CREATE OR REPLACE PROCEDURE GENERAR_DEUDORES_GASTO_COMUN(
    p_anno_mes_proceso      IN NUMBER,
    p_valor_uf              IN NUMBER
) AS

    v_anno_mes_anterior     NUMBER; 
    v_anno_mes_dos_meses    NUMBER; 
    v_fecha_corte           VARCHAR2(10);
    v_valor_multa_2uf       NUMBER;
    v_valor_multa_4uf       NUMBER;
    v_observacion           VARCHAR2(200);
    v_periodos_sin_pago     NUMBER;
    v_contador_procesados   NUMBER := 0;
    v_count_pago_anterior   NUMBER;
    v_count_pago_dos_meses  NUMBER;

    -- Variables del cursor
    v_id_edif               NUMBER;
    v_nombre_edif           VARCHAR2(50);
    v_run_administrador     VARCHAR2(20);
    v_nombre_administrador  VARCHAR2(200);
    v_nro_depto             NUMBER;
    v_run_responsable       VARCHAR2(20);
    v_nombre_responsable    VARCHAR2(200);

    CURSOR c_gastos_comunes IS
        SELECT DISTINCT
            gc.ID_EDIF,
            e.NOMBRE_EDIF,
            CONCAT(CONCAT(TO_CHAR(a.NUMRUN_ADM), '-'), a.DVRUN_ADM) as RUN_ADMINISTRADOR,
            TRIM(CONCAT(CONCAT(CONCAT(a.PNOMBRE_ADM, ' '), NVL(a.SNOMBRE_ADM, '')),
                   CONCAT(CONCAT(' ', a.APPATERNO_ADM), CONCAT(' ', NVL(a.APMATERNO_ADM, ''))))) as NOMBRE_ADMINISTRADOR,
            gc.NRO_DEPTO,
            CONCAT(CONCAT(TO_CHAR(rp.NUMRUN_RPGC), '-'), rp.DVRUN_RPGC) as RUN_RESPONSABLE,
            TRIM(CONCAT(CONCAT(CONCAT(rp.PNOMBRE_RPGC, ' '), NVL(rp.SNOMBRE_RPGC, '')),
                   CONCAT(CONCAT(' ', rp.APPATERNO_RPGC), CONCAT(' ', NVL(rp.APMATERNO_RPGC, ''))))) as NOMBRE_RESPONSABLE
        FROM GASTO_COMUN gc
        JOIN EDIFICIO e ON gc.ID_EDIF = e.ID_EDIF
        JOIN ADMINISTRADOR a ON e.NUMRUN_ADM = a.NUMRUN_ADM
        JOIN RESPONSABLE_PAGO_GASTO_COMUN rp ON gc.NUMRUN_RPGC = rp.NUMRUN_RPGC
        WHERE gc.ANNO_MES_PCGC IN (
            TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(p_anno_mes_proceso), 'YYYYMM'), -1), 'YYYYMM')), 
            TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(p_anno_mes_proceso), 'YYYYMM'), -2), 'YYYYMM'))
        )
        ORDER BY e.NOMBRE_EDIF, gc.NRO_DEPTO;

BEGIN
    -- Limpiar tabla de reporte
    DELETE FROM GASTO_COMUN_PAGO_CERO;
    
    -- Calcular meses dinámicamente según el parámetro de entrada
    v_anno_mes_anterior := TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(p_anno_mes_proceso), 'YYYYMM'), -1), 'YYYYMM'));
    v_anno_mes_dos_meses := TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(p_anno_mes_proceso), 'YYYYMM'), -2), 'YYYYMM'));
    
    v_valor_multa_2uf := p_valor_uf * 2;
    v_valor_multa_4uf := p_valor_uf * 4;

    OPEN c_gastos_comunes;

    LOOP
        FETCH c_gastos_comunes INTO
            v_id_edif, v_nombre_edif, v_run_administrador,
            v_nombre_administrador, v_nro_depto, v_run_responsable, v_nombre_responsable;

        EXIT WHEN c_gastos_comunes%NOTFOUND;

        v_periodos_sin_pago := 0;
        
        -- Verificar mes anterior
        SELECT COUNT(*) INTO v_count_pago_anterior
        FROM PAGO_GASTO_COMUN
        WHERE ANNO_MES_PCGC = v_anno_mes_anterior
        AND ID_EDIF = v_id_edif
        AND NRO_DEPTO = v_nro_depto;

        IF v_count_pago_anterior = 0 THEN
            v_periodos_sin_pago := v_periodos_sin_pago + 1;
        END IF;

        -- Verificar dos meses atrás
        SELECT COUNT(*) INTO v_count_pago_dos_meses
        FROM PAGO_GASTO_COMUN
        WHERE ANNO_MES_PCGC = v_anno_mes_dos_meses
        AND ID_EDIF = v_id_edif
        AND NRO_DEPTO = v_nro_depto;

        IF v_count_pago_dos_meses = 0 THEN
            v_periodos_sin_pago := v_periodos_sin_pago + 1;
        END IF;

        IF v_periodos_sin_pago > 0 THEN

            IF v_periodos_sin_pago = 1 THEN
                -- CASO 1: Multa 2 UF
                v_observacion := 'Se realizará el corte del combustible y agua';
                
                -- Insertar en tabla de reporte
                INSERTAR_DEUDOR_GASTO_COMUN(
                    p_anno_mes_proceso, v_id_edif, v_nombre_edif, v_run_administrador,
                    v_nombre_administrador, v_nro_depto, v_run_responsable,
                    v_nombre_responsable, v_valor_multa_2uf, v_observacion
                );

                -- ACTUALIZAR GASTO_COMUN (Para la Figura 2)
                UPDATE GASTO_COMUN
                SET MULTA_GC = v_valor_multa_2uf
                WHERE ANNO_MES_PCGC = p_anno_mes_proceso
                  AND ID_EDIF = v_id_edif
                  AND NRO_DEPTO = v_nro_depto;

            ELSIF v_periodos_sin_pago >= 2 THEN
                -- CASO 2: Multa 4 UF y Corte
                v_fecha_corte := TO_CHAR(LAST_DAY(TO_DATE(TO_CHAR(p_anno_mes_proceso), 'YYYYMM')) + 15, 'DD/MM/YYYY');
                v_observacion := 'Se realizará el corte del combustible y agua a contar del ' || v_fecha_corte;
                
                -- Insertar en tabla de reporte
                INSERTAR_DEUDOR_GASTO_COMUN(
                    p_anno_mes_proceso, v_id_edif, v_nombre_edif, v_run_administrador,
                    v_nombre_administrador, v_nro_depto, v_run_responsable,
                    v_nombre_responsable, v_valor_multa_4uf, v_observacion
                );

                -- ACTUALIZAR GASTO_COMUN (Para la Figura 2)
                UPDATE GASTO_COMUN
                SET MULTA_GC = v_valor_multa_4uf
                WHERE ANNO_MES_PCGC = p_anno_mes_proceso
                  AND ID_EDIF = v_id_edif
                  AND NRO_DEPTO = v_nro_depto;
            END IF;

            v_contador_procesados := v_contador_procesados + 1;

        END IF;

    END LOOP;

    CLOSE c_gastos_comunes;
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        IF c_gastos_comunes%ISOPEN THEN
            CLOSE c_gastos_comunes;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error en el proceso: ' || SQLERRM);
END GENERAR_DEUDORES_GASTO_COMUN;
/

CREATE OR REPLACE PROCEDURE MOSTRAR_RESULTADOS_DEUDORES AS
    v_count NUMBER;
    v_contador NUMBER := 0;
BEGIN
    SELECT COUNT(*) INTO v_count FROM GASTO_COMUN_PAGO_CERO;


    NULL;

END MOSTRAR_RESULTADOS_DEUDORES;
/

BEGIN
    GENERAR_DEUDORES_GASTO_COMUN(202605, 29509);
    MOSTRAR_RESULTADOS_DEUDORES;
END;
/

select * from GASTO_COMUN_PAGO_CERO


/* ==========================================================================
   DESARROLLO CASO 2: "Multas de gastos comunes"
   ========================================================================== */
   
SELECT 
    ANNO_MES_PCGC,
    ID_EDIF,
    NRO_DEPTO,
    TO_CHAR(FECHA_DESDE_GC, 'DD/MM/YYYY') AS FECHA_DESDE_GC,
    TO_CHAR(FECHA_HASTA_GC, 'DD/MM/YYYY') AS FECHA_HASTA_GC,
    MULTA_GC
FROM GASTO_COMUN
WHERE ANNO_MES_PCGC = 202605 
  AND MULTA_GC > 0
ORDER BY ID_EDIF, NRO_DEPTO;
