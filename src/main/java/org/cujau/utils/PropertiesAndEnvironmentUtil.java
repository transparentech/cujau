package org.cujau.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesAndEnvironmentUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesAndEnvironmentUtil.class);

    public static void logSystemProperties() {
        Properties systemProps = System.getProperties();
        List<String> propList = new ArrayList<>(systemProps.stringPropertyNames());
        Collections.sort(propList);
        LOG.info("System Properties:");
        for (String key : propList) {
            LOG.info("  {} = {}", key, systemProps.getProperty(key));
        }
    }

    public static void logEnvironmentVariables() {
        Map<String, String> env = System.getenv();
        List<String> envList = new ArrayList<>(env.keySet());
        Collections.sort(envList);
        LOG.info("Environment Variables:");
        for (String key : envList) {
            LOG.info("  {} = {}", key, env.get(key));
        }
    }

}
