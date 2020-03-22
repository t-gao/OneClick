package me.tangni.oneclick.gradleplugin.utils;

import org.gradle.api.Project;

public class Logger {
    private static org.gradle.api.logging.Logger logger;

    public static void init(Project project) {
        logger = project.getLogger();
    }

    public static void i(String info) {
        if (null != info && null != logger) {
            logger.info("OneClick::Gradle Plugin >>> " + info);
        }
    }

    public static void d(String debug) {
        if (null != debug && null != logger) {
            logger.debug("OneClick::Gradle Plugin >>> " + debug);
        }
    }

    public static void e(String error) {
        if (null != error && null != logger) {
            logger.error("OneClick::Gradle Plugin >>> " + error);
        }
    }

    public static void w(String warning) {
        if (null != warning && null != logger) {
            logger.warn("OneClick::Gradle Plugin >>> " + warning);
        }
    }

    public static void lifecycle(String lifecycleMsg) {
        if (null != lifecycleMsg && null != logger) {
            Logger.i("OneClick::Gradle Plugin >>> " + lifecycleMsg);
        }
    }
}
