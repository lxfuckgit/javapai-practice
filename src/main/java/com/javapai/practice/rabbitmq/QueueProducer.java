package com.javapai.practice.rabbitmq;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

/**
 * 
 * 功能概要：消息生产者
 * 
 * @author linbingwen
 * @since  2016年1月11日
 */
public class QueueProducer extends EndPoint{
     
    public QueueProducer(String queueName) throws IOException, TimeoutException{
        super(queueName);
    }
 
    public void sendMessageToQueue(Serializable object) throws IOException {
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
    }  
}

