package com.amir.redoneiv.Class;

public class UsageClass {
    private String title,usageLabel,usagePercent;

    public UsageClass(String title, String usageLabel, String usagePercent) {
        this.title = title;
        this.usageLabel = usageLabel;
        this.usagePercent = usagePercent;
    }

    public String getTitle() {
        return title;
    }

    public String getUsageLabel() {
        return usageLabel;
    }

    public String getUsagePercent() {
        return usagePercent;
    }

}
