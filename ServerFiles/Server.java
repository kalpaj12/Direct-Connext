import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.*;
import java.nio.charset.Charset;

public class Server {

    public static final int PORT = 8008;
    public static final int INTEGER_SIZE = 4;
    public static final int LONG_SIZE = 8;

    static Socket socket;
    static String storagePath;
    static boolean closeServer = false;
    static boolean bFoundinServer = false;

    static BufferedInputStream inputStream;
    static FileOutputStream outputStream;

    public String Slash_type() {
        String operSys = System.getProperty("os.name").toLowerCase();

        if (operSys.startsWith("windows")) {
            return "\\";
        } else {
            return "/";
        }
    }

    public String filename(String fullpath) {
        String temp = Slash_type();
        int lastindexof_Slash = fullpath.lastIndexOf(temp);
        String Filename = fullpath.substring(lastindexof_Slash + 1, fullpath.length());
        // System.out.println("Filename from method is:" + Filename);
        return Filename;
    }

    public void readFileFromSocket(SocketChannel socketChannel, String fullPath) {
        RandomAccessFile aFile = null;

        Database_layer db = new Database_layer();
        if (!db.FoundinServer(filename(fullPath))) {
            try {
                // System.out.println(fullPath);
                aFile = new RandomAccessFile(fullPath, "rw");

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                FileChannel fileChannel = aFile.getChannel();
                while (socketChannel.read(buffer) != -1) {
                    buffer.flip();
                    fileChannel.write(buffer);
                    buffer.clear();
                }

                fileChannel.close();

                System.out.println("File Recieved");
                bFoundinServer = false;

            } catch (Throwable e) {
                System.err.println("File Transfer: " + e);
            }
        } else {
            System.out.println("File already exist in the database");
            bFoundinServer = true;
        }

    }

    public static void listen() throws IOException {

        ServerSocketChannel serverSocketChannel = null;
        // ServerSocket serverSocket = null;
        SocketChannel socketChannel = null;
        Server nioServer;

        String fullPath = "";
        String fileName = "";

        nioServer = new Server();

        while (true) {

            try {

                try {
                    serverSocketChannel = ServerSocketChannel.open();
                    serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
                    socketChannel = serverSocketChannel.accept();

                } catch (IOException e) {
                    System.out.println("From serverSocketChannel" + e);
                }

                ByteBuffer bb = ByteBuffer.allocate(128);

                socketChannel.read(bb);
                bb.flip();
                CharBuffer c = Charset.forName("ISO-8859-1").decode(bb);

                bb.clear();

                fileName = c.toString();
                // System.out.println("FileName excpicit" + fileName);

                String operSys = System.getProperty("os.name").toLowerCase();

                if (operSys.startsWith("windows")) {
                    fullPath = storagePath + "\\" + fileName;
                } else {
                    fullPath = storagePath + "/" + fileName;
                }

                nioServer.readFileFromSocket(socketChannel, fullPath);

                // System.out.println("\n------------------------------");
                // System.out.println("Files in Server");
                if (!bFoundinServer) {
                    try {
                        FilesofDirec FilesofDirec = new FilesofDirec();
                        FilesofDirec.listFilesAndFilesSubDirectories(storagePath);
                    } catch (IOException e) {
                    }
                }
                // System.out.println("------------------------------\n");

                serverSocketChannel.close();

            } catch (Throwable e) {
                System.out.println("From outer catch " + e);
            }
        }

    }

    public static void close() {
        closeServer = true;
    }

    public static void setPath(String sp) {
        storagePath = sp;
    }
}