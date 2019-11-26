package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Good, Integer> {
}
