package com.enao.team2.quanlynhanvien.generic;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class UserSpecification {

    /**
     * get user has username
     *
     * @param username
     * @return
     */
    public Specification<UserEntity> hasUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.username.name())), "%" + username.toLowerCase() + "%");
    }

    /**
     * get user has id
     *
     * @param id
     * @return
     */
    public Specification<UserEntity> hasId(UUID id) {
        return (root, query, cb) -> cb.equal(root.get(ESearchKey.id.name()), id);
    }

    /**
     * get user has full name
     *
     * @param fullName
     * @return
     */
    public Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.fullName.name())), "%" + fullName.toLowerCase() + "%");
    }

    public Specification<UserEntity> hasGroupName(String name){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.group.name()).get(ESearchKey.name.name())), "%" + name.toLowerCase() + "%");
    }
    /**
     * get user has email
     *
     * @param email
     * @return
     */
    public Specification<UserEntity> hasEmail(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.email.name())), "%" + email.toLowerCase() + "%");
    }

    /**
     * get user has slug
     *
     * @param slug
     * @return
     */
    public Specification<UserEntity> hasSlug(String slug) {
        return (root, query, cb) -> cb.like(root.get(ESearchKey.slug.name()), "%" + slug + "%");
    }

    /**
     * get user has gender
     *
     * @param gender
     * @return
     */
    public Specification<UserEntity> hasGender(Boolean gender) {
        return (root, query, cb) -> cb.equal(root.get(ESearchKey.gender.name()), gender);
    }
}
