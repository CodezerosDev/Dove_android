package com.darpysolutions.dove.Wallet;

public class WalletModel {

    private String privateKey;
    private String publicKey;
    private String ivKey;
    private String salfKey;
    private int type;
    private boolean isActive = false;
    private int walletSequence;

    private String walletName;


    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getWalletSequence() {
        return walletSequence;
    }

    public void setWalletSequence(int walletSequence) {
        this.walletSequence = walletSequence;
    }

    public int getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIvKey() {
        return ivKey;
    }

    public void setIvKey(String ivKey) {
        this.ivKey = ivKey;
    }

    public String getSalfKey() {
        return salfKey;
    }

    public void setSalfKey(String salfKey) {
        this.salfKey = salfKey;
    }
}
