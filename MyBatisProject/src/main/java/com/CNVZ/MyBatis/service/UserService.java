package com.CNVZ.MyBatis.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.CNVZ.MyBatis.entity.User;
import com.CNVZ.MyBatis.mapper.UserMapper;
import com.CNVZ.MyBatis.tools.DBTools;

public class UserService {

	public static void insertUser(String username, String password, double account) {
		SqlSession session = DBTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		User user = new User(username, password, account);
		try {
			mapper.insertUser(user);
			System.out.println(user.toString());
			session.commit();
		} catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}
	
	public static void deleteUser(int id) {
		SqlSession session = DBTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.deleteUser(id);
			session.commit();
		} catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}
	
	public static void selectUserById(int id) {
		SqlSession session = DBTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			User user = mapper.selectUserById(id);
			System.out.println(user);
			session.commit();
		} catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}
	
	public static void selectAllUsers() {
		SqlSession session = DBTools.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<User> users = mapper.selectAllUsers();
			for(User u : users) {
				System.out.println(u);
			}
			session.commit();
		} catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}
}
