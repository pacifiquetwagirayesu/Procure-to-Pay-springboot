package org.commitlink.procure.exceptions;

public class InvalidInputException extends BadRequestException {
  public InvalidInputException() {}

  public InvalidInputException(String message) {
    super(message);
  }
}
