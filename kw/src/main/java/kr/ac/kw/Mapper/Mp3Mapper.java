package kr.ac.kw.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.kw.Vo.Mp3Vo;
import kr.ac.kw.form.Mp3Form;

public interface Mp3Mapper {

	public abstract void insertMp3(Mp3Form mp3Form) throws Exception;
	
	public abstract String isExist(String title) throws Exception;
	
	public abstract ArrayList<Mp3Vo> selectMp3List(Map<String, String> valueMap) throws Exception;
	public abstract ArrayList<Mp3Vo> selectMp3SoulList() throws Exception;
	public abstract ArrayList<Mp3Vo> selectMp3LikeList(Map<String, String> valueMap) throws Exception;
	public abstract ArrayList<Mp3Vo> selectMp3RecommandedList(String id) throws Exception;
	public abstract int selectTotalCount(String genre)throws Exception;
	public abstract void updateCount(String song_num) throws Exception;
	// 추천 service
	public abstract ArrayList<Mp3Vo> selectRecommandMp3v2(HashMap<String, String> valueMap) throws Exception;
	public abstract ArrayList<Mp3Vo> selectRecommandMp3v1(String value) throws Exception;
	
	public abstract String selectBalladList() throws Exception;
	

	
}
