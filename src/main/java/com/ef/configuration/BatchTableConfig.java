package com.ef.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class BatchTableConfig {



    @Autowired
    public DataSource dataSource;

    @Value("classpath:org/springframework/batch/core/schema-drop-mysql.sql")
    private Resource dropTable;

    @Value("classpath:org/springframework/batch/core/schema-mysql.sql")
    private Resource createTable;

    @Value("classpath:schema.sql")
    private Resource logSql;


    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }


    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(dropTable);
        populator.addScript(createTable);
        populator.addScript(logSql);
        return populator;
    }

}
