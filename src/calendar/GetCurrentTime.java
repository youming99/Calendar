package calendar;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;

public class GetCurrentTime extends Thread {
	String format_time = null;
	String format_time2 = null;

	JLabel l_currentTime = null;
	JLabel l_currentTime2 = null;

	public GetCurrentTime(JLabel l_currentTime, JLabel l_currentTime2) {
		this.l_currentTime = l_currentTime;
		this.l_currentTime2 = l_currentTime2;
	}
	
	@Override
	public void run() {
		while(true) {
			SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일");
			SimpleDateFormat format2 = new SimpleDateFormat ( "HH시mm분ss초");
			
			Calendar cal = Calendar.getInstance();
			format_time = format.format(cal.getTime());
			format_time2 = format2.format(cal.getTime());
			
			
			System.out.println(format_time);
			System.out.println(format_time2);
			//l_currentTime.setText(format_time);
			//l_currentTime2.setText(format_time2);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
