package com.method5.jot.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Extrinsic {
    private int version;
    private boolean isSigned;
    private MultiAddress signer;
    private MultiSignature signature;
    private BigInteger tip;
    private BigInteger nonce;
    private int mode;
    private Mortality mortality;
    private String module;
    private String function;
    private List<Object> args;

    public Extrinsic() {
        this.args = new ArrayList<>();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public MultiAddress getSigner() {
        return signer;
    }

    public void setSigner(MultiAddress signer) {
        this.signer = signer;
    }

    public MultiSignature getSignature() {
        return signature;
    }

    public void setSignature(MultiSignature signature) {
        this.signature = signature;
    }

    public BigInteger getTip() {
        return tip;
    }

    public void setTip(BigInteger tip) {
        this.tip = tip;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public Mortality getMortality() {
        return mortality;
    }

    public void setMortality(Mortality mortality) {
        this.mortality = mortality;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Extrinsic{" +
                "version=" + version +
                ", isSigned=" + isSigned +
                ", signer=" + signer +
                ", signature=" + signature +
                ", tip=" + tip +
                ", nonce=" + nonce +
                ", mode=" + mode +
                ", mortality=" + mortality +
                ", module='" + module + '\'' +
                ", function='" + function + '\'' +
                ", args=" + args +
                '}';
    }
}
