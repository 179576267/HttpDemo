package com.xiankezu.sirceo.beans;

public class FilterConditionBean {
	private int aid;//区域id（筛选区域，如静安区，浦东区等）——int（不传或0时不筛选）
	private int complianceSort;//闲客的守约率从高到低筛选——Integer(不传时不筛选)
	private int levelSort;//闲客等级筛选——Integer（不传时不筛选，大于0按从大到小，小于等于0按从小到大排序）
	private int maxMoney;//输入金额的最大金额
	private int minMoney;//输入金额的最小金额
	private int page;//页码——int（默认小于1时都按第一页，第一页时不用传selectTime，第二页需要传入，该参数，由每次返回的数据中获取）
	private int priceSort;//价格筛选——Integer（不穿时不筛选，大于0时由高到低，小于0时由低到高）
	private int selectTime;//默认查询的时间——int(page为1时可以不传，大于1时传入之前该接口返回的selectTime参数)
	private int tcid;//根据课程编号筛选——int（不传或0时不筛选）
	private int tmid;//按授课方式筛选(1:学客上门，2:闲客上门，3:网上授课)——int(不传或0时不筛选)
	public void setAid(int aid) {
		this.aid = aid;
	}
	public void setComplianceSort(int complianceSort) {
		this.complianceSort = complianceSort;
	}
	public void setLevelSort(int levelSort) {
		this.levelSort = levelSort;
	}
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setPriceSort(int priceSort) {
		this.priceSort = priceSort;
	}
	public void setSelectTime(int selectTime) {
		this.selectTime = selectTime;
	}
	public void setTcid(int tcid) {
		this.tcid = tcid;
	}
	public void setTmid(int tmid) {
		this.tmid = tmid;
	}
	@Override
	public String toString() {
		return "FilterConditionBean [aid=" + aid + ", complianceSort="
				+ complianceSort + ", levelSort=" + levelSort + ", maxMoney="
				+ maxMoney + ", minMoney=" + minMoney + ", page=" + page
				+ ", priceSort=" + priceSort + ", selectTime=" + selectTime
				+ ", tcid=" + tcid + ", tmid=" + tmid + "]";
	}

	
	
}
