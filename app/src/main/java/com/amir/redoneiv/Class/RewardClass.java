package com.amir.redoneiv.Class;

public class RewardClass {
    private String uniqueId,rewardId,rewardName,rewardDescription,pointRequired,validUntilDate,minimumMemberLevelRequired;

    public RewardClass(String uniqueId, String rewardId, String rewardName, String rewardDescription, String pointRequired, String validUntilDate
            , String minimumMemberLevelRequired) {
        this.uniqueId = uniqueId;
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.rewardDescription = rewardDescription;
        this.pointRequired = pointRequired;
        this.validUntilDate = validUntilDate;
        this.minimumMemberLevelRequired = minimumMemberLevelRequired;
    }

    public String getPointRequired() {
        return pointRequired;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public String getRewardId() {
        return rewardId;
    }

    public String getMinimumMemberLevelRequired() {
        return minimumMemberLevelRequired;
    }

    public String getRewardName() {
        return rewardName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getValidUntilDate() {
        return validUntilDate;
    }
}
