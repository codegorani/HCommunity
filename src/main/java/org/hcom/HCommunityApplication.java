package org.hcom;

import lombok.RequiredArgsConstructor;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.request.UserSaveRequestDTO;
import org.hcom.models.user.support.UserRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@EnableSwagger2
@EnableBatchProcessing
@MapperScan(basePackageClasses = HCommunityApplication.class)
@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class HCommunityApplication implements CommandLineRunner {

    private final ResourceLoader resourceLoader;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(HCommunityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = makeTempUser(2);
        userRepository.saveAll(userList);
//        doSeleniumTest();
    }

    public List<User> makeTempUser(int count) {
        List<User> returnList = new ArrayList<>();
        for(int i = 1; i <= count; i ++) {
            UserSaveRequestDTO requestDTO = UserSaveRequestDTO.builder()
                    .username("hjhearts" + i)
                    .password("asd5689")
                    .birth("1995-10-29")
                    .firstName("주성")
                    .lastName("한")
                    .nickname("하위" + i)
                    .phoneNum("01022171844")
                    .address1("경기도 오산시 123-45")
                    .email("none@none.com")
                    .build();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            requestDTO.setCryptPassword(passwordEncoder);
            returnList.add(requestDTO.toEntity());
            System.out.println("hjhearts" + i + " / " + "asd5689    created");
        }
        return returnList;
    }

    public void doSeleniumTest() throws Exception {
        System.out.println("===============System Started=================");

        Resource resource;

        File file = new File(System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Last Version");
        Scanner scanner = new Scanner(file);
        String version = scanner.nextLine();
        int versionMajor = Integer.parseInt(version.split("\\.")[0]);

        if(versionMajor == 103) {
            resource = resourceLoader.getResource("classpath:selenium/chromedriver13.exe");
        } else if(versionMajor == 104) {
            resource = resourceLoader.getResource("classpath:selenium/chromedriver14.exe");
        } else {
            resource = null;
        }
        assert resource != null;
        String WEB_DRIVER_PATH = resource.getURI().getPath();
        System.out.println(WEB_DRIVER_PATH);
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--log-level=3");
        chromeOptions.addArguments("load-extension=" + System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\bcjindcccaagfpapjjmafapmmgkkhgoa\\0.6.3_0");
        chromeOptions.setCapability("ignoreProtectedModeSettings",true);

        WebDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get("http://localhost:8080/signup");
        webDriver.findElement(By.id("btnSave")).click();

        webDriver.get("http://localhost:8080/login");
        webDriver.findElement(By.id("username")).sendKeys("hjhearts");
        webDriver.findElement(By.id("password")).sendKeys("asd5689");
        webDriver.findElement(By.id("btn-login")).click();

        webDriver.get("http://localhost:8080/api/v1/user/hjhearts");
    }
}
