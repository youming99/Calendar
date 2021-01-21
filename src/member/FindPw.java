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

	JPanel p_center; // 전체 패널
	JPanel p_title; // 타이틀 패널
	JPanel p_id; // 아이디 패널
	JPanel p_email; // 이메일 패널
	JPanel p_phone; // 전화번호 패널
	JPanel p_bts; // 버튼 패널

	JLabel l_title; // 비밀번호 찾기 라벨
	JLabel l_id; // 아이디 라벨
	JLabel l_email; // 이메일 라벨
	JLabel l_phone; // 전화번호 라벨

	JTextField t_id; // 아이디 텍스트 필드
	JTextField t_email; // 이메일 텍스트 필드
	JTextField t_phone; // 전화번호 텍스트 필드

	JButton bt_cancel; // 취소 버튼
	JButton bt_submit; // 확인 버튼

	ImageIcon icon_bg;
	ImageIcon icon_findPw;

	Connection con;
	String str_pw = null;

	// 접속에 필요한 정보들..
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "projectCalendar";
	private String pass = "1234";

	public FindPw() {
		super.setTitle("비밀번호 찾기");
		this.getConnection();

		icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지
		/* 생성 */
		p_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 500, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		p_title = new JPanel();
		p_id = new JPanel();
		p_email = new JPanel();
		p_phone = new JPanel();
		p_bts = new JPanel();

		l_title = new JLabel("비밀번호 찾기");
		l_id = new JLabel("아이디");
		l_email = new JLabel("이메일");
		l_phone = new JLabel("전화번호");

		t_id = new JTextField(20);
		t_email = new JTextField(20);
		t_phone = new JTextField(20);

		bt_cancel = new JButton("취소");
		bt_submit = new JButton("확인");

		icon_findPw = new ImageIcon("res/logo_findPw.png");
		l_title = new JLabel(icon_findPw);
		// 패널에 로고 넣기 @
		Image img = icon_findPw.getImage();
		img = img.getScaledInstance(120, 60, Image.SCALE_SMOOTH);
		icon_findPw.setImage(img);

		/* 크기 */
		p_center.setPreferredSize(new Dimension(800, 400));
		p_title.setPreferredSize(new Dimension(800, 80));
		p_id.setPreferredSize(new Dimension(800, 40));
		p_email.setPreferredSize(new Dimension(800, 40));
		p_phone.setPreferredSize(new Dimension(800, 40));
		p_bts.setPreferredSize(new Dimension(800, 60));

		/* 스타일 */
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

		/* 조립 */
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

		/* 마우스 리스너 */
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

		// 닫으면 프로세스 종료
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
			pstmt = con.prepareStatement(sql); // 쿼리문 준비
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, t_email.getText());
			pstmt.setString(3, t_phone.getText());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString(3)); // m_pw

				str_pw = rs.getString(3);
				JOptionPane.showMessageDialog(this, "회원님의 비밀번호는 " + str_pw + " 입니다.");

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

	// 접속을 시도하는 메서드 정의
	public void getConnection() {
		try {
			Class.forName(driver);// 드라이버 로드
			con = DriverManager.getConnection(url, user, pass); // 접속시도 후, 객체 반환
			if (con == null) { // 접속실패인경우 메시지 출력
				JOptionPane.showMessageDialog(this, "데이터베이스에 접속하지 못했습니다.");
			} else {// 접속 성공의 경우 윈도우 제목창에 현재 접속자 출력
//				this.setTitle(user + " 접속 중");
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

	public static void main(String[] args) {
		new FindPw();
	}
}
