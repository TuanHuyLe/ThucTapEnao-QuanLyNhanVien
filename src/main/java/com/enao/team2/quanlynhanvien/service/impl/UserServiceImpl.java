package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable) {
        String slugKeyword = slugUtils.slug(keyword);
        GenericSpecification<UserEntity> genericSpecification = new GenericSpecification<>();
//        if (Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(keyword).find()) {
//            genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH));
//        }
//        else if (Constants.VALID_USERNAME_REGEX.matcher(keyword).matches()) {
//            genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH));
//        } else {
        genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH));
        genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH));
        genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH));
//        }
        return userRepository.findAll(genericSpecification, pageable);
    }
}
