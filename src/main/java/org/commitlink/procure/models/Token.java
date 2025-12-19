package org.commitlink.procure.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq_id")
  @SequenceGenerator(name = "token_seq_id", sequenceName = "token_seq_id", allocationSize = 1)
  private long id;

  @Column(length = 1000)
  private String accessToken;

  @Column(length = 1000)
  private String refreshToken;
}
