package com.eroldmr.app.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 14:06
 */
@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

  Optional<Server> findByIpAddress(String ipAddress);
}
