ALTER TABLE BUDGET_PERSONNEL_CAL_AMTS 
    ADD CONSTRAINT FK_BUDGET_PERSONNEL_CAL_AMTS FOREIGN KEY (BUDGET_ID) 
                REFERENCES BUDGET;

ALTER TABLE BUDGET_PERSONNEL_CAL_AMTS 
    ADD CONSTRAINT FK1_BUDGET_PERSONNEL_CAL_AMTS FOREIGN KEY (BUDGET_PERSONNEL_DETAILS_ID) 
                REFERENCES BUDGET_PERSONNEL_DETAILS (BUDGET_PERSONNEL_DETAILS_ID) ;