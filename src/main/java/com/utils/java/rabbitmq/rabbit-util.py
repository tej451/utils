#!/usr/bin/env python
import pika
import argparse


parser = argparse.ArgumentParser()# Add an argument
parser.add_argument('--host', type=str, required=True)# Parse the argument
parser.add_argument('--exchange', type=str, default='')# Parse the argument
parser.add_argument('--queue', type=str, required=True)# Parse the argument
args = parser.parse_args()# Print "Hello" + the user input argument
print('Hello,', args)


credentials = pika.PlainCredentials('guest', 'guest')
parameters = pika.ConnectionParameters(args.host,
                                   5672,
                                   'vhost-name',
                                   credentials)

connection = pika.BlockingConnection(parameters)

#connection = pika.BlockingConnection(pika.ConnectionParameters(args.host))
channel = connection.channel()
channel.queue_declare(queue=args.queue)
channel.basic_publish(exchange=args.exchange,
                      routing_key=args.queue,
                      body='Hello World !')
print(" [x] Sent 'Hello World!'")
connection.close()

