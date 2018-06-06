package com.digiturtle.state;

import com.digiturtle.jsonbeans.JSONSerializable;

@JSONSerializable
public class FileRecord {
	
	private int checksum;
	
	private Version version;
	
	private String name;
	
	private String guid;

	public FileRecord() {
		
	}
	
	public int getChecksum() {
		return checksum;
	}
	
	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public void setVersion(Version version) {
		this.version = version;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
}
