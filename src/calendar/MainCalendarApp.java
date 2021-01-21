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

   JPanel p_center; // 전체 패널
   JPanel p_west; // 서쪽 패널 (프로필+현재시간)
   JPanel p_east; // 동쪽 패널 (캘린더)
   JPanel w_north; // 서쪽의 상단 패널 (프로필)
   JPanel w_center; // 서쪽의 가운데 패널
   JPanel w_south; // 서쪽의 하단 패널 (디데이 + 현재시간)
   JPanel p_dday;
   JPanel e_content; // 동쪽의 상단 패널 (2020년 < 8월 > )
   JPanel e_calendar; // 동쪽의 하단 패널 (캘린더)

   // 프로필 관련
   JPanel can; // 사용자 이미지 사진
   JLabel t_user; // 사용자 이름
   public String str_nickName = null;
   int member_id;
   String str_id = null;
   String str_pw = null;
   String imagepath;
   ImageIcon icon_profile;
   public boolean ImageCheck = false;

   // 현재시간 관련
   JLabel l_currentTime; // 현재 날짜와 시간
   JLabel l_currentTime2; // 현재 날짜와 시간
   Thread thread;
   String format_time = null;
   String format_time2 = null;

   // 오늘할일 관련
//   ToDoList todoList;

   // 캘린더관련
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

   int date = 0; // 매일날짜
   int day; // 각 월의 1일의 요일(일:1 ~ 토:7)
   int count = 0;
   int lastday;

   int countMonthInt = 0;
   int countYearInt = 0;

   Calendar cal2 = Calendar.getInstance();

   // 로그인 상태 여부를 보관할 변수
   public boolean hasSession = false;// 세션을 보유하고 있는지...

   // 일기 작성 여부
   public boolean newCheck = false;

   // 회원정보를 보관할 자료형?
   // 이 변수에 데이터가 채워지는 시점은?? 로그인 성공할때 인스턴스 생성하자!!
   private CalendarMember calendarMember; // 현재는 null임

   // 접속에 필요한 정보들..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";

   // 모든 페이지들이 사용할 접속정보 객체를 공통으로 선언
   // 이 커넥션 객체는 프로그램이 가동과 동시에 접속을 얻어다 놓자!!
   private Connection con;

   JFileChooser chooser = new JFileChooser(
         "C:/Korea_IT_Academy/workspace/java_workspace/KoreaItAcademyJava/res/gallery/0.jpg");
   Toolkit kit = Toolkit.getDefaultToolkit();// 플랫폼 종속적인 경로로 가져올때는 툴킷 쓰자
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

   private JFrame[] pages = new JFrame[2]; // 화면전환에 사용될 프레임들을 담게될 배열

   public ToDoList ToDoList;
   // 상수란? 변하지 않는 데이터에 의미를 부여하여 직관성을 높이기 위해 사용한다
   public static final int Member_LoginApp = 0;
   public static final int Member_SignUpApp = 1;
   public static final int Member_FindId = 2;
   public static final int Member_FindPw = 3;

   public MainCalendarApp() {


      this.getConnection();// 프레임을 보여주기 직전에 데이터베이스 접속 성공해놓자!!

      viewCalendarCurrent();
//      ViewCanlendar();
      getNickName();
      getMemberId();
//      printImage(member_id);
      
//      ViewToDoList(str_id, str_pw, member_id);

//      loginApp = new LoginApp(mainCalendarApp);
//      signUpApp = new SignUpApp(mainCalendarApp);

      icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지
      icon_bg2 = new ImageIcon("res/bg2.jpg"); // 배경 이미지
      icon_bg3 = new ImageIcon("res/bg3.jpg"); // 배경 이미지
      icon_bg4 = new ImageIcon("res/bg4.jpg"); // 배경 이미지
      dday_bg = new ImageIcon("res/logo_dday.png"); // 디데이 이미지
      icon_update = new ImageIcon("res/logo_update.png"); // 업데이트 이미지

      p_center = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 1200, 800, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      p_west = new JPanel() {
//         public void paintComponent(Graphics g) {
//            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 150, null);
//            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
//            super.paintComponent(g);
//         }
      };
      p_east = new JPanel() {
//         public void paintComponent(Graphics g) {
//            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 150, null);
//            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
//            super.paintComponent(g);
//         }
      };
      w_north = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 600, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      w_center = new JPanel();
      p_dday = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(dday_bg.getImage(), 0, 0, 200, 150, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      w_south = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 220, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      e_content = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg2.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      e_calendar = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg2.getImage(), 0, 0, 800, 780, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };

      t_user = new JLabel();
      l_currentTime = new JLabel();
      l_currentTime2 = new JLabel();
      t_year = new JLabel("년", JLabel.CENTER);
      t_before = new JLabel("◀", JLabel.CENTER);
      t_month = new JLabel("월", JLabel.CENTER);
      t_after = new JLabel("▶", JLabel.CENTER);
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

      pages[0] = new LoginApp(this); // 게시판 목록 페이지
      pages[1] = new SignUpApp(this); // 게시판 글쓰기 페이지
//      pages[2] = new FindId(this); //게시판 글쓰기 페이지 
//      pages[3] = new FindPw(this); //게시판 글쓰기 페이지 
      // pages[2] = new MainCalendarApp();
//      pages[2] = new MainCalendarApp(this); //게시판 글쓰기 페이지 

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
      p_east.setBackground(new Color(255, 0, 0, 0)); // 우측 부분 투명
      w_south.setBackground(new Color(255, 0, 0, 0));
      w_center.setBackground(new Color(255, 0, 0, 0)); // 가운데 투명으로 하기 위함
      p_dday.setBackground(new Color(255, 0, 0, 0)); // 디데이 으로 하기 위함
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

      /* 프로필 */
      t_user.setText(str_nickName);

      /* 상단에 현재 연도, 월 */
      viewCalendarCurrent();
      t_year.setText(yearText + "년");
      t_before.setText("◀");
      t_month.setText(monthText + "월");
      t_after.setText("▶");

      t_user.setFont(new Font("SansSerif", Font.BOLD, 30));
      t_year.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_before.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_month.setFont(new Font("SansSerif", Font.BOLD, 40));
      t_after.setFont(new Font("SansSerif", Font.BOLD, 40));

      l_currentTime.setFont(new Font("SansSerif", Font.BOLD, 20));
      l_currentTime2.setFont(new Font("SansSerif", Font.BOLD, 20));

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            disConnection(); // 접속해제

         }
      });

      /* 마우스 리스너 */
      w_north.addMouseListener(new MouseListener() { // 마이페이지 패널 클릭 시
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
      p_dday.addMouseListener(new MouseListener() { // 디데이 패널 클릭 시
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
      l_update.addMouseListener(new MouseListener() { // 업데이트 클릭 시
         @Override
         public void mouseReleased(MouseEvent e) {
         }

         @Override
         public void mousePressed(MouseEvent e) {
            e_calendar.removeAll();
            ViewCanlendar(yearInt, monthInt - 1); // 캘린더 출력
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

      // 윈도우 창 닫으면, 오라클과의 접속 끊고 프로세스도 종료해야 함!!
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            disConnection();// 접속 해제!!
            System.exit(0);// 프로세스 종료
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

   // 접속을 시도하는 메서드 정의
   public void getConnection() {
      try {
         Class.forName(driver);// 드라이버 로드
         con = DriverManager.getConnection(url, user, pass); // 접속시도 후, 객체 반환
         if (con == null) { // 접속실패인경우 메시지 출력
            JOptionPane.showMessageDialog(this, "데이터베이스에 접속하지 못했습니다.");
         } else {// 접속 성공의 경우 윈도우 제목창에 현재 접속자 출력
            this.setTitle(user + " 접속 중");
         }
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   // 접속을 해제하는 메서드 정의
   // 이 메서드는 윈도우창을 닫을때 호출될 예정임
   public void disConnection() {
      // null 이 아닐때만 닫아야 함, 만일 이런 확인절차를 거치지 않으면 NullPointerException 발생할수있슴
      if (con != null) {
         try {
            con.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   // 이 프로그램에서 사용되는 모든 페이지를 제어하는 메서드
   public void setPage(int showIndex) {// 보여주고 싶은 페이지 index 넘겨받자
      for (int i = 0; i < pages.length; i++) {
         if (i == showIndex) {
            pages[i].setVisible(true);
         } else {
            pages[i].setVisible(false);
         }
      }
   }

   // 로그인 여부를 판단해서 만일 로그인하지 않았다면, 로그인페이지 보여주기!!
   public void loginCheck(Boolean hasSession, String nickName, String id, String pw, int memberid) {

      if (hasSession == false) {// 로그인 하지 않은 상태임!!!
         this.setVisible(false);
         JOptionPane.showMessageDialog(this, "로그인이 필요한 서비스입니다");
         setPage(MainCalendarApp.Member_LoginApp);
//         LoginApp loginApp = new LoginApp(mainCalendarApp);
//         loginApp.setVisible(true);
//         dispose();
      } else if (hasSession == true) {// 로그인 한 사람에게는, 원하는 페이지를 보여준다
         this.str_nickName = nickName;
         this.str_id = id;
         this.str_pw = pw;
         this.member_id = memberid;
         w_center.add(new ToDoList(str_id, str_pw, member_id));

         ViewToDoList(str_id, str_pw, member_id);
         
         int year = cal2.get(Calendar.YEAR);
         int month = cal2.get(Calendar.MONTH);
         ViewCanlendar(year, month);
         t_user.setText(str_nickName + "님");
      }
   }

   public void getNickName() {
//      t_user.setText(str_nickName + "님");

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select * from calendar_member where m_id = ? and m_pw=?";
      try {
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {

//            member_id = Integer.parseInt(rs.getString(1));
//            ViewToDoList("ㅅㅂ" + str_id, str_pw, member_id);
//            System.out.println(member_id);
//            System.out.println(rs.getString(1)); // member_id
//            System.out.println(rs.getString(2)); // m_id
//            System.out.println(rs.getString(3)); // m_pw
//            System.out.println(rs.getString(4)); // m_nickName
//            System.out.println(rs.getString(5)); // m_email
//            System.out.println(rs.getString(6)); // m_phone

//            l_nickName.setText(rs.getString(4) + "님");
            t_user.setText(rs.getString(4) + "님");
         } else {
//            JOptionPane.showMessageDialog(this, "입력하신 정보는 존재하지 않습니다.");
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
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {

            int memberid = Integer.parseInt(rs.getString(1));
            member_id = memberid;
//            ViewToDoList("ㅅㅂ" + str_id, str_pw, member_id);
//            todoList = new ToDoList(str_id, str_pw, member_id);
            printImage(member_id);
//            System.out.println(rs.getString(1)); // member_id
//            System.out.println(rs.getString(2)); // m_id
//            System.out.println(rs.getString(3)); // m_pw
//            System.out.println(rs.getString(4)); // m_nickName
//            System.out.println(rs.getString(5)); // m_email
//            System.out.println(rs.getString(6)); // m_phone

//            l_nickName.setText(rs.getString(4) + "님");
//            t_user.setText(rs.getString(4) + "님");
         } else {
//            JOptionPane.showMessageDialog(this, "입력하신 정보는 존재하지 않습니다.");
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
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         if (rs.next()) { // 레코드가 존재한다면 아이디 생성 불가
            ImageCheck = true;
            
            // sql = "update profile_image set image = ?";
            imagepath = rs.getString(2).toString();
            // String imagepath2 = imagepath.replace("\\", "\\\\");

            // String path = "C:\\Users\\pc\\Downloads\\switch_on.png";
            // System.out.println('"'+imagepath+'"');
            // rs.getString(2);

            // C:\Users\pc\Documents\카카오톡 받은 파일

            icon_profile = new ImageIcon(imagepath);

//            can.setBackground(Color.GREEN);

            // 백그라운드 이미지 삽입할 메소드에 이름없는 클래스로 구현
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
//          JOptionPane.showMessageDialog(this, "아이디를 입력하시오.");
//       }
      //

      // getTargetImage(file.getAbsolutePath());
   }

//   public void findImage() {
//      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//         // 파일정보를 구한다!!
//         file = chooser.getSelectedFile();
//         System.out.println("당신이 지금 선택한 파일의 정보 : " + file.getAbsolutePath());
//
//         getTargetImage(file.getAbsolutePath());
//      }
//   }
//   
//   //그려질 이미지 구하기 
//      public void getTargetImage(String path) {
//         img=kit.getImage(path); //멤버변수  img값을 구한다!!   
//         img = ImageUtil.getCustomSize(img, 135, 115);
//      }
//
//   // 미리보기 구현
//   public void preview() {
//      // paint로 그림 처리~~
//      can.repaint();
//   }

   // 다른 객체들이 접근할 수 있도록 getter 제공   
   public Connection getCon() {
      return con;
   }

   /* 오늘 할 일 */
   public void ViewToDoList(String id, String pw, int memberid) {
     getMemberId();
      this.str_id = id;
      this.str_pw = pw;
      this.member_id = memberid;
      System.out.println("후" +str_id + str_pw +member_id);
      ToDoList todoList = new ToDoList(str_id, str_pw, member_id);

   }

   /* 달력 출력 */
   public void ViewCanlendar(int yearInt, int monthInt) {
      count = 0;

//         monthInt = Integer.parseInt(monthText);
//         yearInt = Integer.parseInt(yearText);
////          cal.set(cal.YEAR, Integer.parseInt(t_year.getText()));
////         cal.set(cal.MONTH, monthInt-1); // 1월이 0부터 시작하므로 월에서 -1
////         cal.set(cal.MONTH, Integer.parseInt(t_month.getText())-1); // 1월이 0부터 시작하므로 월에서 -1
//         cal.set(cal.MONTH, monthInt-1); // 1월이 0부터 시작하므로 월에서 -1
//         cal.set(Calendar.DAY_OF_MONTH, 1); // DAY_OF_MONTH를 1로 설정 (월의 첫날)
//         day = cal.get(Calendar.DAY_OF_WEEK); // 그 주의 요일 반환 (일:1 ~ 토:7)
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
      cal2.set(cal2.MONTH, monthInt); // 1월이 0부터 시작하므로 월에서 -1
      cal2.set(Calendar.DAY_OF_MONTH, 1); // DAY_OF_MONTH를 1로 설정 (월의 첫날)
      day = cal2.get(Calendar.DAY_OF_WEEK); // 그 주의 요일 반환 (일:1 ~ 토:7)
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

   /* 상단에 현재 연도와 월 출력 */
   public void viewCalendarCurrent() {
      cal = Calendar.getInstance();

      // 연도
      SimpleDateFormat format = new SimpleDateFormat("yyyy");
      yearText = format.format(cal.getTime());
      // 월
      SimpleDateFormat format2 = new SimpleDateFormat("MM");
      monthText = format2.format(cal.getTime());
   }

   /* 상단에 현재 월 이전 버튼 눌렀을 때 */
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
      t_year.setText(String.valueOf(yearInt) + "년");
      t_month.setText(String.valueOf(monthInt) + "월");
   }

   /* 상단에 현재 월 이후 버튼 눌렀을 때 */
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
      t_year.setText(String.valueOf(yearInt) + "년");
      t_month.setText(String.valueOf(monthInt) + "월");
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
      t_user.setText(str_nickName + "님");
      // System.out.println("gameLoop() called...");
   }

   public static void main(String[] args) {
      JFrame.setDefaultLookAndFeelDecorated(true);

      new MainCalendarApp();
   }

   @Override
   public void run() {
      SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일");
      SimpleDateFormat format2 = new SimpleDateFormat("HH시mm분ss초");
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