package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

}
