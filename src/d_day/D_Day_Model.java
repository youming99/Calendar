package d_day;

import javax.swing.table.AbstractTableModel;

public class D_Day_Model extends AbstractTableModel {
	/*
	 * JTable을 위한 메서드 재정의 즉, 아래의 메서드들은 JTable이 호출해 간다!!
	 */
	String[] column = {"디데이 번호", "디데이 이름", "날짜", "남은 날짜"};
	String[][] data = {};// 비어있는 2차원 배열 선언

	public int getRowCount() { // 레코드 수
		return data.length;// 0
	}

	public int getColumnCount() {// 컬럼 수
		return column.length;// 4
	}

//컬럼제목을 출력하기 위해선 ,이미 지원하는 메서드 오버라이드 
	@Override
	public String getColumnName(int col) {
		return column[col];
	}

//해당 좌표의 데이터
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
