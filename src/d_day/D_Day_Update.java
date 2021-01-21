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

	// 접속에 필요한 정보들..
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
		this.setTitle("디데이 수정");
		this.getConnection();
		
		icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지

		/* 생성 */
		p_center = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon_bg.getImage(), 0, 0, 800, 600, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		p_title = new JPanel();
		p_name = new JPanel();
		p_date = new JPanel();
		p_bts = new JPanel();

		l_title = new JLabel("디데이 수정");
		l_name = new JLabel("디데이 이름 : ");
		l_year = new JLabel(" 년");
		l_month = new JLabel(" 월");
		l_day = new JLabel(" 일");

		t_name = new JTextField(20);
		t_year = new JTextField(10);
		t_month = new JTextField(10);
		t_day = new JTextField(10);

		bt_cancel = new JButton("이전");
		bt_regist = new JButton("수정");

		/* 크기 */
		p_center.setPreferredSize(new Dimension(800, 600));
		p_title.setPreferredSize(new Dimension(800, 100));
		p_name.setPreferredSize(new Dimension(800, 100));
		p_bts.setPreferredSize(new Dimension(800, 300));

		/* 폰트 크기 */
		l_title.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_name.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_year.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_month.setFont(new Font("SansSerif", Font.BOLD, 30));
		l_day.setFont(new Font("SansSerif", Font.BOLD, 30));
		t_name.setFont(new Font("SansSerif", Font.BOLD, 30));
		t_year.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_month.setFont(new Font("SansSerif", Font.BOLD, 20));
		t_day.setFont(new Font("SansSerif", Font.BOLD, 20));

		/* 스타일 */
		p_center.setBackground(new Color(255, 0, 0, 0));
		p_title.setBackground(new Color(255, 0, 0, 0));
		p_name.setBackground(new Color(255, 0, 0, 0));
		p_date.setBackground(new Color(255, 0, 0, 0));
		p_bts.setBackground(new Color(255, 0, 0, 0));

		/* 조립 */
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

		/* 마우스 리스너 */
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

	/* 유저 정보 가져오기 */
	public void getUser() {
		System.out.println("ㅇㅇ");
		//로그인한 유저의 데이터를 가져와야함 (calendar_member 테이블에서 member_id 가져옴)
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		String sql_member = "select * from d_day where dday_id =" + dday_id;
		
		try {
			pstmt = con.prepareStatement(sql_member); // 쿼리문 준비1
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
//				JOptionPane.showMessageDialog(this, "아이디와 패스워드를 다시 입력해주세요");
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
	
	
	/* 디데이 추가 */
	public void regist() {
		if(!t_name.getText().equals("") && !t_year.getText().equals("") && 
				!t_month.getText().equals("") && !t_day.getText().equals("")) {
			
			PreparedStatement pstmt2 = null;
			ResultSet rs = null;
			String sql = "update calendar_member set m_pw = ?,m_nickName = ?,m_email= ?,m_phone = ? where m_id =? and m_pw= ?";
			String sql_add = "update d_day set d_name = ?, d_year = ?, d_month = ?, d_day = ? where dday_id = ?";
			try {
				pstmt2 = con.prepareStatement(sql_add); // 쿼리문 준비2
				
				pstmt2.setString(1, t_name.getText());
				pstmt2.setString(2, t_year.getText());
				pstmt2.setString(3, t_month.getText());
				pstmt2.setString(4, t_day.getText());
				pstmt2.setString(5, dday_id);
				
				int result = pstmt2.executeUpdate();
				if (result == 0) {
					JOptionPane.showMessageDialog(this, "디데이 수정에 실패했습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "디데이 수정 성공했습니다.");
					
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
			JOptionPane.showMessageDialog(this, "정보를 다 입력해주세요.");
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
				this.setTitle(user + " 접속 중");
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

//	public static void main(String[] args) {
//		new D_Day_Add();
//	}

}
