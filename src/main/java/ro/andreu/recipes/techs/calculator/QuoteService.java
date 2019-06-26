package ro.andreu.recipes.techs.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class QuoteService {

    @Autowired
    private MathContexts mathContexts;

    private final int NUMBER_OF_QUOTES = 36;

    public BigDecimal calculateQuote(Float loanAmountFloatParam, Float rateFloatParam) {
        BigDecimal quote = null;

        // The amount of debt over doing monthly interests calculation
        BigDecimal monthlyInitialDebt = new BigDecimal(loanAmountFloatParam);

        // The amount of rate over a month (provided rates are over a year)
        BigDecimal monthlyRate = new BigDecimal(rateFloatParam).divide(new BigDecimal(12), mathContexts.getOperationsScale(), RoundingMode.HALF_UP);

        // The amount to pay over the debt, not the total
        BigDecimal monthlyCancelationQuote = monthlyInitialDebt.divide(new BigDecimal(NUMBER_OF_QUOTES), mathContexts.getOperationsScale(), RoundingMode.HALF_UP);

        // The total amount payed in all quotes
        BigDecimal totalRepayment = BigDecimal.ZERO;

        for(int i = 0; i < NUMBER_OF_QUOTES; i++) {
            // Interesest over actual debt
            BigDecimal monthlyInterestsAmount = monthlyInitialDebt.multiply(monthlyRate);

            // Just amount to cancel out the debt plus interests over actual month
            BigDecimal estimatedMonthlyTotalQuote = monthlyCancelationQuote.add(monthlyInterestsAmount);

            totalRepayment = totalRepayment.add(estimatedMonthlyTotalQuote);
            monthlyInitialDebt = monthlyInitialDebt.subtract(monthlyCancelationQuote);
        }

        quote = totalRepayment.divide(new BigDecimal(NUMBER_OF_QUOTES))
                    .setScale(mathContexts.getQuoteScale(), mathContexts.getQuoteRoundingMode());

        return quote;
    }

    public BigDecimal calculateTotalRepayment(BigDecimal quote) {
        return quote.multiply(new BigDecimal(NUMBER_OF_QUOTES)).setScale(mathContexts.getRepaymentScale(), mathContexts.getRepaymentRoundingMode());
    }
}
