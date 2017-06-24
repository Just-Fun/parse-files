package com.serzh.demo.controller;

import com.serzh.demo.exception.StorageFileNotFoundException;
import com.serzh.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll()
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("files") MultipartFile files[],
                                   RedirectAttributes redirectAttributes) {

        for (MultipartFile file : files) {
            storageService.store(file);
        }
        int length = files.length;
        redirectAttributes.addFlashAttribute("message",
                MessageFormat.format("You successfully uploaded {0} {1} !"
                        , length, length > 1 ? "files" : "file"));

        return "redirect:/";
    }

    @GetMapping("/parse")
    public String parseFiles(RedirectAttributes redirectAttributes)  {
        redirectAttributes.addFlashAttribute("result",
                "Check!");
        return "redirect:/";
    }

    @GetMapping("/clear")
    public String clear()  {
        storageService.cleanDirectory();
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
