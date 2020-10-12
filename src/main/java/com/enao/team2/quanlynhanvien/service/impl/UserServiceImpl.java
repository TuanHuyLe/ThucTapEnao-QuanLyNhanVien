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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Page<UserEntity> findUsersWithPredicate(String keyword, String[] type, Pageable pageable) {
        GenericSpecification<UserEntity> genericSpecification = new GenericSpecification<>();

        for (String t : type) {
            if (ESearchKey.group.name().equals(t)) {
                genericSpecification.add(new SearchCriteria(ESearchKey.group.name(), keyword, ESearchOperation.MATCH, ESearchKey.name.name()));
            } else if (ESearchKey.fullName.name().equals(t)) {
                //tìm kiếm ko dấu thì chuyển có dấu về ko dấu tìm theo slug
                if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                    String slugKeyword = slugUtils.slug(keyword);
                    genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
                } else { // tìm kiếm có dấu thì tìm trực tiếp theo full name
                    genericSpecification.add(new SearchCriteria(ESearchKey.fullName.name(), keyword, ESearchOperation.MATCH, null));
                }
//            } else if (ESearchKey.gender.name().equals(t)) {
//                genericSpecification.add(new SearchCriteria(t, Boolean.valueOf(keyword), ESearchOperation.EQUAL, null));
            } else {
                genericSpecification.add(new SearchCriteria(t, keyword, ESearchOperation.MATCH, null));
            }
        }

        return userRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable) {
        GenericSpecification<UserEntity> genericSpecification = new GenericSpecification<>();

        genericSpecification.add(new SearchCriteria(ESearchKey.group.name(), keyword, ESearchOperation.MATCH, ESearchKey.name.name()));
        genericSpecification.add(new SearchCriteria(ESearchKey.email.name(), keyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.username.name(), keyword, ESearchOperation.MATCH, null));
        //tim kiem khong dau
        if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
            String slugKeyword = slugUtils.slug(keyword);
            genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
        } else {
            genericSpecification.add(new SearchCriteria(ESearchKey.fullName.name(), keyword, ESearchOperation.MATCH, null));
        }
        return userRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity deleteSoftById(UserEntity dataDelete) {
        dataDelete.setActive(false);
        userRepository.save(dataDelete);
        return dataDelete;
    }

}
