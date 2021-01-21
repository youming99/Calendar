package common.file;

public class FileManager {

   //���ϸ� ���ϱ� : �Ű������� ������ ��θ� �Ѱܹ޾� ���ϸ� �����Ѵ�
   public static String getFilename(String path) {
      int lastIndex = path.lastIndexOf("/"); //�������� ��ġ��  /�� �ε��� ���ϱ�!!
      return path.substring(lastIndex+1, path.length());
   }
   
   //Ȯ���� ���ϱ� : �Ű������� ���ϸ��� �Ѱܹ޾� Ȯ���ڴ� �����Ѵ�
   public static String getExtend(String filename) {
      String[] str = filename.split("\\."); //���� �������� ���ڿ� �и�.. �и��Ŀ��� �迭�� ��ȯ��!
      return str[1];//�ι�° ĭ�� Ȯ������
   }
   
   /*
   public static void main(String[] args) {
      String filename = getFilename("http://cdn.011st.com/11dims/resize/600x600/quality/75/11src/pd/20/7/4/9/1/8/3/FmIzU/2003749183_B.jpg");
      System.out.println(filename);
      
      String ext = getExtend(filename);
      System.out.println(ext);
   }
   */
}





