package com.floreantpos.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.util.TicketUtils;
import com.floreantpos.util.NumberUtil;

public class ReceiptPrintService {
	private static final String TIP_AMOUNT = "tipAmount";
	private static final String SERVICE_CHARGE = "serviceCharge";
	private static final String TAX_AMOUNT = "taxAmount";
	private static final String TAX_TEXT = "taxText";

	private static final String DISCOUNT_AMOUNT = "discountAmount";
	private static final String HEADER_LINE5 = "headerLine5";
	private static final String HEADER_LINE4 = "headerLine4";
	private static final String HEADER_LINE3 = "headerLine3";
	private static final String HEADER_LINE2 = "headerLine2";
	private static final String HEADER_LINE1 = "headerLine1";
	private static final String REPORT_DATE = "reportDate";
	private static final String SERVER_NAME = "serverName";
	private static final String GUEST_COUNT = "guestCount";
	private static final String TABLE_NO = "tableNo";
	private static final String CHECK_NO = "checkNo";
	private static final String TERMINAL = "terminal";
	private static final String SHOW_FOOTER = "showFooter";
	private static final String SHOW_HEADER_SEPARATOR = "showHeaderSeparator";
	private static final String SHOW_SUBTOTAL = "showSubtotal";
	private static final String RECEIPT_TYPE = "receiptType";
	private static final String SUB_TOTAL_TEXT = "subTotalText";
	private static final String QUANTITY_TEXT = "quantityText";
	private static final String ITEM_TEXT = "itemText";
	private static final String CURRENCY_SYMBOL = "currencySymbol";
	private static Log logger = LogFactory.getLog(ReceiptPrintService.class);

	public static void printGenericReport(String title, String data) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put("title", title);
		map.put("data", data);
		JasperPrint jasperPrint = createJasperPrint(ReportUtil.getReport("generic-receipt"), map, new JREmptyDataSource());
		jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		printQuitely(jasperPrint);
	}

	// public static JasperPrint createJasperPrint(String reportFile,
	// Map<String, String> properties, JRDataSource dataSource) throws Exception
	// {
	// InputStream ticketReportStream = null;
	//
	// try {
	//
	// ticketReportStream =
	// JReportPrintService.class.getResourceAsStream(reportFile);
	// JasperReport ticketReport = (JasperReport)
	// JRLoader.loadObject(ticketReportStream);
	//
	// JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport,
	// properties, dataSource);
	// return jasperPrint;
	//
	// } finally {
	// IOUtils.closeQuietly(ticketReportStream);
	// }
	// }

	public static JasperPrint createJasperPrint(JasperReport report, Map<String, String> properties, JRDataSource dataSource) throws Exception {
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, properties, dataSource);
		return jasperPrint;
	}

	public static JasperPrint createGeneralTicketPrint(Ticket ticket, Map<String, String> map, PosTransaction transaction) throws Exception {
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(ReportUtil.getReport("ticket-receipt"), map, new JRTableModelDataSource(dataSource));
	}

	public static JasperPrint createMerchantPrint(Ticket ticket, Map<String, String> map, PosTransaction transaction) throws Exception {
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(ReportUtil.getReport("ticket-receipt-mer"), map, new JRTableModelDataSource(dataSource));
	}

	public static void printTicket(Ticket ticket) {
		try {

			TicketPrintProperties printProperties = new TicketPrintProperties("Invoice: " + TicketUtils.getTicketNumber(ticket), true, true, true);
			printProperties.setPrintCookingInstructions(false);

			PosTransaction consT = PosTransactionDAO.getInstance().getConsolidatedTransactionByTicket(ticket);

			HashMap map = populateTicketProperties(ticket, printProperties, consT);

			JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, consT);
			jasperPrint.setName("ORDER_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printLastTicket(Ticket ticket) {
		try {

			TicketPrintProperties printProperties = new TicketPrintProperties("Invoice: " + TicketUtils.getTicketNumber(ticket), true, true, true);
			printProperties.setPrintCookingInstructions(false);
			PosTransaction consT = PosTransactionDAO.getInstance().getConsolidatedTransactionByTicket(ticket);

			HashMap map = populateTicketProperties(ticket, printProperties, consT);

			JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, consT);
			jasperPrint.setName("ORDER_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static JasperPrint createRefundPrint(Ticket ticket, HashMap map) throws Exception {
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(ReportUtil.getReport("refund-receipt"), map, new JRTableModelDataSource(dataSource));
	}

	public static void printRefundTicket(Ticket ticket, RefundTransaction posTransaction) {
		try {
			TicketPrintProperties printProperties = new TicketPrintProperties("*** REFUND RECEIPT ***", true, true, true);
			printProperties.setPrintCookingInstructions(false);

			HashMap map = populateTicketProperties(ticket, printProperties, posTransaction);
			map.put("refundAmountText", "Total Refund");
			map.put("refundAmount", String.valueOf(posTransaction.getAmount()));
			map.put("cashRefundText", "Cash Refund");
			map.put("cashRefund", String.valueOf(posTransaction.getAmount()));

			JasperPrint jasperPrint = createRefundPrint(ticket, map);
			jasperPrint.setName("REFUND_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction) {
		try {

			Ticket ticket = transaction.getTicket();

			TicketPrintProperties printProperties = new TicketPrintProperties("Invoice: " + TicketUtils.getTicketNumber(ticket), true, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Customer Copy");
				JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				map.put("copyType", "Merchant Copy");
				jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getSerialId() + "-MerchantCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			} else {
				JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
				jasperPrint.setName("Customer Ticket-" + ticket.getSerialId());
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				// commented out second print command
				// JasperPrint jasperPrintMer = createMerchantPrint(ticket, map,
				// transaction);
				// jasperPrintMer.setName("Merchant Ticket-" +
				// ticket.getSerialId());
				// jasperPrintMer.setProperty("printerName",
				// Application.getPrinters().getReceiptPrinter());
				// printQuitely(jasperPrintMer);
			}
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	// for credit card
	public static void printTransaction(PosTransaction transaction, boolean printCustomerCopy) {
		try {
			Ticket ticket = transaction.getTicket();

			TicketPrintProperties printProperties = new TicketPrintProperties("Invoice: ", true, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Merchant Copy");

				JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				if (printCustomerCopy) {
					map.put("copyType", "Customer Copy");

					jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
					jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");
					jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
					printQuitely(jasperPrint);
				}
			} else {
				JasperPrint jasperPrint = createGeneralTicketPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId());
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	private static void beginRow(StringBuilder html) {
		html.append("<div>");
	}

	private static void endRow(StringBuilder html) {
		html.append("</div>");
	}

	private static void addColumn(StringBuilder html, String columnText) {
		html.append("<span>" + columnText + "</span>");
	}

	public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties, PosTransaction transaction) {
		Restaurant restaurant = RestaurantDAO.getRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
		map.put(SUB_TOTAL_TEXT, POSConstants.RECEIPT_REPORT_SUBTOTAL_LABEL);
		map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_FOOTER, Boolean.valueOf(printProperties.isShowFooter()));
		if (ticket.getTokenNo() != null && ticket.getTokenNo() > 0) {
			map.put("orderNo", "Token: " + ticket.getTokenNo());
		}
		map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(ticket.getCreateDate());
		map.put(REPORT_DATE, dateString);

		StringBuilder ticketHeaderBuilder = buildTicketHeader(ticket, printProperties);

		map.put("ticketHeader", ticketHeaderBuilder.toString());
		map.put("barcode", String.valueOf(ticket.getId()));

		if (printProperties.isShowHeader()) {
			// map.put(HEADER_LINE1, restaurant.getName());
			if (restaurant.getAddressLine1() != null) {
				map.put(HEADER_LINE2, restaurant.getAddressLine1());
			}
			if (restaurant.getAddressLine2() != null) {
				map.put(HEADER_LINE3, restaurant.getAddressLine2().trim().length() > 0 ? restaurant.getAddressLine2().trim() : null);
			}
			if (restaurant.getAddressLine3() != null) {
				map.put(HEADER_LINE4, restaurant.getAddressLine3().trim().length() > 0 ? restaurant.getAddressLine3().trim() : null);
			}
			if (restaurant.getTelephone() != null) {
				map.put(HEADER_LINE5, restaurant.getTelephone().trim().length() > 0 ? restaurant.getTelephone().trim() : null);
			}
		}

		if (printProperties.isShowFooter()) {
			if (ticket.getDiscountAmount() > 0.0) {
				map.put(DISCOUNT_AMOUNT, NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			buildTaxDetails(ticket, map);

			if (ticket.getServiceCharge() > 0.0) {
				map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
			}
			map.put("subtotalText", POSConstants.SUBTOTAL + " ");

			map.put("basePriceText", "RATE");
			map.put("totalText", POSConstants.RECEIPT_REPORT_TOTAL_LABEL);
			map.put("discountText", POSConstants.RECEIPT_REPORT_DISCOUNT_LABEL);
			map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL);

			map.put("netAmountText", POSConstants.RECEIPT_REPORT_NETAMOUNT_LABEL);
			map.put("paidAmountText", POSConstants.RECEIPT_REPORT_PAIDAMOUNT_LABEL);
			map.put("dueAmountText", POSConstants.RECEIPT_REPORT_DUEAMOUNT_LABEL.trim());

			map.put("totalAmount", NumberUtil.formatNumber(totalAmount));

			double roundOff = NumberUtil.mathRoundOff(totalAmount) - totalAmount;
			map.put("netAmount", NumberUtil.formatNumber(NumberUtil.mathRoundOff(totalAmount + roundOff)));
			map.put("paidAmount", NumberUtil.formatNumber(ticket.getPaidAmount()));
			if (roundOff != 0) {
				map.put("roundOff", Double.toString(NumberUtil.roundToTwoDigit(roundOff)));
				map.put("roundOffText", "Rounding Off");
			}
			roundOff = NumberUtil.mathRoundOff(ticket.getDueAmount()) - ticket.getDueAmount();
			map.put("dueAmount", NumberUtil.formatNumber(ticket.getDueAmount() + roundOff));

			map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
			if (restaurant.getTicketFeedbackMessage() != null) {
				map.put("feedback", restaurant.getTicketFeedbackMessage().trim());
			}
			if (restaurant.getTicketTINMessage() != null) {
				map.put("tin", restaurant.getTicketTINMessage().trim());
			}
			map.put("footerMessage", restaurant.getTicketFooterMessage());
			// map.put("copyType", printProperties.getReceiptCopyType());
			// map.put("tipsText", POSConstants.RECEIPT_REPORT_TIPS_LABEL +
			// currencySymbol);

			if (transaction != null) {
				double tenderedAmount = transaction.getTenderAmount();
				double changedAmount = transaction.getTenderAmount() - transaction.getAmount();
				if (changedAmount < 0) {
					changedAmount = 0;
				}

				double roundOffChanged = NumberUtil.mathRoundOff(changedAmount) - changedAmount;

				map.put("tenderedAmountText", "Tendered Amount");
				map.put("tenderedAmount", NumberUtil.formatNumber(tenderedAmount));
				map.put("changeAmountText", "Change Returned");
				map.put("changedAmount", NumberUtil.formatNumber(changedAmount + roundOffChanged));

				if (transaction.isCard()) {
					map.put("cardPayment", true);

					if (StringUtils.isNotEmpty(transaction.getCardTrack())) {
						BankCardMagneticTrack track = BankCardMagneticTrack.from(transaction.getCardTrack());
						String string = transaction.getCardType();
						string += "<br/>" + "APPROVAL: " + transaction.getCardAuthCode();

						try {
							string += "<br/>" + "ACCT: " + getCardNumber(track);
							string += "<br/>" + "EXP: " + track.getTrack1().getExpirationDate();
							string += "<br/>" + "CARDHOLDER: " + track.getTrack1().getName();
						} catch (Exception e) {
							logger.equals(e);
						}

						map.put("approvalCode", string);
					} else {
						String string = "APPROVAL: " + transaction.getCardAuthCode();
						string += "<br/>" + "Card processed in ext. device.";

						map.put("approvalCode", string);
					}
				}
			}

			String messageString = "<html>";
			// String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);

			// if (customerName != null) {
			// if (customer.hasProperty("mykalaid")) {
			// messageString += "<br/>Customer: " + customer.getName();
			// }
			// }
			if (ticket.hasProperty("mykaladiscount")) {
				messageString += "<br/>My Kala point: " + ticket.getProperty("mykalapoing");
				messageString += "<br/>My Kala discount: " + ticket.getDiscountAmount();
			}
			messageString += "</html>";
			map.put("additionalProperties", messageString);
		}

		return map;
	}

	private static void buildTaxDetails(Ticket ticket, HashMap map) {
		StringBuilder taxNameBuilder = new StringBuilder();
		taxNameBuilder.append("<html>");

		StringBuilder taxRateBuilder = new StringBuilder();
		taxRateBuilder.append("<html>");

		double serviceTax = 0.0;
		double vatTax = 0.0;
		Map<String, Double> taxMap = ticket.getTicketTaxDetails();
		for (Map.Entry<String, Double> entry : taxMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains("service")) {
				serviceTax += entry.getValue();
			} else if (entry.getKey().toLowerCase().contains("vat")) {
				vatTax += entry.getValue();
			}
		}
		if (serviceTax > 0.0) {
			beginRow(taxNameBuilder);
			addColumn(taxNameBuilder, "Service Tax");
			endRow(taxNameBuilder);

			beginRow(taxRateBuilder);
			addColumn(taxRateBuilder, NumberUtil.formatNumber(serviceTax));
			endRow(taxRateBuilder);
		}

		if (vatTax > 0.0) {
			beginRow(taxNameBuilder);
			addColumn(taxNameBuilder, "VAT");
			endRow(taxNameBuilder);

			beginRow(taxRateBuilder);
			addColumn(taxRateBuilder, NumberUtil.formatNumber(vatTax));
			endRow(taxRateBuilder);
		}

		taxNameBuilder.append("</html>");
		taxRateBuilder.append("</html>");

		map.put(TAX_TEXT, taxNameBuilder.toString());
		map.put(TAX_AMOUNT, taxRateBuilder.toString());
	}

	private static StringBuilder buildTicketHeader(Ticket ticket, TicketPrintProperties printProperties) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy, h:m a");

		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "*" + ticket.getType() + "*");
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		endRow(ticketHeaderBuilder);

		if (ticket.getType() == OrderType.DINE_IN) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
			endRow(ticketHeaderBuilder);

			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
			endRow(ticketHeaderBuilder);
		}

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_DATE_LABEL + dateFormat.format(new Date()));
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "");
		endRow(ticketHeaderBuilder);

		// customer info section
		if (ticket.getType() != OrderType.DINE_IN) {

			String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
			String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);

			if (StringUtils.isNotEmpty(customerName)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*Delivery to*");
				endRow(ticketHeaderBuilder);

				if (StringUtils.isNotEmpty(customerName)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerName);
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(ticket.getDeliveryAddress())) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, ticket.getDeliveryAddress());
					endRow(ticketHeaderBuilder);
				} else {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Pickup from hotel");
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(customerPhone)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Tel: " + customerPhone);
					endRow(ticketHeaderBuilder);
				}

				if (ticket.getDeliveryDate() != null) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Delivery: " + dateFormat.format(ticket.getDeliveryDate()));
					endRow(ticketHeaderBuilder);
				}
			}
		}

		ticketHeaderBuilder.append("</html>");
		return ticketHeaderBuilder;
	}

	public static JasperPrint createKitchenPrint(KitchenTicket ticket) throws Exception {
		HashMap map = new HashMap();

		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, "*** KITCHEN RECEIPT *** ");
		map.put("cardPayment", true);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getTicketId());
		// if (ticket.getTableNumbers() != null) {
		// map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL +
		// ticket.getTableNumbers());
		// }
		// map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL +
		// ticket.getNumberOfGuests());
		// map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL +
		// ticket.getServerName());

		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getServerName());
		String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(ticket.getCreateDate());
		map.put(REPORT_DATE, dateString);
		map.put("TicketNo", "Ticket: " + ticket.getSerialId());
		if (ticket.getTokenNo() != null && ticket.getTokenNo() > 0) {
			map.put("OrderNo", "Token: " + ticket.getTokenNo());
		}

		map.put("ticketHeader", "KTICHEN RECEIPT");

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		return createJasperPrint(ReportUtil.getReport("kitchen-receipt"), map, new JRTableModelDataSource(dataSource));
	}

	public static void printToKitchen(Ticket ticket, boolean printAll) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = KitchenTicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			List<KitchenTicket> kitchenTickets = KitchenTicket.fromTicket(ticket, printAll);

			for (KitchenTicket kitchenTicket : kitchenTickets) {

				String deviceName = kitchenTicket.getPrinter().getDeviceName();

				JasperPrint jasperPrint = createKitchenPrint(kitchenTicket);
				jasperPrint.setName("KitchenReceipt-" + ticket.getSerialId());
				jasperPrint.setProperty("printerName", deviceName);

				// KitchenDisplayWindow.instance.addTicket(kitchenTicket);
				printQuitely(jasperPrint);

				session.saveOrUpdate(kitchenTicket);
			}

			transaction.commit();

			ticket.clearDeletedItems();
			TicketDAO.getInstance().saveOrUpdate(ticket, true);

		} catch (Exception e) {
			transaction.rollback();
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			session.close();
		}
	}

	private static void printQuitely(JasperPrint jasperPrint) throws JRException {
		try {
			JasperPrintManager.printReport(jasperPrint, false);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	// private static void markItemsAsPrinted(KitchenTicket ticket) {
	// List<TicketItem> ticketItems = ticket.getTicketItems();
	// if (ticketItems != null) {
	// for (TicketItem ticketItem : ticketItems) {
	// if (!ticketItem.isPrintedToKitchen()) {
	// ticketItem.setPrintedToKitchen(true);
	// }
	//
	// List<TicketItemModifierGroup> modifierGroups =
	// ticketItem.getTicketItemModifierGroups();
	// if (modifierGroups != null) {
	// for (TicketItemModifierGroup modifierGroup : modifierGroups) {
	// modifierGroup.setPrintedToKitchen(true);
	// }
	// }
	//
	// List<TicketItemCookingInstruction> cookingInstructions =
	// ticketItem.getCookingInstructions();
	// if (cookingInstructions != null) {
	// for (TicketItemCookingInstruction ticketItemCookingInstruction :
	// cookingInstructions) {
	// ticketItemCookingInstruction.setPrintedToKitchen(true);
	// }
	// }
	// }
	// }
	//
	// KitchenTicketDAO.getInstance().saveOrUpdate(ticket);
	// }

	private static String getCardNumber(BankCardMagneticTrack track) {
		String no = "";

		try {
			if (track.getTrack1().hasPrimaryAccountNumber()) {
				no = track.getTrack1().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			} else if (track.getTrack2().hasPrimaryAccountNumber()) {
				no = track.getTrack2().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return no;
	}

	public static void main(String args[]) {
		Date ticketDate = new Date();
		String year = null;
		int yearInt = ticketDate.getYear() - 100;
		if (ticketDate.getMonth() >= 3) {
			year = yearInt + "-" + (yearInt + 1);
		} else {
			year = (yearInt - 1) + "-" + yearInt;
		}
		String newstring = new SimpleDateFormat("/MM/dd/").format(ticketDate);
		System.out.println(year + newstring + 1234); // 2011-01-18
		// TicketPrintProperties printProperties = new
		// TicketPrintProperties(year + newstring + 1234, true, true, true);
	}
}
