package calendar;

import java.awt.BorderLayout;
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
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ToDoList extends JPanel {

   JPanel w_center; // ToDoList가 들어갈 패널

   JPanel p_title; // 오늘 할일 타이틀 패널
   JPanel p_date; // 오늘 일자 패널
   JPanel p_list; // 오늘 할 일 리스트 패널
   JScrollBar scroll; // 스크롤

   JList list; // 리스트
   //DefaultListModel model;
   DefaultListModel<String> model;  


   JLabel l_title; // 오늘 할일 타이틀 라벨
   JLabel l_add; // 할일 추가 라벨
   JLabel l_date; // 오늘 일자 라벨

   JButton bt_add; // 할일 추가 버튼

   String str_id; // 유저의 아이디
   String str_pw; // 유저의 비밀번호
   int member_id; // 유저의 DB번호
   int member_id2;

   String[] memo;
   String ans_str;
   String data_memo;

   ArrayList<String> todo;
   
   Connection con;

   int count;
   
   
   ImageIcon icon_bg4;

   // 접속에 필요한 정보들..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";

   public ToDoList(String id, String pw, int memberid) {
      this.str_id = id;
      this.str_pw = pw;
      this.member_id = memberid;
      this.getConnection();
      
      System.out.println("todoList: " + str_id + str_pw + member_id);

      icon_bg4 = new ImageIcon("res/bg4.jpg"); // 배경 이미지
      
      todo = new ArrayList();

//      getUser(str_id, str_pw, member_id);
//      System.out.println("todolist에서 " + str_id + " " + str_pw + " " + member_id);

//      memo = new String[3];

      w_center = new JPanel() {
          public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };

      p_title = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      p_date = new JPanel();
      p_list = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 210, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      
      scroll = new JScrollBar();
//      countTotal();
//      readTodo();

      
//      model = new DefaultListModel<String>();


      
//      memo = (String[]) todo.toArray(new String[todo.size()]);
      list = new JList(new DefaultListModel());
      model = (DefaultListModel<String>)list.getModel();

      DisplayMessageDialog dmd = new DisplayMessageDialog();
      list.addMouseListener(dmd);
      
      getToDoList(member_id);
      
      l_title = new JLabel("오늘 해야 할 일");
      l_add = new JLabel("           +");
      l_date = new JLabel();

      bt_add = new JButton("+");

      /* 폰트 크기 */
      l_title.setFont(new Font("SansSerif", Font.BOLD, 15));
      l_add.setFont(new Font("SansSerif", Font.BOLD, 15));
      list.setFont(new Font("SansSerif", Font.BOLD, 15));
      bt_add.setFont(new Font("SansSerif", Font.BOLD, 25));
//      list.setAlignmentX(CENTER_ALIGNMENT);
      list.setCellRenderer(new DefaultListCellRenderer(){
          public int getHorizontalAlignment() {
                   return CENTER;
          }
      });

      /* 크기 설정 */
      w_center.setPreferredSize(new Dimension(200, 250));
      p_title.setPreferredSize(new Dimension(200, 50));
      p_list.setPreferredSize(new Dimension(200, 190));
      list.setPreferredSize(new Dimension(200, 210));

      /* 배경 설정 */
//      w_center.setBackground(Color.white);
      list.setBackground(new Color(250, 0, 0, 0));

      add(w_center, BorderLayout.CENTER);
      w_center.add(p_title);
//      w_center.add(p_date);
      w_center.add(p_list);

      p_title.add(l_title);
      p_title.add(bt_add, BorderLayout.EAST);

      p_list.add(list);
      p_list.add(scroll);
      
      bt_add.setBorderPainted(false);
      bt_add.setContentAreaFilled(false);

      /* 마우스 이벤트 리스너 */
      bt_add.addActionListener((e) -> {
////        getUser(str_id, str_pw, member_id);
         System.out.println("add리스너에서 " + member_id);

         addToDo(member_id);
      });
//   
   }

   public class DisplayMessageDialog extends MouseAdapter {

      public void mouseClicked(MouseEvent e) {
         JList theList = (JList) e.getSource();
         // if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
         if (e.getClickCount() == 2) {
            int index = theList.locationToIndex(e.getPoint());

            if (index >= 0) {
               Object o = theList.getModel().getElementAt(index);

//               JOptionPane.showMessageDialog(theList, o.toString() + "를 삭제할까요?");
               System.out.println("Double-clicked on: " + o.toString());
               
               String[] buttons = {"삭제" };
               int result = JOptionPane.showOptionDialog(null,  o.toString() +"를 삭제하시겠습니까?", "오늘 할 일 옵션", 0,
                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
               if (result == 0) { // 삭제
                  deleteToDo(o.toString());
                  getToDoList(member_id);
                  
               }
               
            }
         }
      }
   }

   /* 유저 정보 가져오기 */
   public void getUser(String id, String pw, int memberid) {
      this.str_id = id;
      this.str_pw = pw;
      this.member_id = memberid;
      // 로그인한 유저의 데이터를 가져와야함 (calendar_member 테이블에서 member_id 가져옴)
      PreparedStatement pstmt = null;

      ResultSet rs = null;
      String sql_member = "select * from calendar_member where m_id = ? and m_pw = ?";

      try {
         pstmt = con.prepareStatement(sql_member); // 쿼리문 준비1
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);
         rs = pstmt.executeQuery();
         if (rs.next()) {
            member_id = rs.getInt(1);
            getToDoList(member_id);
         } else {
//            JOptionPane.showMessageDialog(this, "아이디와 패스워드를 다시 입력해주세요");
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

   public void getToDoList(int memberid) {
	   this.member_id = memberid;
//	   getUser();
      model.removeAllElements();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      String sql = "select * from todo where member_id= ?";

      try {
         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
         // 스크롤이 가능하고(next()능력+ 자유자재로 이동능력), 읽기전용의 rs를 만들기 위한 옵션!!
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         rs.last();
         int total = rs.getRow(); // 총 레코드 수 반환

         // rs에 들어있는 데이터를 이차원배열로 옮겨심어보자!!
         // 그러기 위해서는 먼저 이차원 배열을 준비해놓자!!
         memo = new String[total];

         rs.beforeFirst();

         int index = 0;
         
         todo = new ArrayList<String>();
//         memo = new String[total];
         
         while (rs.next()) {// 커서 한칸 전진!

            todo.add( rs.getString(3));
//            for (int i = 0; i < total; i++) {
//               
//            }
//            index++;
         }
         for(int i=0; i<total; i++) {
            memo[i] = todo.get(i).toString();
            model.addElement(memo[i]);
            System.out.println(model);
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

   public void addToDo(int memberid) {
      getUser(str_id, str_pw, member_id);
      this.member_id = memberid;
      ans_str = (String) JOptionPane.showInputDialog(this, "할 일을 작성하세요.", "오늘 할 일", JOptionPane.PLAIN_MESSAGE, null,
            null, null);
      if(ans_str!=null) {
         
         System.out.println("ans_str : " + ans_str + member_id); // 입력한 단어가 리턴된다.
         
         PreparedStatement pstmt2 = null;
         ResultSet rs = null;
         
         String sql_add = "insert into todo(todo_id, member_id, memo)";
         sql_add += " values(seq_todo.nextval,?,?)";
         
         try {
            pstmt2 = con.prepareStatement(sql_add); // 쿼리문 준비2
            
            pstmt2.setInt(1, member_id);
            pstmt2.setString(2, ans_str);
            
            int result = pstmt2.executeUpdate();
            
            if (result == 0) {
               JOptionPane.showMessageDialog(this, "할일 등록에 실패했습니다.");
            } else {
               todo.add(ans_str);
               model.addElement(ans_str);
               list.setModel(model);
               
               
               JOptionPane.showMessageDialog(this, "할일 등록 성공했습니다.");
               list.updateUI(); 
               System.out.println(todo);
//            setVisible(false);
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
            if (pstmt2 != null) {
               try {
                  pstmt2.close();
               } catch (SQLException e) {
                  e.printStackTrace();
               }
            }
         }
      }

   }
   
   public void updateToDo() {
      
   }
   
   public void deleteToDo(String memo) {
      this.data_memo = memo;
      System.out.println("delete에서  " + data_memo);
      PreparedStatement pstmt = null;
      int result = 0;

      String sql = "delete from todo where memo = ?";
      try {
         pstmt = con.prepareStatement(sql); // 쿼리 준비
         
         pstmt.setString(1, data_memo);
         result = pstmt.executeUpdate();// 쿼리실행
         if (result == 0) {
            JOptionPane.showMessageDialog(this, "할일 삭제에 실패했습니다.");
         } else {
       
            JOptionPane.showMessageDialog(this, "할일 삭제 성공했습니다.");
            todo.remove(data_memo);
            model.removeElement(data_memo);
            list.setModel(model);
            System.out.println(todo);
             list.updateUI(); 

         }
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

   public void countTotal() {
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select count(*) from todo";
      try {
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         // pstmt.setInt(1, todo_id);
         // pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {
//               System.out.println(rs.getString(1)); // member_id
//               System.out.println(rs.getString(2)); // m_id
//               System.out.println(rs.getString(3)); // m_pw
//               System.out.println(rs.getString(4)); // m_nickName
//               System.out.println(rs.getString(5)); // m_email
//               System.out.println(rs.getString(6)); // m_phone

//               l_nickName.setText(rs.getString(4) + "님");
//               member_id=rs.getInt(1);
//               l_id.setText("아이디 : " + rs.getString(2));
//               t_pw.setText(rs.getString(3));
//               t_nickName.setText(rs.getString(4));
//               t_email.setText(rs.getString(5));
//               t_phone.setText(rs.getString(6));

            // todo.add(rs.getString(3));
            // System.out.println(todo);
            count = rs.getInt(1);

         } else {
            JOptionPane.showMessageDialog(this, "입력하신 정보는 존재하지 않습니다.");
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

   public void readTodo() {
      int todo_id = 0;

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      for (int i = 0; i < count; i++) {
         String sql = "select * from todo where todo_id = ?";
         try {
            pstmt = con.prepareStatement(sql); // 쿼리문 준비
            pstmt.setInt(1, todo_id);
            // pstmt.setString(2, str_pw);

            rs = pstmt.executeQuery();
            if (rs.next()) {


               todo.add(rs.getString(3));
               System.out.println(todo);
            } else {
               JOptionPane.showMessageDialog(this, "입력하신 정보는 존재하지 않습니다.");
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
         todo_id++;
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
//               this.setTitle(user + " 접속 중");
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

}