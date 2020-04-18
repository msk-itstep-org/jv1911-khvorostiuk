package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.AudioRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AudioRecordRepository extends JpaRepository<AudioRecord, Long> {
    Set<AudioRecord> findByAuthorLikeOrNameLike(String author, String name);
}
