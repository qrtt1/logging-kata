package hello;

import java.security.SecureRandom;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SampleController extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SampleController.class);
    }

    ArrayList<Object> keepMemory = new ArrayList<>();
    SecureRandom random = new SecureRandom();

    @RequestMapping("/")
    @ResponseBody
    String handle(HttpServletRequest request) {
        return "Hello World!";
    }

    @RequestMapping("/keepMemory")
    long keepMemory() {
        keepMemory.add(new byte[1024 * 1024]);
        return keepMemory.size();
    }

    @RequestMapping("/releaseMemory")
    long releaseMemory() {
        keepMemory.clear();
        return keepMemory.size();
    }

    @RequestMapping("/longQuery")
    long longQuery() throws InterruptedException {
        long sleep = Math.abs(random.nextLong()) % (30 * 1000);
        Thread.sleep(sleep);
        return sleep;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}