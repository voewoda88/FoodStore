package com.boots.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class ImageController {

    @GetMapping("/img/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        String imagePath = "D:/SpringProjects/FoodStore/spring-reg-auth-master/src/main/resources/img/";

        Resource imageResource = new FileSystemResource(imagePath + imageName);

        if(imageResource.exists()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageResource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
