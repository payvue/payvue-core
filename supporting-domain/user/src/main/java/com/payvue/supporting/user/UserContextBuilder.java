package com.payvue.supporting.user;

import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.UserId;
import com.payvue.supporting.user.command.UserRepository;
import com.payvue.supporting.user.query.UserQueryRepository;

import java.util.Objects;
import java.util.function.Consumer;

class UserContextBuilder {

  private final CommandSpec commandSpec = new CommandSpec();
  private final QuerySpec querySpec = new QuerySpec();
  private UserViewDelegator delegator = new DefaultUserViewDelegator();

  public static UserContextBuilder create() {
    return new UserContextBuilder();
  }

  public UserContextBuilder command(Consumer<CommandSpec> consumer) {
    consumer.accept(commandSpec);
    return this;
  }

  public UserContextBuilder query(Consumer<QuerySpec> consumer) {
    consumer.accept(querySpec);
    return this;
  }

  public UserContextBuilder delegator(UserViewDelegator delegator) {
    if (delegator != null) {
      this.delegator = delegator;
    }
    return this;
  }

  public UserContextManager build() {
    return new DefaultUserContextManager(
        commandSpec.build(),
        querySpec.build(),
        delegator
    );
  }

  public static class CommandSpec {
    private UserRepository repository;
    private final java.util.Map<String, CommandPolicy> policiesByType = new java.util.HashMap<>();
    private final java.util.Map<String, FeatureDefaultsRegistry> featureDefaultsByType =
        new java.util.HashMap<>();
    private UserLookup<?> lookup;

    public CommandSpec repository(UserRepository repository) {
      this.repository = repository;
      return this;
    }

    public CommandSpec lookup(UserLookup<?> lookup) {
      this.lookup = lookup;
      return this;
    }

    public ContextSpec context(String type) {
      return new ContextSpec(this, type);
    }

    UserCommandContext build() {
      Objects.requireNonNull(repository, "userRepository is required");
      return new UserCommandContext(repository, policiesByType, featureDefaultsByType, lookup);
    }
  }

  public static class QuerySpec {
    private UserQueryRepository repository;
    private UserLookup<?> lookup;

    public QuerySpec repository(UserQueryRepository repository) {
      this.repository = repository;
      return this;
    }

    public QuerySpec lookup(UserLookup<?> lookup) {
      this.lookup = lookup;
      return this;
    }

    UserQueryContext build() {
      Objects.requireNonNull(repository, "userQueryRepository is required");
      return new UserQueryContext(repository, lookup);
    }
  }

  private static final class DefaultUserContextManager implements UserContextManager {
    private final UserCommandContext commandContext;
    private final UserQueryContext queryContext;
    private final UserViewDelegator delegator;

    private DefaultUserContextManager(
        UserCommandContext commandContext,
        UserQueryContext queryContext,
        UserViewDelegator delegator
    ) {
      this.commandContext = commandContext;
      this.queryContext = queryContext;
      this.delegator = delegator == null ? new DefaultUserViewDelegator() : delegator;
    }

    @Override
    public void save(User user, UserCommand command) {
      if (user == null) {
        throw new IllegalArgumentException("user is required");
      }
      if (command != null) {

        CommandPolicy policy = commandContext.policy(command.type());
        if (policy != null) {

          if (policy.validate(command)) {
            FeatureDefaultsRegistry defaults = commandContext.featureDefaults(command.type());
            if (defaults != null) {
              defaults.applyTo(user);
            }
            commandContext.repository().save(user);
          }
        }
      }
    }

    @Override
    public <T> java.util.Optional<T> findById(UserId id, Class<T> viewType) {
      if (id == null) {
        return java.util.Optional.empty();
      }
      return find(new UserLookupKey<UserId>() {
        @Override
        public Class<UserId> type() {
          return UserId.class;
        }

        @Override
        public UserId value() {
          return id;
        }
      }, viewType);
    }

    @Override
    public <T> java.util.Optional<T> findByEmail(String email, Class<T> viewType) {
      if (email == null || email.isBlank()) {
        return java.util.Optional.empty();
      }
      return find(new UserLookupKey<String>() {
        @Override
        public Class<String> type() {
          return String.class;
        }

        @Override
        public String value() {
          return email;
        }
      }, viewType);
    }

    private <T> java.util.Optional<T> find(UserLookupKey<?> key, Class<T> viewType) {
      if (key == null || viewType == null) {
        return java.util.Optional.empty();
      }
      UserLookup<?> lookup = User.class.equals(viewType)
          ? commandContext.lookup()
          : queryContext.lookup();
      if (lookup == null) {
        return java.util.Optional.empty();
      }
      return java.util.Optional.ofNullable(delegator.resolve(lookup, key, viewType));
    }
  }

  public static final class ContextSpec {
    private final CommandSpec parent;
    private final String type;

    private ContextSpec(CommandSpec parent, String type) {
      this.parent = parent;
      this.type = type;
    }

    public ContextSpec policy(CommandPolicy policy) {
      if (type != null && !type.isBlank() && policy != null) {
        parent.policiesByType.put(type, policy);
      }
      return this;
    }

    public ContextSpec features(FeatureDefaultsRegistry defaults) {
      if (type != null && !type.isBlank() && defaults != null) {
        parent.featureDefaultsByType.put(type, defaults);
      }
      return this;
    }

    public ContextSpec lookup(UserLookup<?> lookup) {
      parent.lookup(lookup);
      return this;
    }

    public ContextSpec context(String type) {
      return parent.context(type);
    }

    public CommandSpec end() {
      return parent;
    }
  }
}
