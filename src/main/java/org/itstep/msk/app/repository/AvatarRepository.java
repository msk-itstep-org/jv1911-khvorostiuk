package org.itstep.msk.app.repository;


import org.itstep.msk.app.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Image, Long> {
}
