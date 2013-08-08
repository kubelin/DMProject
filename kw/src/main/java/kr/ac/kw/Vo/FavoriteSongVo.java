package kr.ac.kw.Vo;

public class FavoriteSongVo {
	
	private int fs_num;
	private String id;
	private String genre;
	private int fs_1;
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public int getFs_num() {
		return fs_num;
	}
	public void setFs_num(int fs_num) {
		this.fs_num = fs_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFs_1() {
		return fs_1;
	}
	public void setFs_1(int fs_1) {
		this.fs_1 = fs_1;
	}
	
	@Override
	public String toString() {
		return "FavoriteSongVo [fs_num=" + fs_num + ", id=" + id + ", genre="
				+ genre + ", fs_1=" + fs_1 + "]";
	}
}
