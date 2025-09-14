-- Generado por Oracle SQL Developer Data Modeler 24.3.1.351.0831
--   en:        2025-09-14 02:13:43 CLST
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE AFP 
    ( 
     codigo_afp       VARCHAR2 (10 CHAR)  NOT NULL , 
     nombre_afp       VARCHAR2 (50 CHAR)  NOT NULL , 
     PACIENTE_VARCHAR VARCHAR2 (12 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE AFP 
    ADD CONSTRAINT AFP_PK PRIMARY KEY ( codigo_afp ) ;

CREATE TABLE ATENCIÓN 
    ( 
     id_atencion         INTEGER  NOT NULL , 
     fecha               DATE  NOT NULL , 
     hora                VARCHAR2 (5 CHAR)  NOT NULL , 
     tipo_atencion       VARCHAR2 (20 CHAR)  NOT NULL , 
     diagnostico         VARCHAR2 (500 CHAR) , 
     rut_paciente        VARCHAR2 (12 CHAR)  NOT NULL , 
     rut_medico          VARCHAR2 (12 CHAR)  NOT NULL , 
     PAGO_id_pago        INTEGER  NOT NULL , 
     id_solicitud_examen NUMBER  NOT NULL 
    ) 
;
CREATE UNIQUE INDEX ATENCIÓN__IDX ON ATENCIÓN 
    ( 
     PAGO_id_pago ASC 
    ) 
;

ALTER TABLE ATENCIÓN 
    ADD CONSTRAINT ATENCIÓN_PK PRIMARY KEY ( id_atencion ) ;

CREATE TABLE COMUNA 
    ( 
     id_comuna        INTEGER  NOT NULL , 
     nombre_comuna    VARCHAR2 (100 CHAR)  NOT NULL , 
     id_region        INTEGER  NOT NULL , 
     UNIDAD_id_unidad INTEGER  NOT NULL , 
     PACIENTE_VARCHAR VARCHAR2 (12 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE COMUNA 
    ADD CONSTRAINT COMUNA_PK PRIMARY KEY ( id_comuna ) ;

CREATE TABLE ESPECIALIDAD 
    ( 
     id_especialidad     INTEGER  NOT NULL , 
     nombre_especialidad VARCHAR2 (100 CHAR)  NOT NULL , 
     MÉDICO_rut_medico   VARCHAR2 (12 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE ESPECIALIDAD 
    ADD CONSTRAINT ESPECIALIDAD_PK PRIMARY KEY ( id_especialidad ) ;

CREATE TABLE EXAMEN 
    ( 
     codigo_examen           VARCHAR2 (10 CHAR)  NOT NULL , 
     nombre_examen           VARCHAR2 (100 CHAR)  NOT NULL , 
     tipo_muestra            VARCHAR2 (50 CHAR)  NOT NULL , 
     condiciones_preparacion VARCHAR2 (200 CHAR) , 
     id_solicitud_examen     NUMBER  NOT NULL 
    ) 
;

CREATE TABLE INSTITUCION_SALUD 
    ( 
     codigo_salud       VARCHAR2 (10 CHAR)  NOT NULL , 
     nombre_institucion VARCHAR2 (100 CHAR)  NOT NULL , 
     PACIENTE_VARCHAR   VARCHAR2 (12 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE INSTITUCION_SALUD 
    ADD CONSTRAINT INSTITUCION_SALUD_PK PRIMARY KEY ( codigo_salud ) ;

CREATE TABLE MÉDICO 
    ( 
     rut_medico           VARCHAR2 (12 CHAR)  NOT NULL , 
     nombre_completo      VARCHAR2 (100 CHAR)  NOT NULL , 
     direccion            VARCHAR2 (100 CHAR)  NOT NULL , 
     id_comuna            INTEGER  NOT NULL , 
     fecha_ingreso        DATE  NOT NULL , 
     id_especialidad      INTEGER  NOT NULL , 
     codigo_afp           VARCHAR2 (10 CHAR)  NOT NULL , 
     codigo_salud         VARCHAR2 (10 CHAR)  NOT NULL , 
     id_unidad            INTEGER  NOT NULL , 
     ATENCIÓN_id_atencion INTEGER  NOT NULL 
    ) 
;

ALTER TABLE MÉDICO 
    ADD CONSTRAINT MÉDICO_PK PRIMARY KEY ( rut_medico ) ;

CREATE TABLE PACIENTE 
    ( 
     "VARCHAR"            VARCHAR2 (12 CHAR)  NOT NULL , 
     nombre_completo      VARCHAR2 (100 CHAR)  NOT NULL , 
     direccion            VARCHAR2 (100 CHAR)  NOT NULL , 
     id_comuna            INTEGER  NOT NULL , 
     tipo_usuario         VARCHAR2 (20 CHAR)  NOT NULL , 
     sexo                 CHAR (1 CHAR)  NOT NULL , 
     fecha_nacimiento     DATE  NOT NULL , 
     ATENCIÓN_id_atencion INTEGER  NOT NULL 
    ) 
;

ALTER TABLE PACIENTE 
    ADD CONSTRAINT PACIENTE_PK PRIMARY KEY ( "VARCHAR" ) ;

CREATE TABLE PAGO 
    ( 
     id_pago              INTEGER  NOT NULL , 
     id_atencion          INTEGER  NOT NULL , 
     monto                NUMBER (10,2) , 
     tipo_pago            VARCHAR2 (20 CHAR)  NOT NULL , 
     ATENCIÓN_id_atencion INTEGER  NOT NULL 
    ) 
;
CREATE UNIQUE INDEX PAGO__IDX ON PAGO 
    ( 
     ATENCIÓN_id_atencion ASC 
    ) 
;

ALTER TABLE PAGO 
    ADD CONSTRAINT PAGO_PK PRIMARY KEY ( id_pago ) ;

CREATE TABLE REGIÓN 
    ( 
     id_region        INTEGER  NOT NULL , 
     nombre_region    VARCHAR2 (100 CHAR)  NOT NULL , 
     COMUNA_id_comuna INTEGER  NOT NULL 
    ) 
;

ALTER TABLE REGIÓN 
    ADD CONSTRAINT REGIÓN_PK PRIMARY KEY ( id_region ) ;

CREATE TABLE SOLICITUD_EXAMEN 
    ( 
     id_solicitud        INTEGER  NOT NULL , 
     id_atencion         INTEGER  NOT NULL , 
     codigo_examen       VARCHAR2 (10 CHAR)  NOT NULL , 
     resultado           VARCHAR2 (500 CHAR) , 
     SOLICITUD_EXAMEN_ID NUMBER  NOT NULL 
    ) 
;

ALTER TABLE SOLICITUD_EXAMEN 
    ADD CONSTRAINT SOLICITUD_EXAMEN_PK PRIMARY KEY ( SOLICITUD_EXAMEN_ID ) ;

CREATE TABLE SUPERVISIÓN 
    ( 
     rut_supervisado   VARCHAR2 (12 CHAR)  NOT NULL , 
     rut_supervisor    VARCHAR2 (12 CHAR)  NOT NULL , 
     MÉDICO_rut_medico VARCHAR2 (12 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE SUPERVISIÓN 
    ADD CONSTRAINT SUPERVISIÓN_PK PRIMARY KEY ( rut_supervisado, rut_supervisor ) ;

CREATE TABLE UNIDAD 
    ( 
     id_unidad                      INTEGER  NOT NULL , 
     nombre_unidad                  VARCHAR2 (100 CHAR)  NOT NULL , 
     INSTITUCION_SALUD_codigo_salud VARCHAR2 (10 CHAR)  NOT NULL 
    ) 
;

ALTER TABLE UNIDAD 
    ADD CONSTRAINT UNIDAD_PK PRIMARY KEY ( id_unidad ) ;

CREATE TABLE UNIDAD_ESPECIALIDAD 
    ( 
     id_unidad       INTEGER  NOT NULL , 
     id_especialidad INTEGER  NOT NULL 
    ) 
;

ALTER TABLE UNIDAD_ESPECIALIDAD 
    ADD CONSTRAINT UNIDAD_ESPECIALIDAD_PK PRIMARY KEY ( id_unidad, id_especialidad ) ;

ALTER TABLE AFP 
    ADD CONSTRAINT AFP_PACIENTE_FK FOREIGN KEY 
    ( 
     PACIENTE_VARCHAR
    ) 
    REFERENCES PACIENTE 
    ( 
     "VARCHAR"
    ) 
;

ALTER TABLE ATENCIÓN 
    ADD CONSTRAINT ATENCION_PAGO_FK FOREIGN KEY 
    ( 
     PAGO_id_pago
    ) 
    REFERENCES PAGO 
    ( 
     id_pago
    ) 
;

ALTER TABLE COMUNA 
    ADD CONSTRAINT COMUNA_PACIENTE_FK FOREIGN KEY 
    ( 
     PACIENTE_VARCHAR
    ) 
    REFERENCES PACIENTE 
    ( 
     "VARCHAR"
    ) 
;

ALTER TABLE COMUNA 
    ADD CONSTRAINT COMUNA_UNIDAD_FK FOREIGN KEY 
    ( 
     UNIDAD_id_unidad
    ) 
    REFERENCES UNIDAD 
    ( 
     id_unidad
    ) 
;

ALTER TABLE ESPECIALIDAD 
    ADD CONSTRAINT ESP_MED_FK FOREIGN KEY 
    ( 
     MÉDICO_rut_medico
    ) 
    REFERENCES MÉDICO 
    ( 
     rut_medico
    ) 
;

ALTER TABLE ESPECIALIDAD 
    ADD CONSTRAINT ESP_UNI_FK FOREIGN KEY 
    ( 
     id_especialidad
    ) 
    REFERENCES UNIDAD_ESPECIALIDAD 
    ( 
     id_unidad,
     id_especialidad
    ) 
;

ALTER TABLE EXAMEN 
    ADD CONSTRAINT EXAMEN_SOLICITUD_FK FOREIGN KEY 
    ( 
     id_solicitud_examen
    ) 
    REFERENCES SOLICITUD_EXAMEN 
    ( 
     SOLICITUD_EXAMEN_ID
    ) 
;

ALTER TABLE ATENCIÓN 
    ADD CONSTRAINT id_solicitud_examen FOREIGN KEY 
    ( 
     id_solicitud_examen
    ) 
    REFERENCES SOLICITUD_EXAMEN 
    ( 
     SOLICITUD_EXAMEN_ID
    ) 
;

ALTER TABLE INSTITUCION_SALUD 
    ADD CONSTRAINT INSTITUCION_SALUD_PACIENTE_FK FOREIGN KEY 
    ( 
     PACIENTE_VARCHAR
    ) 
    REFERENCES PACIENTE 
    ( 
     "VARCHAR"
    ) 
;

ALTER TABLE MÉDICO 
    ADD CONSTRAINT MÉDICO_ATENCIÓN_FK FOREIGN KEY 
    ( 
     ATENCIÓN_id_atencion
    ) 
    REFERENCES ATENCIÓN 
    ( 
     id_atencion
    ) 
;

ALTER TABLE PACIENTE 
    ADD CONSTRAINT PACIENTE_ATENCIÓN_FK FOREIGN KEY 
    ( 
     ATENCIÓN_id_atencion
    ) 
    REFERENCES ATENCIÓN 
    ( 
     id_atencion
    ) 
;

ALTER TABLE PAGO 
    ADD CONSTRAINT PAGO_ATENCIÓN_FK FOREIGN KEY 
    ( 
     ATENCIÓN_id_atencion
    ) 
    REFERENCES ATENCIÓN 
    ( 
     id_atencion
    ) 
;

ALTER TABLE REGIÓN 
    ADD CONSTRAINT REGIÓN_COMUNA_FK FOREIGN KEY 
    ( 
     COMUNA_id_comuna
    ) 
    REFERENCES COMUNA 
    ( 
     id_comuna
    ) 
;

ALTER TABLE SUPERVISIÓN 
    ADD CONSTRAINT SUPERVISIÓN_MÉDICO_FK FOREIGN KEY 
    ( 
     MÉDICO_rut_medico
    ) 
    REFERENCES MÉDICO 
    ( 
     rut_medico
    ) 
;

ALTER TABLE UNIDAD 
    ADD CONSTRAINT UNIDAD_INSTITUCION_SALUD_FK FOREIGN KEY 
    ( 
     INSTITUCION_SALUD_codigo_salud
    ) 
    REFERENCES INSTITUCION_SALUD 
    ( 
     codigo_salud
    ) 
;

ALTER TABLE UNIDAD 
    ADD CONSTRAINT UNIDAD_UNIDAD_ESPECIALIDAD_FK FOREIGN KEY 
    ( 
     id_unidad
    ) 
    REFERENCES UNIDAD_ESPECIALIDAD 
    ( 
     id_unidad,
     id_especialidad
    ) 
;

CREATE OR REPLACE TRIGGER FKNTM_AFP 
BEFORE UPDATE OF PACIENTE_VARCHAR 
ON AFP 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table AFP is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_ATENCIÓN 
BEFORE UPDATE OF PAGO_id_pago, id_solicitud_examen 
ON ATENCIÓN 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table ATENCIÓN is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_COMUNA 
BEFORE UPDATE OF UNIDAD_id_unidad, PACIENTE_VARCHAR 
ON COMUNA 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table COMUNA is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_ESPECIALIDAD 
BEFORE UPDATE OF id_especialidad, MÉDICO_rut_medico 
ON ESPECIALIDAD 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table ESPECIALIDAD is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_EXAMEN 
BEFORE UPDATE OF id_solicitud_examen 
ON EXAMEN 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table EXAMEN is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_INSTITUCION_SALUD 
BEFORE UPDATE OF PACIENTE_VARCHAR 
ON INSTITUCION_SALUD 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table INSTITUCION_SALUD is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_MÉDICO 
BEFORE UPDATE OF ATENCIÓN_id_atencion 
ON MÉDICO 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table MÉDICO is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_PACIENTE 
BEFORE UPDATE OF ATENCIÓN_id_atencion 
ON PACIENTE 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table PACIENTE is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_PAGO 
BEFORE UPDATE OF ATENCIÓN_id_atencion 
ON PAGO 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table PAGO is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_REGIÓN 
BEFORE UPDATE OF COMUNA_id_comuna 
ON REGIÓN 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table REGIÓN is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_SUPERVISIÓN 
BEFORE UPDATE OF MÉDICO_rut_medico 
ON SUPERVISIÓN 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table SUPERVISIÓN is violated'); 
END; 
/

CREATE OR REPLACE TRIGGER FKNTM_UNIDAD 
BEFORE UPDATE OF INSTITUCION_SALUD_codigo_salud, id_unidad 
ON UNIDAD 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table UNIDAD is violated'); 
END; 
/

CREATE SEQUENCE SOLICITUD_EXAMEN_SOLICITUD_EXA 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER SOLICITUD_EXAMEN_SOLICITUD_EXA 
BEFORE INSERT ON SOLICITUD_EXAMEN 
FOR EACH ROW 
WHEN (NEW.SOLICITUD_EXAMEN_ID IS NULL) 
BEGIN 
    :NEW.SOLICITUD_EXAMEN_ID := SOLICITUD_EXAMEN_SOLICITUD_EXA.NEXTVAL; 
END;
/



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            14
-- CREATE INDEX                             2
-- ALTER TABLE                             29
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                          13
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          1
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
