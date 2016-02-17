package com.floreantpos.main;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.AppProperties;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.OrderTypePropertiesDAO;
import com.floreantpos.model.dao.PrinterConfigurationDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.Report;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.CashierSwitchBoardView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftException;
import com.floreantpos.util.ShiftUtil;
import com.floreantpos.util.UserNotFoundException;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public class Application {
	private static Log logger = LogFactory.getLog(Application.class);
	private boolean developmentMode = false;
	private boolean reportMode = false;
	private Timer autoDrawerPullTimer;
	private PluginManager pluginManager;

	private Terminal terminal;
	private PosWindow posWindow;
	private User currentUser;
	private RootView rootView;
	private BackOfficeWindow backOfficeWindow;
	private Shift currentShift;
	public PrinterConfiguration printConfiguration;
	private Restaurant restaurant;
	private PosPrinters printers;

	private static Application instance;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
	SimpleDateFormat yearFolderFormat = new SimpleDateFormat("yyyy");
	SimpleDateFormat monthFolderFormat = new SimpleDateFormat("MMM");
	SimpleDateFormat dateFolderFormat = new SimpleDateFormat("dd_MM_yyyy");
	SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss");
	private static ImageIcon applicationIcon;

	private boolean systemInitialized;

	public final static String VERSION = AppProperties.getVersion();

	private Application() {
		// Locale.setDefault(Locale.forLanguageTag("ar-EG"));

		applicationIcon = new ImageIcon(getClass().getResource("/icons/icon.png")); //$NON-NLS-1$
		posWindow = new PosWindow();
		posWindow.setTitle(getTitle());
		posWindow.setIconImage(applicationIcon.getImage());
	}

	public void start() {
		pluginManager = PluginManagerFactory.createPluginManager();
		pluginManager.addPluginsFrom(new File("plugins/").toURI());

		if (developmentMode) {
			pluginManager.addPluginsFrom(new File("C:/MyProjects/InventoryPlugin/orostock/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/CustPlugin/orocust/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/ticketDetailReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/menuItemDetailReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/dayWiseReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/voidDetailReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/categoryDetailReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/groupDetailReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/categoryWiseReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/groupWiseReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/menuItemWiseReport/target/classes").toURI());
			pluginManager.addPluginsFrom(new File("C:/MyProjects/ReportPlugin/cardPaymentReport/target/classes").toURI());
		}

		setApplicationLook();

		rootView = RootView.getInstance();

		posWindow.getContentPane().add(rootView);
		posWindow.setupSizeAndLocation();

		if (TerminalConfig.isFullscreenMode()) {
			posWindow.enterFullScreenMode();
		}
		if (!reportMode) {
			posWindow.setVisible(true);
		}

		initializeSystem();

		if (reportMode) {
			printAllReports();
		}
	}

	private void printAllReports() {
		List<Report> repList = new ArrayList<Report>();
		File f = new File("plugins/");
		try {
			for (File f1 : f.listFiles()) {
				if (f1.getName().endsWith(".jar")) {
					JarFile jarFile = new JarFile(f1);
					Enumeration e = jarFile.entries();
					URL[] urls = { new URL("jar:file:" + f1.getAbsolutePath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					while (e.hasMoreElements()) {
						JarEntry je = (JarEntry) e.nextElement();
						if (je.isDirectory() || !je.getName().endsWith(".class")) {
							continue;
						}
						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						Class c;
						c = cl.loadClass(className);
						if (Report.class.isAssignableFrom(c)) {
							repList.add((Report) c.newInstance());
						}
					}
				}
			}
			for (Report r : repList) {
				Date today = new Date();

				File reportFolder = new File(AppConfig.getReportPath() + "\\Sales_Reports\\" + yearFolderFormat.format(today) + "\\" + monthFolderFormat.format(today) + "\\"
						+ dateFolderFormat.format(today) + "\\");
				File reportFile = new File(reportFolder + "\\" + r.getName() + "_" + fullDateFormat.format(today));
				if (!reportFile.exists()) {
					File parent = reportFile.getParentFile();
					if (parent.exists() == false) {
						FileUtils.forceMkdir(parent);
					}
				}
				if (r.isDailyReport()) {
					Date startDate = new Date();
					startDate.setHours(0);
					startDate.setMinutes(0);
					startDate.setSeconds(0);

					Date endDate = new Date();
					endDate.setHours(23);
					endDate.setMinutes(59);
					endDate.setSeconds(59);

					r.exportReport(startDate, endDate, reportFile.getAbsolutePath());
				}

				Calendar c1 = Calendar.getInstance();
				c1.setTime(today);
				c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
				Date lastDayOfMonth = c1.getTime();
				lastDayOfMonth.setHours(23);
				lastDayOfMonth.setMinutes(59);
				lastDayOfMonth.setSeconds(59);

				if (today.getDate() == lastDayOfMonth.getDate()) {
					Calendar c2 = Calendar.getInstance();
					c2.set(Calendar.DAY_OF_MONTH, 1);
					Date firstDayOfMonth = c2.getTime();
					firstDayOfMonth.setHours(0);
					firstDayOfMonth.setMinutes(0);
					firstDayOfMonth.setSeconds(0);

					r.exportReport(firstDayOfMonth, lastDayOfMonth, reportFile.getAbsolutePath());
				}
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setApplicationLook() {
		try {

			initializeFont();

			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			UIManager.put("ComboBox.is3DEnabled", Boolean.FALSE); //$NON-NLS-1$
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	private void initializeFont() {
		String uiDefaultFont = TerminalConfig.getUiDefaultFont();
		if (StringUtils.isEmpty(uiDefaultFont)) {
			return;
		}

		Font sourceFont = UIManager.getFont("Label.font");
		Font font = new Font(uiDefaultFont, sourceFont.getStyle(), sourceFont.getSize());

		UIManager.put("ArrowButton.size", 40);
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", Font.PLAIN, 35)));
		UIManager.put("Button.font", font);
		UIManager.put("ToggleButton.font", font);
		UIManager.put("RadioButton.font", font);
		UIManager.put("CheckBox.font", font);
		UIManager.put("ColorChooser.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("Label.font", font);
		UIManager.put("List.font", font);
		UIManager.put("MenuBar.font", font);
		UIManager.put("MenuItem.font", font);
		UIManager.put("RadioButtonMenuItem.font", font);
		UIManager.put("CheckBoxMenuItem.font", font);
		UIManager.put("Menu.font", font);
		UIManager.put("PopupMenu.font", font);
		UIManager.put("OptionPane.font", font);
		UIManager.put("Panel.font", font);
		UIManager.put("ProgressBar.font", font);
		UIManager.put("ScrollPane.font", font);
		UIManager.put("Viewport.font", font);
		UIManager.put("TabbedPane.font", font);
		UIManager.put("Table.font", font);
		UIManager.put("TableHeader.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("PasswordField.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("TextPane.font", font);
		UIManager.put("EditorPane.font", font);
		UIManager.put("TitledBorder.font", font);
		UIManager.put("ToolBar.font", font);
		UIManager.put("ToolTip.font", font);
		UIManager.put("Tree.font", font);
	}

	public void initializeSystem() {
		if (isSystemInitialized()) {
			return;
		}
		try {
			posWindow.setGlassPaneVisible(true);
			// posWindow.setGlassPaneMessage(com.floreantpos.POSConstants.LOADING);
			DatabaseUtil.checkConnection(DatabaseUtil.initialize());
			initTerminal();
			initPrintConfig();
			refreshRestaurant();
			loadPrinters();
			// setTicketActiveSetterScheduler();
			setSystemInitialized(true);
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			if (writer.toString().contains("Another instance of Derby may have already booted")) {
				POSMessageDialog.showError("Another FloreantPOS instance may be already running.\n" + "Multiple instances cannot be run in Derby single mode");
				return;
			} else {
				int option = JOptionPane.showConfirmDialog(getPosWindow(), Messages.getString("Application.0"), Messages.getString(POSConstants.POS_MESSAGE_ERROR), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (option == JOptionPane.YES_OPTION) {
					DatabaseConfigurationDialog.show(Application.getPosWindow());
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
			e.printStackTrace();
			logger.error(e);
		} finally {
			getPosWindow().setGlassPaneVisible(false);
		}
	}

	private void loadPrinters() {
		printers = PosPrinters.load();
		if (printers == null) {
			printers = new PosPrinters();
		}
	}

	// private void setTicketActiveSetterScheduler() {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) +
	// 1);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.SECOND, 0);
	//
	// }

	private void initPrintConfig() {
		printConfiguration = PrinterConfigurationDAO.getInstance().get(PrinterConfiguration.ID);
		if (printConfiguration == null) {
			printConfiguration = new PrinterConfiguration();
		}
	}

	private void initTerminal() {
		int terminalId = TerminalConfig.getTerminalId();

		if (terminalId == -1) {
			Random random = new Random();
			terminalId = random.nextInt(10000) + 1;
		}

		Terminal terminal = null;
		try {
			terminal = TerminalDAO.getInstance().get(new Integer(terminalId));
			if (terminal == null) {
				terminal = new Terminal();
				terminal.setId(terminalId);
				terminal.setOpeningBalance(new Double(500));
				terminal.setCurrentBalance(new Double(500));
				terminal.setName(String.valueOf(terminalId)); //$NON-NLS-1$

				TerminalDAO.getInstance().saveOrUpdate(terminal);
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}

		TerminalConfig.setTerminalId(terminalId);
		RootView.getInstance().getLoginScreen().setTerminalId(terminalId);

		if (terminal.isHasCashDrawer() && terminal.isAutoDrawerPullEnable() && autoDrawerPullTimer == null) {
			autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
			autoDrawerPullTimer.start();
		} else {
			if (autoDrawerPullTimer != null) {
				autoDrawerPullTimer.stop();
				autoDrawerPullTimer = null;
			}
		}

		this.terminal = terminal;

		OrderTypePropertiesDAO.populate();
	}

	public void refreshRestaurant() {
		try {
			this.restaurant = RestaurantDAO.getRestaurant();

			if (restaurant.getUniqueId() == null || restaurant.getUniqueId() == 0) {
				restaurant.setUniqueId(RandomUtils.nextInt());
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			}

			// if (restaurant.isItemPriceIncludesTax()) {
			// posWindow.setStatus("Tax is included in item price");
			// } else {
			// posWindow.setStatus("Tax is not included in item price");
			// }
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}
	}

	public static String getCurrencyName() {
		Application application = getInstance();
		if (application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencyName();
	}

	public static String getCurrencySymbol() {
		Application application = getInstance();
		if (application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencySymbol();
	}

	public synchronized static Application getInstance() {
		if (instance == null) {
			instance = new Application();

		}
		return instance;
	}

	public void shutdownPOS() {
		User user = getCurrentUser();

		// if (user != null && !user.hasPermission(UserPermission.SHUT_DOWN)) {
		// POSMessageDialog.showError("You do not have permission to execute this action");
		// return;
		// }

		int option = JOptionPane.showOptionDialog(getPosWindow(), com.floreantpos.POSConstants.SURE_SHUTDOWN_, com.floreantpos.POSConstants.CONFIRM_SHUTDOWN, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		posWindow.saveSizeAndLocation();

		System.exit(0);
	}

	public synchronized void doLogin(String secretKey) {
		initializeSystem();
		UserDAO dao = new UserDAO();
		User user = dao.findUserBySecretKey(secretKey);

		if (user == null) {
			throw new UserNotFoundException();
		}

		Shift currentShift = ShiftUtil.getCurrentShift();
		if (currentShift == null) {
			throw new ShiftException(POSConstants.NO_SHIFT_CONFIGURED);
		}

		ShiftUtil.adjustUserShift(user, currentShift);

		setCurrentUser(user);
		setCurrentShift(currentShift);

		RootView rootView = getRootView();

		if (TerminalConfig.isCashierMode()) {
			// SwitchboardView.doTakeout(OrderType.TAKE_OUT);
			if (!rootView.hasView(CashierSwitchBoardView.VIEW_NAME)) {
				CashierSwitchBoardView view = new CashierSwitchBoardView();
				rootView.addView(view);
			}

			rootView.showView(CashierSwitchBoardView.VIEW_NAME);
		} else if (TerminalConfig.isKitchenMode()) {
			if (rootView.hasView(KitchenDisplayView.VIEW_NAME)) {
				rootView.showView(KitchenDisplayView.VIEW_NAME);
			} else {
				KitchenDisplayView kitchenDisplayView = new KitchenDisplayView();
				rootView.addView(kitchenDisplayView);
				rootView.showView(KitchenDisplayView.VIEW_NAME);
			}
		} else {
			rootView.showView(SwitchboardView.VIEW_NAME);
		}
	}

	public void doLogout() {
		if (backOfficeWindow != null) {
			backOfficeWindow.setVisible(false);
			backOfficeWindow = null;
			currentShift = null;
		}

		setCurrentUser(null);
		RootView.getInstance().showView(LoginView.VIEW_NAME);
	}

	public static User getCurrentUser() {
		return getInstance().currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public RootView getRootView() {
		return rootView;
	}

	public void setRootView(RootView rootView) {
		this.rootView = rootView;
	}

	public static PosWindow getPosWindow() {
		return getInstance().posWindow;
	}

	public static BackOfficeWindow getBackOfficeWindow() {
		return getInstance().backOfficeWindow;
	}

	public void setBackOfficeWindow(BackOfficeWindow backOfficeWindow) {
		this.backOfficeWindow = backOfficeWindow;
	}

	public Terminal getTerminal() {

		TerminalDAO.getInstance().refresh(terminal);

		return terminal;
	}

	// public static PrinterConfiguration getPrinterConfiguration() {
	// return getInstance().printConfiguration;
	// }

	public static PosPrinters getPrinters() {
		return getInstance().printers;
	}

	public static String getTitle() {
		return "Floreant POS - Version " + VERSION; //$NON-NLS-1$
	}

	public static ImageIcon getApplicationIcon() {
		return applicationIcon;
	}

	public static void setApplicationIcon(ImageIcon applicationIcon) {
		Application.applicationIcon = applicationIcon;
	}

	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public Shift getCurrentShift() {
		return currentShift;
	}

	public void setCurrentShift(Shift currentShift) {
		this.currentShift = currentShift;
	}

	public void setAutoDrawerPullEnable(boolean enable) {
		if (enable) {
			if (autoDrawerPullTimer != null) {
				return;
			} else {
				autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
				autoDrawerPullTimer.start();
			}
		} else {
			autoDrawerPullTimer.stop();
			autoDrawerPullTimer = null;
		}
	}

	public boolean isSystemInitialized() {
		return systemInitialized;
	}

	public void setSystemInitialized(boolean systemInitialized) {
		this.systemInitialized = systemInitialized;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public static PluginManager getPluginManager() {
		return getInstance().pluginManager;
	}

	public static File getWorkingDir() {
		File file = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		return file.getParentFile();
	}

	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(boolean developmentMode) {
		this.developmentMode = developmentMode;
	}

	public boolean isReportMode() {
		return reportMode;
	}

	public void setReportMode(boolean reportMode) {
		this.reportMode = reportMode;
	}

	public boolean isPriceIncludesTax() {
		Restaurant restaurant = getRestaurant();
		if (restaurant == null) {
			return false;
		}

		return POSUtil.getBoolean(restaurant.isItemPriceIncludesTax());
	}

	public String getLocation() {
		File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		return file.getParent();
	}

	public void takeSqlBackup() {
		try {
			Date today = new Date();
			String dateFolderName = dateFolderFormat.format(today);
			String backupFileName = "pos_" + fullDateFormat.format(today) + ".sql";
			File f = new File(AppConfig.getXamppPath() + "\\mysqldump.exe");

			ProcessBuilder pb = new ProcessBuilder(f.getCanonicalPath(), "-u", "pos", "-ppos123", "pos");
			File sqlFile = new File(AppConfig.getExportPath() + "\\posBackup\\" + yearFolderFormat.format(today) + "\\" + monthFolderFormat.format(today) + "\\" + dateFolderName + "\\"
					+ backupFileName);
			if (!sqlFile.exists()) {
				File parent = sqlFile.getParentFile();
				if (parent.exists() == false) {
					FileUtils.forceMkdir(parent);
				}
			}
			pb.redirectOutput(Redirect.to(sqlFile));

			Process p = pb.start();
			int exit = p.waitFor();
			File zipSqlFile = new File(sqlFile.getAbsolutePath() + ".zip");
			gzipFile(sqlFile, zipSqlFile);
			sqlFile.delete();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

	public static void gzipFile(File from, File to) throws IOException {
		FileInputStream in = new FileInputStream(from);
		GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(to));
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.close();
	}
}
