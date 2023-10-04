package com.itheima.pojo;

import java.util.List;

/**
 * 分页查询的JavaBean
 * 声明一个类上的泛型T 将来存什么的时候定义就可以了   使代码通用
 */
public class PageBean<T> {
	// 总记录数
	private int totalCount;

	// 当前页数据
	private List<T> rows;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
