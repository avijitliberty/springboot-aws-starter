package com.example.aws.config;

import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
import org.springframework.cloud.aws.jdbc.datasource.DataSourceFactory;
import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/aws-config.xml")
@EnableRdsInstance(databaseName = "${database-name:}", dbInstanceIdentifier =
  "${db-instance-identifier:}", username = "${rdsUser:}", password = "${rdsPassword:}")
public class AwsResourceConfig {
	
	@Bean
    public RdsInstanceConfigurer instanceConfigurer() {
        return new RdsInstanceConfigurer() {
            @Override
        	public DataSourceFactory getDataSourceFactory() {   		
        		TomcatJdbcDataSourceFactory dataSourceFactory = new TomcatJdbcDataSourceFactory();
                dataSourceFactory.setInitialSize(10);
                dataSourceFactory.setValidationQuery("SELECT 1 FROM DUAL");
                dataSourceFactory.setValidationInterval(10000);
                dataSourceFactory.setTimeBetweenEvictionRunsMillis(20000);
                dataSourceFactory.setTestOnBorrow(true);
                dataSourceFactory.setTestWhileIdle(true);
                return dataSourceFactory;
        	}
        };
    }

}