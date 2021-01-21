package calendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DiaryApp extends JFrame {

   JLabel l_day;
   JLabel l_title;
   JTextField input_title;
   JLabel l_content;
   JTextArea input_content;
   JButton bt_cancel;
   JButton bt_ok;
   JButton bt_remove;

   JPanel p_day;
   JPanel p_title;
   JPanel p_contenttext;
   JPanel p_content;
   JPanel p_button;
   JPanel p_all;

   int date;
   int year;
   int month;
   String nickname;
   static int member_id;
  
   String path;
   String path2;
   
   String filePath;

   BufferedWriter bw;
   FileReader rw;
   FileWriter fw;
   BufferedReader br;

   File deleteFile;
   ThumbPanel thumbpanel;
   
   JLabel logo;
   ImageIcon icon_bg;

   MainCalendarApp mainCalendarApp;
   boolean newCheck;

   public DiaryApp(MainCalendarApp mainCalendarApp, int year, int month, int date, int memberid) {
      this.setTitle("일정 작성");
      this.mainCalendarApp = mainCalendarApp;
      this.date = date;
      this.year = year;
      this.month = month;
      this.member_id = memberid;
      
//      nickname = mainCalendarApp.str_nickName;
      
      System.out.println("다이어리앱 닉네임은"+member_id);
      path="C:/DiaryMemo/"+member_id;
      path2="C:/DiaryMemo/"+member_id+"/";
      
      icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지
      
      p_day = new JPanel();
      p_title = new JPanel();
      p_contenttext = new JPanel();
      p_content = new JPanel();
      p_button = new JPanel();
      p_all = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 900, 800, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };

      l_day = new JLabel(year + "년 " + month + "월 " + date + "일");
      l_day.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_title = new JLabel("제목");
      input_title = new JTextField(30);
      l_content = new JLabel("내용");
      input_content = new JTextArea("");
      bt_cancel = new JButton("취소");
      bt_ok = new JButton("등록");
      bt_remove = new JButton("삭제");

      bt_remove.setVisible(false);

      p_day.add(l_day);
      p_day.setPreferredSize(new Dimension(800, 100));

      p_title.add(l_title);
      p_title.add(input_title);
      p_day.setPreferredSize(new Dimension(800, 300));

      p_contenttext.add(l_content);
      p_contenttext.setPreferredSize(new Dimension(800, 30));

      input_content.setPreferredSize(new Dimension(600, 400));
      p_content.add(input_content);
      p_content.setPreferredSize(new Dimension(800, 450));
      
      l_content.setFont(new Font("SansSerif", Font.BOLD, 20));
      input_content.setFont(new Font("SansSerif", Font.BOLD, 30));
      bt_cancel.setFont(new Font("SansSerif", Font.BOLD, 20));
      bt_ok.setFont(new Font("SansSerif", Font.BOLD, 20));
      bt_remove.setFont(new Font("SansSerif", Font.BOLD, 20));
      
      p_day.setBackground(new Color(255, 0, 0, 0));
      p_title.setBackground(new Color(255, 0, 0, 0));
      p_contenttext.setBackground(new Color(255, 0, 0, 0));
      p_content.setBackground(new Color(255, 0, 0, 0));
      p_button.setBackground(new Color(255, 0, 0, 0));

      p_button.add(bt_cancel);
      p_button.add(bt_ok);
      p_button.add(bt_remove);
      p_day.setPreferredSize(new Dimension(800, 50));

      p_all.add(p_day);
      // p_all.add(p_title);
      p_all.add(p_contenttext);
      p_all.add(p_content);
      p_all.add(p_button);

      add(p_all);
      
     File Folder = new File(path);
    
    if (!Folder.exists()) {
         try{
             Folder.mkdir(); //폴더 생성합니다.
             System.out.println("폴더가 생성되었습니다.");
              } 
              catch(Exception e){
             e.getStackTrace();
         }        
            }else {
         System.out.println("이미 폴더가 생성되어 있습니다.");
      }

      try { // 이미 저장된 파일이 있으면 읽어오고 버튼을 수정으로 바꾼다

         deleteFile = new File(path2 + year + "_" + month + "_" + date + ".txt");

         //filePath = "C:/DiaryMemo/" + year + "_" + month + "_" + date + ".txt";
         rw = new FileReader(path2 + year + "_" + month + "_" + date + ".txt");
         br = new BufferedReader(rw);
         String readLine = null;
         while ((readLine = br.readLine()) != null) {
            bt_ok.setText("수정");
            bt_remove.setVisible(true);
            // System.out.println(readLine);
            input_title.setText(input_title.getText() + readLine);
            input_content.setText(input_content.getText() + readLine + "\n");// 읽어온 내용이 나오게

            bt_remove.addActionListener((e) -> {
               deleteFile = new File(path2 + year + "_" + month + "_" + date + ".txt");

               try {

                  if (deleteFile.exists()) {
                     fw = fw = new FileWriter(deleteFile);
                     // 파일을 삭제합니다.
                     rw.close();
                     br.close();
                     fw.close();
                     deleteFile.delete();
                     JOptionPane.showMessageDialog(this, "삭제되었습니다.");
                     setVisible(false);
                  } else {
                     // System.out.println("파일이 존재하지 않아요ㅠ");
                  }
               } catch (IOException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
            });
         }

         if (deleteFile.exists() && (readLine = br.readLine()) == null) {
            rw.close();
            br.close();
            fw.close();
            deleteFile.delete();

         }

      } catch (Exception e1) {
         // System.out.println(filePath);
         e1.getStackTrace();

         setVisible(true);
         setSize(900, 800);
         WriteMemo(); //미리 저장되어있던 파일이 없을 때
      }

      setVisible(true);
      setSize(900, 800);

      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLocationRelativeTo(null);

      bt_cancel.addActionListener((e) -> {
         setVisible(false);
      });
      bt_ok.addActionListener((e) -> {
//         mainCalendarApp.ViewCanlendar(year, month);
         String str = input_content.getText().toString();
         if (str.equals("")) {
            JOptionPane.showMessageDialog(this, "내용을 입력해주세요");
         } else {
            File file = new File(path2 + year + "_" + month + "_" + date + ".txt");
            JOptionPane.showMessageDialog(this, "등록되었습니다.");
            setVisible(false);
//            mainCalendarApp.setHasSession(true);
//            mainCalendarApp.setVisible(true);
//            mainCalendarApp.setNewCheck(true);
//            mainCalendarApp.newCheck = true;
            try {
               FileWriter fw = new FileWriter(file);
               fw.write(str);
               fw.close();
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      });

   }

   public void WriteMemo() {
      OutputStream output;
      try {

         String str = input_content.getText().toString();
         if (str.equals("")) {
            // JOptionPane.showMessageDialog(this, "내용을 입력해주세요");
         } else {
            
        
            
            
            
            output = new FileOutputStream(path2 + year + "_" + month + "_" + date + ".txt");
            byte[] by = str.getBytes();
            output.write(by);
            output.close();
            // System.out.println("내용을 입력해주세요ㅠ");
         }
      } catch (FileNotFoundException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }

   }

   public static void main(String[] args) {

   }
}