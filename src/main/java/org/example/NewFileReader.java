package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NewFileReader {

    private static final String filePath = "db.txt";

    public static void main(String[] args) throws IOException {
//        String line = getRecordById(999999);
//        System.out.println("line found is: " + line);
        deleteRecordById(1000001);
    }

    private static boolean addRecord(int id, String record){
        File file = new File(filePath);
        FileWriter fr = null;
        try {
            // Below constructor argument decides whether to append or override
            fr = new FileWriter(file, true);
            fr.write(id + "," + record);
            fr.write("\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fr != null;
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getRecordById(int id){
        String line = "";
        Path file = Paths.get(filePath);
        Supplier<Stream<String>> linesSupplier = () -> {
            try {
                return Files.lines(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        int linesCount = (int) linesSupplier.get().count();

        int lowerBound = 1;
        int upperBound = linesCount;
        int middleLineCount;

        int hitCounter = 0;

        Instant b = Instant.now();

        while (upperBound >= lowerBound){
            Instant start = Instant.now();

            middleLineCount = lowerBound + ((upperBound - lowerBound) / 2);
            Optional optionalLine = linesSupplier.get().skip(middleLineCount - 1).findFirst();
            Stream lineStream = optionalLine.stream();

            String middleLine = optionalLine.get().toString();
            int middleLineId = Integer.parseInt(middleLine.split(",")[0]);

            hitCounter++;

            Instant end = Instant.now();
//            System.out.println(
//                    "Line: "  + middleLine + "\n"
//                    + "Time Taken: " + Duration.between(start, end).toMillis() + " \n"
//            );
            if (id > middleLineId){
                lowerBound = middleLineCount + 1;
            }
            else if (id < middleLineId ){
                upperBound = middleLineCount - 1;
            }
            else{
                // id found
                line = middleLine;
                break;
            }
        }

        Instant e = Instant.now();
        System.out.println(
                "Total Time Taken: " + Duration.between(b, e).toMillis() + "\n"
                + "Total Hits    : " + hitCounter
        );
        return line;
    }

    private static boolean deleteRecordById(int id) throws IOException {

        Path path = Path.of("db.txt");
        List<String> lines = Files.readAllLines(path);

        int linesCount = lines.size();
        int lowerBound = 1;
        int upperBound = linesCount;
        int middleLineCount;

        Instant b = Instant.now();

        while (upperBound >= lowerBound){

            middleLineCount = lowerBound + ((upperBound - lowerBound) / 2);
            String middleLine  = lines.get(middleLineCount);
            int middleLineId = Integer.parseInt(middleLine.split(",")[0]);

            if (id > middleLineId){
                lowerBound = middleLineCount + 1;
            }
            else if (id < middleLineId ){
                upperBound = middleLineCount - 1;
            }
            else{
                // id found
                lines.remove(middleLineCount);
                Files.write(path, lines);
                System.out.println(middleLine);
                break;
            }
        }
        System.out.println("time taken: " + Duration.between(b, Instant.now()).toMillis());
        return false;
    }

    private static boolean updateRecordById(int id, String record) {
        return true;
    }
}