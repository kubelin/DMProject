package kr.ac.kw.Commons;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryManager {
	
	public SqlSessionFactoryManager() {
		// TODO Auto-generated constructor stub
	}
	
/*	public static SqlSession getSqlSession(){
		return sqlSessionFactory.openSession();
	}*/
	
	public static SqlSession getSqlSession(boolean autoCommit){
		return sqlSessionFactory.openSession(autoCommit);
	}
	
	public static SqlSessionFactory getSessionFactory(){
		return sqlSessionFactory;
	}
	
	private static SqlSessionFactory sqlSessionFactory;
	
	static{
		try{
			String resource = "Configuration.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			if(sqlSessionFactory == null){
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
}
