DELIMITER /

ALTER TABLE COI_DISCLOSURE
    ADD CONSTRAINT FK_REVIEW_STATUS_CODE FOREIGN KEY (REVIEW_STATUS_CODE)
    REFERENCES COI_REVIEW_STATUS (REVIEW_STATUS_CODE)
/

DELIMITER ;


