package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Proxy; // proxy
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
    public static WebDriver getBrowser(String name){
        WebDriver driver = null;
        String browser_mode = ConfigReader.getBrowserMode();

        switch (name.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver()
                        .clearDriverCache()
                        .clearResolutionCache()
                        .setup();

                //THEM PROXY ZAP
                //Proxy proxy = new Proxy();
                //proxy.setHttpProxy("localhost:8081");
                //proxy.setSslProxy("localhost:8081");

                ChromeOptions chromeOptions = new ChromeOptions();

                //GAN PROXY
                //chromeOptions.setProxy(proxy);
                //chromeOptions.setAcceptInsecureCerts(true);
                //chromeOptions.addArguments("--ignore-certificate-errors");

                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");

                // --- CAU HINH BAT BUOC CHO JENKINS DOCKER ---
                chromeOptions.addArguments("--headless=new"); // Chay ngam
                chromeOptions.addArguments("--window-size=1920,1080"); // Ep kich thuoc man hinh Full HD
                chromeOptions.addArguments("--no-sandbox"); // Vuot bao mat sandbox tren Linux
                chromeOptions.addArguments("--disable-dev-shm-usage"); // Tranh loi tran RAM
                chromeOptions.addArguments("--disable-gpu"); // Tat tang toc phan cung

                if(browser_mode != null && browser_mode.equalsIgnoreCase("incognito")) {
                    chromeOptions.addArguments("--incognito");
                }

                // Nap options vao ChromeDriver
                driver = new ChromeDriver(chromeOptions);

                // LUU Y: Khong su dung driver.manage().window().maximize() khi chay Headless

                driver.switchTo().defaultContent();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fireFoxOptions = new FirefoxOptions();

                if(browser_mode != null && browser_mode.equalsIgnoreCase("incognito"))
                    fireFoxOptions.addArguments("--incognito");

                driver = new FirefoxDriver(fireFoxOptions);
                break;

            default:
                throw new RuntimeException("Invalid browser name " + name);
        }
        return driver;
    }
}