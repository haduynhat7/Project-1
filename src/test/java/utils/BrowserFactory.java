package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Proxy;
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
                        .setup();

                // 1. Cấu hình "Trạm thu phí" ZAP
                // Lưu ý: Cổng 8080 thường trùng với Jenkins, nên ZAP nên dùng 8090 hoặc 8081
                //String zapProxyAddress = "localhost:8090";
                //Proxy proxy = new Proxy();
                //proxy.setHttpProxy(zapProxyAddress);
                //proxy.setSslProxy(zapProxyAddress);

                ChromeOptions chromeOptions = new ChromeOptions();

                // 2. Gán Proxy vào trình duyệt
                //chromeOptions.setProxy(proxy);

                // 3. BẮT BUỘC: Cho phép trình duyệt tin tưởng ZAP (vì ZAP sẽ đứng giữa soi gói tin HTTPS)
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.addArguments("--ignore-certificate-errors");

                // --- CÁC CẤU HÌNH CŨ CỦA BẠN (GIỮ NGUYÊN) ---
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");

                if(browser_mode != null && browser_mode.equalsIgnoreCase("incognito")) {
                    chromeOptions.addArguments("--incognito");
                }

                driver = new ChromeDriver(chromeOptions);
                driver.switchTo().defaultContent();
                break;

            case "firefox":
                // Bạn có thể làm tương tự cho Firefox nếu cần
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