package com.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoAppApplication {

//	@Autowired
//	private StudentService studentService;

	public static void main(String[] args) {
		SpringApplication.run(DemoAppApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner() {
//		return new CommandLineRunner() {
//			@Override
//			public void run(String... args) throws Exception {
//				Faker faker = new Faker();
//				String firstName = faker.name().firstName();
//				String lastName = faker.name().lastName();
//				Random random = new Random();
//				Student student = new Student(
//						firstName + " " + lastName,
//						firstName + "." + lastName + "@test.com",
//						random.nextInt(21, 89)
//				);
//				studentService.saveStudent(new StudentRegistrationData(student.getName(), student.getEmail(), student.getAge()));
//			}
//		};
//	}

}
