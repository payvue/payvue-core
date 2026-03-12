package com.payvue.supporting.interaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InteractionFlowRegistry {

  private final List<InteractionFlow> modules = new ArrayList<>();

  public FlowBuilder flow(String name) {
    return new FlowBuilder(this, name);
  }

  void register(InteractionFlow module) {
    modules.add(Objects.requireNonNull(module, "module은 필수입니다"));
  }

  public InteractionFlow resolve(Decision<?> decision) {
    return modules.stream()
        .filter(module -> module.supports(decision))
        .findFirst()
        .orElseThrow(() -> new UnsupportedOperationException(
            "조건과 일치하는 인터렉션 모듈이 존재하지 않습니다.: "
                + decision.getClass().getSimpleName()
        ));
  }

  public static final class FlowBuilder {

    private final InteractionFlowRegistry registry;
    private final String name;
    private DomainService<?> domainService;
    private final List<InteractionHandler<?>> handlers = new ArrayList<>();
    private HandlerMatchingPolicy matchingPolicy = HandlerMatchingPolicy.FIRST_MATCH;

    FlowBuilder(InteractionFlowRegistry registry, String name) {
      this.registry = registry;
      this.name = name;
    }

    public FlowBuilder domainService(DomainService<?> domainService) {
      this.domainService = domainService;
      return this;
    }

    public FlowBuilder handler(InteractionHandler<?> handler) {
      if (handler != null) {
        handlers.add(handler);
      }
      return this;
    }

    public FlowBuilder matchingPolicy(HandlerMatchingPolicy matchingPolicy) {
      if (matchingPolicy != null) {
        this.matchingPolicy = matchingPolicy;
      }
      return this;
    }

    public InteractionFlow build() {
      if (domainService == null) {
        throw new IllegalArgumentException("domainService는 필수입니다");
      }
      if (handlers.isEmpty()) {
        throw new IllegalArgumentException("handler는 최소 1개 이상 등록해야 합니다");
      }

      InteractionHandler<?> handler = handlers.size() == 1
          ? handlers.get(0)
          : ChainedEventHandler.builder()
              .addAll(handlers)
              .matchingPolicy(matchingPolicy)
              .build();

      InteractionFlow  flow = new InteractionFlow(name, domainService, handler);
      registry.register(flow);
      return flow;
    }
  }
}
