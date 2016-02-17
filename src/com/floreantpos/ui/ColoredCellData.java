package com.floreantpos.ui;

import java.awt.Color;

public class ColoredCellData {
	public ColoredCellData(String dataStr, Color c) {
		super();
		this.dataStr = dataStr;
		this.c = c;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	String dataStr;
	Color c;
}
