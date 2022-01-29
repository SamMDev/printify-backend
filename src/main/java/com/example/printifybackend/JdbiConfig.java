package com.example.printifybackend;

import com.example.printifybackend.binary_obj.EntityBinaryObject;
import com.example.printifybackend.item.EntityItem;
import com.example.printifybackend.jdbi.EntityRowMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapperFactory;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class JdbiConfig {

    private DataSource dataSource;

    private Jdbi jdbi;

    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource driverManagerDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5432/printify");
        driverManagerDataSource.setUsername("postgres");
        driverManagerDataSource.setPassword("helloworld");
        return driverManagerDataSource;
    }

    @PostConstruct
    private void init() {
        this.dataSource = this.driverManagerDataSource();
        this.jdbi = Jdbi.create(this.dataSource);
        this.jdbi.installPlugin(new SqlObjectPlugin());

        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityItem.class, new EntityRowMapper<>(EntityItem.class)));
        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityBinaryObject.class, new EntityRowMapper<>(EntityBinaryObject.class)));
        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityItem.class, new EntityRowMapper<>(EntityItem.class)));
    }

    @Bean
    public Jdbi getJdbi() {
        return this.jdbi;
    }

}
