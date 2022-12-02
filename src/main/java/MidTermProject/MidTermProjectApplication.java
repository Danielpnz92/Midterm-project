package MidTermProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class MidTermProjectApplication {

	public static void main(String[] args) {
		Date today = new Date(String.valueOf(LocalDate.now()));
		System.out.println(today);
		SpringApplication.run(MidTermProjectApplication.class, args);
	}


}
