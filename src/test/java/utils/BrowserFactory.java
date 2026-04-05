package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Proxy; // proxy
import org.openqa.selenium.chrome.ChromeDriver;
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
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");

                //jenkins
                chromeOptions.addArguments("--headless=new"); // Chạy ngầm (dùng chuẩn mới của Chrome)
                chromeOptions.addArguments("--window-size=1920,1080"); // Ép màn hình to bằng Full HD để không bị giấu menu
                chromeOptions.addArguments("--no-sandbox"); // Bỏ qua cơ chế bảo mật sandbox (Rất quan trọng trên Linux)
                chromeOptions.addArguments("--disable-dev-shm-usage"); // Tránh lỗi tràn bộ nhớ (RAM) của Docker
                chromeOptions.addArguments("--disable-gpu"); // Tắt tăng tốc phần cứng

                if(browser_mode.equalsIgnoreCase("incognito")) {
                    chromeOptions.addArguments("--incognito");
                }

                driver = new ChromeDriver(chromeOptions);
                driver.manage().window().maximize();
                driver.switchTo().defaultContent();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fireFoxOptions = new FirefoxOptions();

                if(browser_mode.equalsIgnoreCase("incognito"))
                    fireFoxOptions.addArguments("--incognito");

                driver = new FirefoxDriver(fireFoxOptions);
                break;

            default:
                new RuntimeException("Invalid browser name " + name);
                break;
        }
        return driver;
    }
}