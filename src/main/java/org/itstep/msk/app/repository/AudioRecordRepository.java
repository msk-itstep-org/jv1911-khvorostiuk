package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.AudioRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRecordRepository extends JpaRepository<AudioRecord, Long> {
}
