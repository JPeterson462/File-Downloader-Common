package com.digiturtle.state;

import java.util.ArrayList;

import com.digiturtle.jsonbeans.JSONSerializable;

@JSONSerializable
public class Directory {

	private ArrayList<FileRecord> files = new ArrayList<>();
	
	private ArrayList<Directory> subDirectories = new ArrayList<>();
	
	private String name;
	
	public Directory() {
		
	}
	
	public int size() {
		int size = 0;
		for (int i = 0; i < subDirectories.size(); i++) {
			size += subDirectories.get(i).size();
		}
		size += files.size();
		return size;
	}
	
	public ArrayList<FileRecord> getFiles() {
		return files;
	}
	
	public FileRecord findRecord(String guid) {
		for (int i = 0; i < files.size(); i++) {
			FileRecord file = files.get(i);
			if (file.getGuid().equalsIgnoreCase(guid)) {
				return file;
			}
		}
		return null;
	}
	
	public ArrayList<Directory> getSubDirectories() {
		return subDirectories;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
