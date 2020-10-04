package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.generic.GenericPageable;
import com.enao.team2.quanlynhanvien.generic.GenericSort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class CriteriaBuilderImpl<T> {
    @PersistenceContext
    private EntityManager manager;

    private CriteriaBuilder cb;
    private CriteriaQuery<T> q;
    private Root<T> root;

    public List<T> builder(
            Class<T> typeParameterClass, String attribute, String type, Object search,
            GenericSort genericSort, GenericPageable genericPageable) {

        cb = manager.getCriteriaBuilder();

        q = cb.createQuery(typeParameterClass);
        root = q.from(typeParameterClass);

        return query(type, attribute, search, genericSort, genericPageable);
    }

    public List<T> query(String type, String attribute, Object search,
                         GenericSort genericSort, GenericPageable genericPageable) {
        switch (type) {
            case "string":
                ParameterExpression<String> p1 = cb.parameter(String.class);
                q.select(root).where(cb.like(root.get(attribute), p1));
                if (genericSort.getAsc()) {
                    q.orderBy(cb.asc(root.get(genericSort.getSortBy())));
                } else {
                    q.orderBy(cb.desc(root.get(genericSort.getSortBy())));
                }
                TypedQuery<T> query1 = manager.createQuery(q)
                        .setFirstResult(genericPageable.firstResult()).setMaxResults(genericPageable.getPageSize());
                query1.setParameter(p1, "%" + search + "%");
                return query1.getResultList();
            case "integer":
                ParameterExpression<Integer> p2 = cb.parameter(Integer.class);
                q.select(root).where(cb.equal(root.get(attribute), p2));
                if (genericSort.getAsc()) {
                    q.orderBy(cb.asc(root.get(genericSort.getSortBy())));
                } else {
                    q.orderBy(cb.desc(root.get(genericSort.getSortBy())));
                }
                TypedQuery<T> query2 = manager.createQuery(q)
                        .setFirstResult(genericPageable.firstResult()).setMaxResults(genericPageable.getPageSize());
                query2.setParameter(p2, (int) search);
                return query2.getResultList();
            case "boolean":
                ParameterExpression<Boolean> p3 = cb.parameter(Boolean.class);
                q.select(root).where(cb.equal(root.get(attribute), p3));
                if (genericSort.getAsc()) {
                    q.orderBy(cb.asc(root.get(genericSort.getSortBy())));
                } else {
                    q.orderBy(cb.desc(root.get(genericSort.getSortBy())));
                }
                TypedQuery<T> query3 = manager.createQuery(q)
                        .setFirstResult(genericPageable.firstResult()).setMaxResults(genericPageable.getPageSize());
                query3.setParameter(p3, (boolean) search);
                return query3.getResultList();
            default:
                return null;
        }
    }
}
