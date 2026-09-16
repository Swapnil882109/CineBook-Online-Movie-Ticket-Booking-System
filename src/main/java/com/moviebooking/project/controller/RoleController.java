package com.moviebooking.project.controller;


import com.moviebooking.project.security.services.AuthService;
import com.moviebooking.project.services.RoleService;
import com.moviebooking.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin/promoteUser/{userId}")
    public ResponseEntity<?> makeUserTheatreManager(@PathVariable Long userId) {
        String promoted=roleService.promoteUser(userId);
        return ResponseEntity.ok(promoted);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin/demoteUser/{userId}")
    public ResponseEntity<?> removeTheatreManger(@PathVariable Long userId) {
        String promoted=roleService.demoteUser(userId);
        return ResponseEntity.ok().body(promoted);
    }
}
