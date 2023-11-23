package com.boots;

import com.boots.controller.ImageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTests {
    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;

    @Test
    public void testGetImage_NonExistingImage_ReturnsNotFound() throws Exception {
        String nonExistingImageName = "non_existing_image.png";
        mockMvc = standaloneSetup(imageController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/img/{imageName}", nonExistingImageName))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testGetImage_InvalidImageFormat_ReturnsBadRequest() throws Exception {
        String invalidImageName = "invalid_image.txt";
        mockMvc = standaloneSetup(imageController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/img/{imageName}", invalidImageName))
                .andExpect(MockMvcResultMatchers.status().is(404));

    }
}
