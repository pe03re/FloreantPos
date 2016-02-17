package com.floreantpos.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColoredCellRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private Map<String, JLabel> labelMap = new HashMap<String, JLabel>();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		// Cells are by default rendered as a JLabel.
		// JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
		// isSelected, hasFocus, row, col);
		String loc = row + "_" + col;

		JLabel l;
		if (labelMap.containsKey(loc)) {
			l = labelMap.get(loc);
		} else {
			l = new JLabel();
			l.setOpaque(true);
			labelMap.put(loc, l);
		}

		// Get the status for the current row.
		ColoredCellData level = (ColoredCellData) value;
		l.setBackground(level.getC());
		l.setText(level.getDataStr());
		if (isSelected) {
			l.setBackground(table.getSelectionBackground());
			l.setForeground(table.getSelectionForeground());
		} else {
			l.setForeground(table.getForeground());

		}
		// Return the JLabel which renders the cell.
		return l;
	}
}
