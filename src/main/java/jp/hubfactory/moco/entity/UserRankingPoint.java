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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingPoint implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long userId;
    @Column
    private String rankingDate;
    @Column
    private Long rank;
    @Column
    private Long point;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updDatetime;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date insDatetime;

}
