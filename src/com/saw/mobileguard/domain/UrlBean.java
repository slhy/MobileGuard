package com.saw.mobileguard.domain;
/**
 * @author Administrator
 * @创建时间 2016-9-3 下午9:57:36
 * @描述 url信息的封装
 */
public class UrlBean {
	private String url;//apk下载地址
	private int versionCode;//版本号
	private String desc;//描述
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
