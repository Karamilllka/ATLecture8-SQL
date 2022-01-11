package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;

import static com.codeborne.selenide.Selenide.$;
import static ru.netology.data.UserGenerator.getVerificationCode;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");


    @SneakyThrows
    public DashboardPage validVerify(String verificationCode) {

        codeField.setValue(getVerificationCode("vasya"));
        verifyButton.click();
        return new DashboardPage();
    }
}
