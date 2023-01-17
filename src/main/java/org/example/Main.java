package org.example;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    private static char dataType = 's';
    private static char operationType;
    private static String dataSent;
    private static String dataReceived;
    private static byte[] dataSentInBytes;

    private static DataOutputStream out;
    private static DataInputStream in;

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket;
        socket = new Socket("localhost", 4999);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        in = new DataInputStream(bufferedInputStream);
        out = new DataOutputStream(socket.getOutputStream());

        requestToConnect();
        String dataReceived = readInputStream();
        System.out.println(dataReceived);

        selectOperation();
//        insertOperation();
//        deleteOperation();

        // end database connection
        endConnection();

        Thread.sleep(100);

        dataReceived = readInputStream();
        System.out.println(dataReceived);

        Thread.sleep(999);

    }

    private static String readInputStream() throws IOException {

        char resContentType = in.readChar();
        int resContentLength = in.readInt();
        int resStatusCode = in.readInt();

        byte[] messageByte = new byte[resContentLength];
        boolean end = false;
        StringBuilder dataString = new StringBuilder(resContentLength);
        int totalBytesRead = 0;

        while(!end) {
            int currentBytesRead = in.read(messageByte);
            totalBytesRead = currentBytesRead + totalBytesRead;
            if(totalBytesRead <= resContentLength) {
                dataString.append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
            } else {
                dataString.append(
                        new String(
                                messageByte,
                                0,
                                resContentLength - totalBytesRead + currentBytesRead,
                                StandardCharsets.UTF_8
                        )
                );
            }
            if(dataString.length()>=resContentLength) {
                end = true;
            }
        }
        return dataString.toString();
    }

    private static void requestToConnect() throws IOException {
        operationType = 'c'; // c for connect
        dataSent = "{" +
            "\"username\":\"admin\", " +
            "\"password\": \"123\"," +
        "}";
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }

    private static void selectOperation() throws IOException {
        operationType = 's'; // s for select
        dataSent = "" +
            "{" +
                "\"database\": \"test\"," +
                "\"collection\": \"test\", " +
                "\"payload\": { " +
                    "\"firstName\":\"test1\", " +
//                    "\"status\": {"+
//                        "\"$lte\": \"I\"" +
//                    "}, " +
//                    "\"$OR\" : [" +
//                        "{" +
//                            "\"status\": \"I\"," +
//                        "}, " +
//                        "{" +
//                            "\"status\": \"A\"," +
//                        "}" +
//                    "], " +
                    " \"_id\": \"b14f0654-847a-4d39-a67d-c9d6d09c6aa7\" " +
                "}" +
            "}";
        {

        }
//        dataSent = "" +
//                "{" +
//                "\"database\": \"test\"," +
//                "\"collection\": \"test2\", " +
//                "\"payload\": { " +
//                    "\"firstName\":\"Mahdy\", " +
//                    "\"lastName\":\"Hamad\", " +
//                "}" +
//                "}";
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }

    private static void insertOperation() throws IOException {
        operationType = 'i'; // i for insert
        dataSent = "" +
            "{" +
                " \"type\": \"document\", " +
                " \"database\": \"test\"," +
                " \"collection\": \"test2\", " +
                " \"payload\": { " +
                    "\"firstName\":\"test3\", " +
                    "\"status\": \"A\"," +
                    "\"test_field\": \"mahdy-hamad-test\", " +
                "}" +
            "}";
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }

    private static void updateOperation() throws IOException{
        operationType = 'u'; // u for insert
        dataSent = "" +
            "{" +
                " \"type\": \"document\", " +
                " \"database\": \"test2\"," +
                " \"collection\": \"test\", " +
                " \"payload\": { " +
                    "\"query\": { " +
                        "\"firstName\":\"test3\", " +
                    "}, " +
                    "\"values\": { " +
                        "\"firstName\":\"test3\", " +
                    "}, " +
                "}" +
            "}";
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }

//    {
//        "type": "document",
//        "database": "db-name",
//        "collection": "collection-name",
//        "payload": {...}
//    }

    private static void deleteOperation() throws IOException {
        operationType = 'd'; // s for select
        dataSent = "" +
            "{" +
                " \"type\": \"document\", " +
                " \"collection\": \"test2\", " +
                " \"payload\": { " +
                    " \"_id\":\"e7d8d3ad-ea10-4c68-8320-404ea6509385\", " +
                "}" +
            "}";
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }

    private static void endConnection() throws IOException {
        // send end-connection
        dataSentInBytes = dataSent.getBytes(StandardCharsets.UTF_8);
        operationType = 'e';
        out.writeChar(dataType);
        out.writeChar(operationType);
        out.writeInt(dataSentInBytes.length);
        out.write(dataSentInBytes);
        out.flush();
    }
}