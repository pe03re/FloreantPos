package com.floreantpos.ui;

import java.awt.Color;

public class InventoryLevel {
	public InventoryLevel(String level, Color c) {
		super();
		this.level = level;
		this.c = c;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	String level;
	Color c;
}
