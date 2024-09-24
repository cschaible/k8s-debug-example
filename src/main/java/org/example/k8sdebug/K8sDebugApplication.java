package org.example.k8sdebug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class K8sDebugApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sDebugApplication.class, args);
    }
}

@RestController
class DataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @GetMapping("/data")
    String data() {
        return "<data>";
    }

    @GetMapping("/heap")
    String heap() throws InterruptedException {
        var data = IntStream.range(0, 1024 * 1024).map(i -> ThreadLocalRandom.current().nextInt()).toArray();
        sleep(1000);

        LOGGER.trace("Data: {}", Arrays.toString(data));
        return "<heap>";
    }

    @GetMapping("/stack")
    String stack() throws InterruptedException {
        var data = recursion(10000);

        LOGGER.trace("Data: {}", data);
        return "<stack>";
    }

    long recursion(long i) {
        if (i == 0) {
            return i;
        }
        return recursion(i - 1);
    }
}
