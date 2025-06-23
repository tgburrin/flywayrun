package net.tgburrin.flywayrun;

import org.flywaydb.core.Flyway;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static String usage() {
        return Main.class.getCanonicalName()+ """
                
                Usage:
                \t-p <properties> (required) - the properties file configuring flyway
                \t-m <dir> (optional) - the directory containing migrations (overwrites the properties value)
                """;
    }
    public static void main(String[] args) throws IOException {
        Hashtable<String, String> parsedArgs = GetOpts.parseOpts("p: m:", args);

        if (parsedArgs.get("opt_p") == null) {
            System.err.println("Missing '-p' properties option");
            System.err.println(usage());
            System.exit(1);
        }

        Properties cfg = new Properties();
        cfg.load(new FileInputStream(parsedArgs.get("opt_p")));

        if ( parsedArgs.get("opt_m") != null && Files.isDirectory(Path.of(parsedArgs.get("opt_m"))) ) {
            cfg.setProperty("flyway.locations", "filesystem:"+parsedArgs.get("opt_m"));
        }

        System.out.println("Checking "+cfg.getProperty("flyway.locations"));

        Flyway f = Flyway.configure().configuration(cfg).validateMigrationNaming(true).load();
        f.migrate();
    }
}