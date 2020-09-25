package com.enao.team2.quanlynhanvien.seeders;

import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    @Autowired
    private IUserService userService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private GroupConverter groupConverter;

    @Autowired
    private UserConverter userConverter;

//    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedGroupTable();
        seedUsersTable();
    }

    private void seedGroupTable() {
        List<GroupEntity> groups = groupService.findAll();
        if (groups.isEmpty()) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName("group1");
            groupDTO.setDescription("group one");
            groupService.save(groupConverter.toEntity(groupDTO));
        } else {
            logger.trace("Groups Seeding Not Required");
        }
    }

    private void seedUsersTable() {
        List<UserEntity> users = userService.findAll();
        Optional<GroupEntity> groupEntity = groupService.findByName("group1");
        if (users.isEmpty() && groupEntity.isPresent()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("admin@gmail.com");
            userDTO.setFullName("admin");
            userDTO.setUsername("admin");
            String salt = BCrypt.gensalt(12);
            userDTO.setPassword(BCrypt.hashpw("admin", salt));
            UserEntity userEntity = userConverter.toEntity(userDTO);
            userEntity.setGroup(groupEntity.get());
            userService.save(userEntity);
        } else {
            logger.trace("Users Seeding Not Required");
        }
    }
}