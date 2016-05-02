import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import java.util.*;

/**
 * Acts as the model in the MVC pattern. Holds event info
 * and current day.
 */
public class CalendarModel{


   private ArrayList<ChangeListener> listeners;
   private GregorianCalendar curSelectedDay;
   private ArrayList<Event> eventList;

   /**
    * Instantiates the CalendarModel with the current system time
    * and an empty calendar list.
    */
   public CalendarModel(){

      listeners = new ArrayList<>();
      curSelectedDay = new GregorianCalendar();
      curSelectedDay.set(Calendar.HOUR_OF_DAY, 0);
      curSelectedDay.set(Calendar.MINUTE, 0);
      eventList = new ArrayList<>();
   }

   /**
    * method returns the event names in a string array corresponding
    * the half hour in the day (48 half hours in a day)
    * @return String array with event titles
    */
   public String[] getEventsOnDay() {
      String[] eventNames = new String[48];
      for (String str : eventNames) {
         str = "";
      }
      int currentDayOfYear = curSelectedDay.get(Calendar.DAY_OF_YEAR);
      int j = 0;
      for (int i = 0; i < eventList.size(); i++) {
         int dayOfYearList = eventList.get(i).startTime.get(Calendar.DAY_OF_YEAR);
         int startTime = eventList.get(i).startTime.get(Calendar.HOUR_OF_DAY);
         int startMin = eventList.get(i).startTime.get(Calendar.MINUTE);
         int endTime = eventList.get(i).endTime.get(Calendar.HOUR_OF_DAY);
         int endMin = eventList.get(i).endTime.get(Calendar.MINUTE);
         if (currentDayOfYear == dayOfYearList) {
            j = startTime * 2 + startMin / 30;
            for (; j <= (endTime * 2 + endMin / 30); j++) {
               eventNames[j] = eventList.get(i).eventTitle;
            }
         }

      }
      return eventNames;
   }

   /**
    * method for attaching listners to the CalendarModel
    * @param cl the changeListener to attach
    */
   public void attach(ChangeListener cl) {
      listeners.add(cl);

   }

   /**
    * changes the current day the CalendarModel is on
    * @param calDay the day to set the CalendarModel to
    */
   public void setCurrentDay(GregorianCalendar calDay) {
      curSelectedDay = calDay;
      notifyListeners();
   }

   /**
    * returns a copy of the current day as a Gregorian Calendar object
    * @return current day the CalendarModel is on
    */
   public GregorianCalendar getCurrentDay() {
      return (GregorianCalendar) curSelectedDay.clone();
   }

   /**
    * Adds event using strings based on the current day the
    * CalendarModel is on. Parses the strings for the start time
    * and end time and retains the event. Will add event if there
    * is no time conflict. If added returns true else false
    * @param eventName name/title of the event
    * @param startTime time in HH:MM 24Hr format
    * @param endTime time in HH:MM 24Hr format
    * @return true or false depending if the event is added or not
    */
   public boolean addEvent(String eventName, String startTime, String endTime) {
      int hr = 0;
      int min = 1;
      int[] parseTimeStart = new int[2];
      int[] parseTimeEnd = new int[2];
      if (!CalendarInputParser.parseTime(startTime, parseTimeStart)) {
         return false;
      } else if (!CalendarInputParser.parseTime(endTime, parseTimeEnd)) {
         return false;
      } else {
         GregorianCalendar start = getCurrentDay();
         start.set(Calendar.HOUR_OF_DAY, parseTimeStart[hr]);
         start.set(Calendar.MINUTE, parseTimeStart[min]);

         GregorianCalendar end = getCurrentDay();
         end.set(Calendar.HOUR_OF_DAY, parseTimeEnd[hr]);
         end.set(Calendar.MINUTE, parseTimeEnd[min]);
         Event event = new Event(eventName, start, end);

         if (noTimeConflict(event)) {
            eventList.add(event);
            Collections.sort(eventList);
            notifyListeners();
            return true;
         } else {
            return false;
         }
      }
   }


   private void notifyListeners() {
      for (ChangeListener l : listeners) {
         l.stateChanged(new ChangeEvent(this));
      }
   }

   /**
    * returns whether there is a time conflict, Assumes we can only add days
    *
    * @param eventToAdd
    * @return
    */
   private boolean noTimeConflict(Event eventToAdd) {
      long startTimeNew = eventToAdd.startTime.getTimeInMillis();
      long endTimeNew = eventToAdd.endTime.getTimeInMillis();
      for (int i = 0; i < eventList.size(); i++) {
         GregorianCalendar curCal = eventList.get(i).startTime;
         long startTimeListItem = curCal.getTimeInMillis();
         long endTimeListItem = eventList.get(i).endTime.getTimeInMillis();
         if (startTimeListItem <= startTimeNew && startTimeNew <= endTimeListItem) {
            return false;
         } else if (startTimeListItem <= endTimeNew && endTimeNew <= endTimeListItem) {
            return false;
         }
      }
      return true;
   }

   /**
    * opens an events.txt with saved events
    */
   public void openFile() {
      String dataFile = "events.txt";
      File input = new File(dataFile);
      if (!input.exists()) {
         try {
            input.createNewFile();
         } catch (IOException e) {
            e.printStackTrace();
         }
      } else {
         try {
            Scanner in = new Scanner(input);
            while(in.hasNext()){
               String line = in.nextLine();
               String[] calVals = line.split(",");
               String eventTitle = calVals[0];
               int year = Integer.parseInt(calVals[1]);
               int month = Integer.parseInt(calVals[2]);
               int dayOfMonth = Integer.parseInt(calVals[3]);
               int startHr = Integer.parseInt(calVals[4]);
               int startMin = Integer.parseInt(calVals[5]);
               int endHr = Integer.parseInt(calVals[6]);
               int endMin = Integer.parseInt(calVals[7]);
               GregorianCalendar startTime = new GregorianCalendar(year, month, dayOfMonth, startHr, startMin);
               GregorianCalendar endTime = new GregorianCalendar(year, month, dayOfMonth, endHr, endMin);
               addEvent(new Event(eventTitle, startTime, endTime));
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * Adds an event to the eventList. Used for openfile()
    * @param e the event to be added
    */
   private void addEvent(Event e) {
      if(noTimeConflict(e)){
         eventList.add(e);
         Collections.sort(eventList);
         notifyListeners();
      }
   }

   /**
    * used to save the events on the eventList to an events.txt file
    */
   public void saveFile() {

      String dataFile = "events.txt";
      File input = new File(dataFile);
      if (!input.exists()) {
         try {
            input.createNewFile();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      try {

         PrintWriter out = new PrintWriter(dataFile);
         for(Event e: eventList) {
            String toPrint = e.eventTitle + "," + e.startTime.get(Calendar.YEAR) +
                  "," + e.startTime.get(Calendar.MONTH) + "," + e.startTime.get(Calendar.DAY_OF_MONTH) +
                  "," + e.startTime.get(Calendar.HOUR_OF_DAY) + "," +
                  e.startTime.get(Calendar.MINUTE) + "," + e.endTime.get(Calendar.HOUR_OF_DAY) +
                  "," + e.endTime.get(Calendar.MINUTE);
            out.println(toPrint);
         }
         out.close();

      } catch (IOException e) {
         e.printStackTrace();
      }


   }

   /**
    * Internal helper class to hold event title and the start and end time
    * using Gregorian Calendar.
    */
   class Event implements Comparable<Event>{
      String eventTitle;
      GregorianCalendar startTime;
      GregorianCalendar endTime;

      public Event(String eventTitle, GregorianCalendar startTime, GregorianCalendar endTime) {
         this.eventTitle = eventTitle;
         this.startTime = startTime;
         this.endTime = endTime;
      }

      public int compareTo(Event other) {
         return this.startTime.compareTo(other.startTime);
      }
   }


}
