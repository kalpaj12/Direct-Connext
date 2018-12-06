import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {

    public static final int PACKET_SIZE = 128;
    public static final int PORT = 8008;
    public static final String ServerIP = "localhost";

    public void sendFile(SocketChannel socketChannel, File file) {
        RandomAccessFile aFile = null;
        try {
            // System.out.println("File Size " + file.length());
            long FileSizeinBytes = file.length();
            long CurrentProgress = 0;
            aFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int sent = 0;
            while (sent != -1) {
                sent = inChannel.read(buffer);
                if (sent != -1) {
                    CurrentProgress += sent;
                    double val = ((double) CurrentProgress / (double) FileSizeinBytes) * 100;
                    int temp = (int) Math.ceil(val);
                    ClientGUI.progressBar.setValue(temp);
                }
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }

            aFile.close();
            System.out.println(file.getName() + " Uploaded!");

        } catch (Throwable e) {
            System.err.println("From sendFile" + e);
        }

    }

    public void sendFileName(SocketChannel socketChannel, String fileName) {

        try {

            ByteBuffer FileNamebuffer = ByteBuffer.allocate(128);
            FileNamebuffer.clear();
            FileNamebuffer.put(fileName.getBytes());
            FileNamebuffer.flip();
            socketChannel.write(FileNamebuffer);

            FileNamebuffer.clear();

            // System.out.println("File Name Sent " + fileName);

        } catch (Throwable e) {
            System.err.println("From sendFile" + e);
        }

    }

    public static void send(File file) throws UnknownHostException, IOException {

        SocketChannel socketChannel = null;
        Client nioClient = new Client();

        try {

            socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(ServerIP, PORT);
            socketChannel.connect(socketAddress);
        } catch (IOException e) {

        }

        String fileName = file.getName();

        nioClient.sendFileName(socketChannel, fileName);

        try {
            Thread.sleep(25);
        } catch (InterruptedException x) {

        }

        nioClient.sendFile(socketChannel, file);
        socketChannel.close();

    }
}