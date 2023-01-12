package com.eroldmr.app.server;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 14:12
 */
@Service
public interface ServerService {

  Server create(Server server);
  Server ping(String ipAddress) throws IOException;
  Collection<Server> list(int limit);
  Server get(Long id);
  Server update(Server server);
  Boolean delete(Long id);
}
