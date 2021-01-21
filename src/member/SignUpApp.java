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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import calendar.MainCalendarApp;

public class SignUpApp extends JFrame {

	JPanel signUp_center; // ��ü �г�
	JPanel p_title; // ȸ������ Ÿ��Ʋ �г�
	JPanel p_id; // ���̵� �г�
	JPanel p_pw; // �н����� �г�
	JPanel p_pwCh; // �н����� üũ �г�
	JPanel p_nickName; // �г��� �г�
	JPanel p_email; // �̸��� �г�
	JPanel p_phone; // ��ȭ��ȣ �г�
	JPanel p_bts; // ��ư �г�

	JLabel l_signUp; // ȸ������ Ÿ��Ʋ ��
	JLabel l_id; // ���̵� ��
	JLabel l_pw; // �н����� ��
	JLabel l_pwCh; // �н����� üũ ��
	JLabel l_nickName; // �г��� ��
	JLabel l_email; // �̸��� ��
	JLabel l_phone; // ��ȭ��ȣ ��

	JTextField t_id; // ���̵� �ؽ�Ʈ
	JPasswordField t_pw; // �н����� �ؽ�Ʈ
	JPasswordField t_pwCh; // �н����� üũ �ؽ�Ʈ
	JTextField t_nickName; // �г��� �ؽ�Ʈ
	JTextField t_email; // �̸��� �ؽ�Ʈ
	JTextField t_phone; // ��ȭ��ȣ �ؽ�Ʈ

	JButton bt_overlap; // ���̵� �ߺ� ��ư
	JButton bt_pwCh; // �н����� üũ ��ư
	JButton bt_prev; // ���� ��ư
	JButton bt_signUp; // ���� ��ư

	Connection con;
	MainCalendarApp mainCalendarApp;

	boolean idCh = false;
	boolean pwCh = false;

	ImageIcon icon_bg;
	ImageIcon icon_signUp;

	public SignUpApp(MainCalendarApp mainCalendarApp) {
		super.setTitle("ȸ������");
		this.mainCalendarApp = mainCalendarApp;
		con = mainCalendarApp.getCon();

		icon_bg = new ImageIcon("res/bg.jpg"); // ��� �̹���

		/* ���� */
		signUp_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
				setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
				super.paintComponent(g);
			}
		};
		p_title = new JPanel();
		p_id = new JPanel();
		p_pw = new JPanel();
		p_pwCh = new JPanel();
		p_nickName = new JPanel();
		p_email = new JPanel();
		p_phone = new JPanel();
		p_bts = new JPanel();

		l_signUp = new JLabel("ȸ������");
		l_id = new JLabel("���̵�");
		l_pw = new JLabel("�н�����");
		l_pwCh = new JLabel("�н����� Ȯ�� ");
		l_nickName = new JLabel("�г���  ");
		l_email = new JLabel("�̸���  ");
		l_phone = new JLabel("��ȭ��ȣ");

		t_id = new JTextField(20);
		t_pw = new JPasswordField(20);
		t_pwCh = new JPasswordField(20);
		t_nickName = new JTextField(20);
		t_email = new JTextField(20);
		t_phone = new JTextField(20);

		bt_overlap = new JButton("�ߺ� �˻�");
		bt_pwCh = new JButton("Ȯ��");
		bt_prev = new JButton("����");
		bt_signUp = new JButton("����");
		
		icon_signUp = new ImageIcon("res/logo_signUp.png");
		l_signUp = new JLabel(icon_signUp);
		// �гο� �ΰ� �ֱ� @
		Image img = icon_signUp.getImage();
		img = img.getScaledInstance(100, 90, Image.SCALE_SMOOTH);
		icon_signUp.setImage(img);


		/* ũ�� ���� */
		signUp_center.setPreferredSize(new Dimension(1200, 700));
		p_title.setPreferredSize(new Dimension(1200, 100));
		p_id.setPreferredSize(new Dimension(1200, 40));
		p_pw.setPreferredSize(new Dimension(1200, 40));
		p_pwCh.setPreferredSize(new Dimension(1200, 40));
		p_nickName.setPreferredSize(new Dimension(1200, 40));
		p_email.setPreferredSize(new Dimension(1200, 40));
		p_phone.setPreferredSize(new Dimension(1200, 40));
		p_bts.setPreferredSize(new Dimension(1200, 100));

//		l_id.setPreferredSize(new Dimension(150, 40));
//		l_pw.setPreferredSize(new Dimension(150, 40));

		/* ��Ʈ ���� */
		l_signUp.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_pw.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_pwCh.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_nickName.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		l_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_pw.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_pwCh.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_nickName.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_email.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_phone.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_overlap.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_prev.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_pwCh.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_signUp.setFont(new Font("SansSerif", Font.BOLD, 20));

		/* ��Ÿ�� */
		signUp_center.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_id.setBackground(new Color(255, 0, 0, 0));
		p_pw.setBackground(new Color(255, 0, 0, 0));
		p_pwCh.setBackground(new Color(255, 0, 0, 0));
		p_nickName.setBackground(new Color(255, 0, 0, 0));
		p_email.setBackground(new Color(255, 0, 0, 0));
		p_phone.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		/* ���� */
		add(signUp_center);
		signUp_center.add(p_title, BorderLayout.NORTH);
		signUp_center.add(p_id);
		signUp_center.add(p_pw);
		signUp_center.add(p_pwCh);
		signUp_center.add(p_nickName);
		signUp_center.add(p_email);
		signUp_center.add(p_phone);
		signUp_center.add(p_bts);
		p_title.add(l_signUp, BorderLayout.SOUTH);
		p_id.add(l_id);
		p_id.add(t_id);
		p_id.add(bt_overlap);
		p_pw.add(l_pw);
		p_pw.add(t_pw);
		p_pwCh.add(l_pwCh);
		p_pwCh.add(t_pwCh);
		p_pwCh.add(bt_pwCh);
		p_nickName.add(l_nickName);
		p_nickName.add(t_nickName);
		p_email.add(l_email);
		p_email.add(t_email);
		p_phone.add(l_phone);
		p_phone.add(t_phone);
		p_bts.add(bt_prev, BorderLayout.WEST);
		p_bts.add(bt_signUp, BorderLayout.CENTER);
		
		
//		bt_overlap.setBorderPainted(true);
//		bt_overlap.setContentAreaFilled(false);
//		bt_pwCh.setBorderPainted(true);
//		bt_pwCh.setContentAreaFilled(true);
//		bt_prev.setBorderPainted(true);
//		bt_prev.setContentAreaFilled(false);
//		bt_signUp.setBorderPainted(true);
//		bt_signUp.setContentAreaFilled(false);
//		bt_signUp.setFocusPainted(false);

		setSize(800, 500);
//		setVisible(true);
		setLocationRelativeTo(null);

		/* ��ư ������ */
		bt_overlap.addActionListener((e) -> {
			overlap();
		});
		bt_pwCh.addActionListener((e) -> {
			passwordCheck();
		});
		bt_prev.addActionListener((e) -> {
			mainCalendarApp.setPage(MainCalendarApp.Member_LoginApp);
		});
		bt_signUp.addActionListener((e) -> {
			regist();
		});

		// ������ ���μ��� ����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void overlap() {
		if(!t_id.getText().equals("")) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from calendar_member where m_id=?";
			try {
				pstmt = con.prepareStatement(sql); // ������ �غ�
				pstmt.setString(1, t_id.getText());
				
				rs = pstmt.executeQuery();
				if (rs.next()) { // ���ڵ尡 �����Ѵٸ� ���̵� ���� �Ұ�
					idCh = false;
					JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵��Դϴ�.");
					t_id.setText("");
				} else { // ���ڵ尡 �������� �ʴ´ٸ� ���̵� ���� ����
					idCh = true;
					JOptionPane.showMessageDialog(this, "��� ������ ���̵��Դϴ�.");
					
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
		} else {
			JOptionPane.showMessageDialog(this, "���̵� �Է��Ͻÿ�.");
		}
	}

	public void passwordCheck() {
		if(!String.valueOf(t_pw.getPassword()).equals("")) {
			if (String.valueOf(t_pw.getPassword()).equals(String.valueOf(t_pwCh.getPassword()))) {
//			System.out.println("��ġ");
				pwCh = true;
				JOptionPane.showMessageDialog(this, "��й�ȣ ��ġ�մϴ�.");
			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ ��ġ���� �ʽ��ϴ�.");
			}
		}else {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��Ͻÿ�.");
		}
	}

	public void regist() {
		if (!t_id.getText().equals("") && !t_pw.getPassword().equals("") && !t_pwCh.getPassword().equals("")
				&& !t_nickName.getText().equals("") && !t_email.getText().equals("") && !t_phone.getText().equals("")) {
			if (idCh == true) { // ���̵� �ߺ� �˻� ���� ��
				if (pwCh == true) { // �н����� ���� �˻� ���� ��

					String sql = "insert into calendar_member(member_id,m_id,m_pw,m_nickName,m_email,m_phone)";
					sql += " values(seq_calendar_member.nextval, ?,?,?,?,?)";

					PreparedStatement pstmt = null; // �� ���������� ���� �־�� ��
//					System.out.println("����");
					try {
						pstmt = con.prepareStatement(sql);// ���������� �غ�
						pstmt.setString(1, t_id.getText());

						pstmt.setString(2, new String(t_pw.getPassword())); // char �迭�� ��Ʈ������...
						pstmt.setString(3, t_nickName.getText());
						pstmt.setString(4, t_email.getText());
						pstmt.setString(5, t_phone.getText());

						// DML �� ���, �� �������࿡ ���� ������ ���� ���ڵ���� ��ȯ�ǹǷ� , ���� 0�� ��ȯ�ȴٸ� ���з� �Ǵ�!!
						int result = pstmt.executeUpdate(); // DML�� ���� executeUpdate(), select�� executeQuery()
						if (result == 0) {
							JOptionPane.showMessageDialog(this, "���Կ� �����Ͽ����ϴ�\n�����ڿ� �����ּ���");
						} else {
							JOptionPane.showMessageDialog(this, "������ ���ϵ����^^");

							mainCalendarApp.setPage(MainCalendarApp.Member_LoginApp);
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
				} else {
					JOptionPane.showMessageDialog(this, "�н����� ���� �˻� üũ���ּ���.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "���̵� �ߺ� �˻� üũ���ּ���.");
			}

		} else {
			JOptionPane.showMessageDialog(this, "��� ������ �Է����ּ���.");
		}
	}

}
