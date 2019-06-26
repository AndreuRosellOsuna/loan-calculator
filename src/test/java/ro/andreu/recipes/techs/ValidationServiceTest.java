package ro.andreu.recipes.techs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ro.andreu.recipes.techs.model.Lender;
import ro.andreu.recipes.techs.validation.ValidationService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ValidationServiceTest {

    private ValidationService validationService;

    @Before
    public void prepare() {
        validationService = new ValidationService();
    }

    @Test
    public void checkPresenceZeroArgumentsTest() {
        String[] args = {};
        boolean result = validationService.checkPresenceArguments(args);
        assertThat(result).isFalse();
    }

    @Test
    public void checkPresenceOneArgumentsTest() {
        String[] args = {"foo"};
        boolean result = validationService.checkPresenceArguments(args);
        assertThat(result).isFalse();
    }

    @Test
    public void checkPresenceTwoArgumentsTest() {
        String[] args = {"foo", "123"};
        boolean result = validationService.checkPresenceArguments(args);
        assertThat(result).isTrue();
    }

    @Test
    public void checkPresenceThreeArgumentsTest() {
        String[] args = {"foo", "123", "trousers"};
        boolean result = validationService.checkPresenceArguments(args);
        assertThat(result).isFalse();
    }

    @Test
    public void checkAmountNonNumericDecimalsTest() {
        String amount = "yeah";
        boolean result = validationService.checkAmountNumeric(amount);
        assertThat(result).isFalse();
    }

    @Test
    public void checkAmountNumericWithDecimalsTest() {
        String amount = "123.1";
        boolean result = validationService.checkAmountNumeric(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkAmountNumericWithoutDecimalsTest() {
        String amount = "123";
        boolean result = validationService.checkAmountNumeric(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkAmountIncrementWithoutDecimalsOKTest() {
        String amount = "1200";
        boolean result = validationService.checkAmountIncrement(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkAmountIncrementWithoutDecimalsKOTest() {
        String amount = "1230";
        boolean result = validationService.checkAmountIncrement(amount);
        assertThat(result).isFalse();
    }

    @Test
    public void checkAmountIncrementWithDecimalsKOTest() {
        String amount = "1230.12";
        boolean result = validationService.checkAmountIncrement(amount);
        assertThat(result).isFalse();
    }

    @Test
    public void checkMinimumAmountUnderTest() {
        String amount = "999";
        boolean result = validationService.checkMinimumAmount(amount);
        assertThat(result).isFalse();
    }

    @Test
    public void checkMinimumAmountEqualsTest() {
        String amount = "1000";
        boolean result = validationService.checkMinimumAmount(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkMinimumAmountOverTest() {
        String amount = "1001";
        boolean result = validationService.checkMinimumAmount(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkMaximumAmountUnderTest() {
        String amount = "14999";
        boolean result = validationService.checkMaximumAmount(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkMaximumAmountEqualsTest() {
        String amount = "15000";
        boolean result = validationService.checkMaximumAmount(amount);
        assertThat(result).isTrue();
    }

    @Test
    public void checkMaximumAmountOverTest() {
        String amount = "15001";
        boolean result = validationService.checkMaximumAmount(amount);
        assertThat(result).isFalse();
    }

    @Test
    public void checkAmountAvailabilityUnderTest() {
        Float amount = Float.valueOf(80);
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));

        boolean result = validationService.checkAmountAvailability(amount, lenders);
        assertThat(result).isTrue();
    }

    @Test
    public void checkAmountAvailabilityEqTest() {
        Float amount = Float.valueOf(100);
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(50)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(50)));

        boolean result = validationService.checkAmountAvailability(amount, lenders);
        assertThat(result).isTrue();
    }

    @Test
    public void checkAmountAvailabilityOverTest() {
        Float amount = Float.valueOf(110);
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(50)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(50)));

        boolean result = validationService.checkAmountAvailability(amount, lenders);
        assertThat(result).isFalse();
    }
}
