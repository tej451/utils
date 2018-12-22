package com.utils.java.rabbitmq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQUtil {
	private static final String DEFAULT_EXCHANGE = "";
	private static final String SOURCE_QUEUE_NAME = "source.queue";
	private static String fileName = "/Users/ktejkum//source_file.txt";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		// factory.setHost("localhost");
		factory.setUri("amqp://user:password@localhost/vhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String ccFromFile;
			while ((ccFromFile = bufferedReader.readLine()) != null) {
				// System.out.println("Contents of line:"+ ccFromFile);

				String payload = "{\\\"number\\\":\\\"" + ccFromFile
						+ "\\\",\\\"startDate\\\":\\\"2015-07-01\\\",\\\"endDate\\\":\\\"2020-12-25\\\",\\\"bId\\\":\\\"1\\\",\\\"mId\\\":\\\"1\\\",\\\"cId\\\":\\\"1\\\",\\\"inputTypes\\\":[\\\"TEST\\\"]}";
				String formattedPayload = payload.replace("\\", "");
				String exchange = DEFAULT_EXCHANGE;
				String routeKey = SOURCE_QUEUE_NAME.trim();
				publishToTargetExchange1(channel, formattedPayload, exchange, routeKey);
				// break;

			}
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		channel.close();
		connection.close();
		System.exit(0);
	}

	private static void publishToTargetExchange1(Channel channel, String message, String exchange, String routeKey)
			throws IOException {
		channel.basicPublish(exchange, routeKey.trim(), MessageProperties.BASIC, message.getBytes());
		System.out.println(" [x] Producer : sent '" + message + "'");
	}

}