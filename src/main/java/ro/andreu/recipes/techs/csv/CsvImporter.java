package ro.andreu.recipes.techs.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ro.andreu.recipes.techs.LoanCalculationSystem;
import ro.andreu.recipes.techs.model.Lender;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CsvImporter {

    public List<Lender> getLendersFromFile(String marketFile) throws FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(marketFile))
                .withType(Lender.class).build().parse();
    }
}
