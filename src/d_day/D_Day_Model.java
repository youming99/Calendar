package d_day;

import javax.swing.table.AbstractTableModel;

public class D_Day_Model extends AbstractTableModel {
	/*
	 * JTable�� ���� �޼��� ������ ��, �Ʒ��� �޼������ JTable�� ȣ���� ����!!
	 */
	String[] column = {"���� ��ȣ", "���� �̸�", "��¥", "���� ��¥"};
	String[][] data = {};// ����ִ� 2���� �迭 ����

	public int getRowCount() { // ���ڵ� ��
		return data.length;// 0
	}

	public int getColumnCount() {// �÷� ��
		return column.length;// 4
	}

//�÷������� ����ϱ� ���ؼ� ,�̹� �����ϴ� �޼��� �������̵� 
	@Override
	public String getColumnName(int col) {
		return column[col];
	}

//�ش� ��ǥ�� ������
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
