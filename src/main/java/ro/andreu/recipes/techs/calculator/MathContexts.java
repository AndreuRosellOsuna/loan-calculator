package ro.andreu.recipes.techs.calculator;

import org.springframework.stereotype.Component;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Some constants to work with {@link java.math.BigDecimal}
 */
@Component
public class MathContexts {

    private final int OPERATIONS_SCALE = 10;

    private final int QUOTE_SCALE = 2;
    private final RoundingMode QUOTE_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final int REPAYMENT_SCALE = 2;
    private final RoundingMode REPAYMENT_ROUNDING_MODE = RoundingMode.HALF_UP;

    public int getOperationsScale() {
        return OPERATIONS_SCALE;
    }

    public int getQuoteScale() {
        return QUOTE_SCALE;
    }

    public RoundingMode getQuoteRoundingMode() {
        return QUOTE_ROUNDING_MODE;
    }

    public int getRepaymentScale() {
        return REPAYMENT_SCALE;
    }

    public RoundingMode getRepaymentRoundingMode() {
        return REPAYMENT_ROUNDING_MODE;
    }
}
