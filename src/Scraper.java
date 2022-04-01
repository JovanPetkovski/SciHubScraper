import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

public class Scraper {
    static long download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            return Files.copy(in, Paths.get(fileName),StandardCopyOption.REPLACE_EXISTING);
        } catch(FileNotFoundException ex) {
            System.out.println(fileName);
            return 0;
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path to the text file");
        String pathTo = scanner.nextLine();
        List<String> list = Files.readAllLines(Paths.get(pathTo));
        for (int i = 1; i <= list.size() ; i++) {
            URL url = new URL(list.get(i));
            Scanner sc = new Scanner(url.openStream());
            StringBuffer sb = new StringBuffer();
            while(sc.hasNext())
            {
                sb.append(sc.next());
            }
            String result = sb.toString();
            if(!result.contains("location.href=") || !result.contains("download=true")) continue;
            int begin = result.indexOf("location.href=");
            int end = result.indexOf("download=true");
            String value = result.substring(begin+15,end+13);
            URL downloadURL = new URL("https://sci-hub.se"+value);
            String filename = "result" + i + ".pdf";
            download(downloadURL.toString(),filename);
        }
    }
}
