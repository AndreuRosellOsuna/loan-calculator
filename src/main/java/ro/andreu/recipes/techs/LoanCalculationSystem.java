package ro.andreu.recipes.techs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ro.andreu.recipes.techs.calculator.MarketService;
import ro.andreu.recipes.techs.calculator.MathContexts;
import ro.andreu.recipes.techs.calculator.QuoteService;
import ro.andreu.recipes.techs.csv.CsvImporter;
import ro.andreu.recipes.techs.model.Lender;
import ro.andreu.recipes.techs.validation.ValidationService;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@SpringBootApplication
public class LoanCalculationSystem implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger("LCS_CONSOLE");

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CsvImporter csvImporter;

    @Autowired
    private MarketService marketService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private MathContexts mathContexts;

    @Override
    public void run(String... args) throws Exception {

        validationService.validateArguments(args);

        String marketFileCommandArgument = args[0];
        String loanAmountCommandArgument = args[1];

        Float loanAmount = Float.valueOf(loanAmountCommandArgument);
        List<Lender> lenders = null;
        Map<Lender, Float> bestLendersMap = null;
        Float rate = null;
        BigDecimal quote = null;
        BigDecimal totalRepayment = null;

        try {
            lenders = csvImporter.getLendersFromFile(marketFileCommandArgument);
        } catch (FileNotFoundException e) {
            logger.error("The market file ile {} is not found", marketFileCommandArgument);
            System.exit(1);
        }

        if(Objects.nonNull(lenders)) {

            // Check amounts are enough
            if(!validationService.checkAmountAvailability(loanAmount, lenders)) {
                logger.error("It is not possible to provide such quote at this time");
                System.exit(1);
            }

            bestLendersMap = marketService.getBestLenders(lenders, loanAmount);
        } else {
            logger.error("Some error occurred while loading lenders from market file {}", marketFileCommandArgument);
            System.exit(1);
        }

        if(Objects.nonNull(bestLendersMap)) {
            rate = marketService.getRateFromBestLenders(bestLendersMap, loanAmount);
        } else {
            logger.error("Some error occurred while computing best lenders from market file {}", marketFileCommandArgument);
            System.exit(1);
        }

        if(Objects.nonNull(rate)) {
            quote = quoteService.calculateQuote(loanAmount, rate);
        } else {
            logger.error("Some error occurred while computing rate from best lenders");
            System.exit(1);
        }

        if(Objects.nonNull(quote)) {
            totalRepayment = quoteService.calculateTotalRepayment(quote);
        } else {
            logger.error("Some error occurred while computing monthly quotes from an amount of {} and a rate of {}", loanAmount, rate);
            System.exit(1);
        }

        if(!Objects.nonNull(totalRepayment)) {
            logger.error("Some error occurred while computing total repayment from a monthly quote of {}", quote);
            System.exit(1);
        }

        logger.info("Requested amount: £{}", loanAmountCommandArgument);
        logger.info("Rate: {}%", formatRate(rate));
        logger.info("Monthly repayment: £{}", quote);
        logger.info("Total repayment: £{}", totalRepayment);
    }

    private String formatRate(Float rateParam) {
        BigDecimal rate = new BigDecimal(rateParam).multiply(new BigDecimal(100)).setScale(1, mathContexts.getQuoteRoundingMode());
        return rate.toString();
    }

    private String formatRepayment(BigDecimal repayment) {
        repayment.setScale(2, mathContexts.getQuoteRoundingMode());
        return repayment.toString();
    }
}
