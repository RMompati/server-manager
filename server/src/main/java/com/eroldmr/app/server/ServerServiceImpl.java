package com.eroldmr.app.server;

import com.eroldmr.app.enumeration.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import static com.eroldmr.app.enumeration.Status.SERVER_DOWN;
import static com.eroldmr.app.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 14:16
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService{

  private final ServerRepository serverRepository;

  @Override
  public Server create(Server server) {
    log.info("Saving new server: {}", server.getName());
    server.setImageUrl(setServerImageUrl());
    return serverRepository.save(server);
  }

  @Override
  public Server ping(String ipAddress) throws IOException {
    log.info("Pinging server IP: {}", ipAddress);
    Server server = serverRepository.findByIpAddress(ipAddress)
            .orElseThrow(() -> new IllegalStateException(format("Server with ip address (%s) not found.", ipAddress)));

    InetAddress address = InetAddress.getByName(ipAddress);
    server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);

    return server;
  }

  @Override
  public Collection<Server> list(int limit) {
    log.info("Getting a list of servers: {}", limit);
    return serverRepository.findAll()
            .stream()
            .limit(limit)
            .collect(Collectors.toSet());
  }

  @Override
  public Server get(Long id) {
    log.info("Getting a server with id: {}", id);
    return serverRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(format("Server with id (%s) not found.", id)));
  }

  @Override
  public Server update(Server server) {
    log.info("Updating server: {}", server.getName());
    return serverRepository.save(server);
  }

  @Override
  public Boolean delete(Long id) {
    log.info("Updating server by id: {}", id);
    serverRepository.deleteById(id);
    return TRUE;
  }

  public String setServerImageUrl() {
    String[] imageNames = { "server1.png", "server2.png", "server3.png", "server4.png" };
    return ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/server/image/" + imageNames[new Random().nextInt(4)])
            .toUriString();
  }
}
