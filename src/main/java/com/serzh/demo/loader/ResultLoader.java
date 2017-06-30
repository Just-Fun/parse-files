package com.serzh.demo.loader;

import com.serzh.demo.data.Result;
import com.serzh.demo.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sergii.zagryvyi on 29.06.2017
 */
//@Component
public class ResultLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultLoader(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("Four", 4);
        resultMap.put("FourSec", 4);
        resultMap.put("Two", 2);
        resultMap.put("One", 1);
        Result result = Result.builder().resultMap(resultMap).build();
        resultRepository.save(result);

        Map<String, Integer> resultMap2 = new HashMap<>();
        resultMap2.put("Four", 4);
        resultMap2.put("FourSec", 4);
        resultMap2.put("Two", 2);
        resultMap2.put("One", 1);
        Result result2 = Result.builder().resultMap(resultMap2).build();
        resultRepository.save(result2);

    }
}
