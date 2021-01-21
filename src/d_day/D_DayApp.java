package d_day;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;

import calendar.MainCalendarApp;

public class D_DayApp extends JFrame {

   JPanel p_center; // 전체 패널
   JPanel p_title; // 타이틀 패널
   JPanel p_table; // 테이블 패널
   JPanel p_bts; // 버튼들 패널
   JTable dday_list; // 디데이 리스트(테이블)
   JScrollBar scroll; // 스크롤

   JLabel l_title; // 타이틀 테스트 필드

   JButton bt_cancel;
   JButton bt_add;

   D_Day_Add dday_add;
   D_Day_Update dday_update;
   D_Day_Model dday_model;
   MainCalendarApp maniCalendarApp;

   Connection con;

   // 접속에 필요한 정보들..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";

   String str_id = null; // 유저의 아이디
   String str_pw = null; // 유저의 비밀번호
   int member_id;

   JLabel logo;
   ImageIcon icon_bg;
   ImageIcon icon_bg2;

   D_Day_List ddayList = null; // 수정, 삭제시에도 이 안에 들어있는 정보들을 활용하기 위해
                        // 지역변수로 선언하지 않고, 멤버변수로 선언

   String dday_id = null;

   public D_DayApp(int memberid,String id, String pw) {
      super.setTitle("디데이 목록");
      this.member_id = memberid;
      this.str_id = id;
      this.str_pw = pw;
      this.getConnection();

      icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지
      icon_bg2 = new ImageIcon("res/bg2.jpg"); // 배경 이미지

      System.out.println(str_id + "/ " + str_pw + "/" + member_id);
      /* 생성 */
      p_center = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 800, 600, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      p_title = new JPanel();
      p_table = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      p_bts = new JPanel();
      scroll = new JScrollBar();
      dday_list = new JTable(dday_model = new D_Day_Model());

      l_title = new JLabel("디데이 목록");

      bt_cancel = new JButton("이전");
      bt_add = new JButton("디데이 추가");

      /* 크기 */
      p_center.setPreferredSize(new Dimension(800, 600));
      p_title.setPreferredSize(new Dimension(800, 80));
      dday_list.setPreferredSize(new Dimension(750, 400));
      p_bts.setPreferredSize(new Dimension(800, 600));

      /* 폰트 크기 */
      l_title.setFont(new Font("SansSerif", Font.BOLD, 30));

      dday_list.setRowHeight(30);
      dday_list.setFont(new Font("SansSerif", Font.BOLD, 20));

      /* 스타일 */
//      p_center.setBackground(new Color(255, 0, 0, 0)));
      p_title.setBackground(new Color(255, 0, 0, 0));
      dday_list.setBackground(new Color(255, 0, 0, 0));
      p_table.setBackground(new Color(255, 0, 0, 0));
      p_bts.setBackground(new Color(255, 0, 0, 0));

      getList();

      /* 조립 */
      add(p_center);
      p_center.add(p_title);
      p_center.add(p_table);
      p_center.add(p_bts);
      p_title.add(l_title);
      p_table.add(dday_list);
      p_table.add(scroll);
      p_bts.add(bt_cancel);
      p_bts.add(bt_add);

      bt_cancel.addActionListener((e) -> {
         setVisible(false);
      });
      bt_add.addActionListener((e) -> {
         add();
      });

      dday_list.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e) {
            dday_id = (String) dday_list.getValueAt(dday_list.getSelectedRow(), 0);
            if (e.getClickCount() == 2) { // 셀 더블클릭
               System.out.println("더블클릭");
               System.out.println(dday_id);
               String[] buttons = { "수정", "삭제" };
               int result = JOptionPane.showOptionDialog(null, "해당 디데이의 설정을 선택하세요.", "디데이 옵션", 0,
                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
               if (result == 0) { // 수정
                  System.out.println("수정");
                  update(dday_id);
                  getList();
               } else if (result == 1) { // 삭제
                  delete(dday_id);
                  getList();

               }

            }
         }

      });

      setVisible(true);
      setSize(800, 600);
      setLocationRelativeTo(null);

   }

   /* 디데이 목록 가져오기 */
   public void getList() {

      String str_year = null; // DB에서 가져온 연도
      String str_month = null; // DB에서 가져온 월
      String str_day = null; // DB에서 가져온 일
      String str_allDate = null; // DB에서 가져온 날짜를 한 줄로

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select * from d_day where member_id = ? order by dday_id asc";

      try {
         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
         // 스크롤이 가능하고(next()능력+ 자유자재로 이동능력), 읽기전용의 rs를 만들기 위한 옵션!!
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         rs.last();
         int total = rs.getRow(); // 총 레코드 수 반환

         // rs에 들어있는 데이터를 이차원배열로 옮겨심어보자!!
         // 그러기 위해서는 먼저 이차원 배열을 준비해놓자!!
         String[][] records = new String[total][4];

         rs.beforeFirst();

         int index = 0;
         while (rs.next()) {// 커서 한칸 전진!
            String[] arr = new String[4];
            arr[0] = rs.getString("dday_id"); // 번호
            arr[1] = rs.getString("d_name"); // 디데이 이름

            str_year = rs.getString("d_year");
            str_month = rs.getString("d_month");
            str_day = rs.getString("d_day");
            str_allDate = str_year + " 년" + str_month + "월" + str_day + "일";
            arr[2] = str_allDate; // 날짜

            // 디데이 계산
            Calendar today = Calendar.getInstance(); // 현재 오늘 날짜
            int tyear = today.get(Calendar.YEAR);
            int tmonth = today.get(Calendar.MONTH);
//            tmonth += 1;
            int tday = today.get(Calendar.DAY_OF_MONTH);

//            System.out.println("오늘"+tyear + "/" + (tmonth + 1) + "/" + tday);
//            System.out.println("디데이" + str_year + "/" + (str_month) + "/" + str_day);

            Calendar dday = Calendar.getInstance(); // 디데이 날짜
            dday.set(Integer.parseInt(str_year), Integer.parseInt(str_month) - 1, Integer.parseInt(str_day)); // D-day

            long t = today.getTimeInMillis();
            long d = dday.getTimeInMillis();
            long r = (d - t) / (24 * 60 * 60 * 1000);
            if (r < 85000000 && r > 70000000) {
               r = -1;
            }

            int resultNumber = (int) r;
//            System.out.println("result " + resultNumber);

            if (resultNumber > 0) {
               arr[3] = String.format("D-%d", resultNumber);
            } else if (resultNumber == 0) {
               arr[3] = String.format("D-Day");
            } else {
               int absR = Math.abs(resultNumber);
               arr[3] = String.format("D+%d", absR);
            }

            records[index++] = arr; // 일차원 배열을 이차원배열의 방에 담음
         }

         // 이차원배열이 모두 완성되었으므로, TableModel이 보유한 data 이차원배열에 대입!!
         dday_model.data = records;
         dday_list.updateUI();// jtable UI 갱신
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

   /* 디데이 추가 */
   public void add() {
      dday_add = new D_Day_Add(member_id, str_id, str_pw);
      dday_add.setVisible(true);
      setVisible(false);
   }
   
   /* 디데이 수정 */
   public void update(String dday_id) {
      dday_update = new D_Day_Update(dday_id);
      dday_update.setVisible(true);
      setVisible(false);
   }

   /* 디데이 삭제 */
   public void delete(String dday_id) {
      PreparedStatement pstmt = null;
      int result = 0;

      String sql = "delete from d_day where dday_id=" + dday_id;
      try {
         pstmt = con.prepareStatement(sql); // 쿼리 준비
         result = pstmt.executeUpdate();// 쿼리실행
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         if (pstmt != null) {
            try {
               pstmt.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
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

//   public static void main(String[] args) {
//      new D_DayApp();
//   }
}