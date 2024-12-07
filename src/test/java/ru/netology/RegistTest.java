package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerate.Registration.getUser;
import static ru.netology.DataGenerate.Registration.getValidUser;
import static ru.netology.DataGenerate.randomLogin;
import static ru.netology.DataGenerate.randomPwd;

public class RegistTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void successLoginIfUserValidActive() {
        var validUser = getValidUser("active");
        $("[data-test-id='login'] input").setValue(validUser.getLogin());
        $("[data-test-id='password'] input").setValue(validUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void unsuccessLoginIfUserValidBlocked() {
        var blockedUser = getValidUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    void errorIfInvalidUser() {
        var validUser = getValidUser("active");
        var wrongLogin = randomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(validUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void errorIfWrongLogin() {
        var validUser = getValidUser("active");
        var wrongPassword = randomPwd();
        $("[data-test-id='login'] input").setValue(validUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}
