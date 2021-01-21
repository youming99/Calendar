package member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class FindPw extends JFrame {

	JPanel p_center; // ��ü �г�
	JPanel p_title; // Ÿ��Ʋ �г�
	JPanel p_id; // ���̵� �г�
	JPanel p_email; // �̸��� �г�
	JPanel p_phone; // ��ȭ��ȣ �г�
	JPanel p_bts; // ��ư �г�

	JLabel l_title; // ��й�ȣ ã�� ��
	JLabel l_id; // ���̵� ��
	JLabel l_email; // �̸��� ��
	JLabel l_phone; // ��ȭ��ȣ ��

	JTextField t_id; // ���̵� �ؽ�Ʈ �ʵ�
	JTextField t_email; // �̸��� �ؽ�Ʈ �ʵ�
	JTextField t_phone; // ��ȭ��ȣ �ؽ�Ʈ �ʵ�

	JButton bt_cancel; // ��� ��ư
	JButton bt_submit; // Ȯ�� ��ư

	ImageIcon icon_bg;
	ImageIcon icon_findPw;

	Connection con;
	String str_pw = null;

	// ���ӿ� �ʿ��� ������..
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "projectCalendar";
	private String pass = "1234";

	public FindPw() {
		super.setTitle("��й�ȣ ã��");
		this.getConnection();

		icon_bg = new ImageIcon("res/bg.jpg"); // ��� �̹���
		/* ���� */
		p_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
				setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
				super.paintComponent(g);
			}
		};
		p_title = new JPanel();
		p_id = new JPanel();
		p_email = new JPanel();
		p_phone = new JPanel();
		p_bts = new JPanel();

		l_title = new JLabel("��й�ȣ ã��");
		l_id = new JLabel("���̵�");
		l_email = new JLabel("�̸���");
		l_phone = new JLabel("��ȭ��ȣ");

		t_id = new JTextField(20);
		t_email = new JTextField(20);
		t_phone = new JTextField(20);

		bt_cancel = new JButton("���");
		bt_submit = new JButton("Ȯ��");

		icon_findPw = new ImageIcon("res/logo_findPw.png");
		l_title = new JLabel(icon_findPw);
		// �гο� �ΰ� �ֱ� @
		Image img = icon_findPw.getImage();
		img = img.getScaledInstance(120, 60, Image.SCALE_SMOOTH);
		icon_findPw.setImage(img);

		/* ũ�� */
		p_center.setPreferredSize(new Dimension(800, 400));
		p_title.setPreferredSize(new Dimension(800, 80));
		p_id.setPreferredSize(new Dimension(800, 40));
		p_email.setPreferredSize(new Dimension(800, 40));
		p_phone.setPreferredSize(new Dimension(800, 40));
		p_bts.setPreferredSize(new Dimension(800, 60));

		/* ��Ÿ�� */
		p_center.setBackground(new Color(255, 0, 0, 0));
		p_id.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_email.setBackground(new Color(255, 0, 0, 0));
		p_phone.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		l_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_cancel.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_submit.setFont(new Font("SansSerif", Font.BOLD, 20));

		/* ���� */
		add(p_center);
		p_center.add(p_title, BorderLayout.SOUTH);
		p_center.add(p_id);
		p_center.add(p_email);
		p_center.add(p_phone);
		p_center.add(p_bts);
		p_title.add(l_title);
		p_id.add(l_id);
		p_id.add(t_id);
		p_email.add(l_email);
		p_email.add(t_email);
		p_phone.add(l_phone);
		p_phone.add(t_phone);
		p_bts.add(bt_cancel);
		p_bts.add(bt_submit);

		/* ���콺 ������ */
		bt_cancel.addActionListener((e) -> {
			setVisible(false);
		});
		bt_submit.addActionListener((e) -> {
			findPW();
		});

		setSize(800, 400);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// ������ ���μ��� ����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void findPW() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from calendar_member where m_id = ? and m_email=? and m_phone=?";
		try {
			pstmt = con.prepareStatement(sql); // ������ �غ�
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, t_email.getText());
			pstmt.setString(3, t_phone.getText());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString(3)); // m_pw

				str_pw = rs.getString(3);
				JOptionPane.showMessageDialog(this, "ȸ������ ��й�ȣ�� " + str_pw + " �Դϴ�.");

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

	// ������ �õ��ϴ� �޼��� ����
	public void getConnection() {
		try {
			Class.forName(driver);// ����̹� �ε�
			con = DriverManager.getConnection(url, user, pass); // ���ӽõ� ��, ��ü ��ȯ
			if (con == null) { // ���ӽ����ΰ�� �޽��� ���
				JOptionPane.showMessageDialog(this, "�����ͺ��̽��� �������� ���߽��ϴ�.");
			} else {// ���� ������ ��� ������ ����â�� ���� ������ ���
//				this.setTitle(user + " ���� ��");
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

	public static void main(String[] args) {
		new FindPw();
	}
}
