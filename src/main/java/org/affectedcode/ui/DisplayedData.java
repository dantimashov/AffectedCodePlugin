package org.affectedcode.ui;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author daniil.timashov on 02.08.2020
 */
public class DisplayedData {

    private final Set<Integer> foundIds = new TreeSet<>();
    private final Set<String> foundModules = new HashSet<>();

    public void addFoundId(String id) {
        if (isParametrizedTestId(id)) {
            foundIds.addAll(parseIdsFromParametrizedTests(id));
        } else {
            foundIds.add(Integer.valueOf(id));
        }
    }

    public void addFoundModule(String fileWithIdPath, String projectPath) {
        String module = StringUtils.substringBefore(
                StringUtils.substringAfter(fileWithIdPath, projectPath + "/"), "/src/"
        );
        foundModules.add(module);
    }

    private Set<Integer> parseIdsFromParametrizedTests(String ids) {
        String[] parametrizedIds = ids.replace("{", "").replace("}", "").split(",");
        return Arrays.stream(parametrizedIds).map(id -> Integer.parseInt(id.trim())).collect(Collectors.toSet());
    }

    private boolean isParametrizedTestId(String id) {
        return id.contains("{");
    }

    public String formattedFoundIds() {
        return foundIds.stream()
                       .map(String::valueOf)
                       .collect(Collectors.joining(",", "{", "}"));
    }


    public String formattedFoundModules() {
        return String.join(",", foundModules);
    }

    public int foundIdsNumber() {
        return foundIds.size();
    }
}
