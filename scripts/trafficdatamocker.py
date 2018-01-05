# -*- coding:utf8 -*-
import kafka
import time
import datetime
import random

brokers = ['nbot18.dg.163.org:9092']
topic = 'traffic'

producer = kafka.KafkaProducer(bootstrap_servers=brokers)

# data schema: time, car plate, speed, longitude, latitude
data = "{},{},{},{},{}"


def gen_car():
    head1, head2, num = ('苏', '沪', '浙'), "ABC", (10001, 10010)
    h1 = head1[random.randint(0, len(head1)-1)]
    h2 = head2[random.randint(0, len(head2)-1)]
    h3 = random.randint(num[0], num[1])
    return '{}{}{}'.format(h1, h2, h3)


def gen_speed():
    return random.randint(0, 80)


def gen_longitude():
    return random.uniform(120, 122)


def gen_latitude():
    return random.uniform(80, 86)


key = 0
while 1:
    dt = datetime.datetime.now().isoformat()
    key += 1
    value = data.format(dt, gen_car(), gen_speed(), gen_longitude(), gen_latitude())
    print(value)
    producer.send(topic, value=value.encode())
    producer.flush()

    time.sleep(1)
