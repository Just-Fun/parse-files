package com.serzh.demo.service;

import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public interface Parser {
    Map<String, Integer> pars(List<Resource> resources);
}
