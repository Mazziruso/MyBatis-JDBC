package com.CNVZ.MyBatis;

import com.CNVZ.MyBatis.service.UserService;

public class MyBatisTest {
	
	public static void main(String[] args) {
		UserService.insertUser("ZK", "1234", 10.0);
		UserService.insertUser("YX", "4321", 20.0);
		UserService.insertUser("YQ", "1111", 30.0);
		UserService.selectUserById(3);
		//UserService.deleteUser(2);
		UserService.selectAllUsers();
	}

}
