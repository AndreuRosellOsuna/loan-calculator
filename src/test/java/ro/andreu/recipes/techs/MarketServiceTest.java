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
import ro.andreu.recipes.techs.model.Lender;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
public class MarketServiceTest
{
    @TestConfiguration
    static class CalculatorServiceTestContextConfiguration {

        @Bean
        public MarketService service() {
            return new MarketService();
        }
    }

    @Autowired
    private MarketService service;

    @Test
    public void nullLendersTest() {

        List<Lender> lenders = null;
        Float loanAmount = Float.valueOf(1);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);
        assertThat(best).isEmpty();
    }

    @Test
    public void noLendersTest() {

        List<Lender> lenders = new ArrayList<>();
        Float loanAmount = Float.valueOf(1);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);
        assertThat(best).isEmpty();
    }

    @Test
    public void nullAmountTest() {

        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));

        Map<Lender, Float> best = service.getBestLenders(lenders, null);
        assertThat(best).isEmpty();
    }

    @Test
    public void noAmountTest() {

        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(0);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);
        assertThat(best).isEmpty();
    }

    @Test
    public void oneLenderAmountAvailableMaximumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(100);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(1);

        assertThat(best.values()).contains(Float.valueOf(100f));
    }

    @Test
    public void oneLenderAmountAvailableMinimumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(1);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(1);

        assertThat(best.values()).contains(Float.valueOf(1f));
    }

    @Test
    public void oneLenderAmountAvailableMediumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(10);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(1);

        assertThat(best.values()).contains(Float.valueOf(10f));
    }

    @Test
    public void twoLendersAmountAvailableMinimumButMaximumFirstLoanTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(100);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(1);

        List<Lender> lender1 = best.keySet().stream().filter(lender -> "lender1".equals(lender.getName())).collect(Collectors.toList());
        assertThat(lender1).isEmpty();

        Lender lender2 = best.keySet().stream().filter(lender -> "lender2".equals(lender.getName())).collect(Collectors.toList()).get(0);
        Float lender2Amount = best.get(lender2);
        assertThat(lender2Amount).isEqualTo(100);
    }

    @Test
    public void twoLendersAmountAvailableMinimumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(50);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(1);

        List<Lender> lender1 = best.keySet().stream().filter(lender -> "lender1".equals(lender.getName())).collect(Collectors.toList());
        assertThat(lender1).isEmpty();

        Lender lender2 = best.keySet().stream().filter(lender -> "lender2".equals(lender.getName())).collect(Collectors.toList()).get(0);
        Float lender2Amount = best.get(lender2);
        assertThat(lender2Amount).isEqualTo(50);
    }

    @Test
    public void twoLendersAmountAvailableMediumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(150);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(2);

        Lender lender1 = best.keySet().stream().filter(lender -> "lender1".equals(lender.getName())).collect(Collectors.toList()).get(0);
        Lender lender2 = best.keySet().stream().filter(lender -> "lender2".equals(lender.getName())).collect(Collectors.toList()).get(0);

        Float lender1Amount = best.get(lender1);
        Float lender2Amount = best.get(lender2);

        assertThat(lender1Amount).isEqualTo(50f);
        assertThat(lender2Amount).isEqualTo(100f);
    }

    @Test
    public void twoLendersAmountAvailableMaximumTest() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)));
        lenders.add(new Lender("lender2", Float.valueOf(1), Float.valueOf(100)));
        Float loanAmount = Float.valueOf(200f);

        Map<Lender, Float> best = service.getBestLenders(lenders, loanAmount);

        assertThat(best.size()).isEqualTo(2);

        Lender lender1 = best.keySet().stream().filter(lender -> "lender1".equals(lender.getName())).collect(Collectors.toList()).get(0);
        Lender lender2 = best.keySet().stream().filter(lender -> "lender2".equals(lender.getName())).collect(Collectors.toList()).get(0);

        Float lender1Amount = best.get(lender1);
        Float lender2Amount = best.get(lender2);

        assertThat(lender1Amount).isEqualTo(100f);
        assertThat(lender2Amount).isEqualTo(100f);
    }

    @Test
    public void getSimpleRateTest() {
        Map<Lender, Float> bestLenders = new HashMap<>();
        bestLenders.put(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)), Float.valueOf(100));

        Float totalLoanAmount = Float.valueOf(100f);

        Float result = service.getRateFromBestLenders(bestLenders, totalLoanAmount);
        assertThat(result.floatValue()).isEqualTo(2f);
    }

    @Test
    public void getRateFromTwoEqualLendersTest() {
        Map<Lender, Float> bestLenders = new HashMap<>();
        bestLenders.put(new Lender("lender1", Float.valueOf(2), Float.valueOf(100)), Float.valueOf(50));
        bestLenders.put(new Lender("lender2", Float.valueOf(2), Float.valueOf(100)), Float.valueOf(50));

        Float totalLoanAmount = Float.valueOf(100f);

        Float result = service.getRateFromBestLenders(bestLenders, totalLoanAmount);
        assertThat(result.floatValue()).isEqualTo(2f);
    }

    @Test
    public void getRateFromTwoEqualAmountLendersTest() {
        Map<Lender, Float> bestLenders = new HashMap<>();
        bestLenders.put(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)), Float.valueOf(100));
        bestLenders.put(new Lender("lender2", Float.valueOf(2), Float.valueOf(100)), Float.valueOf(100));

        Float totalLoanAmount = Float.valueOf(200f);

        Float result = service.getRateFromBestLenders(bestLenders, totalLoanAmount);
        assertThat(result.floatValue()).isEqualTo(1.5f);
    }

    @Test
    public void getRateFromTwoEqualRateLendersTest() {
        Map<Lender, Float> bestLenders = new HashMap<>();
        bestLenders.put(new Lender("lender1", Float.valueOf(0.1f), Float.valueOf(100)), Float.valueOf(100));
        bestLenders.put(new Lender("lender2", Float.valueOf(0.1f), Float.valueOf(10)), Float.valueOf(10));

        Float totalLoanAmount = Float.valueOf(110f);

        Float result = service.getRateFromBestLenders(bestLenders, totalLoanAmount);
        assertThat(result.floatValue()).isEqualTo(0.1f);
    }

    @Test
    public void getRateFromTwoDiffLendersTest() {
        Map<Lender, Float> bestLenders = new HashMap<>();
        bestLenders.put(new Lender("lender1", Float.valueOf(0.1f), Float.valueOf(200)), Float.valueOf(200));
        bestLenders.put(new Lender("lender2", Float.valueOf(0.3f), Float.valueOf(50)), Float.valueOf(50));

        Float totalLoanAmount = Float.valueOf(250f);

        Float result = service.getRateFromBestLenders(bestLenders, totalLoanAmount);
        assertThat(result.floatValue()).isEqualTo(0.14f);
    }
}
