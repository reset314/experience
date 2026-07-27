package com.example.experience.domain.rbac.seed;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.rbac.entity.Permission;
import com.example.experience.domain.rbac.entity.Role;
import com.example.experience.domain.rbac.entity.RolePermission;
import com.example.experience.domain.rbac.repository.PermissionRepository;
import com.example.experience.domain.rbac.repository.RolePermissionRepository;
import com.example.experience.domain.rbac.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RbacSeedService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @PostConstruct
    @Transactional
    public void seed() {
        Permission userRead = ensurePermission("user", "read", "读取用户信息");
        Permission userWrite = ensurePermission("user", "write", "修改用户信息");
        Permission userDelete = ensurePermission("user", "delete", "删除用户");
        Permission userManage = ensurePermission("user", "manage", "管理用户");

        Permission datasourceRead = ensurePermission("datasource", "read", "读取数据源");
        Permission datasourceWrite = ensurePermission("datasource", "write", "修改数据源");
        Permission datasourceManage = ensurePermission("datasource", "manage", "管理数据源");

        Permission eventRead = ensurePermission("event", "read", "读取事件");
        Permission eventWrite = ensurePermission("event", "write", "写入事件");

        Permission syncTrigger = ensurePermission("sync", "trigger", "触发同步");
        Permission syncManage = ensurePermission("sync", "manage", "管理同步");

        Permission platformRead = ensurePermission("platform", "read", "读取平台");
        Permission platformWrite = ensurePermission("platform", "write", "修改平台");
        Permission platformManage = ensurePermission("platform", "manage", "管理平台");

        Permission settingManage = ensurePermission("setting", "manage", "管理系统设置");

        Role superadmin = ensureRole("superadmin", "超级管理员", "拥有所有权限", true);
        Role admin = ensureRole("admin", "管理员", "管理用户、数据源、平台", true);
        Role user = ensureRole("user", "普通用户", "操作自己的数据", true);
        Role auditor = ensureRole("auditor", "审计员", "只读访问", true);

        assignAll(superadmin,
            userRead, userWrite, userDelete, userManage,
            datasourceRead, datasourceWrite, datasourceManage,
            eventRead, eventWrite,
            syncTrigger, syncManage,
            platformRead, platformWrite, platformManage,
            settingManage);

        assign(admin,
            userRead, userWrite,
            datasourceRead, datasourceWrite, datasourceManage,
            eventRead,
            syncTrigger, syncManage,
            platformRead, platformWrite, platformManage);

        assign(user,
            datasourceRead, datasourceWrite,
            eventRead,
            syncTrigger);

        assign(auditor,
            userRead, datasourceRead, eventRead, platformRead, syncManage);
    }

    private Permission ensurePermission(String resource, String action, String description) {
        return permissionRepository.findByResourceAndAction(resource, action)
            .orElseGet(() -> {
                Permission permission = Permission.create(Uuid7Utils.generateUuid7(), resource, action, description);
                return permissionRepository.save(permission);
            });
    }

    private Role ensureRole(String name, String displayName, String description, boolean isSystem) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
                Role role = Role.create(Uuid7Utils.generateUuid7(), name, displayName, description, isSystem);
                return roleRepository.save(role);
            });
    }

    private void assignAll(Role role, Permission... permissions) {
        for (Permission permission : permissions) {
            assign(role, permission);
        }
    }

    private void assign(Role role, Permission... permissions) {
        List<String> existing = rolePermissionRepository.findByRoleId(role.getId()).stream()
            .map(rp -> rp.getPermission().getId())
            .toList();
        for (Permission permission : permissions) {
            if (!existing.contains(permission.getId())) {
                RolePermission rolePermission = RolePermission.create(Uuid7Utils.generateUuid7(), role, permission);
                rolePermissionRepository.save(rolePermission);
            }
        }
    }
}
