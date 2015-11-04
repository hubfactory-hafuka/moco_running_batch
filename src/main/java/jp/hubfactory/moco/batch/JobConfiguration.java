package jp.hubfactory.moco.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.hubfactory.moco.bean.UserRankingBean;
import jp.hubfactory.moco.entity.MstRankingReward;
import jp.hubfactory.moco.repository.MstRankingRewardRepository;
import jp.hubfactory.moco.service.RedisService;
import jp.hubfactory.moco.service.UserService;
import jp.hubfactory.moco.util.MocoDateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private StepBuilderFactory steps;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private MstRankingRewardRepository mstRankingRewardRepository;

    @Bean
    public Job loadStampJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("loadRankingJob").incrementer(new RunIdIncrementer()).start(s1).build();
    }

    @Bean(name = "s1")
    public Step step1() {
        return steps.get("step1").tasklet((stepContribution, chunkContext) -> {

            // 現在日時取得
            Date nowDate = MocoDateUtils.getNowDate();

            Calendar cal = Calendar.getInstance();
            cal.setTime(nowDate);
            int day = cal.get(Calendar.DATE);
            if (day != 1) {
                // １日じゃない場合は処理終了
                return RepeatStatus.FINISHED;
            }

            System.out.println("========== ranking batch start ==========");

            Date targetDate = MocoDateUtils.add(nowDate, -1, Calendar.MONTH);

            List<UserRankingBean> userRankingList = redisService.getRankingListForBatch(targetDate);

            if (CollectionUtils.isEmpty(userRankingList)) {
                System.out.println("========== userRankingList is empty ==========");
                System.out.println("========== ranking batch end ==========");
                return RepeatStatus.FINISHED;
            }

            System.out.println("========== userRankingList count =" + userRankingList.size() + " ==========");
            List<MstRankingReward> rewardList = mstRankingRewardRepository.findAll();
            for (UserRankingBean userRankingBean : userRankingList) {

//                boolean isReceived = false;

                for (MstRankingReward reward : rewardList) {

                    if (reward.getFromRank().intValue() <= userRankingBean.getRank().intValue() && userRankingBean.getRank().intValue() <= reward.getToRank().intValue()) {
                        // 報酬付与
                        userService.updUserPoint(userRankingBean.getUserId(), reward.getPoint(), userRankingBean.getRank(), targetDate, nowDate);
//                        isReceived = true;
                        break;
                    }
                }
                // ランキング報酬外の場合
//                if (!isReceived) {
//                    // 参加賞付与
//                    userService.updUserPoint(userRankingBean.getUserId(), 300L, 0L, targetDate, nowDate);
//                }
            }

            System.out.println("========== ranking batch end ==========");
            return RepeatStatus.FINISHED;
        }).build();
    }

}
