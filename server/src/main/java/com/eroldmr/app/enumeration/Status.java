package com.eroldmr.app.enumeration;

import lombok.Getter;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 13:57
 * @since 2022/11/22
 */
@Getter
public enum Status {
  SERVER_UP("SERVER_UP"),
  SERVER_DOWN("SERVER_DOWN");

  private final String status;

  Status(String status) {
    this.status = status;
  }
}