package com.example.coderamabackend;

import com.example.coderamabackend.binary_obj.DaoBinaryObject;
import com.example.coderamabackend.binary_obj.EntityBinaryObject;
import com.example.coderamabackend.item.DaoItem;
import com.example.coderamabackend.item.EntityItem;
import com.example.coderamabackend.jdbi.EntityRowMapper;
import com.example.coderamabackend.rate.DaoRate;
import com.example.coderamabackend.rate.EntityRate;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapperFactory;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Autowired;
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

        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityRate.class, new EntityRowMapper<>(EntityRate.class)));
        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityItem.class, new EntityRowMapper<>(EntityItem.class)));
        this.jdbi.registerRowMapper(RowMapperFactory.of(EntityBinaryObject.class, new EntityRowMapper<>(EntityBinaryObject.class)));
    }

    @Bean
    public Jdbi getJdbi() {
        return this.jdbi;
    }

    @Bean
    public DaoRate daoRate() {
        return this.jdbi.onDemand(DaoRate.class);
    }

    @Bean
    public DaoItem daoItem() {
        return this.jdbi.onDemand(DaoItem.class);
    }

    @Bean
    DaoBinaryObject daoBinaryObject() {
        return this.jdbi.onDemand(DaoBinaryObject.class);
    }


}
