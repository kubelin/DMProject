package kr.ac.kw.form;

import java.util.Calendar;
import java.util.Locale;

public class UserForm {
	private String id;
	private String name;
	private int gender;
	private int age;
	private int count;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(String gender) {
		// 1은 남자 
		// 0은 여자
		if(gender.equals("male")){
			gender = "1";
		}else{
			gender = "0";
		}
		this.gender = Integer.parseInt(gender);
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		int curYear = cal.get( Calendar.YEAR) ;
		this.age = curYear - age;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "UserForm [id=" + id + ", name=" + name + ", gender=" + gender
				+ ", age=" + age + ", count=" + count + "]";
	}
	
	
}
