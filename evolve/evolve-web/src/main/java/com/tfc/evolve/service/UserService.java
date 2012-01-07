/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户账号相关功能
 * 
 * @author taofucheng
 */
@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 这里需要改成真正的用户查询！
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("管理员");
        List<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
        auths.add(auth);
        User user = new User("admin", "123456a", true, true, false, true, auths);
        return user;
    }

}
