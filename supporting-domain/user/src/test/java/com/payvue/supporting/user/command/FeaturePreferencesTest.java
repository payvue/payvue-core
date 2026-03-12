package com.payvue.supporting.user.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.supporting.user.FeaturePreference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeaturePreferencesTest {

  @Test
  @DisplayName("FeaturePreferences는 같은 키에 대해 최신 값으로 덮어쓴다")
  void applyOverridesExistingPreference() {
    FeaturePreferences prefs = FeaturePreferences.empty()
        .apply(new TestPreference("one"))
        .apply(new TestPreference("two"));

    assertThat(prefs.get(TestPreference.KEY)).isPresent();
    TestPreference value = (TestPreference) prefs.get(TestPreference.KEY).orElseThrow();
    assertThat(value.value()).isEqualTo("two");
  }

  static final class TestPreference implements FeaturePreference {
    static final FeatureKey KEY = FeatureKey.of("TEST_PREF");
    private final String value;

    TestPreference(String value) {
      this.value = value;
    }

    @Override
    public FeatureKey key() {
      return KEY;
    }

    String value() {
      return value;
    }
  }
}
