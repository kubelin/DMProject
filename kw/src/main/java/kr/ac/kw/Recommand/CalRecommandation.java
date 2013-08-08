package kr.ac.kw.Recommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kr.ac.kw.Commons.GlobalConstant;
import kr.ac.kw.Service.FavoriteSongService;
import kr.ac.kw.Vo.FavoriteSongVo;
import kr.ac.kw.Vo.Mp3Vo;

import org.springframework.util.CollectionUtils;

public class CalRecommandation {
	public CalRecommandation() {
		// TODO Auto-generated constructor stub
	}
	/*******************************************************************************************************/
	/****		음악 추천 version 1 																																							***/
	/*******************************************************************************************************/
	
	/**
	 * 타니모토 계수 계산
	 * @param tempId
	 * @param m11
	 * @param m10
	 * @param m01
	 * @param tanimotoMap
	 * @return
	 */
	public double calTanimoto(int m11, int m10, int m01) {
		// TODO Auto-generated method stub
		double result = 0 ;
		result = (double)m11/(m01+m10+m11);
		result = (Math.round(result * 100.0)/100.0);
		return result;
	}
	
	/**
	 * 타니모토 계수를 바탕으로 사용자에게 추천할 FavoriteSongVo 추출
	 * @param tempMaxiTani
	 * @param resultId
	 * @param fsList
	 * @param resultFsList
	 */
	public ArrayList<FavoriteSongVo> getRecommandedListV1(double tempMaxiTani, String resultId,
			ArrayList<FavoriteSongVo> fsList,
			ArrayList<FavoriteSongVo> resultFsList) {
			
		for (FavoriteSongVo favoriteSongVo : fsList) {
			for( int i = 0 ; i < resultFsList.size() ; i++){
				if ( favoriteSongVo.getFs_1() == resultFsList.get(i).getFs_1() ){
					resultFsList.remove(i);
				}
			}
		}
		return resultFsList;
	}
	
	public HashMap<String, ArrayList<Mp3Vo>> setMp3ListMapV1(ArrayList<Mp3Vo> resultList){
		
		
		return null;
	}
	
	
	/*******************************************************************************************************/
	/****		음악 추천 version 2 																																							***/
	/*******************************************************************************************************/
	
	/**
	 * 추천된 음악 컨텐츠 메타데이터. 
	 * @param mp3List
	 */
	public void CalValues(ArrayList<Mp3Vo> mp3List){
		/*
		 *  authorList = 가수
		 *  authorMap = 최종. 가수, 빈도수
		 */
		ArrayList<String> authorList = new ArrayList<String>();
		Map<String, Integer> authorMap = new HashMap<String, Integer>();
		int maxAuthorCount = 0 ;
		String tempKey ;
		/*
		 *  연도1990, 1991, 1992~ 2013 
		 */
		int[] dateList = new int[24];
		/*
		 * tempo 추출 및 데이터 이상치 제거를 위한 유효 윈도우 사용.
		 */
		double tempAvg = 0;
		double square = 0;
		ArrayList<Mp3Vo> tempStandardDeviation = new ArrayList<Mp3Vo>();
		
		
		try{
			// ' , ' 가 있는 경우 0번째 배열의 가수가 우선
			// 하나의 어레이에 중복 상관없이 넣기
			for(int i = 0; i < mp3List.size()  ; i++){
				if (mp3List.get(i).getAuthor().matches(".*,.*")) {
					authorList.add(mp3List.get(i).getAuthor().split(",")[0].trim());
					continue;
				}
				authorList.add(i, mp3List.get(i).getAuthor());
				dateList[((mp3List.get(i).getSong_date()) -1990)]++;
			}
			
			// count 체크해서 넣기
			// authorMap에 가수 리스트 넣기 
			for( Mp3Vo mp3 : mp3List){
				int temp = 0;
				for(int i = 0; i < mp3List.size() ; i++){
					if( mp3.getAuthor().equals(authorList.get(i)) || mp3.getAuthor().split(",")[0].trim().equals(authorList.get(i))){
						temp =temp+1;
					}
				}
			// 최종 key value setting
				String authorName = mp3.getAuthor().matches(".*,.*") ? mp3.getAuthor().split(",")[0] : mp3.getAuthor() ;
				authorMap.put(authorName, temp);
			}
			
			// 베스트 템포 시작
						// 평균 구하기
						tempAvg = calAvg(mp3List);
						// 표준편차 구하기
						square = calSD(mp3List, tempAvg);
						System.out.println(square);
						// 이상치 제거 후 값이 null 이 아니면 평균,표준편차를 다시 구한다.
						tempStandardDeviation = removeOutlier(mp3List, tempAvg, square);
						if(tempStandardDeviation!=null){
							tempAvg = calAvg(mp3List);
							square = calSD(mp3List, tempAvg);
						}
						GlobalConstant.min_tempo = (int) (tempAvg - (square*2));
						GlobalConstant.max_tempo = (int) (tempAvg + (square*2));
						
			// 베스트 템포 끝
						
						
			// 베스트 연도 시작
			int max = dateList[0];
			for( int i = 0 ; i  < dateList.length ; i ++){
				if( max <= dateList[i] ){
					max = dateList[i];
					GlobalConstant.pop_year = 1990+i;
				}
			}
			// 베스트 연도 끝
			
			//  베스트 가수 시작 
			// 1 이상일 때만 실행하자.
			maxAuthorCount =  Collections.max(authorMap.values());
			Iterator<String> authorSet = authorMap.keySet().iterator();
			while( authorSet.hasNext() ){
				tempKey = authorSet.next();
				if( maxAuthorCount ==  authorMap.get(tempKey))
					GlobalConstant.pop_author = tempKey;
			}
			//  베스트 가수 끝 
			
			// 베스트 국가 시작
			if( GlobalConstant.count_national_1 >= GlobalConstant.count_national_2 ){
				GlobalConstant.pop_national = "대한민국";
			}else{
				GlobalConstant.pop_national = "미국";
			}
			// 베스트 국가 끝
			
			// 베스트 성별 시작 
			if( GlobalConstant.count_gender_1 >= GlobalConstant.count_gender_2 &&  GlobalConstant.count_gender_1 >= GlobalConstant.count_gender_3){
				GlobalConstant.pop_gender = "남성";
			}else if( GlobalConstant.count_gender_2 >= GlobalConstant.count_gender_1 && GlobalConstant.count_gender_2 >= GlobalConstant.count_gender_3  ){
				GlobalConstant.pop_gender = "여성";
			}else if( GlobalConstant.count_gender_3 >= GlobalConstant.count_gender_1 && GlobalConstant.count_gender_3 >= GlobalConstant.count_gender_3  ){
				GlobalConstant.pop_gender = "혼성";
			}
			// 베스트 성별 끝
			
			// 베스트 활동 시작 
			if( GlobalConstant.author_act_1 >= GlobalConstant.author_act_2 ){
				GlobalConstant.pop_act = "솔로";
			}else{
				GlobalConstant.pop_act = "그룹";
			}
			// 베스트 활동 끝
			
			System.out.println("연도 리스트  = " + CollectionUtils.arrayToList(dateList));
			System.out.println("가수 리스트  = "  + authorList);
			System.out.println("최종 가수 리스트  = " + authorMap);
			System.out.println("가장 많은 연도? " + GlobalConstant.pop_year );
			System.out.println("가장 많은 가수? " + GlobalConstant.pop_author);
			System.out.println("가장 많은 성별? " + GlobalConstant.pop_gender);
			System.out.println("가장 많은 나라? " + GlobalConstant.pop_national);
		//	System.out.println("가장 많은 활동? " + GlobalConstant.pop_act);
			System.out.println("최종 min Temp = " + GlobalConstant.min_tempo);
			System.out.println("최종 max Temp = " + GlobalConstant.max_tempo);
			
		}catch(Exception e){
		    e.printStackTrace();
			System.out.println("Error");
		}
	}

	/**
	 * 평균구하기
	 * @param list
	 * @return
	 */
	public double calAvg(ArrayList<Mp3Vo> list){
		double tempAvg = 0;
		double tempSum = 0;
		for( Mp3Vo mp3 : list){
			tempSum = (tempSum+mp3.getTempo());
			System.out.println("============");
			System.out.println(mp3.getTempo());
			System.out.println("============");
		}
		tempAvg = tempSum / list.size();
		return tempAvg;
	}
	
	/**
	 * 표쥰 편차 구하기
	 * @param list
	 * @param avg
	 * @return
	 */
	public double calSD(ArrayList<Mp3Vo> list, double avg){
		double tempValue = 0;
		double totalTempstandardDeviation = 0;
		double square = 0;
		
		for (Mp3Vo mp3Vo : list) {
			tempValue = mp3Vo.getTempo() - avg;
			totalTempstandardDeviation += Math.pow(tempValue, 2);
		}
		square = Math.sqrt(totalTempstandardDeviation / (list.size() - 1) );
		return square;
	}
	
	/**
	 * 이상치 제거
	 * @param list
	 * @param tempAvg
	 * @param square
	 * @return
	 */
	public ArrayList<Mp3Vo> removeOutlier(ArrayList<Mp3Vo> list, double tempAvg, double square){
		ArrayList<Mp3Vo> tempList = new ArrayList<Mp3Vo>();
		double max = tempAvg+(square*2);
		double min = tempAvg-(square*2);
		
		System.out.println(max );
		System.out.println(min );
		for (Mp3Vo mp3Vo : list) {
			if ( max > (double)mp3Vo.getTempo() && min < (double)mp3Vo.getTempo()  ){
				tempList.add(mp3Vo);				
			}
		}
		
		if( tempList.size() == list.size() ){
			return null;
		}else{
			return tempList;	
		}
	}

	public String countValidation(ArrayList<FavoriteSongVo> fsList) {
		// TODO Auto-generated method stub
		int balCount = 0;
		int danCount = 0;
		int rapCount = 0;
		int rockCount = 0;
		
		for (FavoriteSongVo favoriteSongVo : fsList) {
			if( favoriteSongVo.getGenre().equals("Ballad")){
				balCount++;
			}else if( favoriteSongVo.getGenre().equals("Dance") ){
				danCount++;
			}else if( favoriteSongVo.getGenre().equals("Rock")){
				rockCount++;
			}else if( favoriteSongVo.getGenre().equals("Rap / Hip-hop")){
				rapCount ++;
			}
		}
		
		System.out.println(balCount);
		System.out.println(danCount);
		System.out.println(rapCount);
		System.out.println(rockCount);
		
		if( balCount >= 5 || danCount >= 5 || rockCount >= 5 || rapCount >= 5){
			return "success";
		}else{
			return "fail";
		}
		
	}
}
