package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.RedisKey;
import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    private RedisTemplate template;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserDetails loadUserByUsername(String name) throws UnauthorizedException {
        UserEntity user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UnauthorizedException(
                        "Login failed with username: " + name
                ));
        UserDetails userDetails = UserDetailsImpl.build(user);
        template.opsForValue().set(RedisKey.USER_DETAIL, userDetails);
        return userDetails;
    }
}
