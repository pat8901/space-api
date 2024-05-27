package com.patco.space_api.routecontroller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.patco.space_api.apicontroller.ApiController;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
// @RequestMapping("/api")
public class RouteController {

    @RequestMapping("/")
    public String getFirstGreeting() {
        return "Hello world!";
    }

    @RequestMapping("/greeting")
    public String getSecondGreeting() {
        return "New York City";
    }

    @RequestMapping("/garbage")
    public ResponseEntity<Resource> getImage() throws Exception {
        Path path = Paths.get("src\\main\\resources\\static\\images\\enceladus.jpg");
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @RequestMapping("/resources/nasa")
    public String getNasa() {
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("NASA_API_KEY");
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + key;
        RestTemplate restTemplate = new RestTemplate();
        ApiController apiResponse = restTemplate.getForObject(url, ApiController.class);

        if (apiResponse == null) {
            return null;
        }

        System.out.println("My key: " + key);
        return apiResponse.getUrl();
    }

    @Controller
    public class WebController {

        @GetMapping("/image")
        public String getPage(Model model) {
            String imageUrl = getNasa();
            System.out.println("Fetched Image URL: " + imageUrl);
            model.addAttribute("imageUrl", imageUrl);
            return "image";
        }
    }

}
