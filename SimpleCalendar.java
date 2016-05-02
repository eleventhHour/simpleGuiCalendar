import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Branden on 4/28/2016.
 */
public class SimpleCalendar {



   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      CalendarModel cm = new CalendarModel();
      cm.openFile();
      MonthView mnthVw = new MonthView(cm);

      JPanel mainPanel = new JPanel();
      JPanel northPanel = new JPanel();
      JPanel westPanel = new JPanel();

      //build out the north panel
      //******************************
      JButton prevDay = new JButton();
      prevDay.setText("Previous Day");
      prevDay.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  GregorianCalendar dateToChange = cm.getCurrentDay();
                  dateToChange.add(Calendar.DAY_OF_MONTH, -1);
                  cm.setCurrentDay(dateToChange);
               }
            }
      );

      JButton nextDay = new JButton();
      nextDay.setText("Next Day");
      nextDay.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  GregorianCalendar dateToChange = cm.getCurrentDay();
                  dateToChange.add(Calendar.DAY_OF_MONTH, +1);
                  cm.setCurrentDay(dateToChange);
               }
            }
      );

      JButton quitButton = new JButton();
      quitButton.setText("Quit");
      quitButton.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  cm.saveFile();
                  System.exit(0);
               }
            }
      );

      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
      northPanel.add(prevDay);
      northPanel.add(nextDay);
      northPanel.add(quitButton);
      //******************************

      //finish building the west panel
      //*******************************
      westPanel.setLayout( new BoxLayout(westPanel, BoxLayout.Y_AXIS));
      JButton createButton = new JButton();
      createButton.setText("Create");
      final CreateDialog createDialog = new CreateDialog(frame, cm);
      createDialog.pack();
      createButton.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  createDialog.setLocationRelativeTo(frame);
                  createDialog.setVisible(true);
               }
            }
      );
      westPanel.add(createButton);
      westPanel.add(mnthVw.getPanel());
      //******************************


      //build out the main panel
      //******************************
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(northPanel, BorderLayout.NORTH);
      mainPanel.add(westPanel, BorderLayout.WEST);
      mainPanel.add(new DayView(cm), BorderLayout.CENTER);
      //******************************



      frame.add(mainPanel);
      frame.pack();
      frame.setVisible(true);

   }
   public static void SimpleCalendar(){
      main(new String[0]);
   }
}
