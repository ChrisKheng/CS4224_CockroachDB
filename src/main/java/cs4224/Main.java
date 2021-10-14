package cs4224;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cs4224.module.BaseModule;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("[START OF PROGRAM]");

        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("[END OF PROGRAM]");
    }

    private static void run(String[] args) throws Exception {
        InputParser parser = new InputParser();
        CommandLine parsedArguments = parser.parse(args);

        if (parsedArguments == null) {
            throw new IllegalArgumentException("Incorrect arguments");
        }

        String logFileName = parsedArguments.hasOption("l") ? parsedArguments.getOptionValue("l") : "";
        setLogFileName(logFileName);

        String keyspace = parsedArguments.getOptionValue("k");
        String ip = parsedArguments.hasOption("i") ? parsedArguments.getOptionValue("i") : "";
        int port = parsedArguments.hasOption("p") ? Integer.parseInt(parsedArguments.getOptionValue("p")) : -1;

        Injector injector = Guice.createInjector(new BaseModule(keyspace, ip, port));
        final Driver driver = injector.getInstance(Driver.class);

        String fileName = parsedArguments.getOptionValue("f");
        driver.runQueries(fileName);
    }

    private static void setLogFileName(String name) {
        String fileName = name;
        if (name.isEmpty()) {
            fileName = "out.log";
        }

        // Reference: // https://newbedev.com/log4j2-assigning-file-appender-filename-at-runtime
        System.setProperty("logFilename", fileName);
        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}
