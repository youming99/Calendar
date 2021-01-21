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

   JPanel w_center; // ToDoList�� �� �г�

   JPanel p_title; // ���� ���� Ÿ��Ʋ �г�
   JPanel p_date; // ���� ���� �г�
   JPanel p_list; // ���� �� �� ����Ʈ �г�
   JScrollBar scroll; // ��ũ��

   JList list; // ����Ʈ
   //DefaultListModel model;
   DefaultListModel<String> model;  


   JLabel l_title; // ���� ���� Ÿ��Ʋ ��
   JLabel l_add; // ���� �߰� ��
   JLabel l_date; // ���� ���� ��

   JButton bt_add; // ���� �߰� ��ư

   String str_id; // ������ ���̵�
   String str_pw; // ������ ��й�ȣ
   int member_id; // ������ DB��ȣ
   int member_id2;

   String[] memo;
   String ans_str;
   String data_memo;

   ArrayList<String> todo;
   
   Connection con;

   int count;
   
   
   ImageIcon icon_bg4;

   // ���ӿ� �ʿ��� ������..
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

      icon_bg4 = new ImageIcon("res/bg4.jpg"); // ��� �̹���
      
      todo = new ArrayList();

//      getUser(str_id, str_pw, member_id);
//      System.out.println("todolist���� " + str_id + " " + str_pw + " " + member_id);

//      memo = new String[3];

      w_center = new JPanel() {
          public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };

      p_title = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 800, 120, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      p_date = new JPanel();
      p_list = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg4.getImage(), 0, 0, 200, 210, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
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
      
      l_title = new JLabel("���� �ؾ� �� ��");
      l_add = new JLabel("           +");
      l_date = new JLabel();

      bt_add = new JButton("+");

      /* ��Ʈ ũ�� */
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

      /* ũ�� ���� */
      w_center.setPreferredSize(new Dimension(200, 250));
      p_title.setPreferredSize(new Dimension(200, 50));
      p_list.setPreferredSize(new Dimension(200, 190));
      list.setPreferredSize(new Dimension(200, 210));

      /* ��� ���� */
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

      /* ���콺 �̺�Ʈ ������ */
      bt_add.addActionListener((e) -> {
////        getUser(str_id, str_pw, member_id);
         System.out.println("add�����ʿ��� " + member_id);

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

//               JOptionPane.showMessageDialog(theList, o.toString() + "�� �����ұ��?");
               System.out.println("Double-clicked on: " + o.toString());
               
               String[] buttons = {"����" };
               int result = JOptionPane.showOptionDialog(null,  o.toString() +"�� �����Ͻðڽ��ϱ�?", "���� �� �� �ɼ�", 0,
                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
               if (result == 0) { // ����
                  deleteToDo(o.toString());
                  getToDoList(member_id);
                  
               }
               
            }
         }
      }
   }

   /* ���� ���� �������� */
   public void getUser(String id, String pw, int memberid) {
      this.str_id = id;
      this.str_pw = pw;
      this.member_id = memberid;
      // �α����� ������ �����͸� �����;��� (calendar_member ���̺��� member_id ������)
      PreparedStatement pstmt = null;

      ResultSet rs = null;
      String sql_member = "select * from calendar_member where m_id = ? and m_pw = ?";

      try {
         pstmt = con.prepareStatement(sql_member); // ������ �غ�1
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);
         rs = pstmt.executeQuery();
         if (rs.next()) {
            member_id = rs.getInt(1);
            getToDoList(member_id);
         } else {
//            JOptionPane.showMessageDialog(this, "���̵�� �н����带 �ٽ� �Է����ּ���");
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
         // ��ũ���� �����ϰ�(next()�ɷ�+ ��������� �̵��ɷ�), �б������� rs�� ����� ���� �ɼ�!!
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         rs.last();
         int total = rs.getRow(); // �� ���ڵ� �� ��ȯ

         // rs�� ����ִ� �����͸� �������迭�� �Űܽɾ��!!
         // �׷��� ���ؼ��� ���� ������ �迭�� �غ��س���!!
         memo = new String[total];

         rs.beforeFirst();

         int index = 0;
         
         todo = new ArrayList<String>();
//         memo = new String[total];
         
         while (rs.next()) {// Ŀ�� ��ĭ ����!

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
      ans_str = (String) JOptionPane.showInputDialog(this, "�� ���� �ۼ��ϼ���.", "���� �� ��", JOptionPane.PLAIN_MESSAGE, null,
            null, null);
      if(ans_str!=null) {
         
         System.out.println("ans_str : " + ans_str + member_id); // �Է��� �ܾ ���ϵȴ�.
         
         PreparedStatement pstmt2 = null;
         ResultSet rs = null;
         
         String sql_add = "insert into todo(todo_id, member_id, memo)";
         sql_add += " values(seq_todo.nextval,?,?)";
         
         try {
            pstmt2 = con.prepareStatement(sql_add); // ������ �غ�2
            
            pstmt2.setInt(1, member_id);
            pstmt2.setString(2, ans_str);
            
            int result = pstmt2.executeUpdate();
            
            if (result == 0) {
               JOptionPane.showMessageDialog(this, "���� ��Ͽ� �����߽��ϴ�.");
            } else {
               todo.add(ans_str);
               model.addElement(ans_str);
               list.setModel(model);
               
               
               JOptionPane.showMessageDialog(this, "���� ��� �����߽��ϴ�.");
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
      System.out.println("delete����  " + data_memo);
      PreparedStatement pstmt = null;
      int result = 0;

      String sql = "delete from todo where memo = ?";
      try {
         pstmt = con.prepareStatement(sql); // ���� �غ�
         
         pstmt.setString(1, data_memo);
         result = pstmt.executeUpdate();// ��������
         if (result == 0) {
            JOptionPane.showMessageDialog(this, "���� ������ �����߽��ϴ�.");
         } else {
       
            JOptionPane.showMessageDialog(this, "���� ���� �����߽��ϴ�.");
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
         pstmt = con.prepareStatement(sql); // ������ �غ�
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

//               l_nickName.setText(rs.getString(4) + "��");
//               member_id=rs.getInt(1);
//               l_id.setText("���̵� : " + rs.getString(2));
//               t_pw.setText(rs.getString(3));
//               t_nickName.setText(rs.getString(4));
//               t_email.setText(rs.getString(5));
//               t_phone.setText(rs.getString(6));

            // todo.add(rs.getString(3));
            // System.out.println(todo);
            count = rs.getInt(1);

         } else {
            JOptionPane.showMessageDialog(this, "�Է��Ͻ� ������ �������� �ʽ��ϴ�.");
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
            pstmt = con.prepareStatement(sql); // ������ �غ�
            pstmt.setInt(1, todo_id);
            // pstmt.setString(2, str_pw);

            rs = pstmt.executeQuery();
            if (rs.next()) {


               todo.add(rs.getString(3));
               System.out.println(todo);
            } else {
               JOptionPane.showMessageDialog(this, "�Է��Ͻ� ������ �������� �ʽ��ϴ�.");
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

   // ������ �õ��ϴ� �޼��� ����
   public void getConnection() {
      try {
         Class.forName(driver);// ����̹� �ε�
         con = DriverManager.getConnection(url, user, pass); // ���ӽõ� ��, ��ü ��ȯ
         if (con == null) { // ���ӽ����ΰ�� �޽��� ���
            JOptionPane.showMessageDialog(this, "�����ͺ��̽��� �������� ���߽��ϴ�.");
         } else {// ���� ������ ��� ������ ����â�� ���� ������ ���
//               this.setTitle(user + " ���� ��");
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

}