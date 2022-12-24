package com.cooling.hydraulic.model.wx;

import com.cooling.hydraulic.annotation.Query;

public class WxUserQueryCriteria {
    @Query(blurry = "nickName,realName",type = Query.Type.INNER_LIKE)
    private String blurry;

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }
}
