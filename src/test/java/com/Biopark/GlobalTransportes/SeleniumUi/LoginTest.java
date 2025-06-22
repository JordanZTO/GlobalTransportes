package com.Biopark.GlobalTransportes.SeleniumUi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void deveFazerLoginCliente() {
        driver.get("http://localhost:8080/login");

        driver.findElement(By.name("email")).sendKeys("teste@gmail.com");
        driver.findElement(By.name("senha")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.urlContains("/cliente/home"));
        } catch (Exception e) {
            fail("Login falhou: Não foi redirecionado para /cliente/home. Verifique se as credenciais estão corretas.");
        }

        assertTrue(driver.getCurrentUrl().contains("/cliente/home"),
                "Deveria redirecionar para a página /cliente/home após login com sucesso");
    }

    @Test
    public void deveFazerLoginMotorista() {
        driver.get("http://localhost:8080/login");

        driver.findElement(By.name("email")).sendKeys("teste1@gmail.com");
        driver.findElement(By.name("senha")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.urlContains("/motorista/home"));
        } catch (Exception e) {
            fail("Login falhou: Não foi redirecionado para /motorista/home. Verifique se as credenciais estão corretas.");
        }

        assertTrue(driver.getCurrentUrl().contains("/cliente/home"),
                "Deveria redirecionar para a página /motorista/home após login com sucesso");
    }

    @Test
    public void deveIrParaPaginaEsqueciSenha() {
        driver.get("http://localhost:8080/login");

        driver.findElement(By.linkText("Esqueci minha senha!")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.urlContains("/recuperar_senha"));
        } catch (Exception e) {
            fail("Falha ao navegar para a página de recuperação de senha (/recuperar_senha).");
        }

        assertTrue(driver.getCurrentUrl().contains("/recuperar_senha"),
                "Deveria ir para a página /recuperar_senha ao clicar no link 'Esqueci minha senha!'");
    }

    @Test
    public void deveIrParaPaginaCadastro() {
        driver.get("http://localhost:8080/login");

        driver.findElement(By.linkText("Crie sua conta!")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.urlContains("/cadastro"));
        } catch (Exception e) {
            fail("Falha ao navegar para a página de cadastro (/cadastro).");
        }

        assertTrue(driver.getCurrentUrl().contains("/cadastro"),
                "Deveria ir para a página /cadastro ao clicar no link 'Crie sua conta!'");
    }
}
