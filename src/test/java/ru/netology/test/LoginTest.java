package ru.netology.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.UserGenerator;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getAuthInfoWithWrongPassword;

public class LoginTest {
    UserGenerator mySql = new UserGenerator();

    @AfterAll
    static void clean() {
        UserGenerator.cleanDb();
    }

    @SneakyThrows
    @Test
    void shouldLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserGenerator.getVerificationCode("vasya");
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

    }
}
