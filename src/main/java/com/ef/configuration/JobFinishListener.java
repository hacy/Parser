package com.ef.configuration;

import com.ef.model.Result;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JobFinishListener extends JobExecutionListenerSupport {

    @Value("${startDate}")
    private String startDate;

    @Value("${duration}")
    private String duration;

    @Value("${threshold}")
    private int threshold;


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobFinishListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            List<Result> results = jdbcTemplate.query("SELECT ipValue, count(1) as countOfRequest FROM log_data WHERE " +
                            "logDate >= STR_TO_DATE(?, ?) AND " +
                            "logDate <= DATE_ADD(STR_TO_DATE(?, ?), INTERVAL 1 " +
                            (duration.equals(Period.HOURLY.desc) ? Period.HOURLY.interval : Period.DAILY.interval) + ") " +
                            "GROUP BY ipValue HAVING countOfRequest > ?",

                    new Object[]{startDate, "%Y-%d-%m.%T", startDate, "%Y-%d-%m.%T", threshold},

                    (rs, rownum) -> {
                        Result accessResult = new Result(rs.getString(1),"IP access the server more than " + threshold + " requests (" + rs.getInt(2) + ") in 1 " +
                                (duration.equals(Period.HOURLY.desc) ? "hour" : "day") + " which is starts from " + startDate);


                        return accessResult;
                    });

            for (Result result : results) {
                System.out.println(result.getIpValue() + " " + result.getComment());
                jdbcTemplate.update("INSERT INTO result (ipValue, comment) VALUES (?, ?)",
                        new Object[]{result.getIpValue(), result.getComment()});
            }
        }
    }


}
