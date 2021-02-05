package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
    User findByUserId(String userId);
}
