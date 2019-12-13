package com.parsa.chatapp.repo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.parsa.chatapp.model.OnlineUser;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUser, String> {

}
