package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class TicketSelectionDialog extends POSDialog implements ActionListener {
	private int defaultValue;

	private TitlePanel titlePanel;
	private JTextField tfNumber;

	private boolean floatingPoint;
	private PosButton posButton_1;

	public TicketSelectionDialog() {
		this(Application.getPosWindow());
	}

	public TicketSelectionDialog(Frame parent) {
		super(parent, true);
		init();
	}

	public TicketSelectionDialog(Dialog parent) {
		super(parent, true);

		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill][60px,fill][60px,fill]", "[][][][][]");
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap");

		tfNumber = new JTextField();
		// tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		// tfNumber.setEditable(false);
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		// tfNumber.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(tfNumber, "span 2, grow");

		PosButton posButton = new PosButton("DEL");
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		contentPane.add(posButton, "growy,height 55,wrap");

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "/", "0", "-" } };
		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" }, { "1_32.png", "2_32.png", "3_32.png" },
				{ "slash.jpg", "0_32.png", "hyphen.jpg" } };

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				posButton.setFocusable(false);
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				} else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				String constraints = "grow, height 55";
				if (j == numbers[i].length - 1) {
					constraints += ", wrap";
				}
				contentPane.add(posButton, constraints);
			}
		}
		contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20");

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		contentPane.add(posButton, "skip 1,grow");

		posButton_1 = new PosButton(POSConstants.CANCEL);
		posButton_1.setFocusable(false);
		posButton_1.addActionListener(this);
		contentPane.add(posButton_1, "grow");
	}

	private void doOk() {
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		String s = tfNumber.getText();
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		} else {
			s = String.valueOf(defaultValue);
		}
		tfNumber.setText(s);
	}

	private void doInsertNumber(String number) {
		String s = tfNumber.getText();
		s = s + number;
		tfNumber.setText(s);
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		} else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		} else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		} else if (actionCommand.equals("/")) {
			doInsertSlash();
		} else if (actionCommand.equals("-")) {
			doInsertHyphen();
		} else {
			doInsertNumber(actionCommand);
		}

	}

	private void doInsertHyphen() {
		tfNumber.setText(tfNumber.getText() + "-");
	}

	private void doInsertSlash() {
		tfNumber.setText(tfNumber.getText() + "/");
	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	public String getValue() {
		return tfNumber.getText().toString();
	}

	public void setValue(String value) {
		if (value == null) {
			tfNumber.setText("");
		} else {
			tfNumber.setText(value);
			tfNumber.setCaretPosition(value.length());
			tfNumber.moveCaretPosition(value.length());
		}
	}

	public boolean isFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(boolean decimalAllowed) {
		this.floatingPoint = decimalAllowed;
	}

	public static void main(String[] args) {
		TicketSelectionDialog dialog2 = new TicketSelectionDialog();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	// public int getDefaultValue() {
	// return defaultValue;
	// }
	//
	// public void setDefaultValue(int defaultValue) {
	// this.defaultValue = defaultValue;
	// tfNumber.setText(String.valueOf(defaultValue));
	// }

	public static String takeTicketInput(String title, String tHeader) {
		TicketSelectionDialog dialog = new TicketSelectionDialog();
		dialog.setTitle(title);
		dialog.setValue(tHeader);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return null;
		}

		return dialog.getValue();
	}

}
