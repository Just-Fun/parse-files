package com.serzh.demo.controller;

import com.serzh.demo.data.Line;
import com.serzh.demo.exception.StorageFileNotFoundException;
import com.serzh.demo.service.Parser;
import com.serzh.demo.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class FileUploadController {

    private final StorageService storageService;
    private final Parser parser;

    @Autowired
    public FileUploadController(StorageService storageService, Parser parser) {
        this.storageService = storageService;
        this.parser = parser;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        log.info("Check!!!!!!!!!!!");

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
    public String parseFiles(Model model) {
        List<Resource> resources = storageService.loadResources();
        Map<String, Integer> map = parser.pars(resources);

        model.addAttribute("results",
//                MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "parse2").build().toString()
//                serveFile()
                map
        );

        model.addAttribute("files", storageService.loadAll()
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/parse1", method = RequestMethod.GET)
    public List<Line> serveFile() {
        List<Line> result = new ArrayList<>();
        Line line1 = Line.builder().line("One").count(1).build();
        Line line2 = Line.builder().line("Two").count(2).build();
        result.add(line1);
        result.add(line2);
//        Map<Integer, String> result = new HashMap<>();
//        result.put(1, "One");
//        result.put(2, "Two");
//         String result = "Result Check!";
//        return ResponseEntity.ok(result);
        return result;

//        Gson gson = new Gson();
//        String json = gson.toJson(result);
//
//        return json;

    }


  /*  @GetMapping("/parse2")
    public ResponseEntity<String> parseFiles(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("result",
                "Check!");
        return "redirect:/";
    }*/

    @GetMapping("/clear")
    public String clear() {
        storageService.cleanDirectory();
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
