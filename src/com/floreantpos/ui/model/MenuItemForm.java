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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ListTableModel;
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
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

public class MenuItemForm extends BeanEditor<MenuItem> implements ActionListener, ChangeListener {
	ShiftTableModel shiftTableModel;

	private javax.swing.JButton btnAddShift;
	private javax.swing.JButton btnDeleteShift;
	// private javax.swing.JButton btnNewGroup;
	// private javax.swing.JButton btnNewTax;
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
	private javax.swing.JPanel tabShift;
	private Component tabReciepe;

	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane tabbedPane;
	private javax.swing.JTable shiftTable;
	private javax.swing.JTable tableTicketItemModifierGroups;
	private DoubleTextField tfDiscountRate;
	private com.floreantpos.swing.FixedLengthTextField tfName;
	private DoubleTextField tfPrice;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JButton btnSelectImage = new JButton("...");
	private JFileChooser fileChooser = new JFileChooser();
	private JLabel lblImage = new JLabel("Image:");
	private JCheckBox cbShowTextWithImage;
	private JLabel lblBuyPrice;
	private DoubleTextField tfBuyPrice;
	private JLabel lblButtonColor;
	private JLabel lblTextColor;
	private JButton btnButtonColor;
	private JButton btnTextColor;

	/** Creates new form FoodItemEditor */
	public MenuItemForm() {
		this(new MenuItem());
		setPreferredSize(new Dimension(300, 400));
	}

	public MenuItemForm(MenuItem menuItem) {
		initComponents();
		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));

		TaxDAO taxDAO = new TaxDAO();
		List<Tax> taxes = taxDAO.findAll();
		cbTax.setModel(new ComboBoxModel(taxes));

		shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));
		setBean(menuItem);
		addRecepieExtension();

		setFieldsEnable(false);
	}

	public void createNew() {
		MenuItem newMenu = new MenuItem();
		setBean(newMenu);
		if (tabReciepe != null && tabReciepe instanceof IUpdatebleView) {
			IUpdatebleView view = (IUpdatebleView) tabReciepe;
			view.clearTableModel();
			view.setFieldsEnable(true);
		}
		// check whether to make new recipe
	}

	public void setFieldsEnable(boolean enable) {
		this.tfName.setEnabled(enable);
		this.btnAddShift.setEnabled(enable);
		this.btnDeleteShift.setEnabled(enable);
		/*
		 * this.btnNewGroup.setEnabled(enable);
		 * this.btnNewTax.setEnabled(enable);
		 */
		this.cbGroup.setEnabled(enable);
		this.cbTax.setEnabled(enable);
		this.jLabel1.setEnabled(enable);
		this.chkVisible.setEnabled(enable);
		this.jLabel2.setEnabled(enable);
		this.jLabel3.setEnabled(enable);
		this.jLabel4.setEnabled(enable);
		this.jLabel5.setEnabled(enable);
		this.jLabel6.setEnabled(enable);
		this.tfDiscountRate.setEnabled(enable);
		this.tfPrice.setEnabled(enable);
		this.lblImagePreview.setEnabled(enable);
		this.btnClearImage.setEnabled(enable);
		this.fileChooser.setEnabled(enable);
		this.btnSelectImage.setEnabled(enable);
		this.lblImage.setEnabled(enable);
		this.cbShowTextWithImage.setEnabled(enable);
		this.lblButtonColor.setEnabled(enable);
		this.lblTextColor.setEnabled(enable);
		this.btnButtonColor.setEnabled(enable);
		this.btnTextColor.setEnabled(enable);
		if (tabReciepe != null && tabReciepe instanceof IUpdatebleView) {
			IUpdatebleView view = (IUpdatebleView) tabReciepe;
			view.setFieldsEnable(enable);
		}
	}

	public void clearFields() {
		this.tfName.setText("");
		this.cbGroup.setSelectedIndex(-1);
		this.cbTax.setSelectedIndex(-1);
		this.tfPrice.setText("");
		doClearImage();
		this.tfBuyPrice.setText("");
		clearRecepieTableModel();
		clearShiftTableModel();
	}

	private void clearRecepieTableModel() {
		MenuItem menuItem = getBean();
		if (menuItem.getRecepie() != null && menuItem.getRecepie().getRecepieItems() != null && tabReciepe != null && tabReciepe instanceof IUpdatebleView) {
			IUpdatebleView view = (IUpdatebleView) tabReciepe;
			view.clearTableModel();
		}
	}

	public void clearShiftTableModel() {
		if (this.shiftTable != null && this.shiftTable.getModel() != null) {
			ShiftTableModel tableModel = (ShiftTableModel) this.shiftTable.getModel();
			tableModel.setRows(null);
		}
	}

	protected void doSelectImageFile() {
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
		this.tabReciepe = tabbedPane.getComponentAt(tabbedPane.indexOfTab("Recipe"));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

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
		// btnNewGroup = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
		tfPrice = new DoubleTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
		cbTax = new javax.swing.JComboBox();
		// btnNewTax = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel5 = new javax.swing.JLabel();
		tfDiscountRate = new DoubleTextField();
		tfDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
		chkVisible = new javax.swing.JCheckBox();
		jScrollPane1 = new javax.swing.JScrollPane();
		tableTicketItemModifierGroups = new javax.swing.JTable();
		tabShift = new javax.swing.JPanel();
		btnDeleteShift = new javax.swing.JButton();
		btnAddShift = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		shiftTable = new javax.swing.JTable();

		jLabel1.setText(Messages.getString("LABEL_NAME"));
		jLabel4.setText(Messages.getString("LABEL_GROUP"));

		// btnNewGroup.setText("...");
		// btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// doCreateNewGroup(evt);
		// }
		// });

		if (Application.getInstance().isPriceIncludesTax()) {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_INCLUDING_TAX"));
		} else {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_EXCLUDING_TAX"));
		}

		tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel6.setText(Messages.getString("LABEL_TAX"));

		// btnNewTax.setText("...");
		// btnNewTax.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnNewTaxdoCreateNewTax(evt);
		// }
		// });

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
		//		tabGeneral.add(btnNewGroup, "cell 3 4,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(tfDiscountRate, "cell 1 7,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(cbTax, "cell 1 8,growx,aligny top"); //$NON-NLS-1$
		tabGeneral.add(tfPrice, "cell 1 6,growx,aligny top"); //$NON-NLS-1$
		lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
		tabGeneral.add(lblImage, "cell 0 10,aligny center");

		lblImagePreview = new JLabel("");
		lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
		lblImagePreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblImagePreview.setPreferredSize(new Dimension(100, 100));
		tabGeneral.add(lblImagePreview, "cell 1 10");

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

		btnButtonColor = new JButton();
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
		//		tabGeneral.add(btnNewTax, "cell 2 8,alignx left,aligny top"); //$NON-NLS-1$
		tabGeneral.add(jLabel5, "cell 2 7"); //$NON-NLS-1$
		add(tabbedPane);

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
	}

	// private void btnNewTaxdoCreateNewTax(java.awt.event.ActionEvent evt) {
	// BeanEditorDialog dialog = new BeanEditorDialog(new TaxForm(),
	// BackOfficeWindow.getInstance(), true);
	// dialog.open();
	// }
	//
	// private void doCreateNewGroup(java.awt.event.ActionEvent evt) {
	// MenuGroupForm editor = new MenuGroupForm();
	// BeanEditorDialog dialog = new BeanEditorDialog(editor, getParentFrame(),
	// true);
	// dialog.open();
	// if (!dialog.isCanceled()) {
	// MenuGroup foodGroup = (MenuGroup) editor.getBean();
	// ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
	// model.addElement(foodGroup);
	// model.setSelectedItem(foodGroup);
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
		tfName.setText(menuItem.getName());
		tfBuyPrice.setText(formatDouble(getBuyPriceFromInventory(menuItem)));
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

		if (menuItem.getButtonColor() != null) {
			Color color = new Color(menuItem.getButtonColor());
			btnButtonColor.setBackground(color);
			btnTextColor.setBackground(color);
		}

		if (menuItem.getTextColor() != null) {
			Color color = new Color(menuItem.getTextColor());
			btnTextColor.setForeground(color);
		}
		if (tabShift != null) {
			shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));

		}
		if (menuItem.getRecepie() != null && menuItem.getRecepie().getRecepieItems() != null && tabReciepe != null && tabReciepe instanceof IUpdatebleView) {
			IUpdatebleView view = (IUpdatebleView) tabReciepe;
			view.updateView(menuItem);
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
		menuItem.setBuyPrice(Double.valueOf(tfBuyPrice.getText()));
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

	class ShiftTableModel extends ListTableModel<MenuItemShift> {
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
		if (actionCommand.equals(com.floreantpos.POSConstants.ADD_SHIFT)) {
			addShift();
		} else if (actionCommand.equals(com.floreantpos.POSConstants.DELETE_SHIFT)) {
			deleteShift();
		}
	}

	protected String formatDouble(double d) {
		NumberFormat f = new DecimalFormat("0.##");
		return f.format(d);
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
