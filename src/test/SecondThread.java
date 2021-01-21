package test;

import java.awt.*;

import javax.swing.*;
 
class SecondThread extends Thread
{
    int y = 1;
    JLabel myLabel = null;
    public SecondThread(JLabel myLabel)
    {
        this.myLabel = myLabel;
 
    }
    public void run()
    {
        while(true)
        {
            myLabel.setText(""+y);
            try {
                Thread.sleep(330);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            y++;
        }
                
    }
}


