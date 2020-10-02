package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActionRepository extends JpaRepository<Action, String> {
}
