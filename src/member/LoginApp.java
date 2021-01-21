package member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
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

public class LoginApp extends JFrame {
	LoginApp login;

	JPanel p_logo; // 로고를 넣을 패널
	JPanel p_input; // 아이디 패스워드 로그인버튼 넣을 패널
	JPanel p_signfind; // 회원가입 아이디/비밀번호찾기 넣을 패널
	JPanel p_exit; // 종료 넣을 패널
	JPanel p_idpass; // 아이디 패스워드 패널 붙일 패널
	JPanel p_id;// 아이디입력창 넣을 패널
	JPanel p_pass;// 패스워드 입력창 넣을 패널
	JPanel p_loginbt;// 로그인버튼 넣을 패널
	JPanel p_loginfind;
	JPanel p_signup;
	JPanel p_find;
	JPanel p_all;

	public JTextField t_id;
	public JPasswordField t_pass;
	JButton bt_login;

	JLabel signup;
	JLabel find_id;
	JLabel find_pass;
	JLabel l_id;
	JLabel l_pass;
	JLabel exit;

//   Image icon;

	JLabel logo;
	ImageIcon icon_login;
	ImageIcon icon_bg;

	Connection con;

	MainCalendarApp mainCalendarApp;

	public boolean loginCheck = false;
	boolean hasSession;

	String str_nickName = null;
	String str_id = null;
	String str_pw = null;
	int member_id;

	public LoginApp(MainCalendarApp mainCalendarApp) {
		super.setTitle("로그인");
		this.mainCalendarApp = mainCalendarApp;
		con = mainCalendarApp.getCon();

		icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지


		p_logo = new JPanel();
		p_input = new JPanel();
		p_signfind = new JPanel();
		p_exit = new JPanel();
		p_idpass = new JPanel();
		p_id = new JPanel();
		p_pass = new JPanel();
		p_loginbt = new JPanel();
		p_signup = new JPanel();
		p_find = new JPanel();
		p_loginfind = new JPanel();
		p_all = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};

		l_id = new JLabel("아이디     ");
		l_pass = new JLabel("비밀번호  ");

		t_id = new JTextField(20);
		t_pass = new JPasswordField(20);
		bt_login = new JButton("로그인");

		signup = new JLabel("회원가입");
		find_id = new JLabel("아이디");
		find_pass = new JLabel("/비밀번호 찾기");
		exit = new JLabel("종료");

		icon_login = new ImageIcon("res/logo_login.png");
		logo = new JLabel(icon_login);
		// 패널에 로고 넣기 @
		Image img = icon_login.getImage();
		img = img.getScaledInstance(100, 80, Image.SCALE_SMOOTH);
		icon_login.setImage(img);

//		Image img = icon_login.getImage();
//		img = img.getScaledInstance(100, 45, Image.SCALE_SMOOTH);
//		icon_login.setImage(img);

		logo.setPreferredSize(new Dimension(100, 150));
		p_logo.add(logo);
		p_logo.setBackground(new Color(255, 0, 0, 0));
		p_logo.setPreferredSize(new Dimension(1200, 200));

		// 아이디 패널
		p_id.add(l_id);
		p_id.add(t_id);
		p_id.setBackground(new Color(255, 0, 0, 0));

		// 패스워드 패널
		p_pass.add(l_pass);
		p_pass.add(t_pass);
		p_pass.setBackground(new Color(255, 0, 0, 0));

		// 그리드패널에 아이디 패스워드 부착
		p_idpass.setLayout(new GridLayout(2, 1));
		p_idpass.add(p_id);
		p_idpass.add(p_pass);
		p_idpass.setBackground(new Color(255, 0, 0, 0));

		// 로그인 버튼 패널
		p_loginbt.add(bt_login);
		p_loginbt.setBackground(new Color(255, 0, 0, 0));

		// 아이디 패스워드 로그인버튼 부착 @
		p_input.add(p_idpass, BorderLayout.WEST);
		p_input.add(p_loginbt, BorderLayout.EAST);
		p_input.setPreferredSize(new Dimension(1200, 100));
		p_input.setBackground(new Color(255, 0, 0, 0));

		// 패널에 회원가입, 아이디/비밀번호 찾기 부착 @
		p_signfind.add(p_signup, BorderLayout.WEST);
		p_signfind.add(p_find, BorderLayout.EAST);
		p_signfind.setBackground(new Color(255, 0, 0, 0));
		p_signfind.setPreferredSize(new Dimension(1200, 200));

		// 패널에 회원가입 부착
		p_signup.add(signup);
		p_signup.setBackground(new Color(255, 0, 0, 0));

		// 패널에 아이디/비밀번호 찾기 부착
		p_find.add(find_id);
		p_find.add(find_pass);
		p_find.setBackground(new Color(255, 0, 0, 0));

		// 패널에 종료 부착 @
		exit.setBackground(new Color(255, 0, 0, 0));
		p_exit.add(exit);
		p_exit.setBackground(new Color(255, 0, 0, 0));
		p_exit.setPreferredSize(new Dimension(1200, 100));

		p_all.add(p_logo);
		p_all.add(p_input);
		p_all.add(p_signup);
		p_all.add(p_find);
		p_all.add(p_exit);
		p_all.setPreferredSize(new Dimension(800, 500));

		setBackground(new Color(255, 0, 0, 0));
		add(p_all);

		/* 폰트 크기 */
		l_id.setFont(new Font("SansSerif", Font.BOLD, 25));
		l_pass.setFont(new Font("SansSerif", Font.BOLD, 25));
		signup.setFont(new Font("SansSerif", Font.BOLD, 20));
		find_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		find_pass.setFont(new Font("SansSerif", Font.BOLD, 20));
		exit.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_id.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_pass.setFont(new Font("SansSerif", Font.BOLD, 20));
		bt_login.setFont(new Font("SansSerif", Font.BOLD, 20));
		
		bt_login.setBorderPainted(true);
		bt_login.setContentAreaFilled(false);

		setVisible(true);
		setSize(800, 500);
		setLocationRelativeTo(null);

		bt_login.addActionListener((e) -> {
			login();
		});

		signup.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mainCalendarApp.setPage(MainCalendarApp.Member_SignUpApp);
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
		find_id.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				FindId findID = new FindId();
				findID.setVisible(true);
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
		find_pass.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				FindPw findPW = new FindPw();
				findPW.setVisible(true);
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
		exit.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
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

		// 닫으면 프로세스 종료
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void login() {
		if (!t_id.getText().equals("") && !t_pass.getPassword().equals("")) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from calendar_member where m_id=? and m_pw=?";
			try {
				pstmt = con.prepareStatement(sql); // 쿼리문 준비
				pstmt.setString(1, t_id.getText());
				pstmt.setString(2, new String(t_pass.getPassword()));

				rs = pstmt.executeQuery();
				if (rs.next()) {
//					JOptionPane.showMessageDialog(this, "로그인성공");
					member_id = rs.getInt("member_id");
					// 회원정보 채워넣기!!
					CalendarMember calendarMember = new CalendarMember(); // empty
					calendarMember.setMember_id(rs.getInt("member_id"));// id
					calendarMember.setM_id(rs.getString("m_id"));// pk
					calendarMember.setM_pw(rs.getString("m_pw"));// pass
					calendarMember.setM_nickName(rs.getString("m_nickName"));// nickName
					calendarMember.setM_email(rs.getString("m_email"));// email
					calendarMember.setM_phone(rs.getString("m_phone"));// phone

					// LoginApp가 보유한 회원정보 객체에 주소값 전달!
					mainCalendarApp.setCalendarMember(calendarMember);
					str_nickName = mainCalendarApp.getCalendarMember().getM_nickName();
					str_id = mainCalendarApp.getCalendarMember().getM_id();
					str_pw = mainCalendarApp.getCalendarMember().getM_pw();
					mainCalendarApp.setHasSession(true);
					mainCalendarApp.setVisible(true);
					mainCalendarApp.loginCheck(mainCalendarApp.isHasSession(), str_nickName, str_id, str_pw, member_id);
					this.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(this, "아이디와 패스워드를 다시 입력해주세요");
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
			JOptionPane.showMessageDialog(this, "아이디와 패스워드를 입력하세요");
		}

	}

}