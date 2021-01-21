package common.image;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtil {

	//�������� ��ȯ���ִ� �޼��� 
	public static ImageIcon getIcon(Class target, String path, int width, int height) {
		ImageIcon icon=null;
		icon = new ImageIcon(target.getClassLoader().getResource(path));
		//ũ�Ⱑ ������ �̹��� ����
		Image img=icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}
	
	//�̹����� �Ѱܹ޾�, ���ϴ� ũ���� �̹����� ��ȯ�ϴ� �޼��� 
	public static Image getCustomSize(Image img, int width, int height){
		return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
}











