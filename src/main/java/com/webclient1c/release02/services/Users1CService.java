package com.webclient1c.release02.services;
import java.util.List;

import com.webclient1c.release02.entities.Users1C;

public interface Users1CService {
	List<Users1C> findUsers();
	Users1C findUserById(Long id);
	Users1C findUserByName(String name);
	void delete(Users1C user1C);
}
