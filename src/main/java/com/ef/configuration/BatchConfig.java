package com.ef.configuration;


import com.ef.model.LogData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableBatchProcessing
public class BatchConfig {



    @Value("${accesslog}")
    private String accesslog;


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public Step logReadStep() {
        return stepBuilderFactory.get("logReadStep")
                .<LogData, LogData>chunk(3000)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job logDataImport(JobFinishListener listener) {
        return jobBuilderFactory.get("logDataImport")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(logReadStep())
                .end()
                .build();
    }


    @Bean
    public FlatFileItemReader<LogData> reader() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        FlatFileItemReader<LogData> reader = new FlatFileItemReader<>();

        reader.setResource(new FileSystemResource(accesslog));

        reader.setLineMapper(new DefaultLineMapper<LogData>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter("|");
                setNames(new String[]{"logDate", "ipValue", "request", "status", "uAgent"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<LogData>() {{
                setTargetType(LogData.class);
                setCustomEditors(Stream.of(new AbstractMap.SimpleEntry<>(
                        Date.class,
                        new CustomDateEditor(dateFormat, false)
                )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            }});
        }});
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<LogData> writer() {
        JdbcBatchItemWriter<LogData> writer = new JdbcBatchItemWriter<>();

        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        writer.setSql("INSERT INTO log_data (logDate, ipValue, request, status, uAgent) " +
                "VALUES (:logDate, :ipValue, :request, :status, :uAgent)");

        writer.setDataSource(dataSource);

        return writer;
    }




}
