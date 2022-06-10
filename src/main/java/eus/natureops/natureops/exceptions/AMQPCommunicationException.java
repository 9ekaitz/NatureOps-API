package eus.natureops.natureops.exceptions;

public class AMQPCommunicationException extends RuntimeException {
  public AMQPCommunicationException() {
    super("There was an error during the communication with the AMQP server");
  }
}
