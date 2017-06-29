package com.serzh.demo.service.impl;

import com.serzh.demo.service.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author sergii.zagryvyi on 29.06.2017
 */
@Service
@Slf4j
public class ParserImpl implements Parser {

    @Value("${maxThreads}")
    private int maxThreads;

    @Override
    public Map<String, Integer> pars(List<Resource> resources) {

        List<Map<String, Integer>> mapList = parseFiles(resources);

        Map<String, Integer> unsortedMap = mergeMaps(mapList);

        return sortMapByValue(unsortedMap);

    }

    private List<Map<String, Integer>> parseFiles(List<Resource> resources) {
        int size = resources.size();
        ExecutorService executor = Executors.newFixedThreadPool(size > maxThreads ? maxThreads : size);

        return resources.stream()
                .map(r -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return createMapFromFile(r.getInputStream());
                    } catch (IOException e) {
                        log.warn("");
//                        e.printStackTrace();
                        return new HashMap<String, Integer>();
                    }
                }, executor)
                        .exceptionally(error -> new HashMap<>()))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> createMapFromFile(InputStream is) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
               /* if (!line.isEmpty()) {
                    linesToMap(map, line);
                }*/
                if (map.containsKey(line)) {
                    map.put(line, map.get(line) + 1);
                } else {
                    map.put(line, 1);
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return map;
    }

    private Map<String, Integer> mergeMaps(List<Map<String, Integer>> mapList) {
        return mapList.stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }

    private Map<String, Integer> sortMapByValue(Map<String, Integer> unsortMap) {
        return unsortMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //Alternative way
       /* Map<String, Integer> result2 = new LinkedHashMap<>();
        unsortMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));

        return result;*/
    }

}
