package kr.ac.kw.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Named;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

import kr.ac.kw.Commons.GlobalConstant;
import kr.ac.kw.Commons.GlobalEnum;
import kr.ac.kw.Mapper.Mp3Mapper;
import kr.ac.kw.Recommand.CalRecommandation;
import kr.ac.kw.Vo.FavoriteSongVo;
import kr.ac.kw.Vo.Mp3Vo;
import kr.ac.kw.Vo.UserVo;
import kr.ac.kw.form.Mp3Form;

import org.apache.ibatis.session.SqlSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import beatit.BPM2SampleProcessor;
import beatit.EnergyOutputAudioDevice;

@Repository
@Named("mp3Service")
public class Mp3Service {

	/**
	 * insertMp3 to DB
	 * @param session
	 * @param mp3Form
	 * @throws Exception
	 */
	public void insertMp3(SqlSession session, Mp3Form mp3Form) throws Exception {

		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		if (!isExist(session, mp3Form.getTitle())) {
			mapper.insertMp3(mp3Form);
		} else {
			System.out.println("이미 DB에 존재하는 파일입니다.");
			return;
		}
	}
	
	/**
	 * Check if mp3 is exist
	 * @param session
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(SqlSession session, String title) throws Exception {

		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		try {
			mapper.isExist(title);
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
	public int selectTotalCount(SqlSession session, String genre)throws Exception{
		
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		
		if( genre.equals("Rap")) genre = "Rap%";
		
		return mapper.selectTotalCount(genre);
		
	}

	/**
	 * get Mp3List using genre
	 *  // like 참조를 쓰기 위한 likeList
	 * 
	 * @param session
	 * @param valueMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Mp3Vo> selectMp3List(SqlSession session, HashMap<String, String> valueMap) throws Exception {
		ArrayList<Mp3Vo> resultList = new ArrayList<Mp3Vo>();
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		
		switch (GlobalEnum.valueOf(valueMap.get("genre"))) {
			case Ballad: {
				return mapper.selectMp3List(valueMap);
			}
			case Dance: {
				return mapper.selectMp3List(valueMap);
			}
			case Soul: {
				return mapper.selectMp3SoulList();
			}
			case Rock: {
				return mapper.selectMp3List(valueMap);
			}
			case Rap: {
				valueMap.put("genre", GlobalEnum.Rap.getString());				
				return mapper.selectMp3LikeList(valueMap);
			}
			default:
				return null;
		}
	}
	
	/**
	 *  추천을 위한 모든 MP3 List 받아오기
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public  Map<String, ArrayList<Mp3Vo>> selectMp3RecommandedList(SqlSession session, String id) throws Exception{
		
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		Map<String, ArrayList<Mp3Vo>> mp3Map = new HashMap<String, ArrayList<Mp3Vo>>();
		ArrayList<Mp3Vo> recommandedMp3List = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedBalladList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedDanceList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRockList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRapList = new ArrayList<Mp3Vo>();
		
		recommandedMp3List = mapper.selectMp3RecommandedList(id);
		for (Mp3Vo mp3Vo : recommandedMp3List) {
			if( mp3Vo.getGenre().equals("Rap / Hip-hop")){
				recommandedRapList.add(mp3Vo);
				continue;
			}
			switch (GlobalEnum.valueOf(mp3Vo.getGenre())) {
				case Ballad: {
					recommandedBalladList.add(mp3Vo);
					break;
				}
				case Dance: {
					recommandedDanceList.add(mp3Vo);
					break;
				}
				case Soul: {
					break;
				}
				case Rock: {
					recommandedRockList.add(mp3Vo);
					break;
				}
				case Rap: {
					break;
				}
				default: {
					break;
				}
			}
			
		}
		
		mp3Map.put("Ballad", recommandedBalladList);
		mp3Map.put("Dance", recommandedDanceList);
		mp3Map.put("Rock", recommandedRockList);
		mp3Map.put("Rap", recommandedRapList);
		
		return mp3Map;
	}
	
	public ArrayList<Mp3Vo> selectRecommandMp3v1(SqlSession session, String value) throws Exception{
		
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		return mapper.selectRecommandMp3v1(value);
	}
	
	/**
	 * 1. 토니모토 계수 구하기 
	 * 2. 비교 후 추천받을 song_number 구하기
	 * 3. real Mp3List 구하기
	 * 
	 * @param session
	 * @param userList
	 * @param fsList
	 * @param id 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Mp3Vo> selectRecommandedMp3v1(SqlSession session, ArrayList<UserVo> userList, ArrayList<FavoriteSongVo> fsList, String id) throws Exception{
		
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		FavoriteSongService fsService = new FavoriteSongService();
		ArrayList<FavoriteSongVo> fsList2 = new ArrayList<FavoriteSongVo>();
		ArrayList<FavoriteSongVo> resultFsList = new ArrayList<FavoriteSongVo>();
		CalRecommandation calRecommand = new CalRecommandation();
		
		int m11 = 0;
		int m01 = 0;
		int m10 = 0;
		int tempFs = 0;
		double tempMaxiTani = 0;
		String tempId = null;
		String resultId = null;
		Iterator<UserVo> iter  = userList.iterator();
		StringBuffer strBuffer = new StringBuffer();
		
		while( iter.hasNext() ){
			tempId = iter.next().getId();
			
			if( !tempId.equals(id) ){
				
				fsList2 = fsService.selectFSList(session, tempId);
			
				for( int i = 0; i < fsList.size() ; i++){
					tempFs = fsList.get(i).getFs_1();
					for( int j = 0 ; j < fsList2.size() ; j ++ ){
						if( tempFs == fsList2.get(j).getFs_1() ){
							m11++;
							break;
						}else if( j == fsList2.size()-1 && tempFs != fsList2.get(j).getFs_1() ){
							m10++;
						}
					}
				}
				
				m01 = fsList2.size() - m11;
				
					if( tempMaxiTani < calRecommand.calTanimoto(m11, m10, m01)){
						tempMaxiTani = calRecommand.calTanimoto(m11, m10, m01);
						resultId = tempId;
						resultFsList.clear();
						resultFsList = fsList2;
					}
			}
		}
		
		System.out.println("누구의? " + resultId);
		System.out.println("토니모토 " + tempMaxiTani);
		
		// 걸러서 번호 추출.
		fsList = calRecommand.getRecommandedListV1(tempMaxiTani, resultId, fsList, resultFsList);
		System.out.println(fsList);
		
		for(int i = 0 ; i < resultFsList.size() ; i++){
			strBuffer.append(resultFsList.get(i).getFs_1());
			if( i != resultFsList.size()-1) strBuffer.append(",");
		}
		
		System.out.println(strBuffer.toString());
		return mapper.selectRecommandMp3v1(strBuffer.toString());
	}
	
	/**
	 * 1. v2 추천에 필요한 메타 데이터 얻기
	 * 2. 얻은 메타 데이터 기반으로 추천 리스트 받아오기 
	 * @param session
	 * @param mp3List
	 * @param fsList 
	 * @return 
	 * @throws Exception
	 */
	public ArrayList<Mp3Vo> selectRecommandedMp3v2(SqlSession session,  ArrayList<Mp3Vo> mp3List, ArrayList<FavoriteSongVo> fsList) throws Exception{
		
		HashMap<String, String> valueMap = new HashMap<String, String>();
		ArrayList<Mp3Vo> tempList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> returnList = new ArrayList<Mp3Vo>();
		CalRecommandation recommand  = new CalRecommandation();
		recommand.CalValues(mp3List);
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		String authorName = null;
		String genre = mp3List.get(0).getGenre();
		System.out.println("발라드도? = "+ genre);
		
		valueMap.put("genre", genre);
		valueMap.put("author_gender", GlobalConstant.pop_gender);
		valueMap.put("author_nationality", GlobalConstant.pop_national);
		valueMap.put("author_act", GlobalConstant.pop_act);
		//valueMap.put("author_act", null);
		//valueMap.put("author_act2", "솔로");
		valueMap.put("min_song_date", Integer.toString(GlobalConstant.pop_year - 1));
		valueMap.put("max_song_date", Integer.toString(GlobalConstant.pop_year + 1));
		valueMap.put("min_tempo", Integer.toString(GlobalConstant.min_tempo));
		valueMap.put("max_tempo", Integer.toString(GlobalConstant.max_tempo));
		
		tempList = mapper.selectRecommandMp3v2(valueMap);
		tempList = setRecValue(tempList, fsList);
		
		System.out.println(tempList);
		// 선호 가수 우선 정렬
		for ( Mp3Vo mp3Vo : tempList ) {
			authorName	= mp3Vo.getAuthor().matches(".*,.*") ? mp3Vo.getAuthor().split(",")[0] : mp3Vo.getAuthor() ;			
			if( authorName.equals(GlobalConstant.pop_author) && mp3Vo.getIsRecommanded() == 0){
				returnList.add(0, mp3Vo);
			}else if ( mp3Vo.getIsRecommanded() == 0 )
				returnList.add(mp3Vo);
		}
		return returnList;
	}
	
	/**
	 *  mp3 추천 count 증가
	 */
	public void updateCount(SqlSession session, String song_num) throws Exception{
		
		Mp3Mapper mapper = session.getMapper(Mp3Mapper.class);
		mapper.updateCount(song_num);
		
	}
	

	/**
	 * Mp3Form Setting from File data ( ID3V1 Data)
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public Mp3Form setMp3Info(File file) throws Exception {
		Mp3Form mp3 = new Mp3Form();
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		
		try{
			baseFileFormat = AudioSystem.getAudioFileFormat(file);
			baseFormat = baseFileFormat.getFormat();

			if (baseFileFormat instanceof TAudioFileFormat) {

				Map properties = ((TAudioFileFormat) baseFileFormat).properties();
				String keys[] = { "title", "author", "mp3.id3tag.genre", "album",
									      "date", "copyright" };

				mp3.setTitle((String) properties.get(keys[0]));
				mp3.setAuthor((String) properties.get(keys[1]));
				mp3.setGenre((String) properties.get(keys[2]));
				mp3.setAlbum((String) properties.get(keys[3]));
				mp3.setSong_date( Integer.parseInt(((String) properties.get(keys[4])).substring(0, 4) ));
				mp3.setCopyright((String) properties.get(keys[5]));
				
				// set TEMPO from MP3 files
				BPM2SampleProcessor processor = new BPM2SampleProcessor();
		        processor.setSampleSize(512);
		        EnergyOutputAudioDevice output = new EnergyOutputAudioDevice(processor);
		        output.setAverageLength(1024);
		        Player player = new Player(new FileInputStream(file), output);
		        player.play();
		        System.out.println( "calculated BPM: " + processor.getBPM() );
		        
		        mp3.setTempo(processor.getBPM());
				
				setMp3ExtraInfo(mp3);
				System.out.println(mp3);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		return mp3;
	}
	
	/**
	 *		Mp3Form Setting Extra Data from melon 
	 * @param mp3
	 * @return
	 * @throws Exception
	 */
	public Mp3Form setMp3ExtraInfo(Mp3Form mp3) throws Exception{
		
		ArrayList<String> mp3Set = new ArrayList<String>();	
		String test = URLEncoder.encode(mp3.getAuthor(), "euc-kr");
		System.out.println(mp3.getAuthor());
		System.out.println(test);
		
		try{
			
			String SUrl = "http://www.melon.com/cds/search/web/searchtotalmain_list.htm?menu_id=total&srchSec=&srchSecId=&query="+test;
			Document doc = Jsoup.connect(SUrl).get();
			Elements titles = doc.select(".smalBox dd");
				
			for (Element element : titles) {
					System.out.println(element.text());
					mp3Set.add(element.text());
				}
			
			String[] splitout = mp3Set.get(1).split("/");
			mp3Set.remove(1);
			mp3Set.add(1,splitout[0]);
			mp3Set.add(2,splitout[1]);
			// 멜론에서 파싱한 가수 정보들
			mp3.setAuthor_nationality(mp3Set.get(0));
			mp3.setAuthor_gender(mp3Set.get(1));
			mp3.setAuthor_act(mp3Set.get(2));
			mp3.setAuthor_genre(mp3Set.get(4));
			mp3.setAuthor_similar(mp3Set.get(7));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		for( int i = 0  ; i < mp3Set.size() ; i ++ ){
			mp3Set.remove(i);
		}
		
		return mp3;
	}
	
	
	/**
	 * 추천음악 속성 값 셋팅.
	 * 같으면 1 다르면 0  
	 * @param mp3List
	 * @param fsList
	 * @return
	 */
	public ArrayList<Mp3Vo> setRecValue(ArrayList<Mp3Vo> mp3List, ArrayList<FavoriteSongVo> fsList) {
		
			for (FavoriteSongVo favoriteSongVo : fsList) {
				for( int i = 0; i < mp3List.size() ; i++){
					if( favoriteSongVo.getFs_1() == mp3List.get(i).getSong_number()){
						System.out.println("둘이 같네요");
						mp3List.get(i).setIsRecommanded(1);
					}
				}
			}
		return mp3List;
	}
}
