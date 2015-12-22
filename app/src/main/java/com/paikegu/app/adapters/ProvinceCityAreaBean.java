package com.xiankezu.sirceo.adapters;

import java.util.List;

public class ProvinceCityAreaBean {

	public int pid;//省份id
	public String provinceName;//省份名称
	public List<Citys> citys;//市区级联集合
	
	
	
	public class Citys{
		public int cid;//城市id
		public String cityName;//城市名称
		public List<Areas> areas;//区级联集合
	}
	
	
	public class Areas{
		public int aid;//区域id
		public String areaName;//区域名称
	}

}
