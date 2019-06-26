package ro.andreu.recipes.techs.launcher;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import ro.andreu.recipes.techs.LoanCalculationSystem;

/**
 * Hello world!
 *
 */
public class Launcher
{
    public static void main( String[] args )
    {
        SpringApplication app = new SpringApplication(LoanCalculationSystem.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }
}
