package com.floreantpos.inventory.report;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class SelectionManager<T> implements ActionListener {
	JComboBox combo = null;
	List<T> selectedItems = new ArrayList<T>();
	List<T> nonSelectables = new ArrayList<T>();

	public void actionPerformed(ActionEvent e) {
		if (combo == null) {
			combo = (JComboBox) e.getSource();
		}
		T item = (T) combo.getSelectedItem();
		// Toggle the selection state for item.
		if (selectedItems.contains(item)) {
			selectedItems.remove(item);
		} else if (!nonSelectables.contains(item)) {
			selectedItems.add(item);
		}
	}

	/**
	 * The varargs feature (Object... args) is new in j2se 1.5 You can replace
	 * the argument with an array.
	 */
	public void setNonSelectables(T... args) {
		for (int j = 0; j < args.length; j++) {
			nonSelectables.add(args[j]);
		}
	}

	public boolean isSelected(T item) {
		return selectedItems.contains(item);
	}
}

/** Implementation copied from source code. */
class MultiRenderer extends BasicComboBoxRenderer {

	SelectionManager selectionManager;
	private Map<Integer, Component> compMap = new HashMap<Integer, Component>();

	public MultiRenderer(SelectionManager sm) {
		selectionManager = sm;
	}

	// public Component getListCellRendererComponent(JList list, Object value,
	// int index, boolean isSelected, boolean cellHasFocus) {
	// if (selectionManager.isSelected(value)) {
	// setBackground(list.getSelectionBackground());
	// setForeground(list.getSelectionForeground());
	// } else {
	// setBackground(list.getBackground());
	// setForeground(list.getForeground());
	// }
	//
	// setFont(list.getFont());
	//
	// if (value instanceof Icon) {
	// setIcon((Icon) value);
	// } else {
	// setText((value == null) ? "" : value.toString());
	// }
	// return this;
	// }

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		if (index >= 0 && compMap.containsKey(index)) {
			JPanel p = (JPanel) compMap.get(index);
			if (selectionManager.isSelected(value)) {
				for (Component c : p.getComponents()) {
					if (c.getName() != null && c.getName().equals("checkbox")) {
						JCheckBox checkbox = (JCheckBox) c;
						checkbox.setSelected(true);
					}
				}
			} else {
				for (Component c : p.getComponents()) {
					if (c.getName() != null && c.getName().equals("checkbox")) {
						JCheckBox checkbox = (JCheckBox) c;
						checkbox.setSelected(false);
					}
				}
			}
			return compMap.get(index);
		}
		if (index == -1) {
			if (compMap.containsKey(-1)) {
				JLabel l = (JLabel) compMap.get(-1);
				return l;
			} else {
				JLabel l = new JLabel();
				compMap.put(index, l);
				return l;
			}
		}
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox checkbox = new JCheckBox(value.toString());
		checkbox.setEnabled(true);
		checkbox.setName("checkbox");
		p.add("checkbox", checkbox);
		p.setEnabled(true);
		compMap.put(index, p);
		if (selectionManager.isSelected(value)) {
			for (Component c : p.getComponents()) {
				if (c.getName() != null && c.getName().equals("checkbox")) {
					JCheckBox checkbox1 = (JCheckBox) c;
					checkbox1.setSelected(true);
				}
			}
		} else {
			for (Component c : p.getComponents()) {
				if (c.getName() != null && c.getName().equals("checkbox")) {
					JCheckBox checkbox1 = (JCheckBox) c;
					checkbox1.setSelected(false);
				}
			}
		}
		// if (selectionManager.isSelected(value)) {
		// setBackground(list.getSelectionBackground());
		// setForeground(list.getSelectionForeground());
		// } else {
		// setBackground(list.getBackground());
		// setForeground(list.getForeground());
		// }
		//
		// setFont(list.getFont());
		//
		// if (value instanceof Icon) {
		// setIcon((Icon) value);
		// } else {
		// setText((value == null) ? "" : value.toString());
		// }
		return p;
	}
}