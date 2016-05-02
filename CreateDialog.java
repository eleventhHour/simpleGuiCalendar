import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Pop up dialog box gui that gets user input for an event. Acts as a controller for
 * CalendarModel and acts a listerner.
 */
public class CreateDialog extends JDialog implements ActionListener, PropertyChangeListener, ChangeListener{
   enum MONTHS
   {
      January, February, March, April, May, June, July, August, September, October, November, December
   }
   private String eventNamePrompt = "Enter event title below";
   private String startHourPrompt = "Select start time below";
   private String endHourPrompt = "Select end time below";
   private String enterBtn = "Save";
   private String cancelBtn = "Cancel";

   private String event;
   private String startTime;
   private String endTime;
   private String[] times;

   private JTextField eventTextField;
   private JComboBox startHrTextField;
   private JComboBox endHrTextField;

   private JOptionPane optionPane;
   private GregorianCalendar cal;
   private CalendarModel model;

   /**
    * creates the gui dialog and attaches itself to the CalendarModel
    * @param frame the frame to pop up from
    * @param model the calendarModel to attach to
    */
   public CreateDialog(JFrame frame, CalendarModel model) {
      super(frame, true);
      this.model = model;
      model.attach(this);
      cal = model.getCurrentDay();
      String dayNum = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
      int monthNum = cal.get(Calendar.MONTH);
      String monthName = MONTHS.values()[monthNum].toString();
      String tableHeader = monthName + " " + dayNum ;
      setTitle("Create an event for" + " " + tableHeader);
      times = new String[48];
      //fill the times
      for(int i = 0; i < times.length; i++) {
         //On the hour
         if(i%2 == 0){
            times[i] = Integer.toString(i/2) + ":00";
         }
         else {
            times[i] = Integer.toString(i/2) + ":30" ;
         }
      }

      eventTextField = new JTextField(30);
      startHrTextField = new JComboBox(times);
      endHrTextField = new JComboBox(times);

      Object[] array = {eventNamePrompt, eventTextField,
            startHourPrompt, startHrTextField, endHourPrompt, endHrTextField};
      Object[] btnText = {enterBtn, cancelBtn};

      //create the JOptionPane
      optionPane = new JOptionPane(array,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            null,
            btnText);

      //make this dialog display it
      setContentPane(optionPane);

      //handle window closing correctly
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
            optionPane.setValue(new Integer(
                  JOptionPane.CLOSED_OPTION));
         }
      });

      //Ensure the event text field always gets the first focus.
      addComponentListener(new ComponentAdapter() {
         public void componentShown(ComponentEvent ce) {
            eventTextField.requestFocusInWindow();
         }
      });


      optionPane.addPropertyChangeListener(this);

   }

   /**
    * hide the textbox
    */
   private void clearAndHide() {
      eventTextField.setText(null);
      setVisible(false);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      optionPane.setValue(enterBtn);
   }

   @Override
   public void propertyChange(PropertyChangeEvent e) {
      String prop = e.getPropertyName();

      if (isVisible()  && (e.getSource() == optionPane)  && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
            JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
         Object value = optionPane.getValue();

         if (value == JOptionPane.UNINITIALIZED_VALUE) {
            //ignore reset
            return;
         }

         //Reset the JOptionPane's value.
         //If you don't do this, then if the user
         //presses the same button next time, no
         //property change event will be fired.
         optionPane.setValue( JOptionPane.UNINITIALIZED_VALUE);
         if(enterBtn.equals(value))
         {
            event = eventTextField.getText();
            startTime = (String) startHrTextField.getSelectedItem();
            endTime = (String) endHrTextField.getSelectedItem();
            if(model.addEvent(event, startTime, endTime)) {
               clearAndHide();
            }
            else {
               //text was invalid
               eventTextField.selectAll();
               JOptionPane.showMessageDialog(
                     CreateDialog.this,
                     "Sorry, There is an event already at this time or bad input",
                     "Try again",
                     JOptionPane.ERROR_MESSAGE);
               event = null;
               startTime = null;
               endTime = null;
               eventTextField.requestFocusInWindow();
            }

         }
         else {
            clearAndHide();
         }



      }
   }

   @Override
   public void stateChanged(ChangeEvent e) {
      cal = model.getCurrentDay();
      String dayNum = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
      int monthNum = cal.get(Calendar.MONTH);
      String monthName = MONTHS.values()[monthNum].toString();
      String tableHeader = monthName + " " + dayNum ;
      setTitle("Create an event for" + " " + tableHeader);
   }
}
