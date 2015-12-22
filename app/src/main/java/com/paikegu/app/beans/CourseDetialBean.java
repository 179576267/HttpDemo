package com.xiankezu.sirceo.beans;
public class CourseDetialBean {

	public long cid;//课程id
	public String courseName;//课程名称
	public String courseDescribe;//课程描述
	public int tmid;//授课方式	（1:学客上门，2:闲客上门，3:网上授课）
	public int totalHours;//总价	（单位：元）
	public int startTime;//开始时间	时间戳（单位：秒）
	public int endTime;//结束时间	时间戳（单位：秒）
	public String imgUrl;//图片url
	public int total;//总价	（单位：元）
	public int peopleCount;//人数
	public int level;//等级
	public int complianceRate;//守约率
	public int guid;//用户ID
	@Override
	public String toString() {
		return "CourseDetialBean [cid=" + cid + ", courseName=" + courseName
				+ ", courseDescribe=" + courseDescribe + ", tmid=" + tmid
				+ ", totalHours=" + totalHours + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", imgUrl=" + imgUrl + ", total="
				+ total + ", peopleCount=" + peopleCount + ", level=" + level
				+ ", complianceRate=" + complianceRate + ", guid=" + guid + "]";
	}
	
	
}
