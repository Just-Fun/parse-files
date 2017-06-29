package com.serzh.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    List<Resource> loadResources();

    Stream<Path> loadAll();

//    Path load(String filename);

//    Resource loadAsResource(String filename);

    void deleteAll();

    void cleanDirectory();
}
