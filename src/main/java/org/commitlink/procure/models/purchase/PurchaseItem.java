package org.commitlink.procure.models.purchase;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class PurchaseItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_id")
  @SequenceGenerator(allocationSize = 1, name = "item_seq_id", sequenceName = "item_seq_id")
  private Long id;

  private String itemName;
  private String description;
  private int quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PrePersist
  public void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
