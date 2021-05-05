package org.lamisplus.modules.bootstrap.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "module_id")
    private String moduleId;

    @Basic
    @Column(name = "archived")
    @JsonIgnore
    private Integer archived;

    @OneToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    @ToString.Exclude
    private Module moduleByMenu;
}
