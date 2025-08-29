package com.method5.jot.entity;

import com.method5.jot.wallet.SS58;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.util.HexUtil;

/**
 * MultiAddress — class for multi address in the Jot SDK. Provides types and data models; SS58
 * address encoding/decoding.
 */
public class MultiAddress {
    private String address;

    public MultiAddress(byte[] publicKey) {
        this.address = HexUtil.bytesToHex(publicKey);
    }

    public static MultiAddress fromSS58(String ss58Address) {
        return new MultiAddress(SS58.decode(ss58Address));
    }

    public static MultiAddress fromAccountId(AccountId accountId) {
        return new MultiAddress(accountId.getPublicKey());
    }

    public MultiAddress() {
    }

    public byte[] encode() {
        byte[] addressBytes = HexUtil.hexToBytes(address);
        if (addressBytes.length != 32) {
            throw new IllegalArgumentException("Key must be 32 bytes");
        }

        byte[] result = new byte[33];
        result[0] = (byte)0;
        System.arraycopy(addressBytes, 0, result, 1, 32);
        return result;
    }

    public static MultiAddress decode(ScaleReader reader) {
        MultiAddress multiAddress = new MultiAddress();
        byte type = reader.readByte();
        if(type != 0) {
            throw new UnsupportedOperationException("Only pub keys supported for address types");
        }
        multiAddress.setAddress(HexUtil.bytesToHex(reader.readBytes(32)));
        return multiAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "MultiAddress{" +
                "address='" + address + '\'' +
                '}';
    }
}
