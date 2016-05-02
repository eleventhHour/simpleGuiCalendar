import java.util.regex.*;
/**
 * Created by branden on 3/12/2016.
 * Parses input for the calendar.
 */
public class CalendarInputParser {

    /**
     * Parses user input for the date and returns the values
     * for the date as an integer array. The year is element 0,
     * the month is element 1, and the day is element 2. If the
     * user input is in valid (wrong characters, date out of range, etc)
     * then the method returns false otherwise the method returns true.
     * Method does not check if date actually exists on calendar,
     * just does parsing and a minimal date checking.
     * @param date the user input to be parsed
     * @param numDate the integer array with the year, month, day
     * @return false if invalid user input, true otherwise
     */
    public static boolean parseDate(String date, int[] numDate) {

        int year = 0;
        int month = 1;
        int day = 2;
        String regex = "[A-Za-z!@#$%&:;-_<> ,\\|\\+\\?\\{\\}\\[\\]\\.\\(\\)\\*\\^]";
        if( regexChecker(regex, date) ){

            return false;
        }
        String[] dateArray = date.split("/");
        if (dateArray.length == 3) {

            numDate[year] = Integer.parseInt(dateArray[2]);
            numDate[month] = Integer.parseInt(dateArray[0]) - 1; //we have to subtract 1 to get a valid month (months go from 0 - 11, not 1 - 12 in gregorian calendar
            numDate[day] = Integer.parseInt(dateArray[1]);
            if( numDate[month] < 1 || numDate[month] > 12 || numDate[day] < 1 || numDate[day] > 31 || numDate[year] < 1 ){
                return false;
            }
            return true;

        } else {
            return false;
        }
    }

    /**
     * Used to find a particular regex pattern. Returns true if the string
     * matches the regex string.
     * @param theRegex the regex string to find
     * @param str2Check the string to check if the regex pattern
     * @return true if pattern found, else false
     */
    private static boolean regexChecker(String theRegex, String str2Check)
    {
        Pattern checkRegex = Pattern.compile(theRegex);
        Matcher regexmatcher = checkRegex.matcher(str2Check);
        while(regexmatcher.find()){
            if(regexmatcher.group().length()!=0){
                return true;
            }
        }
        return false;

    }

    /**
     * Function for parsing user input, if input contains
     * invalid characters or not within an actual time
     * (i.e. more than 24 hours, negative time, or more than 60 minutes)
     * than method returns false.
     *
     * @param time the string to be parsed containing the time
     * @param tme the return value for the time stored in the first and second element of the array
     * @return true if the time could be parsed
     */
    public static boolean parseTime(String time, int[] tme) {
        int hour = 0;
        int min = 1;
        String regex = "[A-Za-z!@#$%&;-_<>,\\|\\+\\?\\{\\}\\[\\]\\.\\(\\)\\*\\^]";
        if( regexChecker(regex, time) ){

            return false;
        }
        String[] parsedTime = time.split(":");
        if (parsedTime.length != 2 || parsedTime[0] == "" || parsedTime[1] == "") return false; //check we didn't get any empty values;

        tme[hour] = Integer.parseInt(parsedTime[hour]);
        tme[min] = Integer.parseInt(parsedTime[min]);
        if (tme[hour] < 0 || tme[hour] > 23 || tme[min] < 0 || tme[min] > 59) {
            tme = null;
            return false;
        }

        return true;
    } //
}
