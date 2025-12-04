package com.continuehub.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.continuehub.server.dto.LoginRequest;
import com.continuehub.server.dto.LoginResponse;
import com.continuehub.server.dto.RefreshRequest;
import com.continuehub.server.dto.RefreshResponse;
import com.continuehub.server.entity.OrgEntity;
import com.continuehub.server.entity.UserEntity;
import com.continuehub.server.entity.UserOrgEntity;
import com.continuehub.server.repository.OrgMapper;
import com.continuehub.server.repository.UserMapper;
import com.continuehub.server.repository.UserOrgMapper;
import com.continuehub.server.security.JwtService;
import com.continuehub.server.security.AuthContext;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")
@Validated
public class AuthController {

    private final UserMapper userMapper;
    private final OrgMapper orgMapper;
    private final UserOrgMapper userOrgMapper;
    private final JwtService jwtService;

    public AuthController(UserMapper userMapper, OrgMapper orgMapper, UserOrgMapper userOrgMapper, JwtService jwtService) {
        this.userMapper = userMapper;
        this.orgMapper = orgMapper;
        this.userOrgMapper = userOrgMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("login:"+request.getUsername());
        // 临时简化登录：密码必须为 123456
        if (!"123456".equals(request.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, request.getUsername())
        );
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        OrgEntity org = orgMapper.selectOne(new LambdaQueryWrapper<OrgEntity>().eq(OrgEntity::getOrgSlug, request.getOrgSlug()));
        if (org == null) {
            return ResponseEntity.status(403).build();
        }

        Optional<UserOrgEntity> membership = Optional.ofNullable(
                userOrgMapper.selectOne(
                        new LambdaQueryWrapper<UserOrgEntity>()
                                .eq(UserOrgEntity::getUserId, user.getId())
                                .eq(UserOrgEntity::getOrgId, org.getId())
                )
        );
        if (membership.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        String token = jwtService.generateToken(user.getUsername(), org.getOrgSlug());
        return ResponseEntity.ok(new LoginResponse(token, org.getOrgSlug()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthContext ctx;
        System.out.println("refresh:"+request.getRefreshToken());
        try {
            ctx = jwtService.parse(request.getRefreshToken());
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, ctx.getUsername())
        );
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        OrgEntity org = orgMapper.selectOne(
                new LambdaQueryWrapper<OrgEntity>().eq(OrgEntity::getOrgSlug, ctx.getOrgSlug())
        );
        if (org == null) {
            return ResponseEntity.status(403).build();
        }

        Optional<UserOrgEntity> membership = Optional.ofNullable(
                userOrgMapper.selectOne(
                        new LambdaQueryWrapper<UserOrgEntity>()
                                .eq(UserOrgEntity::getUserId, user.getId())
                                .eq(UserOrgEntity::getOrgId, org.getId())
                )
        );
        if (membership.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        String newAccessToken = jwtService.generateToken(user.getUsername(), org.getOrgSlug());
        String newRefreshToken = jwtService.generateToken(user.getUsername(), org.getOrgSlug());
        RefreshResponse.User respUser = new RefreshResponse.User(
                user.getDisplayName() != null ? user.getDisplayName() : user.getUsername(),
                "",
                user.getUsername()
        );
        RefreshResponse resp = new RefreshResponse(newAccessToken, newRefreshToken, respUser);
        System.out.println(resp.toString());
        return ResponseEntity.ok(resp);
    }
}
