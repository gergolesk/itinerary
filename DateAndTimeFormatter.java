import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateAndTimeFormatter {

    public static int numberOfChanges = 0;

    public static String formatDateTime(String input) {
        // Regular expression to extract text before date/time and format itself
        Pattern pattern = Pattern.compile("(D|T12|T24)\\((.+?)\\)");
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String formatType = matcher.group(1);    // Format type (D, T12, T24)
            String dateTimeString = matcher.group(2); // Date/Time in line

            // correct the long dash instead of the minus and replace "Z" with "+00:00"
            dateTimeString = dateTimeString.replace("−", "-").replace("Z", "+00:00");

            String replacement;
            try {
                // Parse the string as ZonedDateTime
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString);
                String zoneOutput = zonedDateTime.getOffset().getId();

                // Time zone processing
                if (zoneOutput.equals("Z")) {
                    zoneOutput = "+00:00"; // Заменяем "Z" на "+00:00"
                } else if (!zoneOutput.startsWith("-") && !zoneOutput.startsWith("+")) {
                    zoneOutput = "+" + zoneOutput; // Добавляем "+" к положительным смещениям
                }

                switch (formatType) {
                    case "D":
                        // Formatting date to "DD MMM YYYY"
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
                        replacement = zonedDateTime.format(dateFormatter);
                        numberOfChanges++;
                        break;

                    case "T12":
                        // Formatting time in 12 hour format with time zone
                        DateTimeFormatter time12Formatter = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH);
                        replacement = zonedDateTime.format(time12Formatter) + " (" + zoneOutput + ")";
                        numberOfChanges++;
                        break;

                    case "T24":
                        // Formatting time in 24 hour format with time zone
                        DateTimeFormatter time24Formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                        replacement = zonedDateTime.format(time24Formatter) + " (" + zoneOutput + ")";
                        numberOfChanges++;
                        break;

                    default:
                        replacement = matcher.group(0); // If the format is not supported, leave it as is
                        break;
                }

            } catch (DateTimeParseException e) {
                replacement = matcher.group(0); // Leave unchanged if date is incorrect
            }

            matcher.appendReplacement(result, replacement); // Replace the found format
        }

        matcher.appendTail(result); // Add the rest of the line
        return result.toString();
    }

}
