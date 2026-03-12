package com.payvue.supporting.user.command;

import com.payvue.supporting.user.FeaturePreference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * FeaturePreferences - 기능별 설정 슬롯
 */
@Getter
@EqualsAndHashCode
public class FeaturePreferences {

    private final Map<FeatureKey, FeaturePreference> values;

    private FeaturePreferences(Map<FeatureKey, FeaturePreference> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    public static FeaturePreferences empty() {
        return new FeaturePreferences(new HashMap<>());
    }

    public Optional<FeaturePreference> get(FeatureKey key) {
        return Optional.ofNullable(values.get(key));
    }

    public FeaturePreferences apply(FeaturePreference preference) {
        Map<FeatureKey, FeaturePreference> next = new HashMap<>(values);
        next.put(preference.key(), preference);
        return new FeaturePreferences(next);
    }
}
