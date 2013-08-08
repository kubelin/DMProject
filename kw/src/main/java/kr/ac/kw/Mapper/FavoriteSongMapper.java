package kr.ac.kw.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kw.Vo.FavoriteSongVo;
import kr.ac.kw.form.FavoriteSongForm;

public interface FavoriteSongMapper {
	
	public abstract void insertFS(FavoriteSongForm fsForm) throws Exception;
	
	public abstract ArrayList<FavoriteSongVo> selectFSList(String id) throws Exception;
	public abstract ArrayList<FavoriteSongVo> selectAllFSList() throws Exception;
	public abstract Integer selectCount(HashMap<String, String> valueMap )throws Exception;
}
