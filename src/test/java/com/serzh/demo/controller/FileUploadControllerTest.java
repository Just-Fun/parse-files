package com.serzh.demo.controller;

import com.serzh.demo.service.Parser;
import com.serzh.demo.service.StorageService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
//@MockBean(StorageService.class)
@MockBean(Parser.class)
public class FileUploadControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StorageService storageService;

    @Test
    public void listUploadedFiles() throws Exception {
        given(storageService.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

        mockMvc.perform(
                get("/")
                        /*.content("{ \"body\":\"Что нужно сделать чтобы было артифактори работал нормально?\"}")
                        .accept("application/json")
                        .contentType("application/json")*/
        ).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("first.txt", "second.txt")));
    }

    @Test
    public void handleFileUpload() throws Exception {
    }

    @Test
    public void parseFiles() throws Exception {
    }

    @Test
    public void clear() throws Exception {
    }

    @Test
    public void handleStorageFileNotFound() throws Exception {
    }

}