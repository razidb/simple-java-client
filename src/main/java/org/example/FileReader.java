package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class FileReader {
    public static void main(String[] args) {
        deleteRecord(2);
//        for (int i=1; i <= 100 ; i++)
//            addRecord(" test");
//            addRecord(i + " test");
    }
    public static void addRecord(String record){
        try {
            File file = new File("db.txt");
            RandomAccessFile access = new RandomAccessFile(file, "rw");

            if (false){
                byte[] init = ByteBuffer.allocate(4).putInt(0).array();
                access.write(init);
                access.write(init);
                access.close();
                return;
            }

            byte[] bytes = new byte[4];
            access.read(bytes, 0, 4);
            int recordsCount = ByteBuffer.wrap(bytes).getInt();
            access.read(bytes, 0, 4);
            int lastId = ByteBuffer.wrap(bytes).getInt();

            System.out.println(recordsCount);
            System.out.println(lastId);

            recordsCount += 1;
            lastId += 1;
            access.seek(0);
            access.writeInt(recordsCount);
            access.writeInt(lastId);

            int n = (int) file.length();

            access.seek(n);
            byte[] recordDataBytes = record.getBytes();
            int recordLength = recordDataBytes.length;
            byte[] recordIdBytes = ByteBuffer.allocate(4).putInt(lastId).array();
            byte[] recordLengthBytes = ByteBuffer.allocate(4).putInt(recordLength).array();

//            access.writeBytes(System.getProperty("line.separator"));
            // [record ID][record data in bytes]
            access.write(recordIdBytes);
            access.write(recordLengthBytes);
            access.write(recordDataBytes);
            access.write("EOR".getBytes());

            access.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void goToStartLine(RandomAccessFile access) throws IOException {
        int currentPointer = (int) access.getFilePointer();
        while (true){
            byte b = (byte) access.read();
            if (b == '\n'){
                break;
            }
            else {
                currentPointer -= 1;
                access.seek(currentPointer);
            }
        }
    }
    public static void goToStartRecord(RandomAccessFile access) throws IOException {
        int currentPointer = (int) access.getFilePointer();
        while (true){
            byte[] d = new byte[3];
            access.read(d);
            String r = new String(d);
            if (Objects.equals(r, "EOR")){
                break;
            }
            else {
                currentPointer -= 1;
                access.seek(currentPointer);
            }
        }
    }

    public static void deleteRecord(int recordId){
        try{
            File file = new File("db.txt");
            RandomAccessFile access = new RandomAccessFile(file, "rw");

            // get total size of records in bytes (file size - header size)
            int fileLength = (int) access.length();
            int headersLength = access.readLine().getBytes().length;
            int recordsLength = fileLength - headersLength;

            access.seek(0);

            byte[] bytes = new byte[4];
            access.read(bytes);
            int recordsCount = ByteBuffer.wrap(bytes).getInt();
            access.read(bytes);
            int lastId = ByteBuffer.wrap(bytes).getInt();

            System.out.println("Records Count: " + recordsCount);
            System.out.println("Last Record ID: " + lastId);

            System.out.println("File length: " + fileLength);
            System.out.println("headers length: " + headersLength);
            System.out.println("Records length: " + recordsLength);

            int lowerBound = headersLength;
            int upperBound = fileLength;

            int currentRecordId;
            int currentRecordLength;
            boolean found = false;

            // perform binary search
            while (upperBound - lowerBound > 1){
                // get the middle record
                int middleRecord = lowerBound + ((upperBound - lowerBound) / 2);

                access.seek(middleRecord);
                // get to the beginning of the line
//                goToStartLine(access);
                goToStartRecord(access);

                access.read(bytes, 0, 4);
                currentRecordId = ByteBuffer.wrap(bytes).getInt();


                int currentPointer = (int) access.getFilePointer();
                System.out.println("current pointer: " + currentPointer);
                System.out.println("current record: " + currentRecordId);

                if (recordId > currentRecordId){
                    lowerBound = middleRecord;
                    System.out.println("greater");
                    goToStartRecord(access);
                }
                else if (recordId < currentRecordId){
                    upperBound = middleRecord;
                    System.out.println("smaller");
                    access.read(bytes, 0, 4);
                    currentRecordLength = ByteBuffer.wrap(bytes).getInt();
                    access.skipBytes(currentRecordLength);
                }
                else{
                    found = true;
                    break;
                }


            }

            if (!found)
                System.out.println("Not found");
            else{
                System.out.println("Found Record");
            }

            // look for the id of the record, and perform binary search

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void listRecords(){
        try{
            File file = new File("db.txt");
            RandomAccessFile access = new RandomAccessFile(file, "rw");

            // get total size of records in bytes (file size - header size)
            int fileLength = (int) access.length();
            int headersLength = access.readLine().getBytes().length;
            int recordsLength = fileLength - headersLength;

            access.seek(0);

            byte[] bytes = new byte[4];
            access.read(bytes);
            int recordsCount = ByteBuffer.wrap(bytes).getInt();
            access.read(bytes);
            int lastId = ByteBuffer.wrap(bytes).getInt();

            System.out.println("Records Count: " + recordsCount);
            System.out.println("Last Record ID: " + lastId);

            System.out.println("File length: " + fileLength);
            System.out.println("headers length: " + headersLength);
            System.out.println("Records length: " + recordsLength);

            for (int i=1; i<recordsCount; i++){

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
