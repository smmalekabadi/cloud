package com.cloud.cloud.business.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction",schema = "cloud")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private long profileId;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private long value;
    private long orderId;
    private int statusCode;
    private long refId;
}
