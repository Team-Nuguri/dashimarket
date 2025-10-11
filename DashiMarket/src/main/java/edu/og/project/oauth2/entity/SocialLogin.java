package edu.og.project.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SOCIAL_LOGIN", 
       uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROVIDER", "SOCIAL_ID"})
       })
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator ="SEQ_SOCIAL_NO" )
    @SequenceGenerator(name="SEQ_SOCIAL_NO", sequenceName = "SEQ_SOCIAL_NO", allocationSize = 1)
    @Column(name = "SOCIAL_LOGIN_NO", nullable = false)
    private Long socialNo;

    @Column(name = "PROVIDER", nullable = false, length = 50)
    private String provider;

    @Column(name = "SOCIAL_ID", nullable = false, length = 100)
    private String socialId;
    
    
    @Column(name = "MEMBER_NO", nullable = false)
    private Long memberNo;

}
