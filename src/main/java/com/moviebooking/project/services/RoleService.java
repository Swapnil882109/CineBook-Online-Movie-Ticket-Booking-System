package com.moviebooking.project.services;

public interface RoleService {
    String promoteUser(Long userId);

    String demoteUser(Long userId);
}
