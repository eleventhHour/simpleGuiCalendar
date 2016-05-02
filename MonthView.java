import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * MonthView acts both as viewer and controller of the CalendarModel.
 * Sets the CalendarModel to specific days.
 */
public class MonthView {

   enum MONTHS
   {
      Jan, Feb, March, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec
   }

   enum DAYS
   {
      Sun, Mon, Tue, Wed, Thur, Fri, Sat
   }

   MONTHS[] arrayOfMonths;
   DAYS[] arrayOfDays;

   CalendarModel server;

   JPanel panel;


   private JComponent[] mnthBtns;

   /**
    * Instantiates the month view with the CalendarModel
    * @param cm the CalendarModel to user
    */
   public MonthView(CalendarModel cm) {
      int calendarSize = 49; //so we don't have magic numbers
      mnthBtns = new  JComponent[calendarSize];
      server = cm;
      arrayOfDays = DAYS.values();
      arrayOfMonths = MONTHS.values();
      panel = makePanel();

   }

   /**
    * Returns the month panel to be added to the frame.
    * @return month panel
    */
   public JPanel getPanel() {
      return panel;
   }

   /***
    * Creates the month view gui panel to add to the total calendar
    * @return the panel to create
    */
   private JPanel makePanel(){

      JPanel header = new JPanel();
      JPanel monthPanel = new JPanel();

      //Create Month panel

      //create the day header header
      for(DAYS mth : DAYS.values()) {
         final JLabel lbl = new JLabel();
         lbl.setText(mth.toString());
         mnthBtns[mth.ordinal()] = lbl;
      }

      GregorianCalendar date = server.getCurrentDay();

      int curDayOfMonth = date.get(Calendar.DAY_OF_MONTH); //saves the current day of the month from the server so we can highlight the correct JButton
      int curMonthNum = date.get(Calendar.MONTH); //save the current numbered month for comparison

      //get first day of the month and go from there
      date = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
      DAYS day = arrayOfDays[date.get(Calendar.DAY_OF_WEEK)-1]; //use the enum to get the numbered day of the (0 thru 7)
      date.add(Calendar.DAY_OF_MONTH, -day.ordinal()); //get our starting day

      //create the buttons for the month view and add them to the array
      for(int i=DAYS.Sat.ordinal()+1; i < mnthBtns.length; i++) {
         final JCalButton mt = new JCalButton();
         mnthBtns[i] = mt;
         int dateDayOfMonth = date.get(Calendar.DAY_OF_MONTH);//get the day of the month
         int dateMonthNum = date.get(Calendar.MONTH); //get number of the month for comparison
         mt.setText(String.valueOf(dateDayOfMonth));
         mt.setDate((GregorianCalendar) date.clone());

         //set a different month magenta, and the current day button cyan, otherwise default the button to light gray
         if(curMonthNum != dateMonthNum) {
            mt.setBorder(new LineBorder(Color.lightGray));
            mt.setBackground(Color.MAGENTA);
         }
         else if(dateDayOfMonth == curDayOfMonth){
            mt.setBorder(new LineBorder(Color.black));
            mt.setBackground(Color.cyan);
         }
         else {
            mt.setBorder(new LineBorder(Color.lightGray));
            mt.setBackground(Color.white);
         }


         final int temp = i;
         mt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               server.setCurrentDay(mt.getDate());
            }


         });

         date.add(Calendar.DAY_OF_MONTH, 1); //add a day
      }

      monthPanel.setLayout(new GridLayout(7, 6));
      for(JComponent jb : mnthBtns) {
         monthPanel.add(jb);
      }

      //create the month header plus calendar
      JLabel monthLabel = new JLabel();
      GregorianCalendar cal = server.getCurrentDay();
      String year = Integer.toString(cal.get(Calendar.YEAR));
      int index = cal.get(Calendar.MONTH);
      String monthName = MONTHS.values()[index].toString();

      monthLabel.setText(monthName + " " + year);
      header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
      header.add(monthLabel);
      header.add(monthPanel);

      server.attach(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            //update the header
            int index = server.getCurrentDay().get(Calendar.MONTH);
            String year = Integer.toString(server.getCurrentDay().get(Calendar.YEAR));
            String monthName = MONTHS.values()[index].toString();
            monthLabel.setText(monthName + " " + year);

            //update the buttons
            GregorianCalendar date = server.getCurrentDay();

            //get first day of the month and go from there
            date = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
            DAYS day = arrayOfDays[date.get(Calendar.DAY_OF_WEEK)-1]; //use the enum to get the numbered day of the (0 thru 7)
            date.add(Calendar.DAY_OF_MONTH, -day.ordinal()); //get our starting day

            //create the buttons for the month view and add them to the array
            for(int i=DAYS.Sat.ordinal()+1; i < mnthBtns.length; i++) {
               int dateDayOfMonth = date.get(Calendar.DAY_OF_MONTH);//get the day of the month
               ((JCalButton) mnthBtns[i]).setText(String.valueOf(dateDayOfMonth));
               ((JCalButton) mnthBtns[i]).setDate((GregorianCalendar) date.clone());
               //if different month, button should be magenta
               if(((JCalButton) mnthBtns[i] ).getDate().get(Calendar.MONTH) != server.getCurrentDay().get(Calendar.MONTH)) {
                  mnthBtns[i].setBorder(new LineBorder(Color.lightGray));
                  mnthBtns[i].setBackground(Color.MAGENTA);
               }
               //current day is cyan
               else if(((JCalButton) mnthBtns[i] ).getDate().get(Calendar.DAY_OF_MONTH) == server.getCurrentDay().get(Calendar.DAY_OF_MONTH)){
                  mnthBtns[i].setBorder(new LineBorder(Color.black));
                  mnthBtns[i].setBackground(Color.cyan);
               }
               //everything else grey
               else {
                  mnthBtns[i].setBorder(new LineBorder(Color.lightGray));
                  mnthBtns[i].setBackground(Color.white);
               }
               date.add(Calendar.DAY_OF_MONTH, 1); //add a day
            }
         }
      });

      header.setMaximumSize(new Dimension(250, 145));
      return  header;
   }


   /**
    * custom button to hold day information. Allows changing the CalendarMode
    * to different days.
    */
   class JCalButton extends JButton {
      GregorianCalendar date;
      public void setDate(GregorianCalendar date) {
         this.date = date;
      }

      public GregorianCalendar getDate() {
         return (GregorianCalendar) date.clone();
      }
   }

}
