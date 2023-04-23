package io.github.dbstarll.study.entity.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum Page {
    // ENGLISH
    // word(Module.ENGLISH),

    book(Module.ENGLISH),

    exercise_book(Module.ENGLISH, Mode.USER),

    spell(Module.ENGLISH, Mode.USER, Mode.GUEST),

    spell_exchange(Module.ENGLISH, Mode.USER),

    // listen(Module.ENGLISH, Mode.USER),

    // read(Module.ENGLISH, Mode.USER),

    // MATH
    // custom(Module.MATH, Mode.USER),

    // mix(Module.MATH, Mode.USER, Mode.GUEST),

    // SETTING

    user(Module.SETTING);

    // approve(Module.SETTING);

    public final Module module;

    private final Set<Mode> modes;

    private Page(final Module module, final Mode... modes) {
        this.module = module;
        this.modes = new HashSet<>(Arrays.asList(modes));
    }

    public Collection<Mode> modes() {
        return Collections.unmodifiableCollection(modes);
    }

    public boolean mode(Mode mode) {
        return modes.contains(mode);
    }
}
