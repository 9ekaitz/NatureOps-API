package eus.natureops.natureops.rabbit;

import java.nio.channels.Channel;

import eus.natureops.natureops.domain.Image;
import eus.natureops.natureops.service.ImageService;

public class RabbitConsumer {
  Channel channel;
  ImageService imageService;
}
