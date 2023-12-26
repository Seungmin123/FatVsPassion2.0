package com.whalee.fatvspassion.domain.member

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Entity
data class Member (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var role: String,
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<MemberRole>,
    var createdAt: LocalDateTime? = now()
) {
    var modifiedAt: LocalDateTime? = createdAt
}

enum class MemberRole {
   GUEST, LOW, MIDDLE, HIGH, ADMIN
}