package MidTermProject;

import MidTermProject.model.Accounts.BasicAccount;
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

		BasicAccount b = new BasicAccount(null, 323,null,"2022-09-20");
		System.out.println(b);
	}


}
