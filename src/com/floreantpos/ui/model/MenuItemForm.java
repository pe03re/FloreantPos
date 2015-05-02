/*
 * FoodItemEditor.java
 *
 * Created on August 2, 2006, 10:34 PM
 */

package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleDocument;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.IUpdatebleView;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

/**
 * 
 * @author MShahriar
 */
public class MenuItemForm extends BeanEditor<MenuItem> implements ActionListener, ChangeListener {
	ShiftTableModel shiftTableModel;

	/** Creates new form FoodItemEditor */
	public MenuItemForm() throws Exception {
		this(new MenuItem());
	}

	public MenuItemForm(MenuItem menuItem) throws Exception {
		initComponents();

		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));

		TaxDAO taxDAO = new TaxDAO();
		List<Tax> taxes = taxDAO.findAll();
		cbTax.setModel(new ComboBoxModel(taxes));

		// menuItemModifierGroups = menuItem.getMenuItemModiferGroups();
		shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));

		setBean(menuItem);
		addRecepieExtension();
	}

	protected void doSelectImageFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int option = fileChooser.showOpenDialog(null);

		if (option == JFileChooser.APPROVE_OPTION) {
			File imageFile = fileChooser.getSelectedFile();
			try {

				byte[] itemImage = FileUtils.readFileToByteArray(imageFile);
				int imageSize = itemImage.length / 1024;

				if (imageSize > 20) {
					POSMessageDialog.showMessage("The image is too large. Please select an image within 20 KB in size");
					itemImage = null;
					return;
				}
				FileUtils.copyFileToDirectory(imageFile, new File("images/"));
				String filePath = new String("images/") + imageFile.getName();

				ImageIcon imageIcon = new ImageIcon(new ImageIcon(itemImage).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
				lblImagePreview.setIcon(imageIcon);

				MenuItem menuItem = (MenuItem) getBean();
				menuItem.setImage(filePath);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doClearImage() {
		MenuItem menuItem = (MenuItem) getBean();
		menuItem.setImage(null);
		lblImagePreview.setIcon(null);
	}

	public void addRecepieExtension() {
		InventoryPlugin plugin = Application.getPluginManager().getPlugin(InventoryPlugin.class);
		if (plugin == null) {
			return;
		}
		plugin.addRecepieView(tabbedPane, (MenuItem) getBean());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {

		tabbedPane = new javax.swing.JTabbedPane();
		tabGeneral = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
		tfName = new com.floreantpos.swing.FixedLengthTextField();
		tfName.setLength(120);
		jLabel4 = new javax.swing.JLabel();
		jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
		cbGroup = new javax.swing.JComboBox();
		btnNewGroup = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
		tfPrice = new DoubleTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
		cbTax = new javax.swing.JComboBox();
		btnNewTax = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel5 = new javax.swing.JLabel();
		tfDiscountRate = new DoubleTextField();
		tfDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
		chkVisible = new javax.swing.JCheckBox();
		// tabModifier = new javax.swing.JPanel();
		// btnNewModifierGroup = new javax.swing.JButton();
		// btnDeleteModifierGroup = new javax.swing.JButton();
		// btnEditModifierGroup = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		tableTicketItemModifierGroups = new javax.swing.JTable();
		tabShift = new javax.swing.JPanel();
		btnDeleteShift = new javax.swing.JButton();
		btnAddShift = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		shiftTable = new javax.swing.JTable();

		jLabel1.setText(Messages.getString("LABEL_NAME"));
		jLabel4.setText(Messages.getString("LABEL_GROUP"));

		btnNewGroup.setText("...");
		btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCreateNewGroup(evt);
			}
		});

		if (Application.getInstance().isPriceIncludesTax()) {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_INCLUDING_TAX"));
		} else {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_EXCLUDING_TAX"));
		}

		tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel6.setText(Messages.getString("LABEL_TAX"));

		btnNewTax.setText("...");
		btnNewTax.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewTaxdoCreateNewTax(evt);
			}
		});

		jLabel2.setText(com.floreantpos.POSConstants.DISCOUNT_RATE + ":");

		jLabel5.setText("%");

		chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
		chkVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));

		tabbedPane.addTab(com.floreantpos.POSConstants.GENERAL, tabGeneral);
		btnAddShift.addActionListener(this);
		btnDeleteShift.addActionListener(this);

		tfDiscountRate.setDocument(new DoubleDocument());
		tabGeneral.setLayout(new MigLayout("", "[104px][100px,grow][][49px]", "[19px][][][][25px][][19px][19px][25px][][][][][][15px]"));

		lblBuyPrice = new JLabel(Messages.getString("LABEL_BUY_PRICE"));
		lblBuyPrice.setEnabled(false);
		tabGeneral.add(lblBuyPrice, "cell 0 5");

		tfBuyPrice = new DoubleTextField();
		tfBuyPrice.setEnabled(false);
		tfBuyPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		tabGeneral.add(tfBuyPrice, "cell 1 5,growx");
		tabGeneral.add(jLabel3, "cell 0 6,alignx left,aligny center");
		tabGeneral.add(jLabel4, "cell 0 4,alignx left,aligny center");
		setLayout(new BorderLayout(0, 0));
		tabGeneral.add(jLabel6, "cell 0 8,alignx left,aligny center"); //$NON-NLS-1$
		tabGeneral.add(jLabel2, "cell 0 7,alignx left,aligny center"); //$NON-NLS-1$
		tabGeneral.add(jLabel1, "cell 0 0,alignx left,aligny center"); //$NON-NLS-1$
		tabGeneral.add(tfName, "cell 1 0 3 1,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(cbGroup, "cell 1 4,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(btnNewGroup, "cell 3 4,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(tfDiscountRate, "cell 1 7,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(cbTax, "cell 1 8,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(tfPrice, "cell 1 6,growx,aligny top"); //$NON-NLS-1$

		// lblKitchenPrinter = new JLabel("Kitchen & Bar Printer");
		//		tabGeneral.add(lblKitchenPrinter, "cell 0 9"); //$NON-NLS-1$
		//
		// cbPrinter = new JComboBox<VirtualPrinter>(new
		// DefaultComboBoxModel<VirtualPrinter>(VirtualPrinterDAO.getInstance().findAll()
		// .toArray(new VirtualPrinter[0])));
		//		tabGeneral.add(cbPrinter, "cell 1 9,growx"); //$NON-NLS-1$

		JLabel lblImage = new JLabel("Image:");
		lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
		tabGeneral.add(lblImage, "cell 0 10,aligny center");

		lblImagePreview = new JLabel("");
		lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
		lblImagePreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblImagePreview.setPreferredSize(new Dimension(100, 100));
		tabGeneral.add(lblImagePreview, "cell 1 10");

		JButton btnSelectImage = new JButton("...");
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSelectImageFile();
			}
		});
		tabGeneral.add(btnSelectImage, "cell 2 10");

		btnClearImage = new JButton("Clear");
		btnClearImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClearImage();
			}
		});
		tabGeneral.add(btnClearImage, "cell 3 10");

		lblButtonColor = new JLabel(Messages.getString("MenuItemForm.lblButtonColor.text")); //$NON-NLS-1$
		tabGeneral.add(lblButtonColor, "cell 0 11");

		btnButtonColor = new JButton(); //$NON-NLS-1$
		btnButtonColor.setPreferredSize(new Dimension(140, 40));
		tabGeneral.add(btnButtonColor, "cell 1 11");

		lblTextColor = new JLabel(Messages.getString("MenuItemForm.lblTextColor.text")); //$NON-NLS-1$
		tabGeneral.add(lblTextColor, "cell 0 12");

		btnTextColor = new JButton(Messages.getString("MenuItemForm.SAMPLE_TEXT")); //$NON-NLS-1$
		btnTextColor.setPreferredSize(new Dimension(140, 40));
		tabGeneral.add(btnTextColor, "cell 1 12");

		cbShowTextWithImage = new JCheckBox("Show image only");
		cbShowTextWithImage.setActionCommand("Show Text with Image");
		tabGeneral.add(cbShowTextWithImage, "cell 1 13"); //$NON-NLS-1$
		tabGeneral.add(chkVisible, "cell 1 14,alignx left,aligny top"); //$NON-NLS-1$
		tabGeneral.add(btnNewTax, "cell 2 8,alignx left,aligny top"); //$NON-NLS-1$
		tabGeneral.add(jLabel5, "cell 2 7"); //$NON-NLS-1$
		add(tabbedPane);

		// addRecepieExtension();

		btnButtonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, "Select color", btnButtonColor.getBackground());
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, "Select color", btnTextColor.getForeground());
				btnTextColor.setForeground(color);
			}
		});

		jScrollPane1.setViewportView(tableTicketItemModifierGroups);

		// GroupLayout jPanel2Layout = new GroupLayout(tabModifier);
		// jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(
		// jPanel2Layout
		// .createSequentialGroup()
		// .addContainerGap()
		// .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 412,
		// Short.MAX_VALUE)
		// .addPreferredGap(ComponentPlacement.RELATED)
		// .addGroup(
		// jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(btnDeleteModifierGroup)
		// .addComponent(btnEditModifierGroup).addComponent(btnNewModifierGroup)).addContainerGap()));
		// jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(
		// jPanel2Layout
		// .createSequentialGroup()
		// .addContainerGap()
		// .addGroup(
		// jPanel2Layout
		// .createParallelGroup(Alignment.LEADING)
		// .addGroup(
		// jPanel2Layout.createSequentialGroup().addComponent(btnNewModifierGroup)
		// .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnEditModifierGroup)
		// .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDeleteModifierGroup))
		// .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 421,
		// Short.MAX_VALUE)).addContainerGap()));
		// tabModifier.setLayout(jPanel2Layout);
		//
		// tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUPS,
		// tabModifier);

		btnDeleteShift.setText(com.floreantpos.POSConstants.DELETE_SHIFT);

		btnAddShift.setText(com.floreantpos.POSConstants.ADD_SHIFT);

		shiftTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } },
				new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		jScrollPane2.setViewportView(shiftTable);

		org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(tabShift);
		tabShift.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel3Layout
						.createSequentialGroup()
						.addContainerGap(76, Short.MAX_VALUE)
						.add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 387, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup().add(btnAddShift).add(5, 5, 5).add(btnDeleteShift))).addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel3Layout.createSequentialGroup().add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 281, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnAddShift).add(btnDeleteShift))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		tabbedPane.addTab(com.floreantpos.POSConstants.SHIFTS, tabShift);

		tabbedPane.addChangeListener(this);
	}// </editor-fold>//GEN-END:initComponents

	private void btnNewTaxdoCreateNewTax(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewTaxdoCreateNewTax
		BeanEditorDialog dialog = new BeanEditorDialog(new TaxForm(), BackOfficeWindow.getInstance(), true);
		dialog.open();
	}// GEN-LAST:event_btnNewTaxdoCreateNewTax

	// private void
	// btnNewModifierGroupActionPerformed(java.awt.event.ActionEvent evt) {//
	// GEN-FIRST:event_btnNewModifierGroupActionPerformed
	//
	// }// GEN-LAST:event_btnNewModifierGroupActionPerformed

	private void doCreateNewGroup(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doCreateNewGroup
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(editor, getParentFrame(), true);
		dialog.open();
		if (!dialog.isCanceled()) {
			MenuGroup foodGroup = (MenuGroup) editor.getBean();
			ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
			model.addElement(foodGroup);
			model.setSelectedItem(foodGroup);
		}
	}// GEN-LAST:event_doCreateNewGroup

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnAddShift;
	// private javax.swing.JButton btnDeleteModifierGroup;
	private javax.swing.JButton btnDeleteShift;
	// private javax.swing.JButton btnEditModifierGroup;
	private javax.swing.JButton btnNewGroup;
	// private javax.swing.JButton btnNewModifierGroup;
	private javax.swing.JButton btnNewTax;
	private javax.swing.JComboBox cbGroup;
	private javax.swing.JComboBox cbTax;
	private javax.swing.JCheckBox chkVisible;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel tabGeneral;
	// private javax.swing.JPanel tabModifier;
	private javax.swing.JPanel tabShift;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane tabbedPane;
	private javax.swing.JTable shiftTable;
	private javax.swing.JTable tableTicketItemModifierGroups;
	private DoubleTextField tfDiscountRate;
	private com.floreantpos.swing.FixedLengthTextField tfName;
	private DoubleTextField tfPrice;
	// End of variables declaration//GEN-END:variables
	// private List<MenuItemModifierGroup> menuItemModifierGroups;
	// private MenuItemMGListModel menuItemMGListModel;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JCheckBox cbShowTextWithImage;
	private JLabel lblBuyPrice;
	private DoubleTextField tfBuyPrice;
	// private JLabel lblKitchenPrinter;
	// private JComboBox<VirtualPrinter> cbPrinter;
	// private JLabel lblBarcode;
	// private FixedLengthTextField tfBarcode;
	private JLabel lblButtonColor;
	private JLabel lblTextColor;
	private JButton btnButtonColor;
	private JButton btnTextColor;

	// private JLabel lblTranslatedName;
	// private FixedLengthTextField tfTranslatedName;
	// private JLabel lblSortOrder;
	// private IntegerTextField tfSortOrder;

	// private void addMenuItemModifierGroup() {
	// try {
	// MenuItemModifierGroupForm form = new MenuItemModifierGroupForm();
	// BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
	// true);
	// dialog.open();
	// if (!dialog.isCanceled()) {
	// MenuItemModifierGroup modifier = (MenuItemModifierGroup) form.getBean();
	// // modifier.setParentMenuItem((MenuItem) this.getBean());
	//
	// for (MenuItemModifierGroup modifierGroup : menuItemModifierGroups) {
	// if (modifierGroup.getModifierGroup().equals(modifier.getModifierGroup()))
	// {
	// POSMessageDialog.showError("This modifier group already exists");
	// return;
	// }
	// }
	//
	// menuItemMGListModel.add(modifier);
	// }
	// } catch (Exception x) {
	// MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
	// }
	// }
	//
	// private void editMenuItemModifierGroup() {
	// try {
	// int index = tableTicketItemModifierGroups.getSelectedRow();
	// if (index < 0)
	// return;
	//
	// MenuItemModifierGroup menuItemModifierGroup =
	// menuItemMGListModel.get(index);
	//
	// MenuItemModifierGroupForm form = new
	// MenuItemModifierGroupForm(menuItemModifierGroup);
	// BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
	// true);
	// dialog.open();
	// if (!dialog.isCanceled()) {
	// // menuItemModifierGroup.setParentMenuItem((MenuItem)
	// // this.getBean());
	// menuItemMGListModel.fireTableDataChanged();
	// }
	// } catch (Exception x) {
	// MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
	// }
	// }
	//
	// private void deleteMenuItemModifierGroup() {
	// try {
	// int index = tableTicketItemModifierGroups.getSelectedRow();
	// if (index < 0)
	// return;
	//
	// if (ConfirmDeleteDialog.showMessage(this,
	// com.floreantpos.POSConstants.CONFIRM_DELETE,
	// com.floreantpos.POSConstants.CONFIRM) == ConfirmDeleteDialog.YES) {
	// menuItemMGListModel.remove(index);
	// }
	// } catch (Exception x) {
	// MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
	// }
	// }

	@Override
	public boolean save() {
		Session session = MenuItemDAO.getInstance().createNewSession();
		Transaction tx = session.beginTransaction();
		boolean actionPerformed = false;
		try {
			if (updateModel()) {
				MenuItem menuItem = (MenuItem) getBean();
				if (menuItem.getPrice() == null || menuItem.getPrice().isNaN()) {
					POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please add a valid Sell Price!!");
					actionPerformed = false;
				} else if (menuItem.getParent() == null) {
					POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please add a valid Menu Group!!");
					actionPerformed = false;
				} else if (menuItem.getDiscountRate() == null || menuItem.getDiscountRate().isNaN()) {
					POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please add a valid Discount Rate!!");
					actionPerformed = false;
				} else if (menuItem.getTax() == null) {
					POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please select a valid Tax!!");
					actionPerformed = false;
				} else if (menuItem.getRecepie() == null) {
					POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please add a Recipe!!");
					actionPerformed = false;
				} else {
					MenuItemDAO menuItemDAO = new MenuItemDAO();
					menuItemDAO.saveOrUpdate(menuItem);
					actionPerformed = true;
				}
			}
			if (actionPerformed) {
				tx.commit();
			} else {
				tx.rollback();
				return false;
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
			POSMessageDialog.showError(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		MenuItem menuItem = getBean();

		// if (menuItem.getId() != null &&
		// !Hibernate.isInitialized(menuItem.getMenuItemModiferGroups())) {
		// // initialize food item modifer groups.
		// MenuItemDAO dao = new MenuItemDAO();
		// Session session = dao.getSession();
		// menuItem = (MenuItem) session.merge(menuItem);
		// Hibernate.initialize(menuItem.getMenuItemModiferGroups());
		// session.close();
		// }

		tfName.setText(menuItem.getName());
		// tfTranslatedName.setText(menuItem.getTranslatedName());
		// tfBarcode.setText(menuItem.getBarcode());

		tfBuyPrice.setText(String.valueOf(getBuyPriceFromInventory(menuItem)));
		tfPrice.setText(String.valueOf(menuItem.getPrice()));
		tfDiscountRate.setText(String.valueOf(menuItem.getDiscountRate()));
		chkVisible.setSelected(menuItem.isVisible());
		cbShowTextWithImage.setSelected(menuItem.isShowImageOnly());
		if (menuItem.getImage() != null) {
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(menuItem.getImage()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
			lblImagePreview.setIcon(imageIcon);
		}

		cbGroup.setSelectedItem(menuItem.getParent());
		cbTax.setSelectedItem(menuItem.getTax());

		// cbPrinter.setSelectedItem(menuItem.getVirtualPrinter());

		// if (menuItem.getSortOrder() != null) {
		// tfSortOrder.setText(menuItem.getSortOrder().toString());
		// }

		if (menuItem.getButtonColor() != null) {
			Color color = new Color(menuItem.getButtonColor());
			btnButtonColor.setBackground(color);
			btnTextColor.setBackground(color);
		}

		if (menuItem.getTextColor() != null) {
			Color color = new Color(menuItem.getTextColor());
			btnTextColor.setForeground(color);
		}
	}

	private Double getBuyPriceFromInventory(MenuItem menuItem) {
		double buyPrice = 0.0d;
		if (menuItem != null && menuItem.getRecepie() != null) {
			List<RecepieItem> riList = menuItem.getRecepie().getRecepieItems();
			if (riList != null && !riList.isEmpty()) {
				for (RecepieItem ri : riList) {
					if (ri != null && ri.getInventoryItem() != null) {
						Double itemQty = ri.getPercentage();
						buyPrice += ri.getInventoryItem().getAverageRunitPrice() * itemQty;
					}
				}
			}
		}
		return buyPrice;
	}

	@Override
	protected boolean updateModel() {
		String itemName = tfName.getText();
		if (POSUtil.isBlankOrNull(itemName)) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Name is required");
			return false;
		}

		MenuItem menuItem = getBean();
		menuItem.setName(itemName);
		menuItem.setBarcode("");
		menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
		menuItem.setPrice(Double.valueOf(tfPrice.getText()));
		menuItem.setBuyPrice(Double.valueOf(tfPrice.getText()));
		menuItem.setTax((Tax) cbTax.getSelectedItem());
		menuItem.setVisible(chkVisible.isSelected());
		menuItem.setShowImageOnly(cbShowTextWithImage.isSelected());

		menuItem.setTranslatedName(itemName);
		menuItem.setSortOrder(menuItem.getSortOrder());

		menuItem.setButtonColor(btnButtonColor.getBackground().getRGB());
		menuItem.setTextColor(btnTextColor.getForeground().getRGB());

		try {
			menuItem.setDiscountRate(Double.parseDouble(tfDiscountRate.getText()));
		} catch (Exception x) {
		}
		// menuItem.setMenuItemModiferGroups(menuItemModifierGroups);
		menuItem.setShifts(shiftTableModel.getShifts());

		int tabCount = tabbedPane.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			Component componentAt = tabbedPane.getComponent(i);
			if (componentAt instanceof IUpdatebleView) {
				IUpdatebleView view = (IUpdatebleView) componentAt;
				if (!view.updateModel(menuItem)) {
					return false;
				}
			}
		}

		menuItem.setVirtualPrinter(menuItem.getVirtualPrinter());
		return true;
	}

	public String getDisplayText() {
		MenuItem foodItem = (MenuItem) getBean();
		if (foodItem.getId() == null) {
			return com.floreantpos.POSConstants.NEW_MENU_ITEM;
		}
		return com.floreantpos.POSConstants.EDIT_MENU_ITEM;
	}

	// class MenuItemMGListModel extends AbstractTableModel {
	// String[] cn = { com.floreantpos.POSConstants.GROUP_NAME,
	// com.floreantpos.POSConstants.MIN_QUANTITY,
	// com.floreantpos.POSConstants.MAX_QUANTITY };
	//
	// MenuItemMGListModel() {
	// }
	//
	// public MenuItemModifierGroup get(int index) {
	// return menuItemModifierGroups.get(index);
	// }
	//
	// public void add(MenuItemModifierGroup group) {
	// if (menuItemModifierGroups == null) {
	// menuItemModifierGroups = new ArrayList<MenuItemModifierGroup>();
	// }
	// menuItemModifierGroups.add(group);
	// fireTableDataChanged();
	// }
	//
	// public void remove(int index) {
	// if (menuItemModifierGroups == null) {
	// return;
	// }
	// menuItemModifierGroups.remove(index);
	// fireTableDataChanged();
	// }
	//
	// public void remove(MenuItemModifierGroup group) {
	// if (menuItemModifierGroups == null) {
	// return;
	// }
	// menuItemModifierGroups.remove(group);
	// fireTableDataChanged();
	// }
	//
	// public int getRowCount() {
	// if (menuItemModifierGroups == null)
	// return 0;
	//
	// return menuItemModifierGroups.size();
	//
	// }

	// public int getColumnCount() {
	// return cn.length;
	// }
	//
	// @Override
	// public String getColumnName(int column) {
	// return cn[column];
	// }
	//
	// public Object getValueAt(int rowIndex, int columnIndex) {
	// MenuItemModifierGroup menuItemModifierGroup =
	// menuItemModifierGroups.get(rowIndex);
	//
	// switch (columnIndex) {
	// case 0:
	// return menuItemModifierGroup.getModifierGroup().getName();
	//
	// case 1:
	// return Integer.valueOf(menuItemModifierGroup.getMinQuantity());
	//
	// case 2:
	// return Integer.valueOf(menuItemModifierGroup.getMaxQuantity());
	// }
	// return null;
	// }
	// }

	class ShiftTableModel extends AbstractTableModel {
		List<MenuItemShift> shifts;
		String[] cn = { com.floreantpos.POSConstants.START_TIME, com.floreantpos.POSConstants.END_TIME, com.floreantpos.POSConstants.PRICE };
		Calendar calendar = Calendar.getInstance();

		ShiftTableModel(List<MenuItemShift> shifts) {
			if (shifts == null) {
				this.shifts = new ArrayList<MenuItemShift>();
			} else {
				this.shifts = new ArrayList<MenuItemShift>(shifts);
			}
		}

		public MenuItemShift get(int index) {
			return shifts.get(index);
		}

		public void add(MenuItemShift group) {
			if (shifts == null) {
				shifts = new ArrayList<MenuItemShift>();
			}
			shifts.add(group);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (shifts == null) {
				return;
			}
			shifts.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemShift group) {
			if (shifts == null) {
				return;
			}
			shifts.remove(group);
			fireTableDataChanged();
		}

		public int getRowCount() {
			if (shifts == null)
				return 0;

			return shifts.size();

		}

		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public List<MenuItemShift> getShifts() {
			return shifts;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemShift shift = shifts.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getStartTime());

			case 1:
				return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getEndTime());

			case 2:
				return String.valueOf(shift.getShiftPrice());
			}
			return null;
		}
	}

	private void addShift() {
		// TODO: ???
		MenuItemShiftDialog dialog = new MenuItemShiftDialog((Dialog) this.getTopLevelAncestor());
		dialog.setSize(350, 220);
		dialog.open();

		if (!dialog.isCanceled()) {
			MenuItemShift menuItemShift = dialog.getMenuItemShift();
			shiftTableModel.add(menuItemShift);
		}
	}

	private void deleteShift() {
		int selectedRow = shiftTable.getSelectedRow();
		if (selectedRow >= 0) {
			shiftTableModel.remove(selectedRow);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		// if (actionCommand.equals("AddModifierGroup")) {
		// addMenuItemModifierGroup();
		// } else if (actionCommand.equals("EditModifierGroup")) {
		// editMenuItemModifierGroup();
		// } else if (actionCommand.equals("DeleteModifierGroup")) {
		// deleteMenuItemModifierGroup();
		// } else
		if (actionCommand.equals(com.floreantpos.POSConstants.ADD_SHIFT)) {
			addShift();
		} else if (actionCommand.equals(com.floreantpos.POSConstants.DELETE_SHIFT)) {
			deleteShift();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Component selectedComponent = tabbedPane.getSelectedComponent();
		if (!(selectedComponent instanceof IUpdatebleView)) {
			return;
		}

		IUpdatebleView view = (IUpdatebleView) selectedComponent;

		MenuItem menuItem = (MenuItem) getBean();
		view.initView(menuItem);
	}
}
