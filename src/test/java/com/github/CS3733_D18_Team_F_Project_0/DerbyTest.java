package com.github.CS3733_D18_Team_F_Project_0;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DerbyTest {
    @Test
    public void dummyTest() {
        // make sure to 'reset' the test database
        try {
            Files.walk(Paths.get("temp/TestDB"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatabaseHandler dbHandler = new DatabaseHandler("temp/TestDB");
        DummyGraph graph = new DummyGraph();

        dbHandler.trackAndInitItem(graph);


        dbHandler.disconnectFromDatabase();
    }
}
