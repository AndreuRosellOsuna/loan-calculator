package ro.andreu.recipes.techs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.andreu.recipes.techs.calculator.MarketService;
import ro.andreu.recipes.techs.calculator.MathContexts;
import ro.andreu.recipes.techs.calculator.QuoteService;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RunWith(SpringRunner.class)
public class QuoteServiceTest {

    @TestConfiguration
    static class QuoteServiceTestContextConfiguration {

        @Bean
        public QuoteService quote() {
            return new QuoteService();
        }

        @Bean
        public MathContexts math() {
            return new MathContexts();
        }
    }

    @Autowired
    private QuoteService quoteService;

//    @MockBean
//    private MathContexts mathContexts;
//
//    @Before
//    public void prepare() {
//
//        MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
//        Mockito.when(mathContexts.getQuoteScale()).thenReturn(mathContext);
//    }

    @Test
    public void totalRepaymentOneAmountTest() {
        BigDecimal quote = new BigDecimal(1f);
        BigDecimal result = quoteService.calculateTotalRepayment(quote);

        assertThat(result.floatValue()).isEqualTo(36f);
    }

    @Test
    public void totalRepaymentTwoAmountTest() {
        BigDecimal quote = new BigDecimal(2f);
        BigDecimal result = quoteService.calculateTotalRepayment(quote);

        assertThat(result.floatValue()).isEqualTo(72f);
    }

    @Test
    public void totalRepaymentTechTestExampleTest() {
        BigDecimal quote = new BigDecimal(30.78f);
        BigDecimal result = quoteService.calculateTotalRepayment(quote);

        assertThat(result.floatValue()).isEqualTo(1108.08f);
    }

    @Test
    public void quoteTechTestExampleTest() {
        Float loanAmount = Float.valueOf(1000f);
        Float rate = Float.valueOf(0.07f);
        BigDecimal result = quoteService.calculateQuote(loanAmount, rate);

        assertThat(result.floatValue()).isEqualTo(30.78f);
    }

    @Test
    public void quoteFromInternetExampleTest() {
        Float loanAmount = Float.valueOf(2000f);
        Float rate = Float.valueOf(0.09f);
        BigDecimal result = quoteService.calculateQuote(loanAmount, rate);

        assertThat(result.floatValue()).isEqualTo(63.26f);
    }

    @Test
    public void quoteFromInternetExample2Test() {
        Float loanAmount = Float.valueOf(8000f);
        Float rate = Float.valueOf(0.081f);
        BigDecimal result = quoteService.calculateQuote(loanAmount, rate);

        assertThat(result.floatValue()).isEqualTo(249.97f);
    }
}
