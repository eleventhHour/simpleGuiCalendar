import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Viewer of the current day in Calendar model and the events
 * on the current day.
 */
public class DayView extends JPanel implements ChangeListener {
   @Override
   public void stateChanged(ChangeEvent e) {
      GregorianCalendar cal = server.getCurrentDay();
      String dayNum = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
      int monthNum = cal.get(Calendar.MONTH);
      String monthName = MONTHS.values()[monthNum].toString();
      String tableHeader = monthName + " " + Integer.toString(monthNum+1) + "/" + dayNum ;
      table.getTableHeader().getColumnModel().getColumn(1).setHeaderValue(tableHeader);
      String[] eventTitles = server.getEventsOnDay();
      for(int i = 0; i < eventTitles.length; i++){
         table.setValueAt(eventTitles[i],i,1);
      }

      repaint();

   }

   enum MONTHS
   {
      Jan, Feb, March, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec
   }

   final JTable table;

   CalendarModel server;

   /**
    * Creates the DayView gui and attached itself to the CalendarModel
    * @param cm the CalendarModel to attach DayView to
    */
   public DayView(CalendarModel cm) {

      server = cm;
      server.attach(this); //class is an action listener

      GregorianCalendar cal = server.getCurrentDay();
      String dayNum = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
      int monthNum = cal.get(Calendar.MONTH);
      String monthName = MONTHS.values()[monthNum].toString();
      String tableHeader = monthName + " " + Integer.toString(monthNum) + "/" + dayNum ;

      Object[][] times = new String[48][2];
      Object[] colName = new String[2];
      colName[0] = "";
      colName[1] = tableHeader ;
      for(int i = 0; i < times.length; i++) {
         //On the hour
         if(i%2 == 0){
            times[i][0] = Integer.toString(i/2) + ":00";
         }
         else {
            times[i][0] = Integer.toString(i/2) + ":30" ;
         }
      }

      table = new JTable(times, colName){
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };

      String[] eventTitles = server.getEventsOnDay();
      for(int i = 0; i < eventTitles.length; i++){
         table.setValueAt(eventTitles[i],i,1);
      }



      JScrollPane dayView = new JScrollPane(table);
      dayView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      dayView.setPreferredSize(new Dimension(250, 400));
      dayView.setMinimumSize(new Dimension(10, 10));
      add(dayView);
   }

}
