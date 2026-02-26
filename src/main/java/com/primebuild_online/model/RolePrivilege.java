package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.primebuild_online.model.enumerations.Privileges;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "role_privilege")
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "privilege"})
    private Role role;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "privilege")
    private Privileges privilege;
}
