package d_day;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import calendar.MainCalendarApp;
import member.CalendarMember;

public class D_Day_Update extends JFrame {

	JPanel p_center;
	JPanel p_title;
	JPanel p_name;
	JPanel p_date;
	JPanel p_bts;

	JLabel l_title;
	JLabel l_name;
	JLabel l_year;
	JLabel l_month;
	JLabel l_day;

	JTextField t_name;
	JTextField t_year;
	JTextField t_month;
	JTextField t_day;

	JButton bt_cancel;
	JButton bt_regist;

	Connection con;

	// ���ӿ� �ʿ��� ������..
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "projectCalendar";
	private String pass = "1234";

	String dday_id = null;
	int member_id = 0;

	MainCalendarApp mainCalendarApp;
	
	ImageIcon icon_bg;

	public D_Day_Update(String dday_id) {
		this.dday_id = dday_id;
		this.setTitle("���� ����");
		this.getConnection();
		
		icon_bg = new ImageIcon("res/bg.jpg"); // ��� �̹���

		/* ���� */
		p_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 600, null);
				setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
				super.paintComponent(g);
			}
		};
		p_title = new JPanel();
		p_name = new JPanel();
		p_date = new JPanel();
		p_bts = new JPanel();

		l_title = new JLabel("���� ����");
		l_name = new JLabel("���� �̸� : ");
		l_year = new JLabel(" ��");
		l_month = new JLabel(" ��");
		l_day = new JLabel(" ��");

		t_name = new JTextField(20);
		t_year = new JTextField(10);
		t_month = new JTextField(10);
		t_day = new JTextField(10);

		bt_cancel = new JButton("����");
		bt_regist = new JButton("����");

		/* ũ�� */
		p_center.setPreferredSize(new Dimension(800, 600));
		p_title.setPreferredSize(new Dimension(800, 100));
		p_name.setPreferredSize(new Dimension(800, 100));
		p_bts.setPreferredSize(new Dimension(800, 300));

		/* ��Ʈ ũ�� */
		l_title.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_name.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_year.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_month.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_day.setFont(new Font("SansSerif", Font.BOLD, 30));
		t_name.setFont(new Font("SansSerif", Font.BOLD, 30));
		t_year.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_month.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_day.setFont(new Font("SansSerif", Font.BOLD, 20));

		/* ��Ÿ�� */
		p_center.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_name.setBackground(new Color(255, 0, 0, 0));
		p_date.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		/* ���� */
		add(p_center);
		p_center.add(p_title);
		p_center.add(p_name);
		p_center.add(p_date);
		p_center.add(p_bts);
		p_title.add(l_title);
		p_name.add(l_name);
		p_name.add(t_name);
		p_date.add(t_year);
		p_date.add(l_year);
		p_date.add(t_month);
		p_date.add(l_month);
		p_date.add(t_day);
		p_date.add(l_day);
		p_bts.add(bt_cancel);
		p_bts.add(bt_regist);
		
		getUser();

		/* ���콺 ������ */
		bt_cancel.addActionListener((e) -> {
			setVisible(false);
		});
		bt_regist.addActionListener((e) -> {
			regist();
		});

		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/* ���� ���� �������� */
	public void getUser() {
		System.out.println("����");
		//�α����� ������ �����͸� �����;��� (calendar_member ���̺��� member_id ������)
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		String sql_member = "select * from d_day where dday_id =" + dday_id;
		
		try {
			pstmt = con.prepareStatement(sql_member); // ������ �غ�1
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String dday_name = rs.getString(3);
				String dday_year = rs.getString(4);
				String dday_month = rs.getString(5);
				String dday_day = rs.getString(6);
				System.out.println(dday_name);
				t_name.setText(dday_name);
				t_year.setText(dday_year);
				t_month.setText(dday_month);
				t_day.setText(dday_day);
				
			} else {
//				JOptionPane.showMessageDialog(this, "���̵�� �н����带 �ٽ� �Է����ּ���");
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
	
	
	/* ���� �߰� */
	public void regist() {
		if(!t_name.getText().equals("") && !t_year.getText().equals("") && 
				!t_month.getText().equals("") && !t_day.getText().equals("")) {
			
			PreparedStatement pstmt2 = null;
			ResultSet rs = null;
			String sql = "update calendar_member set m_pw = ?,m_nickName = ?,m_email= ?,m_phone = ? where m_id =? and m_pw= ?";
			String sql_add = "update d_day set d_name = ?, d_year = ?, d_month = ?, d_day = ? where dday_id = ?";
			try {
				pstmt2 = con.prepareStatement(sql_add); // ������ �غ�2
				
				pstmt2.setString(1, t_name.getText());
				pstmt2.setString(2, t_year.getText());
				pstmt2.setString(3, t_month.getText());
				pstmt2.setString(4, t_day.getText());
				pstmt2.setString(5, dday_id);
				
				int result = pstmt2.executeUpdate();
				if (result == 0) {
					JOptionPane.showMessageDialog(this, "���� ������ �����߽��ϴ�.");
				} else {
					JOptionPane.showMessageDialog(this, "���� ���� �����߽��ϴ�.");
					
					setVisible(false);
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
		} else {
			JOptionPane.showMessageDialog(this, "������ �� �Է����ּ���.");
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

//	public static void main(String[] args) {
//		new D_Day_Add();
//	}

}
