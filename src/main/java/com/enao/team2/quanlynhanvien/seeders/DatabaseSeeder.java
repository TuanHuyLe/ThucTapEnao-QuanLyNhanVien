package com.enao.team2.quanlynhanvien.seeders;

import com.enao.team2.quanlynhanvien.model.Module;
import com.enao.team2.quanlynhanvien.model.*;
import com.enao.team2.quanlynhanvien.repository.IActionRepository;
import com.enao.team2.quanlynhanvien.repository.IModuleRepository;
import com.enao.team2.quanlynhanvien.repository.IPermissionRepository;
import com.enao.team2.quanlynhanvien.repository.IRoleRepository;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionRepository permissionRepository;

    @Autowired
    private IActionRepository actionRepository;

    @Autowired
    private IModuleRepository moduleRepository;

    @EventListener
    @Transactional
    public void seed(ContextRefreshedEvent event) {
        try {
            seedActionTable();
            seedModuleTable();
            seedPermissionTable();
            seedRoleTable();
            seedUsersTable();
        } catch (Exception e) {
            logger.trace("Loi : " + e.getMessage());
        }
    }

    public void seedActionTable() {
        if (actionRepository.findAll().isEmpty()) {
            actionRepository.save(new Action("ADD", "Thêm", null));
            actionRepository.save(new Action("EDIT", "Sửa", null));
            actionRepository.save(new Action("REMOVE", "Xóa", null));
            actionRepository.save(new Action("VIEW", "Xem", null));
        } else {
            logger.trace("Actions Seeding Not Required!");
        }
    }

    public void seedModuleTable() {
        if (moduleRepository.findAll().isEmpty()) {
            moduleRepository.save(new Module("USER", "Nhân viên", null));
            moduleRepository.save(new Module("GROUP", "Phòng ban", null));
            moduleRepository.save(new Module("POSITION", "Chức vụ", null));
            moduleRepository.save(new Module("ROLE", "Vai trò", null));
        } else {
            logger.trace("Permission Seeding Not Required!");
        }
    }

    public void seedPermissionTable() {
        if (permissionRepository.findAll().isEmpty()) {
            List<Action> actions = actionRepository.findAll();
            List<Module> modules = moduleRepository.findAll();
            for (Action action : actions) {
                for (Module module : modules) {
                    PermissionEntity permission = new PermissionEntity();
                    permission.setCode(action.getCode() + "_" + module.getCode());
                    permission.setName(action.getName() + " " + module.getName());
                    permission.setAction(action);
                    permission.setModule(module);
                    permissionRepository.save(permission);
                }
            }
        } else {
            logger.trace("Permissions Seeding Not Required!");
        }
    }

    public void seedRoleTable() {
        RoleEntity roleFull = new RoleEntity();
        List<PermissionEntity> allPermission = permissionRepository.findAll();
        roleFull.setId(UUID.randomUUID());
        roleFull.setActive(true);
        roleFull.setName("ROLE_FULL");
        roleFull.setPermissions(new HashSet<>(allPermission));
        roleRepository.save(roleFull);
    }

    public void seedUsersTable() {
        UserEntity admin1 = new UserEntity();
        admin1.setId(UUID.randomUUID());
        admin1.setFullName("Lưu Trường Quốc");
        admin1.setEmail("luutruongquoc@gmail.com");
        admin1.setActive(true);
        admin1.setUsername("admin1");
        admin1.setPassword(new BCryptPasswordEncoder().encode("admin1"));
        RoleEntity roleFUll = roleRepository.findByName("ROLE_FULL").get();
        List<RoleEntity> listRoleOfAdmin1 = new ArrayList<>();
        listRoleOfAdmin1.add(roleFUll);
        admin1.setRoles(new HashSet<>(listRoleOfAdmin1));
        userService.save(admin1);
    }

//    public void seedGroupTable() {
//        List<GroupEntity> groups = groupService.findAll();
//        if (groups.isEmpty()) {
//            GroupDTO groupDTO = new GroupDTO();
//            groupDTO.setName("group1");
//            groupDTO.setDescription("group one");
//            groupService.save(groupConverter.toEntity(groupDTO));
//        } else {
//            logger.trace("Groups Seeding Not Required");
//        }
//    }

}
