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
import com.patco.space_api.apicontroller.MarsRover;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
// @RequestMapping("/api")
public class RouteController {

    @RequestMapping("/")
    public String getFirstGreeting() {
        return "Hello world!";
    }

    @RequestMapping("/garbage")
    public ResponseEntity<Resource> getImage() throws Exception {
        Path path = Paths.get("src\\main\\resources\\static\\images\\enceladus.jpg");
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @RequestMapping("/resources/nasa/mars/rover")
    public String getRoverData() {
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("NASA_API_KEY");
        String url = "https://api.nasa.gov/insight_weather/?api_key=" + key + "&feedtype=json&ver=1.0";
        RestTemplate restTemplate = new RestTemplate();
        MarsRover marsRoverResponse = restTemplate.getForObject(url, MarsRover.class);

        if (marsRoverResponse == null) {
            return null;
        }

        return marsRoverResponse.getSomething();
    }

    @RequestMapping("/resources/nasa/image")
    public String getNasa() {
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("NASA_API_KEY");
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + key;
        RestTemplate restTemplate = new RestTemplate();
        ApiController apiResponse = restTemplate.getForObject(url, ApiController.class);

        if (apiResponse == null) {
            return null;
        }

        return apiResponse.getUrl();
    }

    @Controller
    public class WebController {

        @GetMapping("/home")
        public String getHomePage(Model model) {
            return "home";
        }

        @GetMapping("/image")
        public String getSpaceImagePage(Model model) {
            String imageUrl = getNasa();
            model.addAttribute("imageUrl", imageUrl);
            return "image";
        }
    }

}
