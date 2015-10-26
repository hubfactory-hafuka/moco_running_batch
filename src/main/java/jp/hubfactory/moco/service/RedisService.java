package jp.hubfactory.moco.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jp.hubfactory.moco.bean.UserRankingBean;
import jp.hubfactory.moco.entity.User;
import jp.hubfactory.moco.util.MocoDateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisService {

    private static final String ALL_RANKING_KEY = "all_ranking_";
    private static final String USER_KEY = "user";

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Redisのユーザー情報更新
     * @param user
     */
    public void updateUser(User user) {

        ObjectMapper mapper = new ObjectMapper();

        // UserエンティティをJson文字列に変換
        String userJson = null;
        try {
            userJson = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON変換エラー");
        }

        HashOperations<String, Object, Object> hashOps = this.redisTemplate.opsForHash();
        hashOps.put(USER_KEY, user.getUserId().toString(), userJson);

    }

    public List<UserRankingBean> getRankingListForBatch(Date date) {
        String rankingKey = this.getAllRankingKey(date);
        return this.getList(rankingKey, 100);
    }


    private List<UserRankingBean> getList(String rankingKey, long limit) {

        ZSetOperations<String, String> zsetOps = this.redisTemplate.opsForZSet();

        long rank = 0L;
        long start = 0;
        long end = limit - 1;

        // ランキングデータ取得
        List<UserRankingBean> userRankingList = new ArrayList<>();
        Set<TypedTuple<String>> set = zsetOps.reverseRangeWithScores(rankingKey, start, end);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }

        for (TypedTuple<String> typedTuple : set) {

            // 順位取得
            rank = zsetOps.reverseRank(rankingKey, typedTuple.getValue()) + 1;
            //rank = zsetOps.count(rankingKey, typedTuple.getScore() + 1, Double.MAX_VALUE) + 1;
            // ユーザーID
            Long rankUserId = Long.valueOf(typedTuple.getValue());
            // 小数点第以下切り捨て
            Double userScore = new BigDecimal(String.valueOf(typedTuple.getScore())).setScale(2, RoundingMode.FLOOR).doubleValue();

            UserRankingBean rankingBean = new UserRankingBean();
            rankingBean.setUserId(rankUserId);
            rankingBean.setRank(rank);
            rankingBean.setDistance(userScore);
            userRankingList.add(rankingBean);
        }
        return userRankingList;
    }

    /**
     * 全体ランキングキー取得
     * @return
     */
    private String getAllRankingKey(Date targetDate) {
        return ALL_RANKING_KEY + MocoDateUtils.convertString(targetDate, MocoDateUtils.DATE_FORMAT_yyyyMM);
    }
}
