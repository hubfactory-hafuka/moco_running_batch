package jp.hubfactory.moco.batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RunScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

//    @Scheduled(cron="0 0 * * * *") テスト用(１時間毎)
    // 毎月1日0時に起動
    @Scheduled(cron="0 0 0 1 * ?")
    public void run() {

        try {
            String dateParam = new Date().toString();
            JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
            JobExecution execution = jobLauncher.run(job, param);
            System.out.println("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
