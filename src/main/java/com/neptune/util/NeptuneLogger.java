/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import com.neptune.common.DataHolder;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author ragesh.raveendran
 */
public class NeptuneLogger {

    private static NeptuneLogger instance = null;
    Logger logger = Logger.getLogger("Neptune");
    FileHandler fh;

    protected NeptuneLogger() {
        try {
            fh = new FileHandler(DataHolder.neptuneLoggerFile);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NeptuneLogger getInstance() {
        if (instance == null) {
            instance = new NeptuneLogger();
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
    
}
