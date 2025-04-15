import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to server");
            while (true) {
                System.out.println("Enter the command to upload a file or 'exit' to quit:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                if (input.startsWith("UPLOAD ")) {
                    String fileName = input.split(" ")[1];
                    File file = new File(fileName);
                    if (!file.exists() || (!fileName.endsWith(".txt") && !fileName.endsWith(".jpg") && !fileName.endsWith(".png") && !fileName.endsWith(".mp4"))) {
                        System.out.println("Invalid file.");
                    } else {
                        Thread uploadThread = new Thread(new FileUploadTask(file));
                        uploadThread.start();
                    }
                } else {
                    System.out.println("Invalid command. Please use the format 'UPLOAD <filename>' or type 'exit' to quit.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class FileUploadTask implements Runnable {
        private final File file;

        public FileUploadTask(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(HTTPServer.Localhost, HTTPServer.PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                OutputStream outputStream = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(file);
                out.println("UPLOAD " + file.getName());
                out.flush();
                byte[] buffer = new byte[HTTPServer.CHUNK_SIZE];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                System.out.println("File " + file.getName() + " uploaded successfully.");
                fis.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Error uploading file: " + file.getName());
                e.printStackTrace();
            }
        }
    }
}
