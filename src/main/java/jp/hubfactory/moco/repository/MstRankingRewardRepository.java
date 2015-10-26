package jp.hubfactory.moco.repository;

import jp.hubfactory.moco.entity.MstRankingReward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstRankingRewardRepository extends JpaRepository<MstRankingReward, Integer> {
}
