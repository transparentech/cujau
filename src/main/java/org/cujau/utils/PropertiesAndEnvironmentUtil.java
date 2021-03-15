package org.cujau.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesAndEnvironmentUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesAndEnvironmentUtil.class);

    public static void logClassLoaderUrls() {
        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        LOG.info("ClassLoader URLs:");
        for (URL url : classLoader.getURLs()) {
            LOG.info("  {}", url.toString());
        }
    }

    public static void logSystemProperties(String... excludes) {
        Set<String> excluded = new HashSet<>(Arrays.asList(excludes));
        Properties systemProps = System.getProperties();
        List<String> propList = new ArrayList<>(systemProps.stringPropertyNames());
        Collections.sort(propList);
        LOG.info("System Properties:");
        for (String key : propList) {
            if (!excluded.contains(key)) {
                LOG.info("  {} = {}", key, systemProps.getProperty(key));
            }
        }
    }

    public static void logEnvironmentVariables(String... excludes) {
        Set<String> excluded = new HashSet<>(Arrays.asList(excludes));
        Map<String, String> env = System.getenv();
        List<String> envList = new ArrayList<>(env.keySet());
        Collections.sort(envList);
        LOG.info("Environment Variables:");
        for (String key : envList) {
            if (!excluded.contains(key)) {
                LOG.info("  {} = {}", key, env.get(key));
            }
        }
    }

}
