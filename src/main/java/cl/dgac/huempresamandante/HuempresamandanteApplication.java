package cl.dgac.huempresamandante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "cl.dgac.huempresamandante")
public class HuempresamandanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuempresamandanteApplication.class, args);
	}

}
