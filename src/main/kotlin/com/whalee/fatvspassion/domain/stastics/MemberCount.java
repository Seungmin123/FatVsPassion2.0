package com.whalee.fatvspassion.domain.stastics;

import jakarta.persistence.*;

@Entity
@Table(name = "mentions")
public class MemberCount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "mention_id", nullable = false)
    private Long mentionId;

    public Long getMentionId() {
        return mentionId;
    }

    public void setMentionId(Long mentionId) {
        this.mentionId = mentionId;
    }
}
