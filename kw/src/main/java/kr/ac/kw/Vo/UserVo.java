package kr.ac.kw.Vo;

public class UserVo {
	private String id;
	private String name;
	private int gender;
	private int age;
	private int count;
	private int isrec1;
	private int isrec2;
	private int issurvey;
	
	
	public int getIsrec1() {
		return isrec1;
	}
	public void setIsrec1(int isrec1) {
		this.isrec1 = isrec1;
	}
	public int getIsrec2() {
		return isrec2;
	}
	public void setIsrec2(int isrec2) {
		this.isrec2 = isrec2;
	}
	public int getIssurvey() {
		return issurvey;
	}
	public void setIssurvey(int issurvey) {
		this.issurvey = issurvey;
	}
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
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "UserVo [id=" + id + ", name=" + name + ", gender=" + gender
				+ ", age=" + age + ", count=" + count + ", isrec1=" + isrec1
				+ ", isrec2=" + isrec2 + ", issurvey=" + issurvey + "]";
	}
	
}
