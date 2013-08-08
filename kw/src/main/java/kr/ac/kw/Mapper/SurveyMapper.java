package kr.ac.kw.Mapper;

import java.util.ArrayList;

import kr.ac.kw.Vo.SurveyVo;
import kr.ac.kw.form.SurveyForm;

public interface SurveyMapper {
	
	public abstract void insertSurvey(SurveyForm surveyForm) throws Exception;
	public abstract ArrayList<SurveyVo> selectAll() throws Exception;
	
}
