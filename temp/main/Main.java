import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
   private static String REGEX = "(a*b)(?:foo)?";
   private static String INPUT = "summer && winter";
   private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
   private static final String expr = "(&&|\\|\\|)";
   
   public static void main(String[] args) {
	   System.out.println(String.format(WITH_DELIMITER, expr));
	   System.out.println("ssss");
	   System.out.println(Arrays.toString(INPUT.split(String.format(WITH_DELIMITER, expr))));
   }
}