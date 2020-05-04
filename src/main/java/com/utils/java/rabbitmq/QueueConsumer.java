package com.utils.java.rabbitmq;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class QueueConsumer {

	private static final String SOURCE_QUEUE_NAME = "test.retry.queue.v2";
	private static final String TARGET_QUEUE_NAME = "test.queue.v2";
	private static List<String> typesToIgnore = Arrays.asList("UPDATE_TIMEFRAME");

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		// factory.setHost("localhost");
		//factory.setUri("amqp://<User-Name>:<Password>@<HostName>/VirtualHost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// configure message queues as durable
		boolean durable = true;
		// channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
		System.out.println(" [*] Consumer : waiting for messages. To exit press CTRL+C");
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		boolean autoAck = false;
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(SOURCE_QUEUE_NAME.trim(), autoAck, consumer);
		int messagesToConsume = 0;
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String routeKey = delivery.getEnvelope().getRoutingKey();
			String exchange = delivery.getEnvelope().getExchange();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Consumer : received '" + message + "'");
			publishToTargetQueue1(channel, message, exchange, routeKey);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			messagesToConsume = messagesToConsume+1;
			if(messagesToConsume == 396) { //7222
				break;
			}
			int sourceQCount = channel.queueDeclarePassive(SOURCE_QUEUE_NAME.trim()).getMessageCount();
			if (sourceQCount == 0) {
				break;
			}
		}
		channel.close();
		connection.close();
	}
	
	
	private static void publishToTargetQueue(Channel channel, String message, String exchange, String routeKey)
			throws IOException {
		Customer emsg = convertJson1(message, Customer.class);
				channel.basicPublish("", TARGET_QUEUE_NAME.trim() , MessageProperties.BASIC, message.getBytes());
		System.out.println(" [x] Producer : sent '" + message + "'");
	}
	
	private static void publishToTargetQueue1(Channel channel, String message, String exchange, String routeKey)
			throws IOException {
				channel.basicPublish("", TARGET_QUEUE_NAME.trim() , MessageProperties.BASIC, message.getBytes());
		System.out.println(" [x] Producer : sent '" + message + "'");
	}

	private static void publishToTargetExchange(Channel channel, String message, String exchange, String routeKey)
			throws IOException {
		Customer emsg = convertJson(message, Customer.class);
		// if(typesToIgnore.contains(emsg.getType())) { // check the message attributes to skip publishing
		if(!(emsg.getName().equals("john") && emsg.getDepartment().equals("dept1"))) {
				channel.basicPublish(exchange, routeKey, MessageProperties.BASIC, message.getBytes());
		 }
		System.out.println(" [x] Producer : sent '" + message + "'");
	}

	private static Customer convertJson(String message, Class<Customer> clazz) {
		try {
			return new ObjectMapper().readValue(message, clazz);
		} catch (IOException exception) {
			throw new NonRetryableMessageException(format("Message in wrong format: %s"), exception);
		}
	}
	
	private static Customer convertJson1(String message, Class<Customer> clazz) {
		try {
			return new ObjectMapper().readValue(message, clazz);
		} catch (IOException exception) {
			throw new NonRetryableMessageException(format("Message in wrong format: %s"), exception);
		}
	}

}

