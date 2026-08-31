package com.example.demo;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserE2ETest {

    @Test
    void ユーザー登録ができること() {

        try (Playwright playwright = Playwright.create()) {

            Browser browser =
                    playwright.chromium()
                            .launch(
                                    new BrowserType.LaunchOptions()
                                            .setHeadless(false));

            Page page = browser.newPage();

            //page.navigate("http://localhost:8080/users-page/new");
            page.navigate("http://localhost:8080/user-form");
            page.fill("#name", "山田太郎");

            page.fill("#birthday", "1995-01-01");

            page.fill("#address", "東京都千代田区");

            page.click("button[type='submit']");

            assertTrue(
                    page.content().contains("山田太郎")
            );

            browser.close();
        }

    }

}