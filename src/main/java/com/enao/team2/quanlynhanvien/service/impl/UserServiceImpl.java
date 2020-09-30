package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private SlugUtils slugUtils;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "users")
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void flushCache() {
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    @Cacheable(cacheNames = "users")
    public Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable) {
        String slugKeyword = slugUtils.slug(keyword);
        GenericSpecification<UserEntity> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria("group", keyword, ESearchOperation.MATCH, "name"));
        genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH, null));
        return userRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
