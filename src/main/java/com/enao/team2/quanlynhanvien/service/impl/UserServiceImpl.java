package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.generic.UserSpecification;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Autowired
    private UserSpecification userSpecification;

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
            } else {
                genericSpecification.add(new SearchCriteria(t, keyword, ESearchOperation.MATCH, null));
            }
        }

        return userRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, String[] type, Pageable pageable, String groupName) {
        Specification<UserEntity> specification = Specification
                .where(userSpecification.hasUsername(keyword))
                .or(userSpecification.hasEmail(keyword));
        for (String t : type) {
            if (ESearchKey.fullName.name().equals(t)) {
                //tìm kiếm ko dấu thì chuyển có dấu về ko dấu tìm theo slug
                if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                    specification = specification.or(userSpecification.hasSlug(keyword)).and(userSpecification.hasGroupName(groupName));
                } else { // tìm kiếm có dấu thì tìm trực tiếp theo full name
                    specification = specification.or(userSpecification.hasFullName(keyword)).and(userSpecification.hasGroupName(groupName));
                }
            }
            if (ESearchKey.username.name().equals(t)) {
                specification = specification.or(userSpecification.hasUsername(keyword)).and(userSpecification.hasGroupName(groupName));
            }
            if (ESearchKey.email.name().equals(t)) {
                specification = specification.or(userSpecification.hasEmail(keyword)).and(userSpecification.hasGroupName(groupName));
            }
            else{
                if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                    specification = specification.or(userSpecification.hasSlug(keyword)).and(userSpecification.hasGroupName(groupName));
                } else { // tìm kiếm có dấu thì tìm trực tiếp theo full name
                    specification = specification.or(userSpecification.hasFullName(keyword)).and(userSpecification.hasGroupName(groupName));
                }
            }
        }
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable) {
        Specification<UserEntity> specification = Specification
                .where(userSpecification.hasUsername(keyword))
                .or(userSpecification.hasEmail(keyword));
        //tìm kiếm ko dấu thì chuyển có dấu về ko dấu tìm theo slug
        if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
            String slugKeyword = slugUtils.slug(keyword);
            specification = specification.or(userSpecification.hasSlug(slugKeyword));
        } else { // tìm kiếm có dấu thì tìm trực tiếp theo full name
            specification = specification.or(userSpecification.hasFullName(keyword));
        }

        return userRepository.findAll(specification, pageable);
    }

    @Override
    public Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable, String groupName) {
        Specification<UserEntity> specification = Specification
                .where(userSpecification.hasUsername(keyword))
                .or(userSpecification.hasEmail(keyword));
        //tìm kiếm ko dấu thì chuyển có dấu về ko dấu tìm theo slug
        if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
            String slugKeyword = slugUtils.slug(keyword);
            specification = specification.or(userSpecification.hasSlug(slugKeyword)).and(userSpecification.hasGroupName(groupName));
        } else { // tìm kiếm có dấu thì tìm trực tiếp theo full name
            specification = specification.or(userSpecification.hasFullName(keyword)).and(userSpecification.hasGroupName(groupName));
        }

        return userRepository.findAll(specification, pageable);
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

    @Override
    public Page<UserEntity> findByGroupName(String name, Pageable pageable) {
        return userRepository.findByGroupName(name, pageable);
    }
}
