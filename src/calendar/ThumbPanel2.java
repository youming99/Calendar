/* 달력의 네모 칸 하나 정의 */
package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThumbPanel2 extends JPanel {
	
	JPanel e_calendar; //전체 패널
	JLabel l_day; //요일 텍스트
	
	String day=null;
	Color color;
	
	
	public ThumbPanel2(String day, Color color) {
		this.day = day;
		this.color = color;
		
		e_calendar = new JPanel();
		l_day = new JLabel();
		
		e_calendar.setPreferredSize(new Dimension(90,70));
		e_calendar.setBackground(Color.white);
		
		l_day.setForeground(color);
		l_day.setText(day);
		l_day.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_day.setHorizontalAlignment(JLabel.CENTER);
		l_day.setVerticalAlignment(Label.CENTER);
		
		add(e_calendar, BorderLayout.CENTER);
		e_calendar.add(l_day,BorderLayout.CENTER);
		
		
		
	
		
	}

}
