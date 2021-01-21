package test;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TimerTest extends JFrame{
    JLabel timerLabel = null;
    JLabel secondLabel = null;
    public TimerTest()
    {
        this.setTitle("Timer Test");
        Container c = this.getContentPane();
        c.setLayout(new FlowLayout());
        timerLabel = new JLabel("0");
        timerLabel.setFont(new Font("Gothic",Font.ITALIC,80));
        
        secondLabel = new JLabel("0");
        secondLabel.setFont(new Font("Gothic",Font.ITALIC,80));
        
        c.add(timerLabel);
        c.add(secondLabel);
        
        this.setSize(300,150);
        this.setVisible(true);
        
        (new SecondThread(secondLabel)).start();
        
        int k = 0;
        
        while(true)
        {
            timerLabel.setText(""+k);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            k++;
        }
    }
    public static void main(String[] args) {
        new TimerTest();
    }
}
 


