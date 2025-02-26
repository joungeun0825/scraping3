package scraping.gookmin;

public class Decoder {
    public static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(str, 16);
            output.append((char) decimal);
        }
        return output.toString();
    }
}
