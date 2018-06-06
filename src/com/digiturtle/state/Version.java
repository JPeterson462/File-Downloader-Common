package com.digiturtle.state;

import com.digiturtle.jsonbeans.JSONSerializable;

@JSONSerializable
public class Version {

	private int major, minor, revision;
	
	public Version() {
		
	}
	
	public void set(int major, int minor, int revision) {
		if (major > 0xFF) {
			throw new IllegalArgumentException("Invalid major version: " + major);
		}
		if (minor > 0xFFF) {
			throw new IllegalArgumentException("Invalid minor version: " + minor);
		}
		if (revision > 0xFFF) {
			throw new IllegalArgumentException("Invalid revision: " + revision);
		}
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}
	
	public int getMajor() {
		return major;
	}
	
	public int getMinor() {
		return minor;
	}
	
	public int getRevision() {
		return revision;
	}
	
	public int getVersionIndex() {
		return (major << 6) | (minor << 3) | (revision);
	}
	
}
