package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primebuild_online.model.enumerations.Privilege;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "role_privilege")
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "build_status")
    private Privilege privilege;
}
