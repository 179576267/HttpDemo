package com.xiankezu.sirceo.beans;

import java.util.List;

/**
 * 课程的分类查找bean
 * @author zhenfei.wang
 */
public class CourseClassifyMainBean {
	public int ocid;
	public String ocName;
	public List<SecondClassifyBean> category;
	
	public class SecondClassifyBean{
		public int tcid;
		public String tcName;
	}

}
