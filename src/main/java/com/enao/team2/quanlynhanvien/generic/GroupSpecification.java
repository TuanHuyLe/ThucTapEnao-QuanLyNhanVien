package com.enao.team2.quanlynhanvien.generic;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GroupSpecification {
    public Specification<GroupEntity> hasName(String name){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.name.name())), "%" + name.toLowerCase() + "%");
    }

    public Specification<GroupEntity> hasDescription(String description){
        return (root, query, cb) -> cb.like(cb.lower(root.get(ESearchKey.name.name())), "%" + description.toLowerCase() + "%");
    }
}
