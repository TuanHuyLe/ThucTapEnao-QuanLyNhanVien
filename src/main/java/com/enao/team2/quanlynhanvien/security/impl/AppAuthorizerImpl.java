package com.enao.team2.quanlynhanvien.security.impl;

import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.security.IAppAuthorizer;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("appAuthorizer")
public class AppAuthorizerImpl implements IAppAuthorizer {

    private final Logger logger = LoggerFactory.getLogger(AppAuthorizerImpl.class);

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    @Override
    public boolean authorize(Authentication authentication, String permission) {
        try {
            //get user authenticated
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            //check user
            if (user == null) {
                throw new UnauthorizedException(new MessageResponse("User not exists"));
            }
            //get user detail
            UserDetailsImpl userDetails = (UserDetailsImpl) user.getPrincipal();
            //check user authorities
            if (userDetails == null || userDetails.getAuthorities().size() == 0) {
                throw new UnauthorizedException(new MessageResponse("User not exists authorities"));
            }
            //get required permissions
            Optional<PermissionEntity> permissionEntity = permissionService.findByName(permission);
            //check required permissions
            if (!permissionEntity.isPresent()) {
                throw new UnauthorizedException(new MessageResponse("Permissions not exists"));
            }
            //get role name of user from user detail
            List<String> rolesName = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            //create a set roles
            Set<RoleEntity> roleEntities = new HashSet<>();
            //create a set permissions
            Set<PermissionEntity> permissionEntities = new HashSet<>();
            //get roles from role name and append to set roles
            for (String roleName : rolesName) {
                Optional<RoleEntity> role = roleService.findByName(roleName);
                role.ifPresent(roleEntities::add);
            }
            //get permissions from per role in set roles and append to set permissions
            for (RoleEntity role : roleEntities) {
                permissionEntities.addAll(role.getPermissions());
            }
            //check if set permissions contain required permissions
            if (!permissionEntities.contains(permissionEntity.get())){
                throw new UnauthorizedException(new MessageResponse("Forbidden! Not permission"));
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
