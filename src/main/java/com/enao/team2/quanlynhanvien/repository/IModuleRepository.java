package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModuleRepository extends JpaRepository<Module, String> {
}
