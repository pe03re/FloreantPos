package com.floreantpos.sale.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.service.ReportService;

public class SaleDetailsReport extends Report {
	private SaleDetailsReportModel itemReportModel;
	private SaleDetailsReportModel modifierReportModel;

	public SaleDetailsReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		SaleDetailsReportModel itemReportModel = this.itemReportModel;
		SaleDetailsReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = ReportUtil.getReport("sales_sub_report_new");
		JasperReport modifierReport = ReportUtil.getReport("sales_sub_report_new");

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", "============================ Sale Detail Report ===============================");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);

		JasperReport masterReport = ReportUtil.getReport("sales_report");

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new JRViewer(print);
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	private void refreshBuyPrice() {
		MenuItemDAO menuItemDAO = new MenuItemDAO();
		List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
		for (MenuItem m : itemList) {
			m.setBuyPrice(getBuyPriceFromInventory(m));
			menuItemDAO.saveOrUpdate(m);
		}
	}

	private static Double getBuyPriceFromInventory(MenuItem menuItem) {
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

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2, true);
		refreshBuyPrice();
		HashMap<String, SaleDetailReportItem> itemMap = new HashMap<String, SaleDetailReportItem>();
		HashMap<String, SaleDetailReportItem> modifierMap = new HashMap<String, SaleDetailReportItem>();
		// Map<String, List<TicketItem>> ticketItems = new HashMap<String,
		// List<TicketItem>>();
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket t = (Ticket) iter.next();

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());

			// ticketItems.put(ticket.getCreateDateFormatted(),
			// ticket.getTicketItems());
			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;

			String key = null;
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				} else {
					key = ticketItem.getItemId().toString();
				}
				SaleDetailReportItem reportItem = itemMap.get(key);
				MenuItem mi = MenuItemDAO.getInstance().findByItemId(ticketItem.getItemId());
				if (reportItem == null) {
					reportItem = new SaleDetailReportItem();
					reportItem.setId(key);
					reportItem.setDate(ticket.getCreateDateTimeFormatted());
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setBuyPrice(mi.getBuyPrice());
					reportItem.setProfit(ticketItem.getUnitPrice() - mi.getBuyPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxList(ticketItem.getTaxList());
					reportItem.setDiscount(ticketItem.getDiscountAmount());
					reportItem.setQuantity(ticketItem.getItemCount());
					itemMap.put(key, reportItem);
				}

				if (ticketItem.isHasModifiers() && ticketItem.getTicketItemModifierGroups() != null) {
					List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();

					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						List<TicketItemModifier> modifiers = ticketItemModifierGroup.getTicketItemModifiers();
						for (TicketItemModifier modifier : modifiers) {
							if (modifier.getItemId() == null) {
								key = modifier.getName();
							} else {
								key = modifier.getItemId().toString();
							}
							SaleDetailReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new SaleDetailReportItem();
								modifierReportItem.setId(key);
								modifierReportItem.setPrice(modifier.getUnitPrice());
								modifierReportItem.setName(modifier.getName());
								// modifierReportItem.setTaxRate(modifier.getTaxRate());
								modifierReportItem.setTaxList(modifier.getTaxList());

								modifierMap.put(key, modifierReportItem);
							}
							modifierReportItem.setQuantity(modifierReportItem.getQuantity() + 1);
							// modifierReportItem.setTotal(modifierReportItem.getTotal()
							// + modifier.getTotal());
						}
					}
				}
			}
			ticket = null;
			iter.remove();
		}
		itemReportModel = new SaleDetailsReportModel();
		itemReportModel.setItems(new ArrayList<SaleDetailReportItem>(itemMap.values()));
		itemReportModel.calculateGrandTotal();

		modifierReportModel = new SaleDetailsReportModel();
		modifierReportModel.setItems(new ArrayList<SaleDetailReportItem>(modifierMap.values()));
		modifierReportModel.calculateGrandTotal();
	}

	@Override
	public void generateReport(Date startDate, Date endDate) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createModels(Date date1, Date date2) {
		// TODO Auto-generated method stub
		
	}
}
