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

public class FindId extends JFrame {

	JPanel p_center; // ��ü �г�
	JPanel p_title; // Ÿ��Ʋ �г�
	JPanel p_email; // �̸��� �г�
	JPanel p_phone; // ��ȭ��ȣ �г�
	JPanel p_bts; // ��ư �г�

	JLabel l_title; // ���̵� ã�� ��
	JLabel l_email; // �̸��� ��
	JLabel l_phone; // ��ȭ��ȣ ��

	JTextField t_email; // �̸��� �ؽ�Ʈ �ʵ�
	JTextField t_phone; // ��ȭ��ȣ �ؽ�Ʈ �ʵ�

	JButton bt_cancel; // ��� ��ư
	JButton bt_submit; // Ȯ�� ��ư

	Connection con;
	String str_id = null;

	ImageIcon icon_bg;
	ImageIcon icon_findId;

	// ���ӿ� �ʿ��� ������..
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "projectCalendar";
	private String pass = "1234";

	public FindId() {
		super.setTitle("���̵� ã��");
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
		p_email = new JPanel();
		p_phone = new JPanel();
		p_bts = new JPanel();

		l_title = new JLabel("���̵� ã��");
		l_email = new JLabel("�̸���");
		l_phone = new JLabel("��ȭ��ȣ");

		t_email = new JTextField(20);
		t_phone = new JTextField(20);

		bt_cancel = new JButton("���");
		bt_submit = new JButton("Ȯ��");

		icon_findId = new ImageIcon("res/logo_findId.png");
		l_title = new JLabel(icon_findId);
		// �гο� �ΰ� �ֱ� @
		Image img = icon_findId.getImage();
		img = img.getScaledInstance(120, 110, Image.SCALE_SMOOTH);
		icon_findId.setImage(img);

		/* ũ�� */
		p_center.setPreferredSize(new Dimension(800, 400));
		p_title.setPreferredSize(new Dimension(800, 110));
		p_email.setPreferredSize(new Dimension(800, 40));
		p_phone.setPreferredSize(new Dimension(800, 40));
		p_bts.setPreferredSize(new Dimension(800, 60));

		/* ��Ÿ�� */
		p_center.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_email.setBackground(new Color(255, 0, 0, 0));
		p_phone.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		l_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_cancel.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_submit.setFont(new Font("SansSerif", Font.BOLD, 20));

		/* ���� */
		add(p_center);
		p_center.add(p_title, BorderLayout.SOUTH);
		p_center.add(p_email);
		p_center.add(p_phone);
		p_center.add(p_bts);
		p_title.add(l_title);
		p_email.add(l_email);
		p_email.add(t_email);
		p_phone.add(l_phone);
		p_phone.add(t_phone);
		p_bts.add(bt_cancel);
		p_bts.add(bt_submit);

//		bt_cancel.setBorderPainted(true);
//		bt_cancel.setContentAreaFilled(false);

		/* ���콺 ������ */
		bt_cancel.addActionListener((e) -> {
			setVisible(false);
		});
		bt_submit.addActionListener((e) -> {
			findID();
		});

		setSize(800, 300);
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

	public void findID() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from calendar_member where m_email=? and m_phone=?";
		try {
			pstmt = con.prepareStatement(sql); // ������ �غ�
			pstmt.setString(1, t_email.getText());
			pstmt.setString(2, t_phone.getText());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString(1)); // member_id
				System.out.println(rs.getString(2)); // m_id

				str_id = rs.getString(2);
				JOptionPane.showMessageDialog(this, "ȸ������ ���̵�� " + str_id + " �Դϴ�.");

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
		new FindId();
	}
}
