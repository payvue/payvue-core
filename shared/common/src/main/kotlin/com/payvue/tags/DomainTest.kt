package com.payvue.tags

import io.kotest.core.Tag

/**
 * 도메인 레이어 테스트를 위한 Tag
 * 
 * 순수한 도메인 로직을 테스트하는 단위 테스트에 사용됩니다.
 * - 외부 의존성 없음 (DB, API 등)
 * - 빠른 실행 속도
 * - 비즈니스 규칙 검증
 * 
 * 사용 예시:
 * ```
 * class CommunityPolicySpec : BehaviorSpec({
 *     tags(DomainTest)
 *     // ...
 * })
 * ```
 */
object DomainTest : Tag()
