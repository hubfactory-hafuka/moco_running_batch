package jp.hubfactory.moco.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * ユーザーのランキング情報
 */
@Data
public class UserRankingBean implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ユーザーID */
    private Long userId;
    /** ユーザー名 */
    private String name;
    /** 距離 */
    private Double distance;
    /** 順位 */
    private Long rank;
    /** ガールID */
    private Integer girlId;
    /** ランキング年月 */
    private String yearMonth;
    /** プロフィール画像 */
    private String profImgPath;
}
