package com.xiaochuan.wang.stormtraffic.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class WordCountSpout implements IRichSpout {
    private FileReader reader;
    private TopologyContext context;
    private SpoutOutputCollector collector;
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {

        try {
            reader = new FileReader(map.get("file").toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        context = topologyContext;
        collector = spoutOutputCollector;
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        String strLine;
        BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            while ((strLine = bufferedReader.readLine()) != null) {
                System.out.println("#################### process line: " + strLine);
                collector.emit(Arrays.asList(strLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void fail(Object o) {
        System.out.println("failed:" + o);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
