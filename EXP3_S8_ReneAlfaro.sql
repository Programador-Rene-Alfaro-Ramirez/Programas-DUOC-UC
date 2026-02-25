-- ----------------------------------------------------------------------------- 
-- SEMANA 8 - PRY2206_SEMANA8
-- Base de datos: PRACT8_PRY2206 (Oracle Cloud) 
-- Estudiante: René Alfaro 
-- -----------------------------------------------------------------------------

-- Proyecto "Sistema de reservación y cobranza - Hotel Última Oportunidad"

-- =============================================================================
-- BLOQUE ADMIN (ADMIN_PRY2206_S8): Configuración y Creación de Usuarios
-- =============================================================================

CREATE USER PRY2206_P7 IDENTIFIED BY "PRY2206.practica_7"
DEFAULT TABLESPACE "DATA"
TEMPORARY TABLESPACE "TEMP";
ALTER USER PRY2206_P7  QUOTA UNLIMITED ON DATA;
GRANT CREATE SESSION TO PRY2206_P7;
GRANT "RESOURCE" TO PRY2206_P7;
ALTER USER PRY2206_P7 DEFAULT ROLE "RESOURCE";

/* ==========================================================================
   DESARROLLO CASO 1: "Consumo de huéspedes y reserva"
   ========================================================================== */
-- -----------------------------------------------------------------------------
-- Creación de TRIGGER TRG_ACTUALIZA_TOTAL_CONSUMOS
-- -----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_ACTUALIZA_TOTAL_CONSUMOS
AFTER INSERT OR UPDATE OR DELETE ON CONSUMO
FOR EACH ROW
BEGIN
    -- a) Si se inserta un nuevo consumo
    IF INSERTING THEN
        UPDATE TOTAL_CONSUMOS
        SET MONTO_CONSUMOS = ROUND(MONTO_CONSUMOS + :NEW.MONTO)
        WHERE ID_HUESPED = :NEW.ID_HUESPED;
        
    -- b) Si se actualiza el monto de un consumo
    ELSIF UPDATING THEN
        -- Sumamos la diferencia: restamos el monto antiguo y sumamos el nuevo
        UPDATE TOTAL_CONSUMOS
        SET MONTO_CONSUMOS = ROUND(MONTO_CONSUMOS - :OLD.MONTO + :NEW.MONTO)
        WHERE ID_HUESPED = :NEW.ID_HUESPED;
        
    -- c) Si se elimina un consumo
    ELSIF DELETING THEN
        UPDATE TOTAL_CONSUMOS
        SET MONTO_CONSUMOS = ROUND(MONTO_CONSUMOS - :OLD.MONTO)
        WHERE ID_HUESPED = :OLD.ID_HUESPED;
    END IF;
END;
/

DECLARE
    -- Variable para guardar la próxima ID paramétricamente
    v_next_id_consumo CONSUMO.ID_CONSUMO%TYPE;
BEGIN
    -- Obtenemos la ID siguiente a la última ingresada de forma dinámica
    SELECT NVL(MAX(ID_CONSUMO), 0) + 1 
    INTO v_next_id_consumo 
    FROM CONSUMO;

    -- 1. Inserta un nuevo consumo
    INSERT INTO CONSUMO (ID_CONSUMO, ID_RESERVA, ID_HUESPED, MONTO)
    VALUES (v_next_id_consumo, 1587, 340006, 150);

    -- 2. Elimina el consumo con ID 11473 
    DELETE FROM CONSUMO
    WHERE ID_CONSUMO = 11473;

    -- 3. Actualiza a US$ 95 el monto del consumo con ID 10688
    UPDATE CONSUMO
    SET MONTO = 95
    WHERE ID_CONSUMO = 10688;

    -- Confirmamos los cambios en la base de datos
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Pruebas del bloque anónimo ejecutadas correctamente. ¡Revisa tus tablas!');
END;
/

-- verificación del "Consumo"
SELECT * FROM CONSUMO;

-- Verificación del "Valor Total Consumos"
SELECT * FROM TOTAL_CONSUMOS;

-- verificación del funcionamiento del TRIGGER
SELECT * FROM TOTAL_CONSUMOS 
WHERE ID_HUESPED IN (340006, 340004, 340008)
ORDER BY ID_HUESPED;

/* *****************************************************************************
   NOTA DE VERIFICACIÓN - CASO 1:
   Se comprueba que el trigger TRG_ACTUALIZA_TOTAL_CONSUMOS funciona 
   correctamente tras la ejecución del bloque anónimo, coincidiendo 
   exactamente con la "Figura 2" de las instrucciones:
   
   1. Huésped 340006 (INSERT): Se agregó un nuevo consumo de US$ 150. 
      Su monto total pasó de US$ 278 a US$ 428.
   2. Huésped 340004 (DELETE): Se eliminó el consumo ID 11473 (US$ 63). 
      Su monto total se rebajó de US$ 158 a US$ 95.
   3. Huésped 340008 (UPDATE): Se actualizó el consumo ID 10688 rebajándolo 
      de US$ 117 a US$ 95. Su monto total bajó de US$ 211 a US$ 189.
   ************************************************************************** */
   
/* ==========================================================================
   DESARROLLO CASO 2: "Resumen de consumos y descuentos por huésped y agencia de viajes" 
   ========================================================================== */

-- 1. Creación del PACKAGE: PKG_COBROS_HOTEL

CREATE OR REPLACE PACKAGE PKG_COBROS_HOTEL IS
    v_total_tours NUMBER;
    FUNCTION FN_MONTO_TOURS(p_id_huesped IN NUMBER) RETURN NUMBER;
END PKG_COBROS_HOTEL;
/

CREATE OR REPLACE PACKAGE BODY PKG_COBROS_HOTEL IS
    FUNCTION FN_MONTO_TOURS(p_id_huesped IN NUMBER) RETURN NUMBER IS
        v_monto NUMBER := 0;
    BEGIN
        -- Multiplicamos el valor del tour por el número de personas que lo tomaron
        SELECT NVL(SUM(t.valor_tour * ht.num_personas), 0)
        INTO v_monto
        FROM huesped_tour ht
        JOIN tour t ON ht.id_tour = t.id_tour
        WHERE ht.id_huesped = p_id_huesped;

        RETURN v_monto;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN 0;
    END FN_MONTO_TOURS;
END PKG_COBROS_HOTEL;
/


-- 2. FUNCIONES ALMACENADAS

CREATE OR REPLACE FUNCTION FN_AGENCIA (p_id_huesped IN NUMBER) RETURN VARCHAR2 IS
    v_nombre_agencia VARCHAR2(100); 
BEGIN
    -- La agencia se obtiene cruzando directamente con el huésped
    SELECT a.nom_agencia
    INTO v_nombre_agencia
    FROM huesped h
    JOIN agencia a ON h.id_agencia = a.id_agencia
    WHERE h.id_huesped = p_id_huesped;

    RETURN v_nombre_agencia;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO reg_errores (id_error, nomsubprograma, msg_error)
        VALUES (sq_error.NEXTVAL, 
                'Error en la función FN_AGENCIA al recuperar la agencia del huesped con id ' || p_id_huesped, 
                'ORA-01403: No se ha encontrado ningún dato');
        RETURN 'NO REGISTRA AGENCIA';
END;
/

CREATE OR REPLACE FUNCTION FN_CONSUMOS (p_id_huesped IN NUMBER) RETURN NUMBER IS
    v_monto_consumos NUMBER;
BEGIN
    SELECT monto_consumos
    INTO v_monto_consumos
    FROM total_consumos
    WHERE id_huesped = p_id_huesped;

    RETURN v_monto_consumos;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO reg_errores (id_error, nomsubprograma, msg_error)
        VALUES (sq_error.NEXTVAL, 
                'Error en la función FN_CONSUMOS al recuperar los consumos del cliente con Id ' || p_id_huesped, 
                'ORA-01403: No se ha encontrado ningún dato');
        RETURN 0;
END;
/


-- 3. PROCEDIMIENTO ALMACENADO PRINCIPAL

CREATE OR REPLACE PROCEDURE SP_PROCESAR_COBROS (
    p_fecha_proceso IN DATE, 
    p_valor_dolar IN NUMBER   
) 
IS
    CURSOR c_huespedes IS 
        SELECT r.id_huesped, 
               -- Concatenamos el nombre completo del huésped
               h.nom_huesped || ' ' || h.appat_huesped || ' ' || h.apmat_huesped AS nombre,
               r.estadia AS dias_estadia, 
               -- Subconsulta para sumar el valor diario de las habitaciones asociadas a la reserva
               NVL((SELECT SUM(hab.valor_habitacion + hab.valor_minibar) 
                    FROM detalle_reserva dr
                    JOIN habitacion hab ON dr.id_habitacion = hab.id_habitacion
                    WHERE dr.id_reserva = r.id_reserva), 0) AS valor_diario
        FROM reserva r
        JOIN huesped h ON r.id_huesped = h.id_huesped
        -- La fecha de salida paramétrica se calcula sumando los días de estadía al ingreso
        WHERE TRUNC(r.ingreso + r.estadia) = TRUNC(p_fecha_proceso);

    -- Variables Dólares
    v_alojamiento_usd NUMBER;
    v_consumos_usd NUMBER;
    v_tours_usd NUMBER;
    v_personas_usd NUMBER;
    v_subtotal_usd NUMBER;
    v_dcto_agencia_usd NUMBER := 0;
    v_total_usd NUMBER;
    
    -- Variables Pesos Chilenos
    v_alojamiento_clp NUMBER;
    v_consumos_clp NUMBER;
    v_tours_clp NUMBER;
    v_subtotal_clp NUMBER;
    v_dcto_agencia_clp NUMBER;
    v_total_clp NUMBER;
    
    v_agencia VARCHAR2(100);

BEGIN
    DELETE FROM detalle_diario_huespedes;
    DELETE FROM reg_errores;
    COMMIT;

    FOR reg IN c_huespedes LOOP
        
        v_agencia := FN_AGENCIA(reg.id_huesped);
        v_consumos_usd := FN_CONSUMOS(reg.id_huesped);
        PKG_COBROS_HOTEL.v_total_tours := PKG_COBROS_HOTEL.FN_MONTO_TOURS(reg.id_huesped);
        v_tours_usd := PKG_COBROS_HOTEL.v_total_tours;

        -- 1. Alojamiento en dólares
        v_alojamiento_usd := reg.valor_diario * reg.dias_estadia;
        
        -- 2. Valor por personas ($35.000 a USD). (Se asume 1 tarifa base por reserva)
        v_personas_usd := (35000 / p_valor_dolar) * 1;
        
        -- 3. Subtotal
        v_subtotal_usd := v_alojamiento_usd + v_consumos_usd + v_tours_usd + v_personas_usd;
        
        -- 4. Descuentos
        IF UPPER(v_agencia) = 'VIAJES ALBERTI' THEN
            v_dcto_agencia_usd := v_subtotal_usd * 0.12;
        ELSE
            v_dcto_agencia_usd := 0;
        END IF;
        
        v_total_usd := v_subtotal_usd - v_dcto_agencia_usd;

        -- CONVERSIÓN A PESOS Y REDONDEO
        v_alojamiento_clp := ROUND(v_alojamiento_usd * p_valor_dolar);
        v_consumos_clp := ROUND(v_consumos_usd * p_valor_dolar);
        v_tours_clp := ROUND(v_tours_usd * p_valor_dolar);
        v_subtotal_clp := ROUND(v_subtotal_usd * p_valor_dolar);
        v_dcto_agencia_clp := ROUND(v_dcto_agencia_usd * p_valor_dolar);
        v_total_clp := ROUND(v_total_usd * p_valor_dolar);

        -- INSERCIÓN EN TABLA DE RESULTADOS
        INSERT INTO detalle_diario_huespedes (
            id_huesped, nombre, agencia, alojamiento, consumos, tours,
            subtotal_pago, descuento_consumos, descuentos_agencia, total
        ) VALUES (
            reg.id_huesped, reg.nombre, v_agencia, v_alojamiento_clp, v_consumos_clp, v_tours_clp,
            v_subtotal_clp, 0, v_dcto_agencia_clp, v_total_clp
        );

    END LOOP;
    
    COMMIT;
END;
/

-- 4. BLOQUE ANÓNIMO DE EJECUCIÓN Y PRUEBA

BEGIN
    SP_PROCESAR_COBROS(TO_DATE('18/08/2021', 'DD/MM/YYYY'), 915);
    DBMS_OUTPUT.PUT_LINE('Proceso ejecutado. Verifique la tabla DETALLE_DIARIO_HUESPEDES.');
END;
/

-- Consultas de verificación
SELECT * FROM DETALLE_DIARIO_HUESPEDES ORDER BY ID_HUESPED;
SELECT * FROM REG_ERRORES ORDER BY ID_ERROR;

/* *****************************************************************************
   NOTA DE VERIFICACIÓN - CASO 2:
   Se comprueba que el procedimiento SP_PROCESAR_COBROS se ejecuta correctamente:
   1. Los cálculos insertados en la tabla DETALLE_DIARIO_HUESPEDES coinciden con 
      la "Figura 3" de la pauta. Los montos fueron calculados en dólares, 
      convertidos a pesos chilenos ($915) y redondeados a valores enteros.
   2. El descuento del 12% se aplicó exitosamente solo a las reservas asociadas 
      a la agencia "VIAJES ALBERTI".
   3. La tabla REG_ERRORES (Figura 4) capturó correctamente las excepciones 
      (ORA-01403) para los huéspedes que no tenían registros de agencia o no 
      presentaban consumos adicionales, permitiendo que el proceso no se cayera.
   ************************************************************************** */
