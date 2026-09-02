package com.example.technicalissuemanager.model;

/**
 * Aggregated counts for the issue list.
 */
public class IssueStatistics {

    private final int totalCount;
    private final int openCount;
    private final int inProgressCount;
    private final int resolvedCount;
    private final int highCount;
    private final int mediumCount;
    private final int lowCount;

    public IssueStatistics(int totalCount, int openCount, int inProgressCount,
                           int resolvedCount, int highCount, int mediumCount,
                           int lowCount) {
        this.totalCount = totalCount;
        this.openCount = openCount;
        this.inProgressCount = inProgressCount;
        this.resolvedCount = resolvedCount;
        this.highCount = highCount;
        this.mediumCount = mediumCount;
        this.lowCount = lowCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getOpenCount() {
        return openCount;
    }

    public int getInProgressCount() {
        return inProgressCount;
    }

    public int getResolvedCount() {
        return resolvedCount;
    }

    public int getHighCount() {
        return highCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public int getLowCount() {
        return lowCount;
    }
}
