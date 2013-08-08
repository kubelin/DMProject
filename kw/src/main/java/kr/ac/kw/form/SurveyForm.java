package kr.ac.kw.form;

public class SurveyForm {
	private String id;
	private int recommand_v1;
	private int recommand_v2;
	private String etc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRecommand_v1() {
		return recommand_v1;
	}
	public void setRecommand_v1(int recommand_v1) {
		this.recommand_v1 = recommand_v1;
	}
	public int getRecommand_v2() {
		return recommand_v2;
	}
	public void setRecommand_v2(int recommand_v2) {
		this.recommand_v2 = recommand_v2;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	@Override
	public String toString() {
		return "SurveyForm [id=" + id + ", recommand_v1=" + recommand_v1
				+ ", recommand_v2=" + recommand_v2 + ", etc=" + etc + "]";
	}
	
	
	
}
