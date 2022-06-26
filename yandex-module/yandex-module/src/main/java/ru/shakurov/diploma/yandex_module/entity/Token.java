package ru.shakurov.diploma.yandex_module.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "user")
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_token_id_seq")
    @SequenceGenerator(name = "token_token_id_seq", sequenceName = "token_token_id_seq", allocationSize = 1)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "disk_type")
    @Enumerated(EnumType.STRING)
    private DiskType diskType;
}
