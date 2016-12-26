package mian;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.pojo.Cousmer;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String resource = "mybatis-config.xml";
		InputStream is = App.class.getClassLoader().getResourceAsStream(resource);
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		
		SqlSession session = sessionFactory.openSession();
		
		String statement = "dao.mapper.CousmerDao.getUser";
		
		List<Cousmer> users = session.selectList(statement);
		for(Cousmer user:users){
			System.out.println(user);
		}
		session.close();
	}

}
