package edu.mipt.accounts.impl;

import edu.mipt.accounts.AccountResponse;
import edu.mipt.accounts.ResponseRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ResponseRecordRepository extends JpaRepository<ResponseRecord, String> {
    Optional<ResponseRecord> findById(String rqUID);
}
