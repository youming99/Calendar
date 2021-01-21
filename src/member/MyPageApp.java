package member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import calendar.MainCalendarApp;
import common.image.ImageUtil;

public class MyPageApp extends JFrame{
   
   JPanel p_center; //전체 패널
   JPanel p_title; //타이틀 패널
   JPanel p_profile; //프로필 사진 패널 + 닉네임
   JPanel p_id; //아이디 패널
   JPanel p_pw; //패스워드 패널
   JPanel p_nickName; //닉네임 패널
   JPanel p_email; //이메일 패널
   JPanel p_phone; //전화번호 패널
   JPanel p_bts; //버튼 패널
   
   JLabel l_title;
   JLabel logo;
   JLabel l_id;
   JLabel l_pw;
   JLabel l_nickName; 
   JLabel l_email;
   JLabel l_phone;
   
   JLabel t_id;
   JTextField t_pw;
   JTextField t_nickName;
   JTextField t_email;
   JTextField t_phone;
   
   JButton bt_cancel;
   JButton bt_update;
   JButton bt_image;
   
   ImageIcon icon;
   ImageIcon icon_bg;
   
   MainCalendarApp mainCalendarApp;
   private Boolean hasSession;
   private String nickName;
   private String id;
   private String pw;
   
   String str_id = null;
   String str_pw = null;
   static int member_id;
   
   boolean check=false;
   public boolean imageCheck = false;
   Connection con;

   // 접속에 필요한 정보들..
   private String driver = "oracle.jdbc.driver.OracleDriver";
   private String url = "jdbc:oracle:thin:@localhost:1521:XE";
   private String user = "projectCalendar";
   private String pass = "1234";
   
   JFileChooser chooser=new JFileChooser("C:/Korea_IT_Academy/workspace/java_workspace/KoreaItAcademyJava/res/gallery");
   Toolkit kit=Toolkit.getDefaultToolkit();//플랫폼 종속적인 경로로 가져올때는 툴킷 쓰자
   File file;
   Image img;
   String imagePath;
   BufferedImage imageFile;
   
   public MyPageApp(boolean check) {
	  this.imageCheck = check;
      this.setTitle("마이페이지");
      this.getConnection();
      
  
      
      
      icon_bg = new ImageIcon("res/bg.jpg"); // 배경 이미지
      icon = new ImageIcon(imagePath);
      logo = new JLabel(icon);
//      Image img = icon.getImage();
//      img = img.getScaledInstance(100, 45, Image.SCALE_SMOOTH);
//      icon.setImage(img);
//      p_profile.setBackground(Color.white);
//      p_profile.setPreferredSize(new Dimension(1200, 200));

      /*생성*/
      p_center = new JPanel() {
         public void paintComponent(Graphics g) {
            g.drawImage(icon_bg.getImage(), 0, 0, 800, 600, null);
            setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
            super.paintComponent(g);
         }
      };
      p_title = new JPanel();
      p_profile = new JPanel();
      p_id = new JPanel();
      p_pw = new JPanel();
      p_nickName = new JPanel();
      p_email = new JPanel();
      p_phone = new JPanel();
      p_bts = new JPanel();
      
      l_title = new JLabel("마이페이지");
      l_id = new JLabel("아이디 : ");
      l_pw = new JLabel("패스워드 : ");
      l_nickName = new JLabel("닉네임 : ");
      l_email = new JLabel("이메일 : ");
      l_phone = new JLabel("전화번호 : ");
      
      t_id = new JLabel();
      t_pw = new JTextField(20);
      t_nickName = new JTextField(20);
      t_email = new JTextField(20);
      t_phone = new JTextField(20);
      
      bt_cancel = new JButton("취소");
      bt_image = new JButton("이미지 불러오기");
      bt_update = new JButton("수정");
      
      p_profile = new JPanel() {
         public void paint(Graphics g) {
            g.drawImage(img, 100, 4, p_profile);
         }
      };
      
      /* 크기 */
      p_center.setPreferredSize(new Dimension(800, 600));
      p_title.setPreferredSize(new Dimension(800, 80));
      p_profile.setPreferredSize(new Dimension(800, 130));
      p_id.setPreferredSize(new Dimension(800, 40));
      p_pw.setPreferredSize(new Dimension(800, 40));
      p_nickName.setPreferredSize(new Dimension(800, 40));
      p_email.setPreferredSize(new Dimension(800, 40));
      p_phone.setPreferredSize(new Dimension(800, 40));
      p_bts.setPreferredSize(new Dimension(800, 100));
      
      logo.setPreferredSize(new Dimension(100, 150));
//      l_id.setPreferredSize(new Dimension(500, 40));
//      l_email.setPreferredSize(new Dimension(500, 40));
      
      
      
      /* 스타일 */
      p_center.setBackground(new Color(255, 0, 0, 0));
      p_title.setBackground(new Color(255, 0, 0, 0));
      p_profile.setBackground(new Color(255, 0, 0, 0));
      p_id.setBackground(new Color(255, 0, 0, 0));
      p_pw.setBackground(new Color(255, 0, 0, 0));
      p_nickName.setBackground(new Color(255, 0, 0, 0));
      p_email.setBackground(new Color(255, 0, 0, 0));
      p_phone.setBackground(new Color(255, 0, 0, 0));
      p_bts.setBackground(new Color(255, 0, 0, 0));

      l_title.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_nickName.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_id.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_nickName.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_pw.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_email.setFont(new Font("SansSerif", Font.BOLD, 30));
      l_phone.setFont(new Font("SansSerif", Font.BOLD, 30));
      
      t_nickName.setFont(new Font("SansSerif", Font.BOLD, 30));
      t_pw.setFont(new Font("SansSerif", Font.BOLD, 30));
      t_email.setFont(new Font("SansSerif", Font.BOLD, 30));
      t_phone.setFont(new Font("SansSerif", Font.BOLD, 30));
      
      
      /*조립*/
      add(p_center);
      p_center.add(p_title, BorderLayout.SOUTH);
      p_center.add(p_profile);
      p_center.add(p_id);
      p_center.add(p_pw);
      p_center.add(p_nickName);
      p_center.add(p_email);
      p_center.add(p_phone);
      p_center.add(p_bts);
      p_title.add(l_title);
      p_profile.add(logo, BorderLayout.SOUTH);
      p_profile.add(l_nickName);
      p_id.add(l_id);
//      p_id.add(t_id);
      p_pw.add(l_pw);
      p_pw.add(t_pw);
      p_nickName.add(l_nickName);
      p_nickName.add(t_nickName);
      p_email.add(l_email);
      p_email.add(t_email);
      p_phone.add(l_phone);
      p_phone.add(t_phone);
      p_bts.add(bt_cancel);
      p_bts.add(bt_image);
      p_bts.add(bt_update);
      
   
      
      /* 마우스 리스너 */
      bt_cancel.addActionListener((e) -> {
         this.setVisible(false);
//         mainCalendarApp = new MainCalendarApp();
      });
      bt_image.addActionListener((e)-> {
         System.out.println("누름");
         findImage();//사용할 상품이미지 선택!!
         preview();
      });
      bt_update.addActionListener((e) -> {
         update(mainCalendarApp);
         if(imageCheck==false) { //저장된 이미지가 없으면 새로 추가(insert)
             insertImage();
          }else { //저장된 이미지가 있으면 업데이트(update)
             updateImage();
          }
          printImage();

      });
   
      
      setSize(800, 600);
      setLocationRelativeTo(null);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
   }
   
   public void getProfile(String id, String pw) {
      this.str_id = id;
      this.str_pw = pw;
      
//      System.out.println("아이디" + str_id );
//      System.out.println("패스워드" + str_pw );
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      String sql = "select * from calendar_member where m_id = ? and m_pw=?";
      try {
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         pstmt.setString(1, str_id);
         pstmt.setString(2, str_pw);

         rs = pstmt.executeQuery();
         if (rs.next()) {
//            System.out.println(rs.getString(1)); // member_id
//            System.out.println(rs.getString(2)); // m_id
//            System.out.println(rs.getString(3)); // m_pw
//            System.out.println(rs.getString(4)); // m_nickName
//            System.out.println(rs.getString(5)); // m_email
//            System.out.println(rs.getString(6)); // m_phone
            
//            l_nickName.setText(rs.getString(4) + "님");
            member_id=rs.getInt(1);
            l_id.setText("아이디 : " + rs.getString(2));
            t_pw.setText(rs.getString(3));
            t_nickName.setText(rs.getString(4));
            t_email.setText(rs.getString(5));
            t_phone.setText(rs.getString(6));
            
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
   
   public void printImage() {
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      String sql = "select * from profile_image where member_id=?";
	      try {
	         pstmt = con.prepareStatement(sql); // 쿼리문 준비
	         pstmt.setInt(1, member_id);

	         rs = pstmt.executeQuery();
	         if (rs.next()) { // 레코드가 존재한다면 아이디 생성 불가
	            // sql = "update profile_image set image = ?";
	            imagePath = rs.getString(2).toString();
	            // String imagepath2 = imagepath.replace("\\", "\\\\");

	            // String path = "C:\\Users\\pc\\Downloads\\switch_on.png";
	            // System.out.println('"'+imagepath+'"');
	            // rs.getString(2);

	            // C:\Users\pc\Documents\카카오톡 받은 파일
//	            System.out.println(imagepath);

//	            icon_profile = new ImageIcon(imagepath);

//	            can.setBackground(Color.GREEN);

	            // 백그라운드 이미지 삽입할 메소드에 이름없는 클래스로 구현
	            try {                
	               imageFile = ImageIO.read(new File(imagePath));
	            } catch (IOException ex) {
	               // handle exception...
	            }
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

   
   public void update(MainCalendarApp mainCalendarApp) {
      this.mainCalendarApp = mainCalendarApp;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
//      System.out.println(str_id+"와 " +str_pw);
      String sql = "update calendar_member set m_pw = ?,m_nickName = ?,m_email= ?,m_phone = ? where m_id =? and m_pw= ?";
      try {
         pstmt = con.prepareStatement(sql); // 쿼리문 준비
         pstmt.setString(1, t_pw.getText());
         pstmt.setString(2, t_nickName.getText());
         pstmt.setString(3, t_email.getText());
         pstmt.setString(4, t_phone.getText());
         pstmt.setString(5, str_id);
         pstmt.setString(6, str_pw);
         
         int result = pstmt.executeUpdate(); // DML의 경우는 executeUpdate(), select는 executeQuery()
         if (result == 0) {
            JOptionPane.showMessageDialog(this, "회원정보 수정 실패");
         
         } else {
            JOptionPane.showMessageDialog(this, "회원정보 수정 성공");
            this.setVisible(false);
//            mainCalendarApp.setVisible(false);
            
//            mainCalendarApp = new MainCalendarApp();
            
//            mainCalendarApp.str_nickName = t_nickName.getText();
//            mainCalendarApp.getNickName(String.valueOf(t_nickName.getText()));

         }
         
      }catch (SQLException e) {
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
   
   public void insertImage() {
      System.out.println("mypage에서 insert : " + file.getAbsolutePath() + " / " + member_id);
      PreparedStatement pstmt2 = null;
       ResultSet rs = null;
       
       String sql_add = "insert into profile_image(member_id, image)";
       sql_add += " values(?,?)";
       
       try {
          pstmt2 = con.prepareStatement(sql_add); // 쿼리문 준비2
          
          pstmt2.setInt(1, member_id);
          pstmt2.setString(2,  file.getAbsolutePath());
         
          
          int result = pstmt2.executeUpdate();
          if (result == 0) {
             JOptionPane.showMessageDialog(this, "파일 등록에 실패했습니다.");
          } else {
             JOptionPane.showMessageDialog(this, "파일 등록 성공했습니다."); 
             imageCheck = true;
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
//     else {
//       JOptionPane.showMessageDialog(this, "정보를 다 입력해주세요.");
//    }

      
   }
   
   public void updateImage() {
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;

//	      System.out.println(str_id+"와 " +str_pw);
	      String sql = "update profile_image set image = ? where member_id = ?";
	      try {
	         pstmt = con.prepareStatement(sql); // 쿼리문 준비
	         pstmt.setString(1, file.getAbsolutePath());
	         pstmt.setInt(2, member_id);

	         int result = pstmt.executeUpdate(); // DML의 경우는 executeUpdate(), select는 executeQuery()
	         if (result == 0) {
	            JOptionPane.showMessageDialog(this, "이미지 수정 실패");

	         } else {
	            JOptionPane.showMessageDialog(this, "이미지 수정 성공");
	            this.setVisible(false);
	            imageCheck = true;
//	            mainCalendarApp.setVisible(false);

//	            mainCalendarApp = new MainCalendarApp();

//	            mainCalendarApp.str_nickName = t_nickName.getText();
//	            mainCalendarApp.getNickName(String.valueOf(t_nickName.getText()));

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

   
   public void findImage() {
      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
         // 파일정보를 구한다!!
         file = chooser.getSelectedFile();
         
         if(!file.getAbsolutePath().equals("")) {
             PreparedStatement pstmt = null;
             ResultSet rs = null;
             String sql = "select * from profile_image where member_id=?";
             try {
                pstmt = con.prepareStatement(sql); // 쿼리문 준비
                pstmt.setInt(1, member_id);
                
                rs = pstmt.executeQuery();
                if (rs.next()) { // 레코드가 존재한다면 아이디 생성 불가
                   // sql = "update profile_image set  image = ?";
                   rs.getString(2);
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
         
         
         getTargetImage(file.getAbsolutePath());
         
      }
   }
   
   //그려질 이미지 구하기 
      public void getTargetImage(String path) {
         img=kit.getImage(path); //멤버변수  img값을 구한다!!   
         img = ImageUtil.getCustomSize(img, 135, 115);
         
      }

   // 미리보기 구현
   public void preview() {
      // paint로 그림 처리~~
      p_profile.updateUI();
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
   
//   public static void main(String[] args) {
//      new MyPageApp();
//   }
}