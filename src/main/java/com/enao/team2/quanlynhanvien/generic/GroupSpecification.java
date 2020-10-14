package com.enao.team2.quanlynhanvien.generic;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GroupSpecification {
    public Specification<GroupEntity> hasName(String name){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.name.name())), "%" + name.toLowerCase() + "%");
    }

    public Specification<GroupEntity> hasDescription(String description){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.description.name())), "%" + description.toLowerCase() + "%");
    }

    public Specification<GroupEntity> hasSlugName(String slugName) {
        return (root, query, cb) -> cb.like(root.get(ESearchKey.slugname.name()), "%" + slugName + "%");
    }

    public Specification<GroupEntity> hasSlugDescription(String hasSlugDescription) {
        return (root, query, cb) -> cb.like(root.get(ESearchKey.slugdescription.name()), "%" + hasSlugDescription + "%");
    }
}
