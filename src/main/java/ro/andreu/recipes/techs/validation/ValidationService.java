package ro.andreu.recipes.techs.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.andreu.recipes.techs.model.Lender;

import java.text.NumberFormat;
import java.util.List;

@Service
public class ValidationService {

    Logger logger = LoggerFactory.getLogger("LCS_CONSOLE");

    public void validateArguments(String[] args) {
        // Check existance of 2 arguments
        if(!checkPresenceArguments(args)) {
            logger.error("Incorrect number of arguments");
            System.exit(1);
        }

        String marketFileCommandArgument = args[0];
        String loanAmountCommandArgument = args[1];

        // Amount is numeric
        if(!checkAmountNumeric(loanAmountCommandArgument)) {
            logger.error("The amount of loan introduced {} is not numeric", loanAmountCommandArgument);
            System.exit(1);
        }

        // Check amount of 100 increment
        if(!checkAmountIncrement(loanAmountCommandArgument)) {
            logger.error("The amount of loan introduced {} is not any of 100 increment", loanAmountCommandArgument);
            System.exit(1);
        }

        // Check minimum loan amount
        if(!checkMinimumAmount(loanAmountCommandArgument)) {
            logger.error("The amount of loan introduced {} must be at least 1000", loanAmountCommandArgument);
            System.exit(1);
        }

        // Check maximum loan amount
        if(!checkMaximumAmount(loanAmountCommandArgument)) {
            logger.error("The amount of loan introduced {} must be maximum 15000", loanAmountCommandArgument);
            System.exit(1);
        }
    }

    public boolean checkPresenceArguments(String[] args) {
        return args.length == 2;
    }

    public boolean checkAmountNumeric(String loanAmountCommandArgument) {
        try {
            Float amount = Float.valueOf(loanAmountCommandArgument);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkAmountIncrement(String loanAmountCommandArgument) {
        Float amount = Float.valueOf(loanAmountCommandArgument);
        float remainder = amount % 100f;
        return remainder == 0f;
    }

    public boolean checkMinimumAmount(String loanAmountCommandArgument) {
        Float amount = Float.valueOf(loanAmountCommandArgument);
        return amount.compareTo(1000f) >= 0;
    }

    public boolean checkMaximumAmount(String loanAmountCommandArgument) {
        Float amount = Float.valueOf(loanAmountCommandArgument);
        return amount.compareTo(15000f) < 1;
    }

    public boolean checkAmountAvailability(Float loanAmount, List<Lender> lenders) {
        Float totalAvailable = lenders.stream().map(lender -> lender.getAvailable())
                .reduce(0f, (subtotal, element) -> subtotal + element);
        return totalAvailable.compareTo(loanAmount) >= 0;
    }
}
