package com.tcarroll10.finance.repo;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.repo.FindataApiRepoJdbcImpl;

@SpringBootTest
public class FindataApiJDBCRepoTest {

    @Autowired
    private DataSource dataSource; // Autowire the DataSource

    @Autowired
    FindataApiRepo repo;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        repo = new FindataApiRepoJdbcImpl(jdbcTemplate);
    }

}
