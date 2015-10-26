package jp.hubfactory.moco.service;

import java.util.Date;

import jp.hubfactory.moco.entity.User;
import jp.hubfactory.moco.entity.UserRankingPoint;
import jp.hubfactory.moco.repository.UserRankingPointRepository;
import jp.hubfactory.moco.repository.UserRepository;
import jp.hubfactory.moco.util.MocoDateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRankingPointRepository userRankingPointRepository;

    @Autowired
    private RedisService redisService;

    /**
     * ランキングポイント更新
     * @param userId
     * @param point
     */
    public void updUserPoint(Long userId, Long point, Long rank, Date rankingDate, Date nowDate) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            return;
        }
        user.setPoint(user.getPoint().intValue() + point.longValue());
        redisService.updateUser(user);

        String targetDateYm = MocoDateUtils.convertString(rankingDate, MocoDateUtils.DATE_FORMAT_yyyyMM_SLASH);
        UserRankingPoint userRankingPoint = new UserRankingPoint(userId, targetDateYm, rank, point, nowDate, nowDate);
        userRankingPointRepository.save(userRankingPoint);
    }
}
