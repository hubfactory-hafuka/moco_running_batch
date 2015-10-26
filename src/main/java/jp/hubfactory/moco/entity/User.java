package jp.hubfactory.moco.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Email;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long userId;
    @Column
    private String token;
    @Column
    private String name;
    @Column
    private Double totalDistance;
    @Column
    private Integer totalCount;
    @Column
    private String totalAvgTime;
    @Column
    private Integer girlId;
    @Email
    @Column
    private String mailAddress;
    @Column
    private Double height;
    @Column
    private Double weight;
    @Column
    private Long point;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginBonusDatetime;
    @Column
    private String profImgPath;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updDatetime;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date insDatetime;

}
