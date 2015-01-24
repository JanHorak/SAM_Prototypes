/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Validateable class for the Membersettings
 *
 * @author janhorak
 */
public class MemberSettings {

    @NotNull
    private RecreationEnum recreationType;

    @Min(value = 0)
    @Max(value = 30)
    private int recreationDays;

    @NotNull
    private AutoDownload autoDownload;

    @NotNull
    private ValidFor validFor;

    @NotNull
    private String name;

    @NotNull
    private String avatarPath;

    private boolean allowWebClients;

    private boolean saveLocaleHistory;

    @NotNull
    private String histBorder;

    public String getHistBorder() {
        return histBorder;
    }

    public void setHistBorder(String histBorder) {
        this.histBorder = histBorder;
    }

    public enum RecreationEnum {

        AT_LOGIN, BY_DAYS, AT_SERVER
    }

    public enum AutoDownload {

        YES, ASK
    }

    public enum ValidFor {

        WLAN, MOBILE
    }

    public RecreationEnum getRecreationType() {
        return recreationType;
    }

    public void setRecreationType(RecreationEnum recreationType) {
        this.recreationType = recreationType;
    }

    public int getRecreationDays() {
        return recreationDays;
    }

    public void setRecreationDays(int recreationDays) {
        this.recreationDays = recreationDays;
    }

    public AutoDownload getAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(AutoDownload autoDownload) {
        this.autoDownload = autoDownload;
    }

    public ValidFor getValidFor() {
        return validFor;
    }

    public void setValidFor(ValidFor validFor) {
        this.validFor = validFor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public boolean isAllowWebClients() {
        return allowWebClients;
    }

    public void setAllowWebClients(boolean allowWebClients) {
        this.allowWebClients = allowWebClients;
    }

    public boolean isSaveLocaleHistory() {
        return saveLocaleHistory;
    }

    public void setSaveLocaleHistory(boolean saveLocaleHistory) {
        this.saveLocaleHistory = saveLocaleHistory;
    }

}
