package ro.andreu.recipes.techs.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.andreu.recipes.techs.model.Lender;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * Class defining services to work with lenders from the actual market
 */
@Service
public class MarketService {

    /**
     * From a list of lenders and a loan amount, it provides the low rate possible
     * @param lenders the list of lenders
     * @param loanAmount them loan amount
     * @return the list of selected lower rate lenders and the specific amount of each
     */
    public Map<Lender, Float> getBestLenders(List<Lender> lenders, Float loanAmount) {
        Map<Lender, Float> bestLenders = new HashMap<>();

        if(Objects.isNull(lenders) || Objects.isNull(loanAmount) || lenders.isEmpty() || loanAmount.floatValue() == 0f) {
            return bestLenders;
        }

        lenders.sort(Lender::compareTo);

        Float remainingAmount = Float.valueOf(loanAmount);

        for(Lender lender : lenders) {
            Float available = lender.getAvailable();

            if(available.compareTo(remainingAmount) >= 0) {
                bestLenders.put(lender, remainingAmount);
                remainingAmount = Float.valueOf(0);
                break;
            } else {
                bestLenders.put(lender, available);
                remainingAmount -= available;
            }
        }

        return bestLenders;
    }

    /**
     * From a list of the selected lenders and each loan amount, it gets the average rate
     * @param bestLenders the list of selected lenders
     * @param totalLoanAmount the total loan amount
     * @return the average rate
     */
    public Float getRateFromBestLenders(Map<Lender, Float> bestLenders, Float totalLoanAmount) {

        if(Objects.isNull(bestLenders) || Objects.isNull(totalLoanAmount) || bestLenders.isEmpty() || totalLoanAmount.floatValue() == 0f) {
            return Float.valueOf(0f);
        }

        Float rateTimesTotalLoanAmount = bestLenders.keySet().stream()
                .map(lender -> lender.getRate() * bestLenders.get(lender))
                .reduce(0f, (subtotal, element) -> subtotal + element);

        return rateTimesTotalLoanAmount / totalLoanAmount;
    }
}
