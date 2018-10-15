package com.ef;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class Parser {

	private static final Logger log = LoggerFactory.getLogger(Parser.class);

	public static void main(String[] args) {
		SimpleCommandLinePropertySource propertySource = new SimpleCommandLinePropertySource(args);

		String duration = propertySource.getProperty("duration");
		String startDate = propertySource.getProperty("startDate");
        String accessLogPath = propertySource.getProperty("accesslog");

        int threshold =  0 ;
        try {
            threshold =  Integer.parseInt(propertySource.getProperty("threshold"));
        } catch (NumberFormatException exception) {
            log.error("threshold format exception : "+ threshold);
            return;
        }

        boolean isHourly = duration.equals("hourly");
        boolean isDaily = duration.equals("daily");

        if (!isHourly &&  !isDaily) {
			log.error("wrong duration : "+ duration);
			return;
		}

        if (isHourly && (threshold <= 0 || threshold > 200)) {
            log.error("Threshold must be greater than 0 and less than  200 ");
            return ;
        }

        if (isDaily && (threshold <= 0 || threshold > 500)) {
            log.error("Threshold must be greater than 0 and tess than 500 ");
            return ;
        }

        if(!fileExist(accessLogPath)){
            log.error("file not exist : "+ accessLogPath);
            return;
        }

		if(!checkStartDate(startDate)){
			log.error("startDate format exception : "+ startDate);
			return;
		}


		SpringApplication.run(Parser.class, args);
	}

	private static boolean checkStartDate(String startDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
		try {
			sdf.parse(startDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private static boolean fileExist(String filePath) {
		try {
			Path path = Paths.get(filePath);
			return Files.exists(path);

		}catch (Exception e) {
			return false;
		}
	}
}
