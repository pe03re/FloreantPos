package com.floreantpos.swing;

public interface IUpdatebleView<E> {
	void initView(E e);

	void updateView(E e);

	boolean updateModel(E e);

	void clearTableModel();
}
