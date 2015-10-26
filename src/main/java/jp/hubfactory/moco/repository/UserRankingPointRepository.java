package jp.hubfactory.moco.repository;

import jp.hubfactory.moco.entity.UserRankingPoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRankingPointRepository extends JpaRepository<UserRankingPoint, Long> {
}
