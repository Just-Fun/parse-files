package com.serzh.demo.repositories;

import com.serzh.demo.data.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ResultRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ResultRepository resultRepository;

    @Test
    public void should_persist_and_read() {
        Map<String, Integer> resultMap = new HashMap<>();
        Result result = Result.builder().resultMap(resultMap).build();
        Result persist = testEntityManager.persist(result);

        assertNotNull(resultRepository.findOne(persist.getId()));
    }

}