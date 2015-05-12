package com.floreantpos.bo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;

public abstract class ModelBrowser<E> extends JPanel implements ActionListener, ListSelectionListener {

	protected JXTable browserTable;
	protected BeanEditor<E> beanEditor;

	protected JPanel browserPanel = new JPanel(new BorderLayout());
	private JPanel beanPanel = new JPanel(new BorderLayout());
	private JButton btnNext = new JButton("NEXT");
	private JButton btnPrev = new JButton("PREV");

	private JLabel start = new JLabel();
	private JLabel current = new JLabel();
	private JLabel end = new JLabel();

	protected JPanel buttonPanel1 = new JPanel();
	private JButton btnNew = new JButton("NEW");
	private JButton btnEdit = new JButton("EDIT");
	private JButton btnSave = new JButton("SAVE");
	private JButton btnDelete = new JButton("DELETE");
	private JButton btnCancel = new JButton("CANCEL");
	private JXComboBox cbPageSize;
	private JButton btnRefresh = new JButton("REFRESH");;

	public ModelBrowser() {
		this(null);
	}

	public ModelBrowser(BeanEditor<E> beanEditor) {
		super();
		this.beanEditor = beanEditor;
	}

	public void init(final ListTableModel<E> tableModel, Dimension scrollPaneDimension, Dimension beanPaneDimension) {
		browserTable = new JXTable();
		browserTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		browserTable.getSelectionModel().addListSelectionListener(this);
		browserTable.setSortable(false);
		if (tableModel != null) {
			browserTable.setModel(tableModel);
			tableModel.setPageSize(30);
			start.setText("1");
			current.setVisible(false);
			if (tableModel.getPageCount() > 1) {
				end.setText(Integer.toString(tableModel.getPageCount()));
				end.setEnabled(false);
				end.setVisible(true);
			} else {
				end.setVisible(false);
			}

			btnNext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tableModel.pageDown();
					if (tableModel.getPageOffset() == tableModel.getPageCount() - 1) {
						btnNext.setEnabled(false);
						end.setText(Integer.toString(tableModel.getPageCount()));
						end.setEnabled(true);
						current.setVisible(false);
						start.setEnabled(false);
						end.setVisible(true);
					} else {
						end.setText(Integer.toString(tableModel.getPageCount()));
						end.setEnabled(false);
						current.setText(Integer.toString(tableModel.getPageOffset() + 1));
						current.setVisible(true);
						start.setEnabled(false);
						end.setVisible(true);
					}
					btnPrev.setEnabled(true);
				}
			});

			btnPrev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tableModel.pageUp();
					if (tableModel.getPageOffset() == 0) {
						end.setText(Integer.toString(tableModel.getPageCount()));
						btnPrev.setEnabled(false);
						end.setEnabled(false);
						end.setVisible(true);
						current.setVisible(false);
						start.setEnabled(true);
					} else {
						end.setText(Integer.toString(tableModel.getPageCount()));
						end.setEnabled(false);
						end.setVisible(true);
						current.setText(Integer.toString(tableModel.getPageOffset() + 1));
						current.setVisible(true);
						start.setEnabled(false);
					}
					btnNext.setEnabled(true);
				}
			});
			if (tableModel.getPageCount() <= 1) {
				btnNext.setEnabled(false);
				btnPrev.setEnabled(false);
			}
		}

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane jsp = new JScrollPane(browserTable);
		jsp.setPreferredSize(scrollPaneDimension);
		browserPanel.add(jsp);

		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int page = ((ListTableModel) ModelBrowser.this.browserTable.getModel()).getPageOffset();
				ModelBrowser.this.refreshTable();
				for (int i = 0; i < page; i++) {
					ModelBrowser.this.btnNext.doClick();
				}
			}
		});
		cbPageSize = new JXComboBox();
		cbPageSize.setModel(new DefaultComboBoxModel(new Integer[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 }));
		cbPageSize.setSelectedItem(new Integer(30));
		cbPageSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size = (Integer) ((JXComboBox) e.getSource()).getSelectedItem();
				((ListTableModel) ModelBrowser.this.browserTable.getModel()).setPageSize(size);
				ModelBrowser.this.refreshTable();
			}
		});

		JLabel empty1 = new JLabel("      ");
		JLabel empty2 = new JLabel("      ");

		if (tableModel != null) {

			// buttonPanel1.setLayout(new BorderLayout(5, 5));
			buttonPanel1.add(cbPageSize, BorderLayout.WEST);
			buttonPanel1.add(empty1);
			buttonPanel1.add(btnPrev);
			start.setBorder(new EmptyBorder(0, 5, 0, 5));
			end.setBorder(new EmptyBorder(0, 5, 0, 5));
			current.setBorder(new EmptyBorder(0, 5, 0, 5));
			buttonPanel1.add(start);
			buttonPanel1.add(current);
			buttonPanel1.add(end);
			buttonPanel1.add(btnNext);
			buttonPanel1.add(empty2);
			buttonPanel1.add(btnRefresh, BorderLayout.EAST);
			browserPanel.add(buttonPanel1, BorderLayout.SOUTH);
		}

		JPanel searchPanel = createSearchPanel();
		if (searchPanel != null) {
			browserPanel.add(searchPanel, BorderLayout.NORTH);
		}

		add(browserPanel);

		beanPanel.setBorder(BorderFactory.createEtchedBorder());
		beanPanel.add(beanEditor);

		JPanel buttonPanel = new JPanel();

		JButton additionalButton = getAdditionalButton();
		if (additionalButton != null) {
			buttonPanel.add(additionalButton);
			additionalButton.addActionListener(this);
		}

		buttonPanel.add(btnNew);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnCancel);
		beanPanel.setPreferredSize(beanPaneDimension);
		beanPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(beanPanel, BorderLayout.EAST);

		btnNew.addActionListener(this);
		btnEdit.addActionListener(this);
		btnDelete.addActionListener(this);
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);

		btnNew.setEnabled(true);
		btnEdit.setEnabled(false);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
		btnCancel.setEnabled(false);

		beanEditor.clearFields();
		beanEditor.setFieldsEnable(false);
	}

	protected void refreshPageAttr() {
		ListTableModel tableModel = (ListTableModel) browserTable.getModel();
		int currentPage = tableModel.getPageOffset();
		int pageSize = tableModel.getPageSize();
		int pos = tableModel.getRows().indexOf((beanEditor.getBean()));
		int newPage = pos / pageSize;

		int pageToMove = newPage - currentPage;
		if (pageToMove > 0) {
			for (int i = 0; i < pageToMove; i++) {
				this.btnNext.doClick();
			}
		} else {
			if (pageToMove < 0) {
				for (int i = 0; i < pageToMove; i++) {
					this.btnPrev.doClick();
				}
			}
		}
		browserTable.getSelectionModel().setSelectionInterval(0, pos % pageSize);
	}

	public void refreshTable() {
		ListTableModel tableModel = (ListTableModel) browserTable.getModel();
		if (tableModel != null) {
			if (tableModel.getPageCount() <= 1) {
				end.setText(Integer.toString(tableModel.getPageCount()));
				btnNext.setEnabled(false);
				btnPrev.setEnabled(false);
				end.setEnabled(false);
				current.setEnabled(false);
				end.setVisible(false);
				current.setVisible(false);
				start.setEnabled(true);
				start.setVisible(true);
			} else {
				if (tableModel.getPageOffset() == 0) {
					end.setText(Integer.toString(tableModel.getPageCount()));
					btnNext.setEnabled(true);
					btnPrev.setEnabled(false);
					end.setEnabled(false);
					end.setVisible(true);
					current.setVisible(false);
					start.setEnabled(true);
					start.setVisible(true);
				} else if (tableModel.getPageOffset() == tableModel.getPageCount() - 1) {
					end.setText(Integer.toString(tableModel.getPageCount()));
					btnNext.setEnabled(false);
					btnPrev.setEnabled(true);
					end.setEnabled(true);
					end.setVisible(true);
					current.setVisible(false);
					start.setEnabled(false);
				} else {
					end.setText(Integer.toString(tableModel.getPageCount()));
					btnNext.setEnabled(true);
					btnPrev.setEnabled(true);
					end.setEnabled(false);
					start.setEnabled(false);
					end.setVisible(true);
					start.setVisible(true);
					current.setText(Integer.toString(tableModel.getPageOffset() + 1));
					current.setVisible(true);
					current.setEnabled(true);
				}
			}
		}
	}

	public JPanel createSearchPanel() {
		return null;
	}

	protected JButton getAdditionalButton() {
		return null;
	}

	protected void handleAdditionaButtonActionIfApplicable(ActionEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Command command = Command.fromString(e.getActionCommand());

		try {
			switch (command) {
			case NEW:
				beanEditor.createNew();
				beanEditor.setFieldsEnable(true);
				btnNew.setEnabled(false);
				btnEdit.setEnabled(false);
				btnSave.setEnabled(true);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(true);
				browserTable.setFocusable(false);
				// refreshTable();
				break;

			case EDIT:
				beanEditor.setFieldsEnable(true);
				beanEditor.setEditMode(true);
				btnNew.setEnabled(false);
				btnEdit.setEnabled(false);
				btnSave.setEnabled(true);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(true);
				// refreshTable();
				break;

			case CANCEL:
				// browserTable.clearSelection();
				// beanEditor.setBean(null);
				beanEditor.setBean(beanEditor.getBean());
				beanEditor.setFieldsEnable(false);
				btnNew.setEnabled(true);
				btnEdit.setEnabled(true);
				btnSave.setEnabled(false);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(false);
				break;

			case SAVE:
				if (beanEditor.save()) {
					beanEditor.setFieldsEnable(false);
					btnNew.setEnabled(true);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(false);
					refreshTable();
					refreshPageAttr();
				}
				break;

			case DELETE:
				if (beanEditor.delete()) {
					beanEditor.setBean(null);
					beanEditor.setFieldsEnable(false);
					btnNew.setEnabled(true);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(false);
					ListTableModel tableModel = (ListTableModel) browserTable.getModel();
					int pos = tableModel.getPageOffset();
					refreshTable();
					tableModel.setPageOffset(pos);
				}
				break;

			default:
				break;
			}
			handleAdditionaButtonActionIfApplicable(e);
		} catch (Exception e2) {
			POSMessageDialog.showError(e2.getMessage(), e2);
		}
	}

	protected void hideDeleteBtn() {
		this.btnDelete.setVisible(false);
	}

	protected void showDeleteBtn() {
		this.btnDelete.setVisible(true);
	}

	protected void hideCancelBtn() {
		this.btnCancel.setVisible(false);
	}

	protected void showCancelBtn() {
		this.btnCancel.setVisible(true);
	}

	protected void hideNewBtn() {
		this.btnNew.setVisible(false);
	}

	protected void showNewBtn() {
		this.btnNew.setVisible(true);
	}

	protected void hideEditBtn() {
		this.btnEdit.setVisible(false);
	}

	protected void showEditBtn() {
		this.btnEdit.setVisible(true);
	}

	protected void hideSaveBtn() {
		this.btnSave.setVisible(false);
	}

	protected void showSaveBtn() {
		this.btnSave.setVisible(true);
	}

	abstract protected void loadData();

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}

		ListTableModel model = (ListTableModel) browserTable.getModel();
		int selectedRow = browserTable.getSelectedRow();

		if (selectedRow < 0)
			return;

		E data = (E) model.getRowData(selectedRow);
		beanEditor.setBean(data);
		beanEditor.setEnabled(false);
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(true);
		btnCancel.setEnabled(false);
	}

	public void setModels(List<E> models) {
		ListTableModel tableModel = (ListTableModel) browserTable.getModel();
		tableModel.setRows(models);
	}
}
