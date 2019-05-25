package com.greensuresun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        for(int i=0;i<1000000;i++){
            logger.info("logback info 成功了");
            logger.error("logback error 成功了");
            logger.debug("logback debug 成功了");
            logger.warn("logback warn 成功了");
            logger.trace("logback trace 成功了");
        }
    }
}
