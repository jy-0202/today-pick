package com.thejoeun.todaypick.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "ai_review_summary",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_ai_review_summary_restaurant",
        columnNames = "restaurant_id"
    )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiReviewSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "restaurant_id",
        nullable = false,
        unique = true
    )
    private Restaurant restaurant;

    @Lob
    @Column(
        name = "summary",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String summary;

    @Column(name = "positive_keywords", length = 500)
    private String positiveKeywords;

    @Column(name = "negative_keywords", length = 500)
    private String negativeKeywords;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AiReviewSummary(
            Restaurant restaurant,
            String summary,
            String positiveKeywords,
            String negativeKeywords) {

        this.restaurant = restaurant;
        this.summary = summary;
        this.positiveKeywords = positiveKeywords;
        this.negativeKeywords = negativeKeywords;
    }

    public void update(
            String summary,
            String positiveKeywords,
            String negativeKeywords) {

        this.summary = summary;
        this.positiveKeywords = positiveKeywords;
        this.negativeKeywords = negativeKeywords;
    }
}