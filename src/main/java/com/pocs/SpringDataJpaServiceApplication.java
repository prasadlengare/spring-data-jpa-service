package com.pocs;

import com.github.javafaker.Faker;
import com.pocs.entity.Student;
import com.pocs.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringDataJpaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaServiceApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        List<Student> studentList = new ArrayList<>();
        return args -> {
            studentList.add(new Student("Prasad", "Lengare", "prasad.lengare@gmail.com", 30));
            studentList.add(new Student("Alice", "Johnson", "alice.johnson@example.com", 25));
            studentList.add(new Student("Bob", "Smith", "bob.smith@example.com", 28));
            studentList.add(new Student("Eva", "Williams", "eva.williams@example.com", 22));
            studentList.add(new Student("Michael", "Davis", "michael.davis@example.com", 35));
            studentRepository.saveAll(studentList);

            //find by id
            studentRepository.findById(2L).ifPresentOrElse(
                    System.out::println, () -> System.out.println("Student not found with id : 2"));

            studentRepository.findById(11L).ifPresentOrElse(
                    System.out::println, () -> System.out.println("Student not found with id : 7"));

            System.out.println("Delete Student with id : " + 4);
            studentRepository.deleteById(4L);

            System.out.println("Number of Students : " + studentRepository.count());

            System.out.println("Select all Students : ");
            List<Student> stdList = studentRepository.findAll();
            stdList.forEach(System.out::println);

			System.out.println("Find Student by Email Id");
			studentRepository
					.findStudentByEmail("prasad.lengare@gmail.com")
					.ifPresentOrElse(
							System.out::println,
							() -> System.out.println("Student does not found "));

            System.out.println("Find Student using JPQL by Id : "+2);
            studentRepository
                    .findStudentByJPQL(2L)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Student does not found "));

            System.out.println("Find Student using native Query by Id : "+3);
            studentRepository
                    .findStudentByNativeQuery(3L)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Student does not found "));

            System.out.println("Find Student using named param by first name : Michael");
            studentRepository
                    .findStudentByNamedParameter("Michael")
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Student does not found "));

            System.out.println("Deleting Student by Id : "+5L);
            System.out.println(studentRepository.deleteStudentById(5L));
        };
    }

    @Bean
    @Primary
    CommandLineRunner commandLineRunnerWithFaker(StudentRepository studentRepository) {
        return args -> {
            generateRandomStudents(studentRepository);
            System.out.println("First Name by Ascending......");
            Sort sortAsc = Sort.by(Sort.Direction.ASC, "firstName");
            studentRepository.findAll(sortAsc).forEach(student -> System.out.println(student.getFirstName()));

            System.out.println("Last Name by Descending......");
            Sort sortDesc = Sort.by(Sort.Direction.DESC, "lastName");
            studentRepository.findAll(sortDesc).forEach(student -> System.out.println(student.getLastName()));
        };
    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i <= 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName.toLowerCase(), lastName.toLowerCase());
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(18, 50));
            studentRepository.save(student);
        }
    }
}
