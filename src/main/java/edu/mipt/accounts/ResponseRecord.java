package edu.mipt.accounts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ResponseRecord {
    @Id
    private String rqUID;
    private AccountResponse accountResponse;

    public ResponseRecord() {
    }

    public ResponseRecord(String rqUID, AccountResponse accountResponse) {
        this.rqUID = rqUID;
        this.accountResponse = accountResponse;
    }
}
