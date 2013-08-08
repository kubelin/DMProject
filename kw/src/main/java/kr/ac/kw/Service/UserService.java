package kr.ac.kw.Service;

import java.util.ArrayList;

import javax.inject.Named;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.ac.kw.Mapper.UserMapper;
import kr.ac.kw.Vo.UserVo;
import kr.ac.kw.form.UserForm;

@Repository
@Named("userService")
public class UserService {
	
	/**
	 * INSERT USER
	 * @param session
	 * @param userForm
	 * @throws Exception
	 */
	public void insertUser(SqlSession session, UserForm userForm) throws Exception{
		
		UserMapper mapper = session.getMapper(UserMapper.class);
		
		mapper.insertUser(userForm);
		
	};
	
	/**
	 * SEARCH USER
	 * @param session
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 public boolean searchUser(SqlSession session, String id) throws Exception{
		 
		 UserMapper mapper = session.getMapper(UserMapper.class);
		 
		 if( mapper.searchUser(id) != null)
			 return true;
		 else
			 return false;
	 }
	 
	 /**
	  * SELECT ALL USER
	  * @param session
	  * @return
	  * @throws Exception
	  */
	 public  ArrayList<UserVo> searchAllUser(SqlSession session) throws Exception{
		 
		 UserMapper mapper = session.getMapper(UserMapper.class);
		 return mapper.searchAllUser();
	 }
	 
	 /**
	  * 
	  * @param session
	  * @param id
	  * @return
	  * @throws Exception
	  */
	 public UserVo selectUser(SqlSession session,  String id) throws Exception{
		 
		 UserMapper mapper = session.getMapper(UserMapper.class);
		 return mapper.selectUser(id);
		 
	 }
	 
	 /**
	  * 
	  * @param id
	  * @throws Exception
	  */
	 public void updateIsServey(SqlSession session,  String id)throws Exception{
		 
		 UserMapper mapper = session.getMapper(UserMapper.class);
		 mapper.updateIsServey(id);
		 
	 }
	
}
