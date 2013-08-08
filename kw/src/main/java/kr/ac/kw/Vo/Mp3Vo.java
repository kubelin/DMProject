package kr.ac.kw.Vo;

import kr.ac.kw.Commons.GlobalConstant;

public class Mp3Vo {
	private int song_number;
	private String title;
	private String author;
	private String genre;
	private String album;
	private int song_date;
	private String copyright;
	private int recommended;
	private String author_nationality;
	private String author_gender;
	private String author_genre;
	private String author_similar;
	private int isRecommanded;
	private String author_act;
	private int tempo;
	
	
	public int getTempo() {
		return tempo;
	}
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	public String getAuthor_act() {
		return author_act;
	}
	public void setAuthor_act(String author_act) {
		this.author_act = author_act;
		
		if( author_act.equals("솔로") ) 
			GlobalConstant.author_act_1++;
		else
			GlobalConstant.author_act_2++;
	}
	public String getAuthor_nationality() {
		return author_nationality;
	}
	public void setAuthor_nationality(String author_nationality) {
		this.author_nationality = author_nationality;
		if( author_nationality.equals("대한민국")){
			GlobalConstant.count_national_1++;
			}else{
			GlobalConstant.count_national_2++;
		}
	}
	public String getAuthor_gender() {
		return author_gender;
	}
	public void setAuthor_gender(String author_gender) {
		this.author_gender = author_gender;
		System.out.println(author_gender);
		if( author_gender.equals("남성")){
			GlobalConstant.count_gender_1++;
		}else if( author_gender.equals("여성")){
			GlobalConstant.count_gender_2++;
		}else{
			GlobalConstant.count_gender_3++;
		}
	}
	public String getAuthor_genre() {
		return author_genre;
	}
	public void setAuthor_genre(String author_genre) {
		this.author_genre = author_genre;
	}
	public String getAuthor_similar() {
		return author_similar;
	}
	public void setAuthor_similar(String author_similar) {
		this.author_similar = author_similar;
	}
	public int getIsRecommanded() {
		return isRecommanded;
	}
	public void setIsRecommanded(int isRecommanded) {
		this.isRecommanded = isRecommanded;
	}
	public int getSong_number() {
		return song_number;
	}
	public void setSong_number(int song_number) {
		this.song_number = song_number;
	}
	
	public int getRecommended() {
		return recommended;
	}
	public void setRecommended(int recommended) {
		this.recommended = recommended;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}

	public int getSong_date() {
		return song_date;
	}
	public void setSong_date(int song_date) {
		this.song_date = song_date;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	@Override
	public String toString() {
		return "Mp3Vo [song_number=" + song_number + ", title=" + title
				+ ", author=" + author + ", genre=" + genre + ", album="
				+ album + ", song_date=" + song_date + ", copyright="
				+ copyright + ", recommended=" + recommended
				+ ", author_nationality=" + author_nationality
				+ ", author_gender=" + author_gender + ", author_genre="
				+ author_genre + ", author_similar=" + author_similar
				+ ", isRecommanded=" + isRecommanded + "]";
	}
	
}
