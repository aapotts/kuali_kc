DELIMITER /
CREATE TABLE NEGOTIATION_CUSTOM_DATA
(
      NEGOTIATION_CUSTOM_DATA_ID DECIMAL(8)
        , NEGOTIATION_ID DECIMAL(22)
        , NEGOTIATION_NUMBER VARCHAR(12)
        , CUSTOM_ATTRIBUTE_ID DECIMAL(12)
        , VALUE VARCHAR(2000)
        , UPDATE_TIMESTAMP DATE
        , UPDATE_USER VARCHAR(60)
        , VER_NBR DECIMAL(8) default 1
        , OBJ_ID VARCHAR(36) NOT NULL    
        , CONSTRAINT NEGOTIATION_CUSTOM_DATAP1 PRIMARY KEY(NEGOTIATION_CUSTOM_DATA_ID)
)
/
DELIMITER ;
