package io.github.guoyixing.collusion;

import io.github.guoyixing.collusion.core.annotation.composition.EsCompositionScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 敲代码的旺财
 * @date 2022/6/29 15:58
 */
@SpringBootApplication
@EsCompositionScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
