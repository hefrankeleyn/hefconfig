package io.github.hefrankeleyn.hefconfig.demo;

import com.google.common.base.Strings;
import io.github.hefrankeleyn.hefconfig.client.annotation.EnableHefConfigAnnotation;
import io.github.hefrankeleyn.hefconfig.demo.conf.HefConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties(value = {HefConfig.class})
@EnableHefConfigAnnotation
@RestController
public class HefconfigDemoApplication {

	@Value("${hef.a}")
	private String a;
	@Value("${hef.b}")
	private String b;
	@Value("${hef.c}")
	private String c;

	@Resource
	private HefConfig hefConfig;

	@Resource
	private Environment environment;

	@Resource
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(HefconfigDemoApplication.class, args);
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo() {
		Map<String, ApplicationListener> listenerMap = applicationContext.getBeansOfType(ApplicationListener.class);
		System.out.println(listenerMap);
		return Strings.lenientFormat("a: %s, b: %s, c: %s, hefConfig: %s", a, b, c, hefConfig);
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
