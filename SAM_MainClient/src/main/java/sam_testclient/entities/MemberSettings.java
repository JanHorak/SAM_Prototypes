/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Validateable class for the Membersettings
 * @author janhorak
 */
@Entity
@NamedQueries({
   @NamedQuery (name = "MemberSettings.find",  query = "SELECT s FROM MemberSettings s")
})
public class MemberSettings implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private RecreationEnum recreationType;

    @Min(value = 0)
    @Max(value = 30)
    private int recreationDays;
 
    @NotNull
    @Enumerated(EnumType.STRING)
    private AutoDownload autoDownload;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ValidFor validFor;

    @NotNull
    private String name;

    @NotNull
    private String avatarPath;

    @NotNull
    private boolean allowWebClients;

    @NotNull
    private boolean saveLocaleHistory;
    
    @NotNull
    private String histBorder;
    
    @NotNull
    private String announcementName;
    
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnouncementName() {
        return announcementName;
    }

    public void setAnnouncementName(String announcementName) {
        this.announcementName = announcementName;
    }
    
    
}
