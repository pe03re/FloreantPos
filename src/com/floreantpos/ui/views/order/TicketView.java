/*
 * TicketView.java
 *
 * Created on August 4, 2006, 3:42 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.bo.actions.AdminTicketManagerAction;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.InventoryWarehouseItem;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.model.dao.InventoryWarehouseItemDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.TicketUtils;
import com.floreantpos.ui.views.AdminTicketManagerView;
import com.floreantpos.ui.views.CashierSwitchBoardView;
import com.floreantpos.ui.views.CookingInstructionSelectionView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.util.NumberUtil;

/**
 * 
 * @author MShahriar
 */
public class TicketView extends JPanel {
	private java.util.Vector<OrderListener> orderListeners = new java.util.Vector<OrderListener>();
	private Ticket ticket;

	public final static String VIEW_NAME = "TICKET_VIEW";

	private TicketView tv;
	private boolean isInBackOffice;

	public boolean isInBackOffice() {
		return isInBackOffice;
	}

	public void setInBackOffice(boolean isInBackOffice) {
		this.isInBackOffice = isInBackOffice;

	}

	public TicketView(boolean isInBackOffice) {
		this.isInBackOffice = isInBackOffice;
		initComponents();
		btnAddCookingInstruction.setEnabled(false);
		btnIncreaseAmount.setEnabled(false);
		btnDecreaseAmount.setEnabled(false);
		ticketViewerTable.setRowHeight(35);
		ticketViewerTable.getRenderer().setInTicketScreen(true);
		ticketViewerTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					updateSelectionView();
				}
			}

		});
		tv = this;

		ticketViewerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				Object selected = ticketViewerTable.getSelected();
				if (!(selected instanceof ITicketItem)) {
					return;
				}

				ITicketItem item = (ITicketItem) selected;
				// Boolean printedToKitchen = item.isPrintedToKitchen();
				btnAddCookingInstruction.setEnabled(item.canAddCookingInstruction());
				btnIncreaseAmount.setEnabled(true);
				btnDecreaseAmount.setEnabled(true);
				btnDelete.setEnabled(true);
			}

		});

		this.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				Container c = event.getAncestor();
				while (c != null) {
					if (c instanceof BackOfficeWindow) {
						tv.isInBackOffice = true;
						btnPay.setEnabled(false);
						return;
					} else {
						c = c.getParent();
					}
				}
				tv.isInBackOffice = false;
				btnPay.setEnabled(true);

			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				Container c = event.getAncestor();
				while (c != null) {
					if (c instanceof BackOfficeWindow) {
						tv.isInBackOffice = true;
						btnPay.setEnabled(false);
						return;
					} else {
						c = c.getParent();
					}
				}
				tv.isInBackOffice = false;
				btnPay.setEnabled(true);
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				Container c = event.getAncestor();
				while (c != null) {
					if (c instanceof BackOfficeWindow) {
						tv.isInBackOffice = true;
						btnPay.setEnabled(false);
						return;
					} else {
						c = c.getParent();
					}
				}
				tv.isInBackOffice = false;
				btnPay.setEnabled(true);
			}
		});
	}

	public TicketView() {
		this(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jPanel1 = new com.floreantpos.swing.TransparentPanel();
		ticketAmountPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel.setOpaque(true);
		btnPay = new com.floreantpos.swing.PosButton();
		btnCancel = new com.floreantpos.swing.PosButton();
		btnSave = new com.floreantpos.swing.PosButton();
		scrollerPanel = new com.floreantpos.swing.TransparentPanel();
		btnIncreaseAmount = new com.floreantpos.swing.PosButton();
		btnDecreaseAmount = new com.floreantpos.swing.PosButton();
		btnScrollUp = new com.floreantpos.swing.PosButton();
		btnScrollDown = new com.floreantpos.swing.PosButton();
		jPanel2 = new com.floreantpos.swing.TransparentPanel();
		ticketViewerTable = new com.floreantpos.ui.ticket.TicketViewerTable();
		ticketScrollPane = new PosScrollPane(ticketViewerTable);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, com.floreantpos.POSConstants.TICKET, javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		setPreferredSize(new java.awt.Dimension(650, 463));
		setLayout(new java.awt.BorderLayout(5, 5));
		jPanel1.setLayout(new BorderLayout(5, 5));
		ticketAmountPanel.setLayout(new MigLayout("alignx trailing,fill", "[grow][]", "[][][][][][][][]"));

		lblCustomerName = new javax.swing.JLabel();
		lblCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lblCustomerName.setText("CUSTOMER: ");
		ticketAmountPanel.add(lblCustomerName, "cell 0 1,growx");
		lblTokenNo = new javax.swing.JLabel();
		lblTokenNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lblTokenNo.setText("TOKEN NO:");
		ticketAmountPanel.add(lblTokenNo, "cell 0 3");
		tfTokenNo = new IntegerTextField(6);
		tfTokenNo.setHorizontalAlignment(SwingConstants.LEFT);
		ticketAmountPanel.add(tfTokenNo, "cell 0 4");

		jLabel5 = new javax.swing.JLabel();
		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel5.setText(com.floreantpos.POSConstants.SUBTOTAL + ":");
		ticketAmountPanel.add(jLabel5, "cell 2 1,growx,aligny center");
		tfSubtotal = new javax.swing.JTextField(10);
		tfSubtotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfSubtotal.setEditable(false);
		ticketAmountPanel.add(tfSubtotal, "cell 3 1,growx,aligny center");

		// jLabel1 = new javax.swing.JLabel();
		// jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
		// jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		// jLabel1.setText(com.floreantpos.POSConstants.DISCOUNT + ":");
		// ticketAmountPanel.add(jLabel1, "cell 0 2,growx,aligny center");
		// tfDiscount = new javax.swing.JTextField();
		// tfDiscount.setHorizontalAlignment(SwingConstants.TRAILING);
		//
		// tfDiscount.setEditable(false);
		// tfDiscount.setFont(new java.awt.Font("Tahoma", 1, 12));
		// ticketAmountPanel.add(tfDiscount, "cell 1 2,growx,aligny center");

		lblTax = new javax.swing.JLabel();
		lblTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblTax.setText(com.floreantpos.POSConstants.TAX + ":");
		ticketAmountPanel.add(lblTax, "cell 2 3,growx,aligny center");
		tfTax = new javax.swing.JTextField();
		tfTax.setHorizontalAlignment(SwingConstants.TRAILING);

		tfTax.setEditable(false);
		ticketAmountPanel.add(tfTax, "cell 3 3,growx,aligny center");

		// lblServiceCharge = new JLabel();
		// lblServiceCharge.setText("Service Charge:");
		// lblServiceCharge.setHorizontalAlignment(SwingConstants.RIGHT);
		// lblServiceCharge.setFont(new Font("Dialog", Font.BOLD, 12));
		// ticketAmountPanel.add(lblServiceCharge, "cell 0 4,alignx trailing");

		// tfServiceCharge = new JTextField();
		// tfServiceCharge.setHorizontalAlignment(SwingConstants.TRAILING);
		// tfServiceCharge.setEditable(false);
		// ticketAmountPanel.add(tfServiceCharge,
		// "cell 1 4,growx,aligny center");
		// tfServiceCharge.setColumns(10);
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setFont(jLabel6.getFont().deriveFont(Font.BOLD, 16));
		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel6.setText(com.floreantpos.POSConstants.TOTAL + ":");
		ticketAmountPanel.add(jLabel6, "cell 2 4,growx,aligny center");
		tfTotal = new javax.swing.JTextField(10);
		tfTotal.setFont(tfTotal.getFont().deriveFont(Font.BOLD, 16));
		tfTotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfTotal.setEditable(false);
		ticketAmountPanel.add(tfTotal, "cell 3 4,growx,aligny center");

		controlPanel.setLayout(new MigLayout("insets 0, fill, hidemode 3", "fill, grow", ""));

		btnCancel.setText(POSConstants.CANCEL_BUTTON_TEXT);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCancelOrder(evt);
			}
		});
		controlPanel.add(btnCancel);

		btnSave.setText(com.floreantpos.POSConstants.SAVE_BUTTON_TEXT);
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishOrder(evt);
			}
		});
		controlPanel.add(btnSave);
		btnPay.setText(com.floreantpos.POSConstants.PAY_BUTTON_TEXT);
		btnPay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doPayNow(evt);
			}
		});
		controlPanel.add(btnPay);
		if (isInBackOffice) {
			btnPay.setEnabled(false);
		}

		btnIncreaseAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		btnIncreaseAmount.setPreferredSize(new java.awt.Dimension(76, 45));
		btnIncreaseAmount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doIncreaseAmount(evt);
			}
		});
		scrollerPanel.setLayout(new MigLayout("insets 0", "[133px,grow][133px,grow][133px,grow]", "[45px][45px]"));
		scrollerPanel.add(btnIncreaseAmount, "cell 0 0,grow");

		btnDecreaseAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minus_32.png")));
		btnDecreaseAmount.setPreferredSize(new java.awt.Dimension(76, 45));
		btnDecreaseAmount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDecreaseAmount(evt);
			}
		});
		scrollerPanel.add(btnDecreaseAmount, "cell 1 0,grow");

		btnScrollUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up_32.png")));
		btnScrollUp.setPreferredSize(new java.awt.Dimension(76, 45));
		btnScrollUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollUp(evt);
			}
		});
		scrollerPanel.add(btnScrollUp, "cell 2 0,grow");

		btnScrollDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down_32.png")));
		btnScrollDown.setPreferredSize(new java.awt.Dimension(76, 45));
		btnScrollDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollDown(evt);
			}
		});
		btnDelete = new com.floreantpos.swing.PosButton();

		btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_32.png")));
		btnDelete.setText(com.floreantpos.POSConstants.DELETE);
		btnDelete.setPreferredSize(new java.awt.Dimension(80, 17));
		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDeleteSelection(evt);
			}
		});

		btnAddCookingInstruction = new PosButton();
		btnAddCookingInstruction.setEnabled(false);
		btnAddCookingInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddCookingInstruction();
			}
		});
		btnAddCookingInstruction.setText("<html><center>ADD COOKING<br/>INSTRUCTION</center></html>");
		scrollerPanel.add(btnAddCookingInstruction, "cell 0 1,grow");
		scrollerPanel.add(btnDelete, "cell 1 1,grow");
		scrollerPanel.add(btnScrollDown, "cell 2 1,grow");

		JPanel amountPanelContainer = new JPanel(new BorderLayout(5, 5));
		amountPanelContainer.add(ticketAmountPanel);
		amountPanelContainer.add(scrollerPanel, BorderLayout.SOUTH);

		jPanel1.add(amountPanelContainer);
		jPanel1.add(controlPanel, BorderLayout.SOUTH);

		add(jPanel1, java.awt.BorderLayout.SOUTH);

		jPanel2.setLayout(new java.awt.BorderLayout());

		// jScrollPane1.setBorder(null);
		ticketScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ticketScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		ticketScrollPane.setPreferredSize(new java.awt.Dimension(180, 200));
		// jScrollPane1.setViewportView(ticketViewerTable);

		jPanel2.add(ticketScrollPane, java.awt.BorderLayout.CENTER);

		add(jPanel2, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

	protected void doAddCookingInstruction() {

		try {
			Object object = ticketViewerTable.getSelected();
			if (!(object instanceof TicketItem)) {
				POSMessageDialog.showError("Please select an item");
				return;
			}

			TicketItem ticketItem = (TicketItem) object;

			if (ticketItem.isPrintedToKitchen()) {
				POSMessageDialog.showError("Cooking instruction cannot be added to item already printed to kitchen");
				return;
			}

			List<CookingInstruction> list = CookingInstructionDAO.getInstance().findAll();
			CookingInstructionSelectionView cookingInstructionSelectionView = new CookingInstructionSelectionView();
			BeanEditorDialog dialog = new BeanEditorDialog(cookingInstructionSelectionView, Application.getPosWindow(), true);
			dialog.setBean(list);
			dialog.setSize(450, 300);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			if (dialog.isCanceled()) {
				return;
			}

			List<TicketItemCookingInstruction> instructions = cookingInstructionSelectionView.getTicketItemCookingInstructions();
			ticketItem.addCookingInstructions(instructions);

			ticketViewerTable.updateView();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		}
	}

	private void updateInventory(Ticket t) {
		List<TicketItem> tiList = t.getTicketItems();
		List<TicketItem> cons = new ArrayList<TicketItem>();
		List<TicketItem> finalTiList = new ArrayList();
		if (t.getId() != null) {
			Ticket old = TicketDAO.getInstance().findTicket(t.getId());
			List<TicketItem> oldItems = old.getTicketItems();
			finalTiList.addAll(oldItems);
			Map<String, TicketItem> itemMap = new HashMap<String, TicketItem>();

			for (TicketItem ti : oldItems) {
				itemMap.put(ti.getItemId() + ":" + ti.getId(), ti);
			}
			for (TicketItem ti : tiList) {
				if (!itemMap.containsKey(ti.getItemId() + ":" + ti.getId())) {
					cons.add(ti);
					finalTiList.add(ti);
				} else {
					TicketItem ti1 = new TicketItem(ti.getId());
					ti1.setItemCount(ti.getItemCount() - itemMap.get(ti.getItemId() + ":" + ti.getId()).getItemCount());
					itemMap.remove(ti.getItemId() + ":" + ti.getId());
					// if reducing amount then add a void line
					if (ti1.getItemCount() < 0) {
						// add void line
						TicketItem voidTi = new TicketItem(ti);
						voidTi.setName("**" + voidTi.getName());
						voidTi.setItemCount(ti1.getItemCount());
						voidTi.setPrintedToKitchen(false);
						voidTi.setTotalAmount(ti.getTotalAmount() * -1);

						finalTiList.add(voidTi);
					}
					if (ti1.getItemCount() != 0) {
						cons.add(ti1);
					}
				}
			}
			// items completely removed
			for (TicketItem ti : itemMap.values()) {
				TicketItem ti1 = new TicketItem(ti.getId());
				ti1.setItemCount(-1 * ti.getItemCount());
				cons.add(ti1);
				if (!ti.getName().startsWith("**")) {
					TicketItem voidTi = new TicketItem(ti);
					voidTi.setName("**" + voidTi.getName());
					voidTi.setTotalAmount(ti.getTotalAmount() * -1);
					voidTi.setItemCount(ti1.getItemCount());
					voidTi.setPrintedToKitchen(false);
					finalTiList.add(voidTi);
				} else {
					finalTiList.remove(ti);
				}
			}

			// update ticket
			t.getTicketItems().clear();
			for (TicketItem ti : finalTiList) {
				t.addToticketItems(ti);
			}
			t.calculatePrice();
		}
		if (cons.isEmpty()) {
			cons.addAll(t.getTicketItems());
		}
		Session s = InventoryWarehouseItemDAO.getInstance().createNewSession();
		Transaction tx = s.beginTransaction();
		boolean rollback = false;
		ticketLabel: for (TicketItem ti : cons) {
			int itemId = ti.getItemId();
			int itemCount = ti.getItemCount();
			MenuItem mi = MenuItemDAO.getInstance().findByItemId(itemId);
			if (mi != null) {
				List<RecepieItem> riList = mi.getRecepie().getRecepieItems();
				if (riList != null && !riList.isEmpty()) {
					for (RecepieItem ri : riList) {
						if (ri.getInventoryItem() != null) {
							Double itemQty = ri.getPercentage();
							List<InventoryWarehouseItem> wareItemList = InventoryWarehouseItemDAO.getInstance().findByInventoryItem(s, ri.getInventoryItem());
							for (InventoryWarehouseItem wareItem : wareItemList) {
								if (wareItem.getItemLocation().getName().equalsIgnoreCase("cafe")) {
									Double totalUnitsAvilable = wareItem.getTotalRecepieUnits();
									if (totalUnitsAvilable > (itemCount * itemQty)) {
										InventoryWarehouseItemDAO iwDao = InventoryWarehouseItemDAO.getInstance();
										iwDao.refresh(wareItem);
										wareItem.setTotalRecepieUnits(totalUnitsAvilable - (itemCount * itemQty));
										iwDao.saveOrUpdate(wareItem, s);
									} else if (ri.getInventoryItem().getPackageReplenishLevel() >= wareItem.getTotalRecepieUnits()) {
										POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Inventory level less than number of items for " + mi.getName());
									} else {
										POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Inventory level less for " + mi.getName() + ". Please order more!");
										rollback = true;
										break ticketLabel;
									}
								}
							}
						}
					}
				}
			}
		}

		if (!rollback) {
			tx.commit();
		} else {
			tx.rollback();
		}
	}

	private synchronized void doFinishOrder(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doFinishOrder
		try {
			updateModel();
			if (!isInBackOffice) {
				updateInventory(ticket);
			}
			TicketDAO ticketDAO = TicketDAO.getInstance();

			if (ticket.getId() == null) {
				// save ticket first. ticket needs to save so that it
				// contains an id.
				OrderController.assignTicketNumber(ticket);
				OrderController.saveOrder(ticket);
				ticketDAO.refresh(ticket);
			}

			OrderController.saveOrder(ticket);

			if (!isInBackOffice && ticket.needsKitchenPrint()) {
				ReceiptPrintService.printToKitchen(ticket, false);
				ticketDAO.refresh(ticket);
			}

			if (isInBackOffice) {
				// get all transactions for this ticket.
				PosTransactionDAO transDAO = PosTransactionDAO.getInstance();
				List<PosTransaction> allTrans = transDAO.findAllTransactionByTicket(ticket);
				Session session = transDAO.createNewSession();
				Transaction t = session.beginTransaction();
				if (allTrans.size() > 0) {
					// edit the first transaction to amounts as per current
					// ticket.
					ticket.calculatePrice();
					PosTransaction firstTrans = allTrans.get(0);
					firstTrans.setAmount(ticket.getTotalAmount());
					firstTrans.setTenderAmount(NumberUtil.roundOff(ticket.getTotalAmount()));
					// save the transaction.
					transDAO.update(firstTrans);
					for (int i = 1; i < allTrans.size(); i++) {
						PosTransaction trans = allTrans.get(i);
						trans.setTicket(null);
						transDAO.delete(trans);
					}
					ticket.setPaidAmount(ticket.getTotalAmount());
					ticket.setDueAmount(0d);
					ticket.getTransactions().clear();
					ticket.getTransactions().add(firstTrans);
					//ticket.calculatePrice();
					ticketDAO.saveOrUpdate(ticket);
				}				
				try {
					t.commit();
				} catch (Exception e) {
					t.rollback();
				} finally {
					session.close();
				}
			}

			closeView(false);

		} catch (StaleObjectStateException e) {
			POSMessageDialog.showError("It seems the ticket has been modified by some other person or terminal. Save failed.");
			return;
		} catch (PosException x) {
			POSMessageDialog.showError(x.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_doFinishOrder

	private void closeView(boolean orderCanceled) {

		if (isInBackOffice) {
			BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();

			JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
			int index = tabbedPane.indexOfTab("OrderView");
			if (index != -1) {
				tabbedPane.remove(index);
			}
			index = tabbedPane.indexOfTab(AdminTicketManagerAction.adminTicketManagerName);
			if (index != -1) {
				AdminTicketManagerView atv = (AdminTicketManagerView) tabbedPane.getComponentAt(index);
				atv.refreshView();
				tabbedPane.setSelectedIndex(index);
			}
		} else {
			if (TerminalConfig.isCashierMode()) {
				// String message =
				// "Order canceled. What do you want to do next?";
				// if(!orderCanceled) {
				// message = "Ticket no " + getTicket().getId() +
				// " saved. What do you want to do next?";
				// }
				//
				// Window ancestor = SwingUtilities.getWindowAncestor(this);
				// CashierModeNextActionDialog dialog = new
				// CashierModeNextActionDialog((Frame) ancestor, message);
				// dialog.open();
				RootView.getInstance().showView(CashierSwitchBoardView.VIEW_NAME);
			} else {
				RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
			}
		}
	}

	private void doCancelOrder(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doCancelOrder
		closeView(true);
	}// GEN-LAST:event_doCancelOrder

	private synchronized void updateModel() {
		if (ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0) {
			throw new PosException(com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
		}
		// check if all items are void if yes then suggest to void ticket.
		int voidCount = 0;
		int posCount = 0;
		for (TicketItem ti : ticket.getTicketItems()) {
			if (ti.getName().startsWith("**")) {
				voidCount += ti.getItemCount();
			} else {
				posCount += ti.getItemCount();
			}
		}
		if (voidCount + posCount == 0) {
			throw new PosException("All items voided. Please void the whole ticket.");
		}
		ticket.setTokenNo(tfTokenNo.getInteger());
		ticket.calculatePrice();
	}

	private void doPayNow(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doPayNow
		try {
			updateModel();
			updateInventory(ticket);
			if (ticket.getId() == null) {
				OrderController.assignTicketNumber(ticket);
			}
			OrderController.saveOrder(ticket);

			firePayOrderSelected();
		} catch (PosException e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}// GEN-LAST:event_doPayNow

	private void doDeleteSelection(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doDeleteSelection
		Object object = ticketViewerTable.deleteSelectedItem();
		if (object != null) {
			updateView();

			// if (object instanceof TicketItemModifier) {
			// ModifierView modifierView =
			// OrderView.getInstance().getModifierView();
			// if (modifierView.isVisible()) {
			// modifierView.updateVisualRepresentation();
			// }
			// }
		}

	}// GEN-LAST:event_doDeleteSelection

	private void doIncreaseAmount(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doIncreaseAmount
		if (ticketViewerTable.increaseItemAmount()) {
			// ModifierView modifierView =
			// OrderView.getInstance().getModifierView();
			// if (modifierView.isVisible()) {
			// modifierView.updateVisualRepresentation();
			// }
			updateView();
		}

	}// GEN-LAST:event_doIncreaseAmount

	private void doDecreaseAmount(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doDecreaseAmount
		if (ticketViewerTable.decreaseItemAmount()) {
			// ModifierView modifierView =
			// OrderView.getInstance().getModifierView();
			// if (modifierView.isVisible()) {
			// modifierView.updateVisualRepresentation();
			// }
			updateView();
		}
	}// GEN-LAST:event_doDecreaseAmount

	private void doScrollDown(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doScrollDown
		ticketViewerTable.scrollDown();
	}// GEN-LAST:event_doScrollDown

	private void doScrollUp(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doScrollUp
		ticketViewerTable.scrollUp();
	}// GEN-LAST:event_doScrollUp

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.TransparentPanel controlPanel;
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnDecreaseAmount;
	private com.floreantpos.swing.PosButton btnDelete;
	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnIncreaseAmount;
	private com.floreantpos.swing.PosButton btnPay;
	private com.floreantpos.swing.PosButton btnScrollDown;
	private com.floreantpos.swing.PosButton btnScrollUp;
	// private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel lblTax;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel lblTokenNo;
	private javax.swing.JLabel lblCustomerName;
	private javax.swing.JLabel jLabel6;
	private com.floreantpos.swing.TransparentPanel jPanel1;
	private com.floreantpos.swing.TransparentPanel jPanel2;
	private com.floreantpos.swing.TransparentPanel ticketAmountPanel;
	private com.floreantpos.swing.TransparentPanel scrollerPanel;
	private javax.swing.JScrollPane ticketScrollPane;
	// private javax.swing.JTextField tfDiscount;
	private javax.swing.JTextField tfSubtotal;
	private IntegerTextField tfTokenNo;
	private javax.swing.JTextField tfTax;
	private javax.swing.JTextField tfTotal;
	private com.floreantpos.ui.ticket.TicketViewerTable ticketViewerTable;
	// private JTextField tfServiceCharge;
	// private JLabel lblServiceCharge;
	private PosButton btnAddCookingInstruction;

	// End of variables declaration//GEN-END:variables

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket _ticket) {
		this.ticket = _ticket;

		ticketViewerTable.setTicket(_ticket);

		updateView();
	}

	public void addTicketItem(TicketItem ticketItem) {
		ticketViewerTable.addTicketItem(ticketItem);
		updateView();
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		ticketViewerTable.removeModifier(parent, modifier);
	}

	public void updateAllView() {
		ticketViewerTable.updateView();
		updateView();
	}

	public void selectRow(int rowIndex) {
		ticketViewerTable.selectRow(rowIndex);
	}

	public void updateView() {
		if (ticket == null) {
			tfSubtotal.setText("");
			// tfDiscount.setText("");
			tfTax.setText("");
			// tfServiceCharge.setText("");
			tfTotal.setText("");

			setBorder(BorderFactory.createTitledBorder(null, "Ticket [ NEW ]", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

			return;
		}

		ticket.calculatePrice();

		tfSubtotal.setText(NumberUtil.formatNumber(ticket.getSubtotalAmount()));
		// tfDiscount.setText(NumberUtil.formatNumber(ticket.getDiscountAmount()));

		if (Application.getInstance().isPriceIncludesTax()) {
			tfTax.setText("INCLUDED");
		} else {
			tfTax.setText(NumberUtil.formatNumber(ticket.getTaxAmount()));
		}
		//
		// tfServiceCharge.setText(NumberUtil.formatNumber(ticket.getServiceCharge()));
		tfTotal.setText(NumberUtil.formatNumber(ticket.getTotalAmount()));
		if (ticket.getCustomer() != null) {
			lblCustomerName.setText("CUSTOMER: " + ticket.getCustomer().getName());
			lblCustomerName.setForeground(Color.red);
		} else {
			lblCustomerName.setText("CUSTOMER: ");
		}
		tfTokenNo.setText(String.valueOf(ticket.getTokenNo()));

		if (ticket.getId() == null) {
			setBorder(BorderFactory.createTitledBorder(null, "Ticket [ NEW ]", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		} else {
			setBorder(BorderFactory.createTitledBorder(null, "Ticket #" + TicketUtils.getTicketNumber(ticket), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		}

		if (ticket.getType() != null && ticket.getType().getProperties() != null) {
			btnSave.setVisible(ticket.getType().getProperties().isAllowDelayPayment());
		} else {
			btnSave.setVisible(true);
		}
	}

	public void addOrderListener(OrderListener listenre) {
		orderListeners.add(listenre);
	}

	public void removeOrderListener(OrderListener listenre) {
		orderListeners.remove(listenre);
	}

	public void firePayOrderSelected() {
		for (OrderListener listener : orderListeners) {
			listener.payOrderSelected(getTicket());
		}
	}

	public void setControlsVisible(boolean visible) {
		if (visible) {
			controlPanel.setVisible(true);
			btnIncreaseAmount.setEnabled(true);
			btnDecreaseAmount.setEnabled(true);
			btnDelete.setEnabled(true);
		} else {
			controlPanel.setVisible(false);
			btnIncreaseAmount.setEnabled(false);
			btnDecreaseAmount.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	private void updateSelectionView() {
		Object selectedObject = ticketViewerTable.getSelected();

		OrderView orderView = OrderView.getInstance();

		TicketItem selectedItem = null;
		if (selectedObject instanceof TicketItem) {
			selectedItem = (TicketItem) selectedObject;
			MenuItemDAO dao = new MenuItemDAO();
			MenuItem menuItem = dao.get(selectedItem.getItemId());
			MenuGroup menuGroup = menuItem.getParent();
			MenuItemView itemView = OrderView.getInstance().getItemView();
			if (!menuGroup.equals(itemView.getMenuGroup())) {
				itemView.setMenuGroup(menuGroup);
			}
			orderView.showView(MenuItemView.VIEW_NAME);

			MenuCategory menuCategory = menuGroup.getParent();
			orderView.getCategoryView().setSelectedCategory(menuCategory);
			return;
		} else if (selectedObject instanceof TicketItemModifier) {
			selectedItem = ((TicketItemModifier) selectedObject).getParent().getParent();
		}
		if (selectedItem == null)
			return;

		ModifierView modifierView = orderView.getModifierView();

		if (selectedItem.isHasModifiers()) {
			MenuItemDAO dao = new MenuItemDAO();
			MenuItem menuItem = dao.get(selectedItem.getItemId());
			if (!menuItem.equals(modifierView.getMenuItem())) {
				menuItem = dao.initialize(menuItem);
				modifierView.setMenuItem(menuItem, selectedItem);
			}

			MenuCategory menuCategory = menuItem.getParent().getParent();
			orderView.getCategoryView().setSelectedCategory(menuCategory);

			orderView.showView(ModifierView.VIEW_NAME);
		}
	}
}
