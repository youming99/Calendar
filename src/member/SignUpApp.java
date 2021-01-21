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

	JPanel signUp_center; // 전체 패널
	JPanel p_title; // 회원가입 타이틀 패널
	JPanel p_id; // 아이디 패널
	JPanel p_pw; // 패스워드 패널
	JPanel p_pwCh; // 패스워드 체크 패널
	JPanel p_nickName; // 닉네임 패널
	JPanel p_email; // 이메일 패널
	JPanel p_phone; // 전화번호 패널
	JPanel p_bts; // 버튼 패널

	JLabel l_signUp; // 회원가입 타이틀 라벨
	JLabel l_id; // 아이디 라벨
	JLabel l_pw; // 패스워드 라벨
	JLabel l_pwCh; // 패스워드 체크 라벨
	JLabel l_nickName; // 닉네임 라벨
	JLabel l_email; // 이메일 라벨
	JLabel l_phone; // 전화번호 라벨

	JTextField t_id; // 아이디 텍스트
	JPasswordField t_pw; // 패스워드 텍스트
	JPasswordField t_pwCh; // 패스워드 체크 텍스트
	JTextField t_nickName; // 닉네임 텍스트
	JTextField t_email; // 이메일 텍스트
	JTextField t_phone; // 전화번호 텍스트

	JButton bt_overlap; // 아이디 중복 버튼
	JButton bt_pwCh; // 패스워드 체크 버튼
	JButton bt_prev; // 이전 버튼
	JButton bt_signUp; // 가입 버튼

	Connection con;
	MainCalendarApp mainCalendarApp;

	boolean idCh = false;
	boolean pwCh = false;

	ImageIcon icon_bg;
	ImageIcon icon_signUp;

	public SignUpApp(MainCalendarApp mainCalendarApp) {
		super.setTitle("회원가입");
		this.mainCalendarApp = mainCalendarApp;
		con = mainCalendarApp.getCon();

		icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지

		/* 생성 */
		signUp_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
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

		l_signUp = new JLabel("회원가입");
		l_id = new JLabel("아이디");
		l_pw = new JLabel("패스워드");
		l_pwCh = new JLabel("패스워드 확인 ");
		l_nickName = new JLabel("닉네임  ");
		l_email = new JLabel("이메일  ");
		l_phone = new JLabel("전화번호");

		t_id = new JTextField(20);
		t_pw = new JPasswordField(20);
		t_pwCh = new JPasswordField(20);
		t_nickName = new JTextField(20);
		t_email = new JTextField(20);
		t_phone = new JTextField(20);

		bt_overlap = new JButton("중복 검사");
		bt_pwCh = new JButton("확인");
		bt_prev = new JButton("이전");
		bt_signUp = new JButton("가입");
		
		icon_signUp = new ImageIcon("res/logo_signUp.png");
		l_signUp = new JLabel(icon_signUp);
		// 패널에 로고 넣기 @
		Image img = icon_signUp.getImage();
		img = img.getScaledInstance(100, 90, Image.SCALE_SMOOTH);
		icon_signUp.setImage(img);


		/* 크기 지정 */
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

		/* 폰트 지정 */
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

		/* 스타일 */
		signUp_center.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_id.setBackground(new Color(255, 0, 0, 0));
		p_pw.setBackground(new Color(255, 0, 0, 0));
		p_pwCh.setBackground(new Color(255, 0, 0, 0));
		p_nickName.setBackground(new Color(255, 0, 0, 0));
		p_email.setBackground(new Color(255, 0, 0, 0));
		p_phone.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		/* 조립 */
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

		/* 버튼 리스너 */
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

		// 닫으면 프로세스 종료
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
				pstmt = con.prepareStatement(sql); // 쿼리문 준비
				pstmt.setString(1, t_id.getText());
				
				rs = pstmt.executeQuery();
				if (rs.next()) { // 레코드가 존재한다면 아이디 생성 불가
					idCh = false;
					JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
					t_id.setText("");
				} else { // 레코드가 존재하지 않는다면 아이디 생성 가능
					idCh = true;
					JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다.");
					
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
			JOptionPane.showMessageDialog(this, "아이디를 입력하시오.");
		}
	}

	public void passwordCheck() {
		if(!String.valueOf(t_pw.getPassword()).equals("")) {
			if (String.valueOf(t_pw.getPassword()).equals(String.valueOf(t_pwCh.getPassword()))) {
//			System.out.println("일치");
				pwCh = true;
				JOptionPane.showMessageDialog(this, "비밀번호 일치합니다.");
			} else {
				JOptionPane.showMessageDialog(this, "비밀번호 일치하지 않습니다.");
			}
		}else {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하시오.");
		}
	}

	public void regist() {
		if (!t_id.getText().equals("") && !t_pw.getPassword().equals("") && !t_pwCh.getPassword().equals("")
				&& !t_nickName.getText().equals("") && !t_email.getText().equals("") && !t_phone.getText().equals("")) {
			if (idCh == true) { // 아이디 중복 검사 했을 때
				if (pwCh == true) { // 패스워드 동일 검사 했을 때

					String sql = "insert into calendar_member(member_id,m_id,m_pw,m_nickName,m_email,m_phone)";
					sql += " values(seq_calendar_member.nextval, ?,?,?,?,?)";

					PreparedStatement pstmt = null; // 매 쿼리문마다 갖고 있어야 함
//					System.out.println("성공");
					try {
						pstmt = con.prepareStatement(sql);// 쿼리실행할 준비
						pstmt.setString(1, t_id.getText());

						pstmt.setString(2, new String(t_pw.getPassword())); // char 배열을 스트링으로...
						pstmt.setString(3, t_nickName.getText());
						pstmt.setString(4, t_email.getText());
						pstmt.setString(5, t_phone.getText());

						// DML 의 경우, 이 쿼리수행에 의해 영향을 받은 레코드수가 반환되므로 , 만일 0이 반환된다면 실패로 판단!!
						int result = pstmt.executeUpdate(); // DML의 경우는 executeUpdate(), select는 executeQuery()
						if (result == 0) {
							JOptionPane.showMessageDialog(this, "가입에 실패하였습니다\n관리자에 문의주세요");
						} else {
							JOptionPane.showMessageDialog(this, "가입을 축하드려요^^");

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
					JOptionPane.showMessageDialog(this, "패스워드 동일 검사 체크해주세요.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "아이디 중복 검사 체크해주세요.");
			}

		} else {
			JOptionPane.showMessageDialog(this, "모든 정보를 입력해주세요.");
		}
	}

}
