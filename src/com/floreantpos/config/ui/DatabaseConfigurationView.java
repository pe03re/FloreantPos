package com.floreantpos.config.ui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.Database;
import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;

public class DatabaseConfigurationView extends ConfigurationView implements ActionListener {

	private static final String CONFIGURE_DB = "CD"; //$NON-NLS-1$
	private static final String SAVE = "SAVE"; //$NON-NLS-1$
	private static final String CANCEL = "cancel"; //$NON-NLS-1$
	private static final String TEST = "test"; //$NON-NLS-1$
	private static final String XAMPP = "xampp";
	private static final String EXPORT = "export";
	private POSTextField tfServerAddress;
	private POSTextField tfServerPort;
	private POSTextField tfDatabaseName;
	private POSTextField tfUserName;
	private POSPasswordField tfPassword;
	private POSTextField tfXamppPath;
	private POSTextField tfDbExportPath;
	private JButton btnXamppPath;
	private JButton btnExportPath;
	private JButton btnTestConnection;
	private JButton btnCreateDb;
	private JButton btnSave;
	private JComboBox databaseCombo;
	private JLabel lblServerAddress;
	private JLabel lblServerPort;
	private JLabel lblDbName;
	private JLabel lblUserName;
	private JLabel lblDbPassword;
	private JLabel lblDbXamppPath;
	private JLabel lblDbExportPath;

	public DatabaseConfigurationView() throws HeadlessException {
		super();
		initUI();
		addUIListeners();
	}

	protected void initUI() {
		setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][][][grow,fill]"));
		tfServerAddress = new POSTextField();
		tfServerPort = new POSTextField();
		tfDatabaseName = new POSTextField();
		tfUserName = new POSTextField();
		tfPassword = new POSPasswordField();
		databaseCombo = new JComboBox(Database.values());
		tfXamppPath = new POSTextField();
		tfDbExportPath = new POSTextField();

		String databaseProviderName = AppConfig.getDatabaseProviderName();
		if (StringUtils.isNotEmpty(databaseProviderName)) {
			databaseCombo.setSelectedItem(Database.getByProviderName(databaseProviderName));
		}

		add(new JLabel(Messages.getString("DatabaseConfigurationDialog.8"))); //$NON-NLS-1$
		add(databaseCombo, "grow, wrap"); //$NON-NLS-1$
		lblServerAddress = new JLabel(Messages.getString("DatabaseConfigurationDialog.10") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblServerAddress);
		add(tfServerAddress, "grow, wrap"); //$NON-NLS-1$
		lblServerPort = new JLabel(Messages.getString("DatabaseConfigurationDialog.13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblServerPort);
		add(tfServerPort, "grow, wrap"); //$NON-NLS-1$
		lblDbName = new JLabel(Messages.getString("DatabaseConfigurationDialog.16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblDbName);
		add(tfDatabaseName, "grow, wrap"); //$NON-NLS-1$
		lblUserName = new JLabel(Messages.getString("DatabaseConfigurationDialog.19") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblUserName);
		add(tfUserName, "grow, wrap"); //$NON-NLS-1$
		lblDbPassword = new JLabel(Messages.getString("DatabaseConfigurationDialog.22") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblDbPassword);
		add(tfPassword, "grow, wrap"); //$NON-NLS-1$
		lblDbXamppPath = new JLabel(Messages.getString("Xampp Path") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblDbXamppPath);
		add(tfXamppPath, "grow, wrap"); //$NON-NLS-1$
		lblDbExportPath = new JLabel(Messages.getString("Export SQL Path") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblDbExportPath);
		add(tfDbExportPath, "grow, wrap"); //$NON-NLS-1$
		add(new JSeparator(), "span, grow, gaptop 10"); //$NON-NLS-1$

		btnXamppPath = new JButton(Messages.getString("Choose XAMPP Path").toUpperCase()); //$NON-NLS-1$
		btnXamppPath.setActionCommand(XAMPP);
		btnExportPath = new JButton(Messages.getString("Choose Export Path").toUpperCase()); //$NON-NLS-1$
		btnExportPath.setActionCommand(EXPORT);
		btnTestConnection = new JButton(Messages.getString("DatabaseConfigurationDialog.26")); //$NON-NLS-1$
		btnTestConnection.setActionCommand(TEST);
		btnSave = new JButton(Messages.getString("DatabaseConfigurationDialog.27")); //$NON-NLS-1$
		btnSave.setActionCommand(SAVE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnCreateDb = new JButton(Messages.getString("DatabaseConfigurationDialog.29")); //$NON-NLS-1$
		btnCreateDb.setActionCommand(CONFIGURE_DB);
		buttonPanel.add(btnXamppPath);
		buttonPanel.add(btnExportPath);
		buttonPanel.add(btnCreateDb);
		buttonPanel.add(btnTestConnection);
		buttonPanel.add(btnSave);

		add(buttonPanel, "span, grow"); //$NON-NLS-1$
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);
		btnXamppPath.addActionListener(this);
		btnExportPath.addActionListener(this);

		databaseCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database selectedDb = (Database) databaseCombo.getSelectedItem();

				if (selectedDb == Database.DERBY_SINGLE) {
					setFieldsVisible(false);
					return;
				}

				setFieldsVisible(true);

				String databasePort = AppConfig.getDatabasePort();
				if (StringUtils.isEmpty(databasePort)) {
					databasePort = selectedDb.getDefaultPort();
				}

				tfServerPort.setText(databasePort);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		try {
			String command = e.getActionCommand();

			Database selectedDb = (Database) databaseCombo.getSelectedItem();

			String providerName = selectedDb.getProviderName();
			String databaseURL = tfServerAddress.getText();
			String databasePort = tfServerPort.getText();
			String databaseName = tfDatabaseName.getText();
			String user = tfUserName.getText();
			String xampp = tfXamppPath.getText();
			String sqlPath = tfDbExportPath.getText();
			String pass = new String(tfPassword.getPassword());

			String connectionString = selectedDb.getConnectString(databaseURL, databasePort, databaseName);
			String hibernateDialect = selectedDb.getHibernateDialect();
			String driverClass = selectedDb.getHibernateConnectionDriverClass();

			if (XAMPP.equalsIgnoreCase(command)) {
				JFileChooser xChooser = new JFileChooser();
				xChooser.setCurrentDirectory(new java.io.File("."));
				xChooser.setDialogTitle("Select folder");
				xChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				xChooser.setAcceptAllFileFilterUsed(false);
				int ret = xChooser.showOpenDialog(this);
				if (ret == JFileChooser.APPROVE_OPTION)
					tfXamppPath.setText(xChooser.getSelectedFile().getAbsolutePath());
			} else if (EXPORT.equalsIgnoreCase(command)) {
				JFileChooser xXport = new JFileChooser();
				xXport.setCurrentDirectory(new java.io.File("."));
				xXport.setDialogTitle("Select folder");
				xXport.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				xXport.setAcceptAllFileFilterUsed(false);
				int ret = xXport.showOpenDialog(this);
				if (ret == JFileChooser.APPROVE_OPTION)
					tfDbExportPath.setText(xXport.getSelectedFile().getAbsolutePath());
			} else if (TEST.equalsIgnoreCase(command)) {
				Application.getInstance().setSystemInitialized(false);
				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect, xampp, sqlPath);
				;

				try {
					DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass);
				} catch (DatabaseConnectionException e1) {
					JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.32")); //$NON-NLS-1$
					return;
				}

				JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.31")); //$NON-NLS-1$
			} else if (CONFIGURE_DB.equals(command)) {
				Application.getInstance().setSystemInitialized(false);

				int i = JOptionPane.showConfirmDialog(this, Messages.getString("DatabaseConfigurationDialog.33"), Messages.getString("DatabaseConfigurationDialog.34"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (i != JOptionPane.YES_OPTION) {
					return;
				}

				i = JOptionPane.showConfirmDialog(this, "Do you want to generate sample data?", "Confirm", JOptionPane.YES_NO_OPTION);
				boolean generateSampleData = false;
				if (i == JOptionPane.YES_OPTION)
					generateSampleData = true;

				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect, xampp, sqlPath);

				String connectionString2 = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);

				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				boolean createDatabase = DatabaseUtil.createDatabase(connectionString2, hibernateDialect, driverClass, user, pass, generateSampleData);
				this.setCursor(Cursor.getDefaultCursor());

				if (createDatabase) {
					//JOptionPane.showMessageDialog(DatabaseConfigurationView.this, Messages.getString("DatabaseConfigurationDialog.35")); //$NON-NLS-1$
					JOptionPane.showMessageDialog(DatabaseConfigurationView.this, "Database created. Default password is 1111."); //$NON-NLS-1$
				} else {
					JOptionPane.showMessageDialog(DatabaseConfigurationView.this, Messages.getString("DatabaseConfigurationDialog.36")); //$NON-NLS-1$
				}
			} else if (SAVE.equalsIgnoreCase(command)) {
				Application.getInstance().setSystemInitialized(false);
				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect, xampp, sqlPath);
			} else if (CANCEL.equalsIgnoreCase(command)) {
			}
		} catch (Exception e2) {
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e2.getMessage());
		}
	}

	private void saveConfig(Database selectedDb, String providerName, String databaseURL, String databasePort, String databaseName, String user, String pass, String connectionString,
			String hibernateDialect, String xamppPath, String sqlExportPath) {
		AppConfig.setDatabaseProviderName(providerName);
		AppConfig.setConnectString(connectionString);
		AppConfig.setDatabaseHost(databaseURL);
		AppConfig.setDatabasePort(databasePort);
		AppConfig.setDatabaseName(databaseName);
		AppConfig.setDatabaseUser(user);
		AppConfig.setDatabasePassword(pass);
		AppConfig.setXamppPath(xamppPath);
		AppConfig.setSqlExportPath(sqlExportPath);
		try {
			AppConfig.getConfig().save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void setFieldsVisible(boolean visible) {
		lblServerAddress.setVisible(visible);
		tfServerAddress.setVisible(visible);

		lblServerPort.setVisible(visible);
		tfServerPort.setVisible(visible);

		lblDbName.setVisible(visible);
		tfDatabaseName.setVisible(visible);

		lblUserName.setVisible(visible);
		tfUserName.setVisible(visible);

		lblDbPassword.setVisible(visible);
		tfPassword.setVisible(visible);
	}

	@Override
	public boolean save() throws Exception {
		return false;
	}

	@Override
	public void initialize() throws Exception {
		Database selectedDb = (Database) databaseCombo.getSelectedItem();

		String databaseURL = AppConfig.getDatabaseHost();
		tfServerAddress.setText(databaseURL);

		String databasePort = AppConfig.getDatabasePort();
		if (StringUtils.isEmpty(databasePort)) {
			databasePort = selectedDb.getDefaultPort();
		}

		tfServerPort.setText(databasePort);
		tfDatabaseName.setText(AppConfig.getDatabaseName());
		tfUserName.setText(AppConfig.getDatabaseUser());
		tfPassword.setText(AppConfig.getDatabasePassword());

		if (selectedDb == Database.DERBY_SINGLE) {
			setFieldsVisible(false);
		} else {
			setFieldsVisible(true);
		}

		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Database";
	}

}
