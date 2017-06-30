package com.serzh.demo.controller;

import com.serzh.demo.StorageProperties;
import com.serzh.demo.service.StorageService;
import com.serzh.demo.service.impl.FileSystemStorageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @author sergii.zagryvyi on 30.06.2017
 */
@TestConfiguration
public class FileUploadTestConfiguration {

    @Bean
    StorageService storageService(StorageProperties storageProperties) {
        return new FileSystemStorageService(storageProperties );
    }

    @PostConstruct
    public void init() {
        System.out.println("Check!");
    }
 }
