package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.BdInteraction;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getAuthInfoWithWrongPassword;

public class LoginTest {
    BdInteraction mySql = new BdInteraction();

    @AfterAll
    static void clean() {
        BdInteraction.cleanDb();
    }

    @Test
    void shouldLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = BdInteraction.getVerificationCode("vasya");
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBeBlockedAfterThreeWrongPasswords() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfoWithWrongPassword();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        var statusSQL = mySql.getStatusFromDb(authInfo.getLogin());
        assertEquals("blocked", statusSQL);
        $(withText("Превышено число попыток введения пароля. Пользователь заблокирован")).shouldBe(Condition.visible);
    }
}