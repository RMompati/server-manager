package com.eroldmr.app.server;

import com.eroldmr.app.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 13:59
 */
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Server {

  @Id
  @SequenceGenerator(
          name = "server_sequence",
          sequenceName = "server_sequence",
          allocationSize = 1
  )
  @GeneratedValue(
          generator = "server_sequence",
          strategy = GenerationType.SEQUENCE
  )
  private Long id;

  @Column( unique = true )
  @NotEmpty( message = "IP Address cannot be empty or null.")
  private String ipAddress;
  private String name;
  private String memory;
  private String type;
  private String imageUrl;
  @Enumerated(EnumType.STRING)
  private Status status;
}
