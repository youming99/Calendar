package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import d_day.D_DayApp;
import member.CalendarMember;
import member.LoginApp;
import member.MyPageApp;
import member.SignUpApp;

public class MainCalendarApp extends JFrame implements Runnable {

   Thread loopThread;

   JPanel p_center; // ��ü �г�
   JPanel p_west; // ���� �г� (������+����ð�)
   JPanel p_east; // ���� �г� (Ķ����)
   JPanel w_north; // ������ ��� �г� (������)
   JPanel w_center; // ������ ��� �г�
   JPanel w_south; // ������ �ϴ� �г� (���� + ����ð�)
   JPanel p_dday;
   JPanel e_content; // ������ ��� �г� (2020�� < 8�� > )
   JPanel e_calendar; // ������ �ϴ� �г� (Ķ����)

   // ������ ����
   JPanel can; // ����� �̹��� ����
   JLabel t_user; // ����� �̸�
   public String str_nickName = null;
   int member_id;
   String str_id = null;
   String str_pw = null;
   String imagepath;
   ImageIcon icon_profile;
   public boolean ImageCheck = false;

   // ����ð� ����
   JLabel l_currentTime; // ���� ��¥�� �ð�
   JLabel l_currentTime2; // ���� ��¥�� �ð�
   Thread thread;
   String format_time = null;
   String format_time2 = null;

   // �������� ����
//   ToDoList todoList;

   // Ķ��������
   JLabel t_year;
   JLabel t_before;
   JLabel t_month;
   JLabel t_after;
   JLabel l_empty;
   JLabel l_update;
   String yearText = null;
   String monthText = null;
   int yearInt = 0;
   int monthInt = 0;
   Calendar cal;

   ThumbPanel[] thumb = new ThumbPanel[7];
   ThumbPanel2[] thumb2 = new ThumbPanel2[7];

   String[] dayArray = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

   Color[] colorArray = { Color.RED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLUE };

   int date = 0; // ���ϳ�¥
   int day; // �� ���� 1���� ����(��:1 ~ ��:7)
   int count = 0;
   int lastday;

   int countMonthInt = 0;
   int countYearInt = 0;

   Calendar cal2 = Calendar.getInstance();

   // �α��� ���� ���θ� ������ ����
   public boolean hasSession = false;// ������ �����ϰ� �ִ���...

   // �ϱ� �ۼ� ����
   public boolean newCheck = false;

   // ȸ�������� ������ �ڷ���?
   // �� ������ �����Ͱ� ä������ ������?? �α��� �����Ҷ� �ν��Ͻ� ��������!!
   private CalendarMember calendarMember; // ����� null��

   // ���ӿ� �ʿ��� ������..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";

   // ��� ���������� ����� �������� ��ü�� �������� ����
   // �� Ŀ�ؼ� ��ü�� ���α׷��� ������ ���ÿ� ������ ���� ����!!
   private Connection con;

   JFileChooser chooser = new JFileChooser(
         "C:/Korea_IT_Academy/workspace/java_workspace/KoreaItAcademyJava/res/gallery/0.jpg");
   Toolkit kit = Toolkit.getDefaultToolkit();// �÷��� �������� ��η� �����ö��� ��Ŷ ����
   File file;
   Image img;
   BufferedImage imageFile;

   ImageIcon icon_bg;
   ImageIcon icon_bg2;
   ImageIcon icon_bg3;
   ImageIcon icon_bg4;
   ImageIcon dday_bg;
   ImageIcon icon_update;

   MainCalendarApp mainCalendarApp;
   LoginApp loginApp;
   SignUpApp signUpApp;
   DiaryApp diaryApp;

   private JFrame[] pages = new JFrame[2]; // ȭ����ȯ�� ���� �����ӵ��� ��Ե� �迭

   public ToDoList ToDoList;
   // �����? ������ �ʴ� �����Ϳ� �ǹ̸� �ο��Ͽ� �������� ���̱� ���� ����Ѵ�
   public static final int Member_LoginApp = 0;
   public static final int Member_SignUpApp = 1;
   public static final int Member_FindId = 2;
   public static final int Member_FindPw = 3;

   public MainCalendarApp() {


      this.getConnection();// �������� �����ֱ� ������ �����ͺ��̽� ���� �����س���!!

      viewCalendarCurrent();
//      ViewCanlendar();
      getNickName();
      getMemberId();
//      printImage(member_id);
      
//      ViewToDoList(str_id, str_pw, member_id);

//      loginApp = new LoginApp(mainCalendarApp);
//      signUpApp = new SignUpApp(mainCalendarApp);

      icon_bg = new ImageIcon("res/bg.jpg"); // ��� �̹���
      icon_bg2 = new ImageIcon("res/bg2.jpg"); // ��� �̹���
      icon_bg3 = new ImageIcon("res/bg3.jpg"); // ��� �̹���
      icon_bg4 = new ImageIcon("res/bg4.jpg"); // ��� �̹���
      dday_bg = new ImageIcon("res/logo_dday.png"); // ���� �̹���
      icon_update = new ImageIcon("res/logo_update.png"); // ������Ʈ �̹���

      p_center = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 1200, 800, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      p_west = new JPanel() {
//         public void paintComponent(Graphics g) {
//            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 150, null);
//            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
//            super.paintComponent(g);
//         }
      };
      p_east = new JPanel() {
//         public void paintComponent(Graphics g) {
//            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 150, null);
//            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
//            super.paintComponent(g);
//         }
      };
      w_north = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 600, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      w_center = new JPanel();
      p_dday = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(dday_bg.getImage(), 0, 0, 200, 150, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      w_south = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 220, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      e_content = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg2.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      e_calendar = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg2.getImage(), 0, 0, 800, 780, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };

      t_user = new JLabel();
      l_currentTime = new JLabel();
      l_currentTime2 = new JLabel();
      t_year = new JLabel("��", JLabel.CENTER);
      t_before = new JLabel("��", JLabel.CENTER);
      t_month = new JLabel("��", JLabel.CENTER);
      t_after = new JLabel("��", JLabel.CENTER);
      l_empty = new JLabel("                            ");
      l_update = new JLabel(icon_update);

      Image img = icon_update.getImage();
      img = img.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
      icon_update.setImage(img);

      
      can = new JPanel() {
          @Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.drawImage(imageFile, 0, 0, this.getWidth(), this.getHeight(), this); // see javadoc for more info on the parameters            
             }
      };

      int year = cal2.get(Calendar.YEAR);
      int month = cal2.get(Calendar.MONTH);
//      ViewCanlendar(year, month);

      pages[0] = new LoginApp(this); // �Խ��� ��� ������
      pages[1] = new SignUpApp(this); // �Խ��� �۾��� ������
//      pages[2] = new FindId(this); //�Խ��� �۾��� ������ 
//      pages[3] = new FindPw(this); //�Խ��� �۾��� ������ 
      // pages[2] = new MainCalendarApp();
//      pages[2] = new MainCalendarApp(this); //�Խ��� �۾��� ������ 

      p_center.setPreferredSize(new Dimension(1000, 1000));
      p_west.setPreferredSize(new Dimension(200, 900));
      p_east.setPreferredSize(new Dimension(800, 900));
      p_dday.setPreferredSize(new Dimension(200, 150));
      w_north.setPreferredSize(new Dimension(200, 200));
      w_center.setPreferredSize(new Dimension(200, 280));
      w_south.setPreferredSize(new Dimension(200, 300));
      e_content.setPreferredSize(new Dimension(800, 120));
      t_year.setPreferredSize(new Dimension(160, 50));
      t_before.setPreferredSize(new Dimension(60, 50));
      t_month.setPreferredSize(new Dimension(100, 50));
      e_calendar.setPreferredSize(new Dimension(800, 600));
      can.setPreferredSize(new Dimension(200, 150));

//      p_center.setBackground(new Color(255, 0, 0, 0));
      p_west.setBackground(new Color(250, 0, 0, 0));
      p_east.setBackground(new Color(255, 0, 0, 0)); // ���� �κ� ����
      w_south.setBackground(new Color(255, 0, 0, 0));
      w_center.setBackground(new Color(255, 0, 0, 0)); // ��� �������� �ϱ� ����
      p_dday.setBackground(new Color(255, 0, 0, 0)); // ���� ���� �ϱ� ����
//      p_dday.setBackground(new Color(255, 0, 0, 0));
//      e_calendar.setBackground(new Color(254, 0, 0, 0));
//      p_dday.setBackground(new Color(0, 217, 214, 255));
//      e_calendar.setBackground(new Color(0, 235, 232, 255));

      add(p_center);
      p_center.add(p_west, BorderLayout.WEST);
      p_center.add(p_east, BorderLayout.EAST);
      p_west.add(w_north, BorderLayout.NORTH);
      p_west.add(w_center, BorderLayout.CENTER);
      p_west.add(w_south, BorderLayout.SOUTH);
      w_north.add(can, BorderLayout.NORTH);
      w_north.add(t_user);
      w_south.add(l_currentTime, BorderLayout.NORTH);
      w_south.add(l_currentTime2, BorderLayout.NORTH);
      w_south.add(p_dday, BorderLayout.NORTH);
      p_east.add(e_content, BorderLayout.SOUTH);
      p_east.add(e_calendar, BorderLayout.NORTH);
      e_content.add(t_year);
      e_content.add(t_before);
      e_content.add(t_month);
      e_content.add(t_after);
      e_content.add(l_empty);
      e_content.add(l_update, BorderLayout.AFTER_LINE_ENDS);
//      w_center.add(new ToDoList(str_id, str_pw, member_id));

//      l_currentTime.setText(getCurrentTime.date);
//      l_currentTime.setText(getCurrentTime.dayOfWeekStr);
//      l_currentTime.setText(getCurrentTime.time);

      /* ������ */
      t_user.setText(str_nickName);

      /* ��ܿ� ���� ����, �� */
      viewCalendarCurrent();
      t_year.setText(yearText + "��");
      t_before.setText("��");
      t_month.setText(monthText + "��");
      t_after.setText("��");

      t_user.setFont(new Font("SansSerif", Font.BOLD, 30));
      t_year.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_before.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_month.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_after.setFont(new Font("SansSerif", Font.BOLD, 40));

      l_currentTime.setFont(new Font("SansSerif", Font.BOLD, 20));
      l_currentTime2.setFont(new Font("SansSerif", Font.BOLD, 20));

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            disConnection(); // ��������

         }
      });

      /* ���콺 ������ */
      w_north.addMouseListener(new MouseListener() { // ���������� �г� Ŭ�� ��
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            MyPageApp myPageApp = new MyPageApp(ImageCheck);
            myPageApp.setVisible(true);
//            setVisible(false);
            myPageApp.getProfile(str_id, str_pw);
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent arg0) {
         }

         @Override
         public void mouseClicked(MouseEvent arg0) {
         }
      });
      p_dday.addMouseListener(new MouseListener() { // ���� �г� Ŭ�� ��
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            D_DayApp d_dayApp = new D_DayApp(member_id, str_id, str_pw);
            d_dayApp.setVisible(true);
//            setVisible(false);
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent arg0) {
         }

         @Override
         public void mouseClicked(MouseEvent arg0) {
         }
      });
      t_before.addMouseListener(new MouseListener() {
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            before();
            ViewCanlendar(yearInt, monthInt - 1);
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent arg0) {
         }

         @Override
         public void mouseClicked(MouseEvent arg0) {
         }
      });
      t_after.addMouseListener(new MouseListener() {
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            after();
            ViewCanlendar(yearInt, monthInt - 1);
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent arg0) {
         }

         @Override
         public void mouseClicked(MouseEvent arg0) {
         }
      });
      l_update.addMouseListener(new MouseListener() { // ������Ʈ Ŭ�� ��
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            e_calendar.removeAll();
            ViewCanlendar(yearInt, monthInt - 1); // Ķ���� ���
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent arg0) {
         }

         @Override
         public void mouseClicked(MouseEvent arg0) {
         }
      });

      // ������ â ������, ����Ŭ���� ���� ���� ���μ����� �����ؾ� ��!!
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            disConnection();// ���� ����!!
            System.exit(0);// ���μ��� ����
         }

      });

      isHasSession();
//      loginApp.login();

      loginCheck(hasSession, str_nickName, str_id, str_pw, member_id);

//      todoList = new ToDoList(member_id);

      setSize(1200, 800);
      setLocationRelativeTo(null);
//      setBounds(300, 10, 1200, 800);
//      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      new Thread(this).start();
   }

   // ������ �õ��ϴ� �޼��� ����
   public void getConnection() {
      try {
         Class.forName(driver);// ����̹� �ε�
         con = DriverManager.getConnection(url, user, pass); // ���ӽõ� ��, ��ü ��ȯ
         if (con == null) { // ���ӽ����ΰ�� �޽��� ���
            JOptionPane.showMessageDialog(this, "�����ͺ��̽��� �������� ���߽��ϴ�.");
         } else {// ���� ������ ��� ������ ����â�� ���� ������ ���
            this.setTitle(user + " ���� ��");
         }
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   // ������ �����ϴ� �޼��� ����
   // �� �޼���� ������â�� ������ ȣ��� ������
   public void disConnection() {
      // null �� �ƴҶ��� �ݾƾ� ��, ���� �̷� Ȯ�������� ��ġ�� ������ NullPointerException �߻��Ҽ��ֽ�
      if (con != null) {
         try {
            con.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   // �� ���α׷����� ���Ǵ� ��� �������� �����ϴ� �޼���
   public void setPage(int showIndex) {// �����ְ� ���� ������ index �Ѱܹ���
      for (int i = 0; i < pages.length; i++) {
         if (i == showIndex) {
            pages[i].setVisible(true);
         } else {
            pages[i].setVisible(false);
         }
      }
   }

   // �α��� ���θ� �Ǵ��ؼ� ���� �α������� �ʾҴٸ�, �α��������� �����ֱ�!!
   public void loginCheck(Boolean hasSession, String nickName, String id, String pw, int memberid) {

      if (hasSession == false) {// �α��� ���� ���� ������!!!
         this.setVisible(false);
         JOptionPane.showMessageDialog(this, "�α����� �ʿ��� �����Դϴ�");
         setPage(MainCalendarApp.Member_LoginApp);
//         LoginApp loginApp = new LoginApp(mainCalendarApp);
//         loginApp.setVisible(true);
//         dispose();
      } else if (hasSession == true) {// �α��� �� ������Դ�, ���ϴ� �������� �����ش�
         this.str_nickName = nickName;
         this.str_id = id;
         this.str_pw = pw;
         this.member_id = memberid;
         w_center.add(new ToDoList(str_id, str_pw, member_id));

         ViewToDoList(str_id, str_pw, member_id);
         
         int year = cal2.get(Calendar.YEAR);
         int month = cal2.get(Calendar.MONTH);
         ViewCanlendar(year, month);
         t_user.setText(str_nickName + "��");
      }
   }

   public void getNickName() {
//      t_user.setText(str_nickName + "��");

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select * from calendar_member where m_id = ? and m_pw=?";
      try {
         pstmt = con.prepareStatement(sql); // ������ �غ�
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {

//            member_id = Integer.parseInt(rs.getString(1));
//            ViewToDoList("����" + str_id, str_pw, member_id);
//            System.out.println(member_id);
//            System.out.println(rs.getString(1)); // member_id
//            System.out.println(rs.getString(2)); // m_id
//            System.out.println(rs.getString(3)); // m_pw
//            System.out.println(rs.getString(4)); // m_nickName
//            System.out.println(rs.getString(5)); // m_email
//            System.out.println(rs.getString(6)); // m_phone

//            l_nickName.setText(rs.getString(4) + "��");
            t_user.setText(rs.getString(4) + "��");
         } else {
//            JOptionPane.showMessageDialog(this, "�Է��Ͻ� ������ �������� �ʽ��ϴ�.");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
         if (pstmt != null) {
            try {
               pstmt.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
   }
   
   public void getMemberId() {
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select * from calendar_member where m_id = ? and m_pw=?";
      try {
         pstmt = con.prepareStatement(sql); // ������ �غ�
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {

            int memberid = Integer.parseInt(rs.getString(1));
            member_id = memberid;
//            ViewToDoList("����" + str_id, str_pw, member_id);
//            todoList = new ToDoList(str_id, str_pw, member_id);
            printImage(member_id);
//            System.out.println(rs.getString(1)); // member_id
//            System.out.println(rs.getString(2)); // m_id
//            System.out.println(rs.getString(3)); // m_pw
//            System.out.println(rs.getString(4)); // m_nickName
//            System.out.println(rs.getString(5)); // m_email
//            System.out.println(rs.getString(6)); // m_phone

//            l_nickName.setText(rs.getString(4) + "��");
//            t_user.setText(rs.getString(4) + "��");
         } else {
//            JOptionPane.showMessageDialog(this, "�Է��Ͻ� ������ �������� �ʽ��ϴ�.");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
         if (pstmt != null) {
            try {
               pstmt.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public void printImage(int memberid) {
      this.member_id = memberid;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      String sql = "select * from profile_image where member_id=?";
      try {
         pstmt = con.prepareStatement(sql); // ������ �غ�
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         if (rs.next()) { // ���ڵ尡 �����Ѵٸ� ���̵� ���� �Ұ�
            ImageCheck = true;
            
            // sql = "update profile_image set image = ?";
            imagepath = rs.getString(2).toString();
            // String imagepath2 = imagepath.replace("\\", "\\\\");

            // String path = "C:\\Users\\pc\\Downloads\\switch_on.png";
            // System.out.println('"'+imagepath+'"');
            // rs.getString(2);

            // C:\Users\pc\Documents\īī���� ���� ����

            icon_profile = new ImageIcon(imagepath);

//            can.setBackground(Color.GREEN);

            // ��׶��� �̹��� ������ �޼ҵ忡 �̸����� Ŭ������ ����
            try {                
               imageFile = ImageIO.read(new File(imagepath));
            } catch (IOException ex) {
               // handle exception...
            }
         

         }

      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
         if (pstmt != null) {
            try {
               pstmt.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
//        else {
//          JOptionPane.showMessageDialog(this, "���̵� �Է��Ͻÿ�.");
//       }
      //

      // getTargetImage(file.getAbsolutePath());
   }

//   public void findImage() {
//      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//         // ���������� ���Ѵ�!!
//         file = chooser.getSelectedFile();
//         System.out.println("����� ���� ������ ������ ���� : " + file.getAbsolutePath());
//
//         getTargetImage(file.getAbsolutePath());
//      }
//   }
//   
//   //�׷��� �̹��� ���ϱ� 
//      public void getTargetImage(String path) {
//         img=kit.getImage(path); //�������  img���� ���Ѵ�!!   
//         img = ImageUtil.getCustomSize(img, 135, 115);
//      }
//
//   // �̸����� ����
//   public void preview() {
//      // paint�� �׸� ó��~~
//      can.repaint();
//   }

   // �ٸ� ��ü���� ������ �� �ֵ��� getter ����   
   public Connection getCon() {
      return con;
   }

   /* ���� �� �� */
   public void ViewToDoList(String id, String pw, int memberid) {
     getMemberId();
      this.str_id = id;
      this.str_pw = pw;
      this.member_id = memberid;
      System.out.println("��" +str_id + str_pw +member_id);
      ToDoList todoList = new ToDoList(str_id, str_pw, member_id);

   }

   /* �޷� ��� */
   public void ViewCanlendar(int yearInt, int monthInt) {
      count = 0;

//         monthInt = Integer.parseInt(monthText);
//         yearInt = Integer.parseInt(yearText);
////          cal.set(cal.YEAR, Integer.parseInt(t_year.getText()));
////         cal.set(cal.MONTH, monthInt-1); // 1���� 0���� �����ϹǷ� ������ -1
////         cal.set(cal.MONTH, Integer.parseInt(t_month.getText())-1); // 1���� 0���� �����ϹǷ� ������ -1
//         cal.set(cal.MONTH, monthInt-1); // 1���� 0���� �����ϹǷ� ������ -1
//         cal.set(Calendar.DAY_OF_MONTH, 1); // DAY_OF_MONTH�� 1�� ���� (���� ù��)
//         day = cal.get(Calendar.DAY_OF_WEEK); // �� ���� ���� ��ȯ (��:1 ~ ��:7)
//         lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//         // System.out.println(week);

      e_calendar.setVisible(false);
      e_calendar.removeAll();

      for (int i = 0; i < 7; i++) {
         thumb2[i] = new ThumbPanel2(dayArray[i], colorArray[i]);
         e_calendar.add(thumb2[i]);
         thumb2[i].setBackground(Color.DARK_GRAY);
      }

      cal2.set(cal2.YEAR, yearInt);
      cal2.set(cal2.MONTH, monthInt); // 1���� 0���� �����ϹǷ� ������ -1
      cal2.set(Calendar.DAY_OF_MONTH, 1); // DAY_OF_MONTH�� 1�� ���� (���� ù��)
      day = cal2.get(Calendar.DAY_OF_WEEK); // �� ���� ���� ��ȯ (��:1 ~ ��:7)
      lastday = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);

      for (int i = 0; i < 6; i++) {
         for (int j = 0; j < 7; j++) {
            count++;
            if (count < day) {
               thumb[i] = new ThumbPanel(colorArray[j], yearInt, monthInt + 1, 0, day, member_id);
               e_calendar.add(thumb[i]);
            } else if (count == day) {
               date = 1;
               thumb[i] = new ThumbPanel(colorArray[j], yearInt, monthInt + 1, date, day, member_id);
               e_calendar.add(thumb[i]);
               date++;
            } else if (date > lastday) {
               thumb[i] = new ThumbPanel(colorArray[j], yearInt, monthInt + 1, 0, day, member_id);
               e_calendar.add(thumb[i]);
            } else {
               thumb[i] = new ThumbPanel(colorArray[j], yearInt, monthInt + 1, date, day, member_id);
               e_calendar.add(thumb[i]);
               date++;
            }
            thumb[i].setBackground(Color.DARK_GRAY);
         }
      }
      e_calendar.setVisible(true);
   }

   /* ��ܿ� ���� ������ �� ��� */
   public void viewCalendarCurrent() {
      cal = Calendar.getInstance();

      // ����
      SimpleDateFormat format = new SimpleDateFormat("yyyy");
      yearText = format.format(cal.getTime());
      // ��
      SimpleDateFormat format2 = new SimpleDateFormat("MM");
      monthText = format2.format(cal.getTime());
   }

   /* ��ܿ� ���� �� ���� ��ư ������ �� */
   public void before() {
      countMonthInt--;
      monthInt = Integer.parseInt(monthText);
      yearInt = Integer.parseInt(yearText);
      monthInt += countMonthInt;
      if (monthInt < 1) {
         countMonthInt = 0;
         countMonthInt++;
         monthInt = 12;
         countYearInt--;
      }
      yearInt += countYearInt;
      t_year.setText(String.valueOf(yearInt) + "��");
      t_month.setText(String.valueOf(monthInt) + "��");
   }

   /* ��ܿ� ���� �� ���� ��ư ������ �� */
   public void after() {
      countMonthInt++;
      monthInt = Integer.parseInt(monthText);
      yearInt = Integer.parseInt(yearText);
      monthInt += countMonthInt;
      if (monthInt > 12) {
         countMonthInt = -11;
         countMonthInt++;
         monthInt = 1;
         countYearInt++;
      }
      yearInt += countYearInt;
      t_year.setText(String.valueOf(yearInt) + "��");
      t_month.setText(String.valueOf(monthInt) + "��");
   }

   public boolean isHasSession() {
      return hasSession;
   }

   public void setHasSession(boolean hasSession) {
      this.hasSession = hasSession;
   }

   public boolean isnewCheck() {
      return newCheck;
   }

   public void setNewCheck(boolean newCheck) {
      this.newCheck = newCheck;
   }

   public CalendarMember getCalendarMember() {
      return calendarMember;
   }

   public void setCalendarMember(CalendarMember calendarMember) {
      this.calendarMember = calendarMember;
   }

   public void appLoop() {
      t_user.setText(str_nickName + "��");
      // System.out.println("gameLoop() called...");
   }

   public static void main(String[] args) {
      JFrame.setDefaultLookAndFeelDecorated(true);

      new MainCalendarApp();
   }

   @Override
   public void run() {
      SimpleDateFormat format = new SimpleDateFormat("yyyy�� MM��dd��");
      SimpleDateFormat format2 = new SimpleDateFormat("HH��mm��ss��");
      while (true) {

         Calendar cal = Calendar.getInstance();
         format_time = format.format(cal.getTime());
         format_time2 = format2.format(cal.getTime());

//         System.out.println(format_time);
//         System.out.println(format_time2);

         l_currentTime.setText(format_time);
         l_currentTime2.setText(format_time2);

         getNickName();
         getMemberId();
        
//         printImage();
         int year = cal2.get(Calendar.YEAR);
         int month = cal2.get(Calendar.MONTH);

//         e_calendar.removeAll();
//         e_calendar.setVisible(true);

//         if(newCheck == true) {
//            ViewCanlendar(year, month);
//         }
//         
         try {
            Thread.sleep(1000);
//            this.repaint();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

}