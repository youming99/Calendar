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

   JPanel p_center; // ��ü �г�
   JPanel p_title; // Ÿ��Ʋ �г�
   JPanel p_table; // ���̺� �г�
   JPanel p_bts; // ��ư�� �г�
   JTable dday_list; // ���� ����Ʈ(���̺�)
   JScrollBar scroll; // ��ũ��

   JLabel l_title; // Ÿ��Ʋ �׽�Ʈ �ʵ�

   JButton bt_cancel;
   JButton bt_add;

   D_Day_Add dday_add;
   D_Day_Update dday_update;
   D_Day_Model dday_model;
   MainCalendarApp maniCalendarApp;

   Connection con;

   // ���ӿ� �ʿ��� ������..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";

   String str_id = null; // ������ ���̵�
   String str_pw = null; // ������ ��й�ȣ
   int member_id;

   JLabel logo;
   ImageIcon icon_bg;
   ImageIcon icon_bg2;

   D_Day_List ddayList = null; // ����, �����ÿ��� �� �ȿ� ����ִ� �������� Ȱ���ϱ� ����
                        // ���������� �������� �ʰ�, ��������� ����

   String dday_id = null;

   public D_DayApp(int memberid,String id, String pw) {
      super.setTitle("���� ���");
      this.member_id = memberid;
      this.str_id = id;
      this.str_pw = pw;
      this.getConnection();

      icon_bg = new ImageIcon("res/bg.jpg"); // ��� �̹���
      icon_bg2 = new ImageIcon("res/bg2.jpg"); // ��� �̹���

      System.out.println(str_id + "/ " + str_pw + "/" + member_id);
      /* ���� */
      p_center = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 800, 600, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      p_title = new JPanel();
      p_table = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };
      p_bts = new JPanel();
      scroll = new JScrollBar();
      dday_list = new JTable(dday_model = new D_Day_Model());

      l_title = new JLabel("���� ���");

      bt_cancel = new JButton("����");
      bt_add = new JButton("���� �߰�");

      /* ũ�� */
      p_center.setPreferredSize(new Dimension(800, 600));
      p_title.setPreferredSize(new Dimension(800, 80));
      dday_list.setPreferredSize(new Dimension(750, 400));
      p_bts.setPreferredSize(new Dimension(800, 600));

      /* ��Ʈ ũ�� */
      l_title.setFont(new Font("SansSerif", Font.BOLD, 30));

      dday_list.setRowHeight(30);
      dday_list.setFont(new Font("SansSerif", Font.BOLD, 20));

      /* ��Ÿ�� */
//      p_center.setBackground(new Color(255, 0, 0, 0)));
      p_title.setBackground(new Color(255, 0, 0, 0));
      dday_list.setBackground(new Color(255, 0, 0, 0));
      p_table.setBackground(new Color(255, 0, 0, 0));
      p_bts.setBackground(new Color(255, 0, 0, 0));

      getList();

      /* ���� */
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
            if (e.getClickCount() == 2) { // �� ����Ŭ��
               System.out.println("����Ŭ��");
               System.out.println(dday_id);
               String[] buttons = { "����", "����" };
               int result = JOptionPane.showOptionDialog(null, "�ش� ������ ������ �����ϼ���.", "���� �ɼ�", 0,
                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
               if (result == 0) { // ����
                  System.out.println("����");
                  update(dday_id);
                  getList();
               } else if (result == 1) { // ����
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

   /* ���� ��� �������� */
   public void getList() {

      String str_year = null; // DB���� ������ ����
      String str_month = null; // DB���� ������ ��
      String str_day = null; // DB���� ������ ��
      String str_allDate = null; // DB���� ������ ��¥�� �� �ٷ�

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      String sql = "select * from d_day where member_id = ? order by dday_id asc";

      try {
         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
         // ��ũ���� �����ϰ�(next()�ɷ�+ ��������� �̵��ɷ�), �б������� rs�� ����� ���� �ɼ�!!
         pstmt.setInt(1, member_id);
         rs = pstmt.executeQuery();
         rs.last();
         int total = rs.getRow(); // �� ���ڵ� �� ��ȯ

         // rs�� ����ִ� �����͸� �������迭�� �Űܽɾ��!!
         // �׷��� ���ؼ��� ���� ������ �迭�� �غ��س���!!
         String[][] records = new String[total][4];

         rs.beforeFirst();

         int index = 0;
         while (rs.next()) {// Ŀ�� ��ĭ ����!
            String[] arr = new String[4];
            arr[0] = rs.getString("dday_id"); // ��ȣ
            arr[1] = rs.getString("d_name"); // ���� �̸�

            str_year = rs.getString("d_year");
            str_month = rs.getString("d_month");
            str_day = rs.getString("d_day");
            str_allDate = str_year + " ��" + str_month + "��" + str_day + "��";
            arr[2] = str_allDate; // ��¥

            // ���� ���
            Calendar today = Calendar.getInstance(); // ���� ���� ��¥
            int tyear = today.get(Calendar.YEAR);
            int tmonth = today.get(Calendar.MONTH);
//            tmonth += 1;
            int tday = today.get(Calendar.DAY_OF_MONTH);

//            System.out.println("����"+tyear + "/" + (tmonth + 1) + "/" + tday);
//            System.out.println("����" + str_year + "/" + (str_month) + "/" + str_day);

            Calendar dday = Calendar.getInstance(); // ���� ��¥
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

            records[index++] = arr; // ������ �迭�� �������迭�� �濡 ����
         }

         // �������迭�� ��� �ϼ��Ǿ����Ƿ�, TableModel�� ������ data �������迭�� ����!!
         dday_model.data = records;
         dday_list.updateUI();// jtable UI ����
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

   /* ���� �߰� */
   public void add() {
      dday_add = new D_Day_Add(member_id, str_id, str_pw);
      dday_add.setVisible(true);
      setVisible(false);
   }
   
   /* ���� ���� */
   public void update(String dday_id) {
      dday_update = new D_Day_Update(dday_id);
      dday_update.setVisible(true);
      setVisible(false);
   }

   /* ���� ���� */
   public void delete(String dday_id) {
      PreparedStatement pstmt = null;
      int result = 0;

      String sql = "delete from d_day where dday_id=" + dday_id;
      try {
         pstmt = con.prepareStatement(sql); // ���� �غ�
         result = pstmt.executeUpdate();// ��������
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

//   public static void main(String[] args) {
//      new D_DayApp();
//   }
}