package kr.ac.kw.Service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Named;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.ac.kw.Mapper.FavoriteSongMapper;
import kr.ac.kw.Vo.FavoriteSongVo;
import kr.ac.kw.form.FavoriteSongForm;

@Repository
@Named("favoriteSongService")
public class FavoriteSongService {
	
	/**
	 * insert FavoriteSong
	 * @param session
	 * @param fsForm
	 * @throws Exception
	 */
	public void insertFS(SqlSession session,FavoriteSongForm fsForm) throws Exception {
		
		FavoriteSongMapper mapper = session.getMapper(FavoriteSongMapper.class);
		
		mapper.insertFS(fsForm);
		
	}	
	
	/**
	 * select favoriteSongList using ID
	 * @param session
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ArrayList<FavoriteSongVo> selectFSList(SqlSession session, String id) throws Exception{
		
		FavoriteSongMapper mapper = session.getMapper(FavoriteSongMapper.class);
		
		return mapper.selectFSList(id);
		
	}
	
	/**
	 * select all favoriteSongs
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ArrayList<FavoriteSongVo> selectAllFSList(SqlSession session) throws Exception{
		
		FavoriteSongMapper mapper = session.getMapper(FavoriteSongMapper.class);
		
		return mapper.selectAllFSList();
		
	}
	
	public Integer selectCount(SqlSession session, HashMap<String, String> valueMap )throws Exception{
	
		FavoriteSongMapper mapper = session.getMapper(FavoriteSongMapper.class);
		
		return mapper.selectCount(valueMap);
	}
	
	
}
