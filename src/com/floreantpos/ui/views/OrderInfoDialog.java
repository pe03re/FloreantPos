package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class OrderInfoDialog extends POSDialog {
	OrderInfoView view;

	public OrderInfoDialog(OrderInfoView view, JFrame parent) {
		super(parent, true);
		this.view = view;
		setTitle("ORDER PREVIEW");

		createUI();
	}

	public OrderInfoDialog(OrderInfoView view) {
		this.view = view;
		setTitle("ORDER PREVIEW");

		createUI();
	}

	private void createUI() {
		add(view);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		PosButton btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		});
		btnPrint.setText("PRINT");
		panel.add(btnPrint);

		PosButton btnKitchenPrint = new PosButton();
		btnKitchenPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doKitchenPrint();
			}
		});
		btnKitchenPrint.setText("KITCHEN");
		panel.add(btnKitchenPrint);

		PosButton btnCash = new PosButton();
		btnCash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doChangeTicketToCash();
			}
		});
		btnCash.setText("CASH");
		panel.add(btnCash);

		PosButton btnCard = new PosButton();
		btnCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doChangeTicketToCard();
			}
		});
		btnCard.setText("CARD");
		panel.add(btnCard);

		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		PosButton btn99 = new PosButton();
		btn99.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do99();
			}
		});
		btn99.setText("99");
		panel.add(btn99);
		
		btnClose.setText("CLOSE");
		panel.add(btnClose);
	}

	protected void doPrint() {
		try {
			view.print();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}

	protected void doKitchenPrint() {
		try {
			view.kitchenPrint();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}

	private void doChangeTicketToCard() {
		try {
			view.changeToCard();
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}

	private void doChangeTicketToCash() {
		try {
			view.changeToCash();
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}
	
	private void do99() {
		try {
			view.changeTokenTo99();
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}

}
