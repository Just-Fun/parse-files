package com.serzh.demo.service.impl;

import com.serzh.demo.StorageProperties;
import com.serzh.demo.exception.StorageException;
import com.serzh.demo.exception.StorageFileNotFoundException;
import com.serzh.demo.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + originalFilename);
            }
//            Store to Hazelcast
            Files.copy(file.getInputStream(), this.rootLocation.resolve(originalFilename));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }


//    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public List<Resource> loadResources() {
        Stream<Path> pathStream = loadAll();
        return pathStream
                .map(this::loadAsResource)
                .collect(Collectors.toList());
    }

    private Resource loadAsResource(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + path.toString());
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + path.toString(), e);
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation));
//                    .map(other -> this.rootLocation.relativize(other));
//                    .map(this.rootLocation::resolve);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void cleanDirectory() {
        try {
            FileUtils.cleanDirectory(rootLocation.toFile());
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
