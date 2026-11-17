package com.ecomm.productservice.product.messaging.async;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "processed_events")
public class ProcessedEvent {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID eventId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime processedAt;
}
