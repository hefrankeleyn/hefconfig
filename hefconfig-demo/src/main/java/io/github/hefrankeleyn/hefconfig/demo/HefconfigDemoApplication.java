package io.github.hefrankeleyn.hefconfig.demo;

import io.github.hefrankeleyn.hefconfig.client.annotation.EnableHefConfigAnnotation;
import io.github.hefrankeleyn.hefconfig.demo.conf.HefConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties(value = {HefConfig.class})
@EnableHefConfigAnnotation
public class HefconfigDemoApplication {

	@Value("${hef.a}")
	private String a;

	@Resource
	private HefConfig hefConfig;

	@Resource
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(HefconfigDemoApplication.class, args);
	}

	@Bean
	public ApplicationRunner demoRunTest() {
		return (p)->{
			String c = environment.getProperty("hef.c");
			System.out.println(c);
			System.out.println(a);
			System.out.println(hefConfig.getB());
		};
	}
}
