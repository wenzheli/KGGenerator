import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // generate the knowledge graph
        NetworkGenerator generator = new NetworkGenerator();
        generator.run();

        // write into the file
        generator.out2file();
    }
}
