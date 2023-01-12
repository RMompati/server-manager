package com.eroldmr.app.api;

import com.eroldmr.app.response.Response;
import com.eroldmr.app.server.Server;
import com.eroldmr.app.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.eroldmr.app.enumeration.Status.SERVER_UP;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

/**
 * @author Mompati 'Patco' Keetile
 * @created 23-11-2022 @ 08:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/server")
public class ServerApi {
  private final ServerService serverService;

  @GetMapping("/list")
  public ResponseEntity<Response> getServers() {
    return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(of("servers", serverService.list(30)))
                    .message("Servers retrieved.")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
    );
  }

  @GetMapping("/ping/{ipAddress}")
  public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
    Server server = serverService.ping(ipAddress);
    return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(of("server", server))
                    .message(server.getStatus().equals(SERVER_UP) ? "Ping success" : "Ping failed")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
    );
  }

  @PostMapping("/save")
  public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
    return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(of("server", serverService.create(server)))
                    .message("Server created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .build()
    );
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<Response> pingServer(@PathVariable("id") Long id) {
    return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(of("server", serverService.get(id)))
                    .message("Server retrieved")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
    );
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
    return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(of("deleted", serverService.delete(id)))
                    .message("Server deleted")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
    );
  }

  @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
  public byte[] getImage(@PathVariable("fileName") String fileName) throws IOException {
    ClassPathResource image = new ClassPathResource(format("/image/%s", fileName));
    return Files.readAllBytes(image.getFile().toPath());
  }
}
