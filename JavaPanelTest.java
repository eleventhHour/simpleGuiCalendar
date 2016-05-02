import javax.swing.*;

/**
 * Created by Branden on 4/30/2016.
 */
public class JavaPanelTest extends JPanel {
   public JavaPanelTest(){
      JPanel panel = new JPanel();
      JTextArea textArea = new JTextArea();
      textArea.setText("Hello, this is some text, hopefully I will see it");
      panel.add(textArea);
      add(panel);
   }

}
