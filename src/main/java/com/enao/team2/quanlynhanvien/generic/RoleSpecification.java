package com.enao.team2.quanlynhanvien.generic;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RoleSpecification {
    public Specification<RoleEntity> hasName(String name){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.name.name())), "%" + name.toLowerCase() + "%");
    }

    public Specification<RoleEntity> hasDescription(String description){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.description.name())), "%" + description.toLowerCase() + "%");
    }

    public Specification<RoleEntity> hasSlugName(String slugName) {
        return (root, query, cb) -> cb.like(root.get(ESearchKey.slugname.name()), "%" + slugName + "%");
    }

    public Specification<RoleEntity> hasSlugDescription(String hasSlugDescription) {
        return (root, query, cb) -> cb.like(root.get(ESearchKey.slugdescription.name()), "%" + hasSlugDescription + "%");
    }
}
