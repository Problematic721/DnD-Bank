package com.example.dndbank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dndbank.model.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	boolean existsByJoinCode(String code);
	Optional<Campaign> findByJoinCode(String joinCode);
	List<Campaign> findByUsersUsername(String username);
}
