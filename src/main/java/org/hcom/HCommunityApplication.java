package org.hcom;

import lombok.RequiredArgsConstructor;
import org.hcom.services.article.ArticleService;
import org.mybatis.spring.annotation.MapperScan;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;
import java.util.Scanner;

@MapperScan(basePackageClasses = HCommunityApplication.class)
@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class HCommunityApplication implements CommandLineRunner {

    private final ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(HCommunityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

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
        chromeOptions.addArguments("load-extension=" + System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\bcjindcccaagfpapjjmafapmmgkkhgoa\\0.6.2_0");
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
