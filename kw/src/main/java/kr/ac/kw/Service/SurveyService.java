package kr.ac.kw.Service;

import java.util.ArrayList;

import javax.inject.Named;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.ac.kw.Mapper.SurveyMapper;
import kr.ac.kw.Vo.SurveyVo;
import kr.ac.kw.form.SurveyForm;

@Repository
@Named("surveyService")
public class SurveyService {
	
	public void insertSurvey(SqlSession session, SurveyForm surveyForm) throws Exception{
		
		SurveyMapper mapper = session.getMapper(SurveyMapper.class);
		mapper.insertSurvey(surveyForm);
		
	}
	
	public ArrayList<SurveyVo> selectAll(SqlSession session) throws Exception{
		
		SurveyMapper mapper = session.getMapper(SurveyMapper.class);
		return mapper.selectAll();
		
	}

		
}
