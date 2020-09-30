package org.affectedcode.vcs;

import java.util.Set;

/**
 * @author daniil.timashov on 27.07.2020
 */
public interface DiffParser<T extends AffectedCodeUnit> {

    void parse(String diff);

    Set<T> parsedDiff();
}
