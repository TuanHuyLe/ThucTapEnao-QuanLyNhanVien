package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.Constants;
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
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, String type, Pageable pageable) {
        GenericSpecification<UserEntity> genericSpecification = new GenericSpecification<>();
        if ("all".equals(type)) {
            genericSpecification.add(new SearchCriteria(ESearchKey.group.name(), keyword, ESearchOperation.MATCH, "name"));
            genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH, null));
            genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH, null));
            if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                String slugKeyword = slugUtils.slug(keyword);
                genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
            } else {
                genericSpecification.add(new SearchCriteria(ESearchKey.fullName.name(), keyword, ESearchOperation.MATCH, null));
            }
        } else {
            if (ESearchKey.email.name().equals(type)) {
                genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH, null));
            }
            if (ESearchKey.fullName.name().equals(type)) {
                if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                    String slugKeyword = slugUtils.slug(keyword);
                    genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
                } else {
                    genericSpecification.add(new SearchCriteria(ESearchKey.fullName.name(), keyword, ESearchOperation.MATCH, null));
                }
            }
            if (ESearchKey.username.name().equals(type)) {
                genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH, null));
            }
            if (ESearchKey.group.name().equals(type)) {
                genericSpecification.add(new SearchCriteria(ESearchKey.group.name(), keyword, ESearchOperation.MATCH, "name"));
            }
        }

        return userRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
