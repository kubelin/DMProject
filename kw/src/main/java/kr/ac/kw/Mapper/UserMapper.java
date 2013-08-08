package kr.ac.kw.Mapper;

import java.util.ArrayList;

import kr.ac.kw.Vo.UserVo;
import kr.ac.kw.form.UserForm;

public interface UserMapper {
	
	public abstract void insertUser(UserForm user) throws Exception;
	
	public abstract String searchUser(String id) throws Exception;
	
	public abstract ArrayList<UserVo> searchAllUser() throws Exception;
	
	public abstract UserVo selectUser(String id) throws Exception;
	
	public abstract void updateIsServey(String id)throws Exception;
}
