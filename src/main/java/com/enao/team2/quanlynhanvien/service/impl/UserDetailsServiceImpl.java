package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserDetails loadUserByUsername(String name) throws UnauthorizedException {
        UserEntity user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UnauthorizedException(
                        "Login failed with username: " + name
                ));
        return UserDetailsImpl.build(user);
    }
}
