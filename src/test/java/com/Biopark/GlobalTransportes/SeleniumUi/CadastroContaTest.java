package com.Biopark.GlobalTransportes.SeleniumUi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CadastroContaTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void deveRedirecionarParaCadastroMotorista() {
        driver.get("http://localhost:8080/cadastro");

        WebElement botaoMotorista = driver.findElement(By.linkText("Motoristas"));
        botaoMotorista.click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.endsWith("/cadastro-motorista"), "Deveria redirecionar para /cadastro-motorista");
    }

    @Test
    public void deveRedirecionarParaCadastroCliente() {
        driver.get("http://localhost:8080/cadastro");

        WebElement botaoCliente = driver.findElement(By.linkText("Cliente"));
        botaoCliente.click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.endsWith("/cadastro-cliente"), "Deveria redirecionar para /cadastro-cliente");
    }
}
