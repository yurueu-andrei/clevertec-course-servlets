package ru.clevertec.servlets.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlParser {

    private final Map<String, Object> yaml;

    public YamlParser() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("application.yml");
        this.yaml = yaml.load(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * This method enters every nested level of yaml file and returns the value of the last property,
     * may return null in case of absence of given property
     *
     * @param key value to be found in the form of "example.hello.valueToFind"
     * @return value of the given key
     */
    public String getYamlValue(String key) {
        if (key.contains(".")) {
            String[] args = key.split("\\.");
            LinkedHashMap<?, ?> nestedLevel = null;
            String value = null;
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    nestedLevel = (LinkedHashMap<?, ?>) yaml.get(args[i]);
                    continue;
                }

                if (nestedLevel == null) {
                    return null;
                }

                if (i == args.length - 1) {
                    value = String.valueOf(nestedLevel.get(args[args.length - 1]));
                    break;
                }

                nestedLevel = (LinkedHashMap<?, ?>) nestedLevel.get(args[i]);
            }
            return value;
        }
        return yaml.get(key).toString();
    }
}
