package org.lamisplus.modules.bootstrap.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Table(name = "module")
@EqualsAndHashCode(of = "name", callSuper = false)
@ToString(of = {"id", "name"})
@Slf4j
public class Module implements Serializable, Persistable<String> {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String basePackage;

    private String description;

    private String version;

    private Date buildTime;

    private int status;

    @NotNull
    private Boolean active = true;

    private String artifact;

    private Boolean processConfig = true;

    private Boolean uninstall = false;

    private Boolean started = false;

    @Basic
    @Column(name = "code", updatable = false)
    private String code = UUID.randomUUID().toString();

    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] data;

    @Basic
    @Column(name = "archived")
    private Integer archived = 0;

    @Basic
    @Column(name = "module_type")
    private Integer moduleType = 0;

    /*@Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "created_by")
    private String createdBy;*/

    /*@Basic
    @Column(name = "date_modified")
    @JsonIgnore
    @UpdateTimestamp
    private Timestamp dateModified;

    @Basic
    @Column(name = "modified_by")
    @JsonIgnore
    private String modifiedBy;*/

    @Basic
    @Column(name = "date_installed")
    @UpdateTimestamp
    private Timestamp dateInstalled = Timestamp.from(Instant.now());

    @Basic
    @Column(name = "installed_by")
    @JsonIgnore
    private String installedBy;

    @Override
    public boolean isNew() {
        return id == null;
    }

    public Module copy() {
        Module module = new Module();
        BeanUtils.copyProperties(this, module, "webComponents", "forms",
            "menus", "webRemotes");
        return module;
    }
}
