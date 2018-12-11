import java.io.*;

public class FilesofDirec {

    public String filePath() {
        String operSys = System.getProperty("os.name").toLowerCase();

        if (operSys.startsWith("windows")) {
            return "\\";
        } else {
            return "/";
        }
    }

    public void listFilesAndFilesSubDirectories(String directoryName) throws IOException {

        String FileDir = directoryName + filePath() + "FilesinServer.txt";
        PrintStream WritetoFile = new PrintStream(new File(FileDir));

        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && file.getName().compareToIgnoreCase("FilesinServer.txt") != 0) {
                WritetoFile.println(file.getName());
                new Database_layer(file.getName());
            } else if (file.isDirectory()) {
                WritetoFile.println(file.getAbsolutePath());
            }
        }
        WritetoFile.close();
    }

}