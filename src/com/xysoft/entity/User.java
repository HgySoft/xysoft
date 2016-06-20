package com.xysoft.entity;

public class User {
	
	private String name="Ð¡Ç¿";
	private String sex="ÄÐ";
	private int age=0;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name, String sex, int age) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return getName() + "	" + getAge() + "Ëê";
	}
	
}
