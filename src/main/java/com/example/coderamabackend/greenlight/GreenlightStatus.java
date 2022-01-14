package com.example.coderamabackend.greenlight;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum GreenlightStatus {

    New("greenlight-status.new"),
    PendingToCreate("greenlight-status.pending-to-create"),
    PendingToUpdate("greenlight-status.pending-to-update"),
    PendingToDelete("greenlight-status.pending-to-delete"),
    PendingToRestore("greenlight-status.pending-to-restore"),
    Greenlighted("greenlight-status.greenlighted"),
    Deleted("greenlight-status.deleted");

    @Getter
    private final String i18nKey;

    public static final List<GreenlightStatus> UsableStates = Arrays.asList(Greenlighted, PendingToUpdate, PendingToDelete);

    public boolean isUsable() { return UsableStates.contains(this); }

    GreenlightStatus (String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public GreenlightStatus getGreenlightedState() {
        if (this == PendingToDelete) return Deleted;
        return Greenlighted;
    }

    public GreenlightStatus getPendingState() {
        if (this == New) return PendingToCreate;
        if (this == Deleted) return PendingToRestore;
        return PendingToUpdate;
    }

    public boolean isPending() {
        return
                this == GreenlightStatus.PendingToCreate ||
                        this == GreenlightStatus.PendingToDelete ||
                        this == GreenlightStatus.PendingToUpdate ||
                        this == GreenlightStatus.PendingToRestore;
    }

    public boolean isPendingGreenLight(){
        return Objects.equals(this, GreenlightStatus.PendingToCreate) ||
                Objects.equals(this, GreenlightStatus.PendingToUpdate) ||
                Objects.equals(this, GreenlightStatus.PendingToDelete) ||
                Objects.equals(this, GreenlightStatus.PendingToRestore);
    }

    public boolean isNew() {
        return Objects.equals(this, GreenlightStatus.New);
    }

    public boolean isDeleted() {
        return Objects.equals(this, GreenlightStatus.Deleted);
    }

    public static boolean canEditOrRemove(GreenlightStatus currentStatus) {
        return currentStatus == null || (!currentStatus.isPendingGreenLight() && !currentStatus.isDeleted());
    }

    public static boolean canSentToGreenLight(GreenlightStatus currentStatus) {
        return currentStatus != null && !currentStatus.isPendingGreenLight();
    }

    public static boolean canDiscardUpgrade(GreenlightStatus currentStatus) {
        return currentStatus != null && !currentStatus.isPendingGreenLight();
    }

    public static boolean isPendingGreenLight(GreenlightStatus currentStatus) {
        return  currentStatus != null && currentStatus.isPendingGreenLight();
    }
}
