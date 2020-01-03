package com.cloud.cloud.business.data.repository;

import com.cloud.cloud.business.data.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Profile findByEmail(String email);
}
