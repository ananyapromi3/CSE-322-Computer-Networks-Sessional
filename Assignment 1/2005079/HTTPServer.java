import java.io.*;
import java.net.*;
import java.util.Date;

public class HTTPServer {
    static final int PORT = 5079;
    static PrintStream originalOut = System.out;
    static final String Localhost = "127.0.0.1";
    static final int CHUNK_SIZE = 4096;
    private static final String UPLOAD_DIRECTORY = "upload/";
    PrintStream out;

    public HTTPServer(PrintStream out) {
        this.out = out;
    }

    public void handleRequest(Socket socket) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream());
            OutputStream os = socket.getOutputStream();
            String line;
            String path = null;
            String fileName = null;
            String req = null;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                out.println("HTTP Request:");
                out.println(line);
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                    req = "GET";
                    break;
                } else if (line.startsWith("UPLOAD")) {
                    fileName = line.split(" ")[1];
                    req = "UPLOAD";
                    String finalFileName = fileName;
                    handleFileUpload(socket, finalFileName, pr, os);
                    break;
                }
            }
            if (req != null && req.equals("GET")) {
                if (path == null) {
                    error404(pr);
                    return;
                }
                File file = new File("." + path);
                if (file.isDirectory()) {
                    listDirectory(file, pr);
                } else if (file.isFile()) {
                    processFile(file, pr, os);
                } else {
                    error404(pr);
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void error404(PrintWriter pr) throws IOException {
        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        String response = "HTTP/1.0 404 NOT FOUND\r\n" +
                "Server: Java HTTP Server: 1.0\r\n" +
                "Date: " + new Date() + "\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                body;
        pr.write(response);
        pr.flush();
        out.println("Response:");
        out.println(response);
        out.println("\n");
    }

    private void listDirectory(File dir, PrintWriter pr) throws IOException {
        StringBuilder content = new StringBuilder("<html><body><h1>Directory Listing</h1><ul>");
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                content.append("<li><b><i><a href=\"http://").append(Localhost).append(":").append(PORT).append("/").append(file.getPath()).append("\">").append(file.getName()).append("</a></i></b></li>");
            } else {
                content.append("<li><a href=\"http://").append(Localhost).append(":").append(PORT).append("/").append(file.getPath()).append("\" target=\"_blank\">").append(file.getName()).append("</a></li>");
            }
        }
        content.append("</ul></body></html>");
        String str = content.toString();
        String response = "HTTP/1.0 200 OK\r\n" +
                "Server: Java HTTP Server: 1.0\r\n" +
                "Date: " + new Date() + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + str.length() + "\r\n" +
                "\r\n" + str;
        pr.write(response);
        pr.flush();
        out.println("Response:");
        out.println(response);
        out.println("\n");
    }

    private void processFile(File file, PrintWriter pr, OutputStream os) throws IOException {
        String mimeType = getMimeType(file);
        String r1 = "HTTP/1.0 200 OK\r\n" +
                "Server: Java HTTP Server: 1.0\r\n" +
                "Date: " + new Date() + "\r\n" +
                "Content-Type: " + mimeType + "\r\n";
        String r2;
        if (mimeType.startsWith("text/") || mimeType.startsWith("image/")) {
            r2 = "Content-Disposition: inline\r\n";
        } else {
            r2 = "Content-Disposition: attachment; filename=\"" + file.getName() + "\"\r\n";
        }
        String response = r1 + r2 + "\r\n";
        pr.write(response);
        pr.flush();
        out.println("Response:");
        out.println(response);
        out.println("\n");
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMimeType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".apng")) {
            return "image/apng";
        } else if (fileName.endsWith(".avif")) {
            return "image/avif";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "text/javascript";
        } else {
            return "application/octet-stream";
        }
    }

    private void handleFileUpload(Socket socket, String fileName, PrintWriter pr, OutputStream out) throws IOException {
        if (fileName == null) {
            originalOut.println("Invalid request");
            return;
        }
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File file = new File(uploadDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            originalOut.println("File " + fileName + " uploaded successfully.");
        } catch (IOException e) {
            originalOut.println("Error uploading file: " + fileName);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        HTTPServer httpServer = new HTTPServer(new PrintStream(new FileOutputStream("log.txt")));
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        httpServer.handleRequest(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.out.close();
    }
}
