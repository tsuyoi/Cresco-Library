package com.researchworx.cresco.library.utilities;

import com.researchworx.cresco.library.messaging.MsgEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CLogger {
    public enum Level {
        None(-1), Error(0), Warn(1), Info(2), Debug(4), Trace(8);
        private final int level;
        Level(int level) { this.level = level; }
        public int getValue() { return level; }
        public boolean toShow(Level check) { return check.getValue() <= this.getValue(); }
    }
    private String region;
    private String agent;
    private String plugin;
    private Level level;
    private ConcurrentLinkedQueue<MsgEvent> msgOutQueue;

    public CLogger(ConcurrentLinkedQueue<MsgEvent> msgOutQueue, String region, String agent, String plugin) {
        this.region = region;
        this.agent = agent;
        this.plugin = plugin;
        this.level = Level.Info;
        this.msgOutQueue = msgOutQueue;
    }

    public CLogger(ConcurrentLinkedQueue<MsgEvent> msgQueue, String region, String agent, String plugin, Level level) {
        this.region = region;
        this.agent = agent;
        this.plugin = plugin;
        this.level = level;
        this.msgOutQueue = msgQueue;
    }

    public void info(String logMessage) {
        if (!this.level.toShow(Level.Info)) return;
        log(logMessage);
    }

    public void info(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Info)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length) {
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        }
        info(logMessage);
    }

    public void debug(String logMessage) {
        if (!this.level.toShow(Level.Debug)) return;
        log(logMessage);
    }

    public void debug(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Debug)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length) {
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        }
        debug(logMessage);
    }

    public void trace(String logMessage) {
        if (!this.level.toShow(Level.Trace)) return;
        log(logMessage);
    }

    public void trace(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Trace)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length) {
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        }
        trace(logMessage);
    }

    public void log(String logMessage) {
        MsgEvent me = new MsgEvent(MsgEvent.Type.INFO, region, null, null, logMessage);
        me.setParam("src_region", region);
        if (agent != null) {
            me.setParam("src_agent", agent);
            if (plugin != null) {
                me.setParam("src_plugin", plugin);
            }
        }
        me.setParam("dst_region", region);
        this.msgOutQueue.offer(me);
    }

    public void log(MsgEvent me) {
        this.msgOutQueue.offer(me);
    }

    public MsgEvent getLog(String logMessage) {
        MsgEvent me = new MsgEvent(MsgEvent.Type.INFO, region, null, null, logMessage);
        me.setParam("src_region", region);
        if (agent != null) {
            me.setParam("src_agent", agent);
            if (plugin != null) {
                me.setParam("src_plugin", plugin);
            }
        }
        me.setParam("dst_region", region);
        return me;
    }

    public void error(String ErrorMessage, String ... params) {
        if (!this.level.toShow(Level.Error)) return;
        int replaced = 0;
        while (ErrorMessage.contains("{}") && replaced < params.length) {
            ErrorMessage = ErrorMessage.replaceFirst("\\{\\}", params[replaced++]);
        }
        error(ErrorMessage);
    }

    public void error(String ErrorMessage) {
        if (!this.level.toShow(Level.Error)) return;
        MsgEvent ee = new MsgEvent(MsgEvent.Type.ERROR, region, null, null, ErrorMessage);
        ee.setParam("src_region", region);
        if (agent != null) {
            ee.setParam("src_agent", agent);
            if (plugin != null) {
                ee.setParam("src_plugin", plugin);
            }
        }
        ee.setParam("dst_region", region);
        this.msgOutQueue.offer(ee);
    }

    public MsgEvent getError(String ErrorMessage) {
        MsgEvent ee = new MsgEvent(MsgEvent.Type.ERROR, region, null, null, ErrorMessage);
        ee.setParam("src_region", region);
        if (agent != null) {
            ee.setParam("src_agent", agent);
            if (plugin != null) {
                ee.setParam("src_plugin", plugin);
            }
        }
        ee.setParam("dst_region", region);
        return ee;
    }

    public void setLogLevel(Level level) {
        this.level = level;
    }
}