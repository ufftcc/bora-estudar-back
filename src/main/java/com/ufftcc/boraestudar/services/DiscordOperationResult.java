package com.ufftcc.boraestudar.services;

public class DiscordOperationResult {

    private final Long roleId;
    private final String inviteUrl;

    public DiscordOperationResult(Long roleId, String inviteUrl) {
        this.roleId = roleId;
        this.inviteUrl = inviteUrl;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }
}
