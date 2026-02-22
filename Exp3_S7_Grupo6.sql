-- ----------------------------------------------------------------------------- 
-- SEMANA 7 - PRY2206_SEMANA7
-- Base de datos: PRACT7_PRY2206 (Oracle Cloud) 
-- Estudiante: René Alfaro & Verioska Ramírez
-- -----------------------------------------------------------------------------

-- Proyecto "Sistema de Gestión de Pagos de Atenciones Médicas y Multas - Clínica MAXSALUD"

-- =============================================================================
-- BLOQUE ADMIN (ADMIN_PRY2206): Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER PRY2206_P7 IDENTIFIED BY "PRY2206.practica_7"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER PRY2206_P7  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO PRY2206_P7;
GRANT "RESOURCE" TO PRY2206_P7;
ALTER USER PRY2206_P7 DEFAULT ROLE "RESOURCE";

/* **************************************************************************
   DESARROLLO DEL CASO:
   ************************************************************************** */

-- =============================================================================
-- 1. PACKAGE: Especificación y Cuerpo
-- =============================================================================

-- ESPECIFICACIÓN
CREATE OR REPLACE PACKAGE PKG_PAGOS_CLINICA AS
    -- Variables públicas solicitadas
    v_multa NUMBER;
    v_dscto_3ra_edad NUMBER;

    -- Función pública solicitada
    FUNCTION fn_descuento_3ra_edad(p_edad NUMBER) RETURN NUMBER;
END PKG_PAGOS_CLINICA;
/

-- CUERPO
CREATE OR REPLACE PACKAGE BODY PKG_PAGOS_CLINICA AS
    FUNCTION fn_descuento_3ra_edad(p_edad NUMBER) RETURN NUMBER IS
        v_porcentaje NUMBER := 0;
    BEGIN
        -- Buscamos el porcentaje usando los nombres correctos de las columnas
        SELECT PORCENTAJE_DESCTO / 100 
        INTO v_porcentaje
        FROM porc_descto_3ra_edad
        WHERE p_edad BETWEEN ANNO_INI AND ANNO_TER;
        
        RETURN v_porcentaje;
        
    EXCEPTION
        -- Si el paciente no clasifica, retorna 0
        WHEN NO_DATA_FOUND THEN
            RETURN 0; 
    END fn_descuento_3ra_edad;
END PKG_PAGOS_CLINICA;
/

-- =======================================================
-- 2. FUNCIÓN ALMACENADA: Obtener Especialidad
-- =======================================================
CREATE OR REPLACE FUNCTION fn_obtener_especialidad (p_esp_id NUMBER) 
RETURN VARCHAR2 IS
    v_nombre_esp VARCHAR2(25);
BEGIN
    -- Buscamos el nombre de la especialidad según su ID exacto
    SELECT nombre 
    INTO v_nombre_esp
    FROM especialidad
    WHERE esp_id = p_esp_id;
    
    RETURN v_nombre_esp;
    
EXCEPTION
    -- Por si llegara a venir un ID que no existe
    WHEN NO_DATA_FOUND THEN
        RETURN 'No Registra';
END fn_obtener_especialidad;
/

-- =======================================================
-- 3. PROCEDIMIENTO ALMACENADO PRINCIPAL
-- =======================================================
CREATE OR REPLACE PROCEDURE prc_procesa_morosos IS
    -- Requisito: Declaración del VARRAY para los valores de las multas
    TYPE t_multas IS VARRAY(9) OF NUMBER;
    
    -- Inicializamos el VARRAY con los valores según la tabla de multas de MAXSALUD
    -- Valores: 1.Traumatologia, 2.Gastro, 3.Neuro, 4.Geriatria, 5.Oftalmo, 6.Pediatria, 7.Med. General, 8.Gineco, 9.Dermato
    v_arr_multas t_multas := t_multas(1300, 2000, 1700, 1100, 1900, 1700, 1200, 2000, 2300);
    
    v_nombre_esp VARCHAR2(50);
    v_multa_diaria NUMBER;
    v_dias_mora NUMBER;
    v_edad NUMBER;
    v_observacion VARCHAR2(100);
    
    -- Cursor para obtener los pagos atrasados del AÑO ANTERIOR
    -- y ordenados por fecha y apellido paterno, como pide el caso
    CURSOR c_morosos IS
        SELECT p.pac_run, p.dv_run, 
               p.pnombre || ' ' || p.apaterno || ' ' || p.amaterno AS pac_nombre,
               p.fecha_nacimiento,
               a.ate_id, a.fecha_atencion, a.costo,
               pa.fecha_venc_pago, pa.fecha_pago,
               m.esp_id
        FROM paciente p
        JOIN atencion a ON p.pac_run = a.pac_run
        JOIN pago_atencion pa ON a.ate_id = pa.ate_id
        JOIN medico m ON a.med_run = m.med_run
        WHERE pa.fecha_pago > pa.fecha_venc_pago
          AND EXTRACT(YEAR FROM pa.fecha_venc_pago) = EXTRACT(YEAR FROM SYSDATE) - 1
        ORDER BY pa.fecha_venc_pago ASC, p.apaterno ASC;
        
BEGIN
    -- Requisito: Truncar la tabla en tiempo de ejecución
    EXECUTE IMMEDIATE 'TRUNCATE TABLE PAGO_MOROSO';
    
    -- Recorremos a los pacientes
    FOR reg IN c_morosos LOOP
        -- Calculamos días de morosidad
        v_dias_mora := reg.fecha_pago - reg.fecha_venc_pago;
        
        -- USO DE FUNCIÓN INDEPENDIENTE: Obtenemos especialidad
        v_nombre_esp := fn_obtener_especialidad(reg.esp_id);
        
        -- Requisito: Control Condicional y uso del VARRAY
        CASE v_nombre_esp
            WHEN 'Traumatologia' THEN v_multa_diaria := v_arr_multas(1);
            WHEN 'Gastroenterologia' THEN v_multa_diaria := v_arr_multas(2);
            WHEN 'Neurologia' THEN v_multa_diaria := v_arr_multas(3);
            WHEN 'Geriatria' THEN v_multa_diaria := v_arr_multas(4);
            WHEN 'Oftalmologia' THEN v_multa_diaria := v_arr_multas(5);
            WHEN 'Pediatria' THEN v_multa_diaria := v_arr_multas(6);
            WHEN 'Medicina General' THEN v_multa_diaria := v_arr_multas(7);
            WHEN 'Ginecologia' THEN v_multa_diaria := v_arr_multas(8);
            WHEN 'Dermatologia' THEN v_multa_diaria := v_arr_multas(9);
            ELSE v_multa_diaria := 0;
        END CASE;
        
        -- USO DEL PACKAGE (Variables): Calcular multa base
        PKG_PAGOS_CLINICA.v_multa := v_multa_diaria * v_dias_mora;
        
        -- Calculamos la edad a la fecha de la atención
        v_edad := TRUNC(MONTHS_BETWEEN(reg.fecha_atencion, reg.fecha_nacimiento) / 12);
        
        -- USO DEL PACKAGE (Función y Variables): Obtener descuento
        PKG_PAGOS_CLINICA.v_dscto_3ra_edad := PKG_PAGOS_CLINICA.fn_descuento_3ra_edad(v_edad);
        
        -- Aplicamos el descuento si existe
        IF PKG_PAGOS_CLINICA.v_dscto_3ra_edad > 0 THEN
            PKG_PAGOS_CLINICA.v_multa := PKG_PAGOS_CLINICA.v_multa - (PKG_PAGOS_CLINICA.v_multa * PKG_PAGOS_CLINICA.v_dscto_3ra_edad);
            v_observacion := 'Paciente tenia ' || v_edad || ' a la fecha de atención. Se aplicó descuento paciente mayor a 70 años';
        ELSE
            v_observacion := NULL;
        END IF;
        
        -- Insertamos el registro final
        INSERT INTO PAGO_MOROSO (
            pac_run, pac_dv_run, pac_nombre, ate_id, 
            fecha_venc_pago, fecha_pago, dias_morosidad, 
            especialidad_atencion, costo_atencion, 
            monto_multa, observacion
        ) VALUES (
            reg.pac_run, reg.dv_run, reg.pac_nombre, reg.ate_id,
            reg.fecha_venc_pago, reg.fecha_pago, v_dias_mora,
            v_nombre_esp, reg.costo, 
            PKG_PAGOS_CLINICA.v_multa, v_observacion
        );
        
    END LOOP;
    
    COMMIT;
END prc_procesa_morosos;
/

-- =======================================================
-- 4. TRIGGER: Asegurar integridad de datos
-- =======================================================
CREATE OR REPLACE TRIGGER trg_valida_multa
BEFORE INSERT OR UPDATE ON PAGO_MOROSO
FOR EACH ROW
BEGIN
    -- Validamos que el monto de la multa jamás sea un número negativo
    IF :NEW.monto_multa < 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error de Integridad: El monto de la multa no puede ser negativo.');
    END IF;
END;
/

-- Verificación y tabla de PAGO_MOROSO
EXEC prc_procesa_morosos;

SELECT * FROM PAGO_MOROSO;

