import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourceReader {

    public static List<String> getSampleNames() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("names.txt"));
        String line = br.readLine();
        String[] names = line.split("、");

        return Arrays.asList(names);
    }

}
