package ro.andreu.recipes.techs;

import org.junit.Test;
import ro.andreu.recipes.techs.model.Lender;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class LenderTest
{
    @Test
    public void orderTest() {

        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("lender2", Float.valueOf(2), Float.valueOf(100)));
        lenders.add(new Lender("lender1", Float.valueOf(1), Float.valueOf(100)));
        lenders.add(new Lender("lender4", Float.valueOf(4), Float.valueOf(100)));
        lenders.add(new Lender("lender3", Float.valueOf(3), Float.valueOf(100)));

        lenders.sort(Lender::compareTo);

        assertThat(lenders).extracting("name").isSorted();
    }
}
