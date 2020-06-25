package com.bridgelabz.user.repository;

import com.bridgelabz.user.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permissions, Integer> {
}
