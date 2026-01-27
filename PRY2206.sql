-- ----------------------------------------------------------------------------- 
-- SEMANA 3 - PRY2206_SEMANA3
-- Base de datos: PRACT3_PRY2206 (Oracle Cloud) 
-- Estudiante: René Alfaro & Verioska Ramírez
-- -----------------------------------------------------------------------------

-- Proyecto "proceso de pagos y derivación - Clínica Ketekura"

-- =============================================================================
-- BLOQUE ADMIN (ADMIN_PRY2206_P3): Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER PRY2206_P1 IDENTIFIED BY "PRY2206.practica_1"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER PRY2206_P1  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO PRY2206_P1;
GRANT "RESOURCE" TO PRY2206_P1;
ALTER USER PRY2206_P1 DEFAULT ROLE "RESOURCE";

/* ==========================================================================
   DESARROLLO CASO 1: "Cálculo de Multas a Pacientes Morosos"
   ========================================================================== */
   SET SERVEROUTPUT ON;

DECLARE
    -- -------------------------------------------------------------------------
    -- 1. DECLARACIÓN DE TIPOS Y VARIABLES
    -- -------------------------------------------------------------------------
    
    -- VARRAY para las multas (Requerimiento explícito)
    TYPE t_arr_multas IS VARRAY(7) OF NUMBER;
    v_multas t_arr_multas := t_arr_multas(1200, 1300, 1700, 1900, 1100, 2000, 2300);

    -- REGISTRO PL/SQL para el cursor (Requerimiento explícito)
    TYPE r_info_atencion IS RECORD (
        pac_run           PACIENTE.pac_run%TYPE,
        dv_run            PACIENTE.dv_run%TYPE,
        nombre_completo   VARCHAR2(100),
        ate_id            ATENCION.ate_id%TYPE,
        fecha_venc        PAGO_ATENCION.fecha_venc_pago%TYPE,
        fecha_pago        PAGO_ATENCION.fecha_pago%TYPE,
        dias_morosidad    NUMBER(5),
        nom_especialidad  ESPECIALIDAD.nombre%TYPE,
        fecha_nac         PACIENTE.fecha_nacimiento%TYPE
    );
    v_reg_datos r_info_atencion;

    -- Variables simples
    v_valor_dia_multa   NUMBER(8);
    v_monto_multa_bruto NUMBER(10);
    v_monto_multa_final NUMBER(10);
    v_edad_paciente     NUMBER(3);
    v_porc_descuento    NUMBER(3);
    v_contador_proc     NUMBER(5) := 0; -- Para la validación del COMMIT

    -- -------------------------------------------------------------------------
    -- 2. CURSOR EXPLÍCITO (Requerimiento: Año anterior dinámico)
    -- -------------------------------------------------------------------------
    CURSOR cur_morosos IS
        SELECT 
            pac.pac_run,
            pac.dv_run,
            pac.pnombre || ' ' || pac.snombre || ' ' || pac.apaterno || ' ' || pac.amaterno,
            ate.ate_id,
            pa.fecha_venc_pago,
            pa.fecha_pago,
            (pa.fecha_pago - pa.fecha_venc_pago),
            esp.nombre,
            pac.fecha_nacimiento
        FROM atencion ate
        JOIN pago_atencion pa ON ate.ate_id = pa.ate_id
        JOIN paciente pac ON ate.pac_run = pac.pac_run
        JOIN especialidad_medico esp_med ON ate.med_run = esp_med.med_run AND ate.esp_id = esp_med.esp_id
        JOIN especialidad esp ON esp_med.esp_id = esp.esp_id
        WHERE 
            -- Año dinámico: SYSDATE - 1 año
            EXTRACT(YEAR FROM pa.fecha_pago) = EXTRACT(YEAR FROM SYSDATE) - 1
            AND pa.fecha_pago > pa.fecha_venc_pago
        ORDER BY pa.fecha_venc_pago ASC, pac.apaterno ASC;

BEGIN
    -- -------------------------------------------------------------------------
    -- 3. TRUNCATE (Limpieza inicial)
    -- -------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'TRUNCATE TABLE PAGO_MOROSO';

    -- -------------------------------------------------------------------------
    -- 4. BUCLE (LOOP) PARA PROCESAR
    -- -------------------------------------------------------------------------
    OPEN cur_morosos;
    LOOP
        FETCH cur_morosos INTO v_reg_datos;
        EXIT WHEN cur_morosos%NOTFOUND;

        -- A. Asignar valor multa según especialidad (Estructura CASE)
        CASE v_reg_datos.nom_especialidad
            WHEN 'Cirugía General' THEN v_valor_dia_multa := v_multas(1);
            WHEN 'Dermatología'    THEN v_valor_dia_multa := v_multas(1);
            WHEN 'Ortopedia y Traumatología' THEN v_valor_dia_multa := v_multas(2);
            WHEN 'Inmunología'     THEN v_valor_dia_multa := v_multas(3);
            WHEN 'Otorrinolaringología' THEN v_valor_dia_multa := v_multas(3);
            WHEN 'Fisiatría'       THEN v_valor_dia_multa := v_multas(4);
            WHEN 'Medicina Interna' THEN v_valor_dia_multa := v_multas(4);
            WHEN 'Medicina General' THEN v_valor_dia_multa := v_multas(5);
            WHEN 'Psiquiatría Adultos' THEN v_valor_dia_multa := v_multas(6);
            ELSE v_valor_dia_multa := v_multas(7); 
        END CASE;

        -- B. Calcular monto bruto
        v_monto_multa_bruto := v_reg_datos.dias_morosidad * v_valor_dia_multa;

        -- C. Lógica de Descuento (SIN EXCEPTION)
        v_edad_paciente := TRUNC(MONTHS_BETWEEN(SYSDATE, v_reg_datos.fecha_nac)/12);
        
        -- Usamos funciones de grupo para evitar error si no encuentra datos
        SELECT NVL(MAX(porcentaje_descto), 0)
        INTO v_porc_descuento
        FROM PORC_DESCTO_3RA_EDAD
        WHERE v_edad_paciente BETWEEN anno_ini AND anno_ter;

        -- D. Aplicar descuento (Estructura IF)
        IF v_porc_descuento > 0 THEN
            v_monto_multa_final := v_monto_multa_bruto - (v_monto_multa_bruto * v_porc_descuento / 100);
        ELSE
            v_monto_multa_final := v_monto_multa_bruto;
        END IF;

    -- -------------------------------------------------------------------------
    -- 5. INSERTAR EN TABLA DE DESTINO
    -- -------------------------------------------------------------------------
        INSERT INTO PAGO_MOROSO (
            pac_run, pac_dv_run, pac_nombre, ate_id, 
            fecha_venc_pago, fecha_pago, dias_morosidad, 
            especialidad_atencion, monto_multa
        ) VALUES (
            v_reg_datos.pac_run,
            v_reg_datos.dv_run,
            v_reg_datos.nombre_completo,
            v_reg_datos.ate_id,
            v_reg_datos.fecha_venc,
            v_reg_datos.fecha_pago,
            v_reg_datos.dias_morosidad,
            v_reg_datos.nom_especialidad,
            ROUND(v_monto_multa_final)
        );
        
        -- Sumar al contador para validar éxito
        v_contador_proc := v_contador_proc + 1;

    END LOOP;
    CLOSE cur_morosos;

    -- -------------------------------------------------------------------------
    -- 6. VALIDACIÓN LÓGICA ANTES DEL COMMIT (Requerimiento Clave)
    -- -------------------------------------------------------------------------
    
    IF v_contador_proc > 0 THEN
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Proceso finalizado. Filas procesadas: ' || v_contador_proc);
    ELSE
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('No se encontraron datos para procesar. Se revirtieron cambios.');
    END IF;

END;
/
 
    -- -------------------------------------------------------------------------
    -- 7. RESULTADO TABLA
    -- -------------------------------------------------------------------------
   SELECT * FROM PAGO_MOROSO;
   
   /* ==========================================================================
      DESARROLLO CASO 2: "Proceso de Destinación de Médicos a Servicios Comunitarios" 
      ========================================================================== */

DECLARE
    v_anno NUMBER := EXTRACT(YEAR FROM SYSDATE) - 1;

    TYPE t_destino IS VARRAY(3) OF VARCHAR2(60);
    v_destino t_destino := t_destino(
        'Servicio de Atención Primaria de Urgencia (SAPU)',
        'Hospitales del área de la Salud Pública',
        'Centros de Salud Familiar (CESFAM)'
    );

    TYPE r_medico IS RECORD(
        unidad              VARCHAR2(50),
        run_medico          VARCHAR2(15),
        nombre_medico       VARCHAR2(50),
        correo              VARCHAR2(25),
        total_atenciones    NUMBER,
        destinacion         VARCHAR2(60)
    );

    v_reg r_medico;
    v_unidad_norm VARCHAR2(50); 

    CURSOR c_medicos IS
        SELECT  u.nombre AS unidad,
                m.med_run,
                m.dv_run,
                m.pnombre,
                m.snombre,
                m.apaterno,
                COUNT(a.ate_id) AS total_atenciones
        FROM medico m
        JOIN unidad u ON u.uni_id = m.uni_id
        LEFT JOIN atencion a
               ON a.med_run = m.med_run
              AND EXTRACT(YEAR FROM a.fecha_atencion) = v_anno
        GROUP BY u.nombre, m.med_run, m.dv_run,
                 m.pnombre, m.snombre, m.apaterno
        ORDER BY u.nombre, m.apaterno;

BEGIN
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE MEDICO_SERVICIO_COMUNIDAD CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    EXECUTE IMMEDIATE '
        CREATE TABLE MEDICO_SERVICIO_COMUNIDAD
        (
            id_med_scomun NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            unidad VARCHAR2(50),
            run_medico VARCHAR2(15),
            nombre_medico VARCHAR2(50),
            correo_institucional VARCHAR2(25),
            total_aten_medicas NUMBER(2),
            destinacion VARCHAR2(60)
        )';

    FOR r IN c_medicos LOOP

        v_reg.run_medico := r.med_run || '-' || r.dv_run;
        v_reg.nombre_medico := r.pnombre || ' ' || r.snombre || ' ' || r.apaterno;
        v_reg.unidad := r.unidad;
        v_reg.total_atenciones := NVL(r.total_atenciones, 0);

        v_reg.correo :=
            SUBSTR(r.unidad,1,2) ||
            SUBSTR(r.apaterno, LENGTH(r.apaterno)-2, 2) ||
            SUBSTR(r.apaterno, LENGTH(r.apaterno)-1, 3) ||
            '@medicocktk.cl';


        v_unidad_norm :=
            TRIM(
                TRANSLATE(
                    UPPER(r.unidad),
                    'ÁÉÍÓÚ',
                    'AEIOU'
                )
            );


        IF v_unidad_norm IN ('ATENCION ADULTO','ATENCION AMBULATORIA') THEN
            v_reg.destinacion := v_destino(1);

        ELSIF v_unidad_norm = 'ATENCION URGENCIA' THEN
            IF v_reg.total_atenciones <= 3 THEN
                v_reg.destinacion := v_destino(1);
            ELSE
                v_reg.destinacion := v_destino(2);
            END IF;

        ELSIF v_unidad_norm IN ('CARDIOLOGIA','ONCOLOGICA','PACIENTE CRITICO') THEN
            v_reg.destinacion := v_destino(2);

        ELSIF v_unidad_norm IN ('CIRUGIA','CIRUGIA PLASTICA') THEN
            IF v_reg.total_atenciones <= 3 THEN
                v_reg.destinacion := v_destino(1);
            ELSE
                v_reg.destinacion := v_destino(2);
            END IF;

        ELSIF v_unidad_norm = 'PSIQUIATRIA Y SALUD MENTAL' THEN
            v_reg.destinacion := v_destino(3);

        ELSIF v_unidad_norm = 'TRAUMATOLOGIA ADULTO' THEN
            IF v_reg.total_atenciones <= 3 THEN
                v_reg.destinacion := v_destino(1);
            ELSE
                v_reg.destinacion := v_destino(2);
            END IF;

        ELSE
            v_reg.destinacion := v_destino(3);
        END IF;

        INSERT INTO MEDICO_SERVICIO_COMUNIDAD
        (unidad, run_medico, nombre_medico, correo_institucional,
         total_aten_medicas, destinacion)
        VALUES
        (v_reg.unidad, v_reg.run_medico, v_reg.nombre_medico,
         v_reg.correo, v_reg.total_atenciones, v_reg.destinacion);

    END LOOP;

    COMMIT;
END;
/

SELECT
    unidad,
    run_medico,
    nombre_medico,
    correo_institucional,
    total_aten_medicas,
    destinacion
FROM MEDICO_SERVICIO_COMUNIDAD
ORDER BY unidad;
