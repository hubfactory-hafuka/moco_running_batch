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
public class MstRankingReward implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    @Column
    private Integer fromRank;
    @Column
    private Integer toRank;
    @Column
    private Long point;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updDatetime;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date insDatetime;
}
