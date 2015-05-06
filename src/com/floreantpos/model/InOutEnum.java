package com.floreantpos.model;

public enum InOutEnum {
	IN(1), OUT(-1), MOVEMENT(0), ADJUSTMENT(2), WASTAGE(3), CONSUMPTION(4);

	private int type;

	private InOutEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static InOutEnum fromInt(int type) {
		InOutEnum[] values = values();

		for (InOutEnum inOutEnum : values) {
			if (inOutEnum.type == type) {
				return inOutEnum;
			}
		}

		return MOVEMENT;
	}

	@Override
	public String toString() {
		return name();
	}
}
