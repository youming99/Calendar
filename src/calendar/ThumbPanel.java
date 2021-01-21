/* 달력의 네모 칸 하나 정의 */
package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThumbPanel extends JPanel {
   DiaryApp diaryapp;
   MainCalendarApp mainCalendarApp;

   JPanel e_calendar; // 전체 패널
   JPanel p_west; // 서쪽 패널 (날짜)
   JPanel p_east; // 동쪽 패널 (내용)

   JLabel l_date; // 날짜 텍스트
   JButton bt_diaryCheck; // 일기 작성시 생기는 버튼

   int date;
   int day;
   int year;
   int month;
   
   String path2;
   String nickname;
   int member_id;

   Color color;
   
   ImageIcon icon_diary;

   public ThumbPanel(Color color, int year, int month, int date, int day, int memberid) {
      this.date = date;
      this.day = day;
      this.year = year;
      this.month = month;
      this.member_id = memberid;
      
      System.out.println("thumbPanel : " + member_id);
//      boolean check = mainCalendarApp.hasSession;
//      if(check==true) {
//         
//      }
 
//       nickname = mainCalendarApp.str_nickName;
      
      path2="C:/DiaryMemo/"+member_id+"/";
      
      icon_diary = new ImageIcon("res/logo_diary.png");
      Image img = icon_diary.getImage();
      Image newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ; 
      icon_diary = new ImageIcon(newimg);
      
      e_calendar = new JPanel();
      p_west = new JPanel();
      p_east = new JPanel();
      l_date = new JLabel();
      bt_diaryCheck = new JButton(icon_diary);

      e_calendar.setPreferredSize(new Dimension(90, 65));
      p_west.setPreferredSize(new Dimension(30, 90));
      p_east.setPreferredSize(new Dimension(50, 90));
      l_date.setPreferredSize(new Dimension(30, 30));
      bt_diaryCheck.setPreferredSize(new Dimension(50, 50));
      
      bt_diaryCheck.setBorderPainted(false);
      bt_diaryCheck.setFocusPainted(false);
      bt_diaryCheck.setContentAreaFilled(false);

      e_calendar.setBackground(Color.white);
      p_west.setBackground(Color.white);
      p_east.setBackground(Color.white);
//      bt_diaryCheck.setBackground(Color.orange);

      if (date == 0) {
         l_date.setText("");
         bt_diaryCheck.setVisible(false);
      } else {
         l_date.setText(String.valueOf(date));
         bt_diaryCheck.setVisible(false);
      }
      FileReader rw;
      try {
         rw = new FileReader(path2 + year + "_" + month + "_" + date + ".txt");
         if (rw != null) {
            bt_diaryCheck.setVisible(true);
         }
      } catch (FileNotFoundException e1) {
         // TODO Auto-generated catch block
//      e1.printStackTrace();
      }

      l_date.setFont(new Font("SansSerif", Font.BOLD, 20));
      l_date.setForeground(color);

//      p_west.setBackground(Color.GREEN);
//      p_east.setBackground(Color.GRAY);

      add(e_calendar);
      e_calendar.add(p_west, BorderLayout.WEST);
      e_calendar.add(p_east, BorderLayout.EAST);
      p_west.add(l_date);
      p_east.add(bt_diaryCheck);

      e_calendar.addMouseListener(new MouseListener() {

         @Override
         public void mouseReleased(MouseEvent e) {
            if (date != 0) {
               diaryapp = new DiaryApp(mainCalendarApp, year, month, date, member_id);
            }
//         bt_diaryCheck.setVisible(true);

         }

         @Override
         public void mousePressed(MouseEvent e) {

         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

         }

         @Override
         public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

         }
      });

      bt_diaryCheck.addActionListener((e) -> {
         diaryapp = new DiaryApp(mainCalendarApp, year, month, date, member_id);
      });
   }

}