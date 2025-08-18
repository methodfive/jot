package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;

import java.util.Arrays;

public class RewardDestination {
    public enum Type {
        STAKED(0),
        STASH(1),
        CONTROLLER(2),
        ACCOUNT(3);

        private final int tag;

        Type(int tag) {
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }

        public static Type fromTag(int tag) {
            for (Type type : values()) {
                if (type.tag == tag) return type;
            }
            throw new IllegalArgumentException("Unknown RewardDestination tag: " + tag);
        }
    }

    private final Type type;
    private final byte[] account; // only used for ACCOUNT

    private RewardDestination(Type type, byte[] account) {
        this.type = type;
        this.account = account;
    }

    public static RewardDestination staked() {
        return new RewardDestination(Type.STAKED, null);
    }

    public static RewardDestination stash() {
        return new RewardDestination(Type.STASH, null);
    }

    public static RewardDestination controller() {
        return new RewardDestination(Type.CONTROLLER, null);
    }

    public static RewardDestination account(byte[] accountId) {
        if (accountId == null || accountId.length != 32) {
            throw new IllegalArgumentException("AccountId must be 32 bytes");
        }
        return new RewardDestination(Type.ACCOUNT, accountId);
    }

    public byte[] encode() {
        if (type == Type.ACCOUNT) {
            byte[] out = new byte[1 + 32];
            out[0] = (byte) type.getTag();
            System.arraycopy(account, 0, out, 1, 32);
            return out;
        } else {
            return new byte[]{(byte) type.getTag()};
        }
    }

    public static RewardDestination decode(ScaleReader reader) {
        int tag = Byte.toUnsignedInt(reader.readByte());
        Type type = Type.fromTag(tag);

        if (type == Type.ACCOUNT) {
            byte[] account = reader.readBytes(32);
            return account(account);
        } else {
            return switch (type) {
                case STAKED -> staked();
                case STASH -> stash();
                case CONTROLLER -> controller();
                default -> throw new IllegalStateException("Unhandled RewardDestination tag");
            };
        }
    }

    public Type getType() {
        return type;
    }

    public byte[] getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "RewardDestination{" +
                "type=" + type +
                ", account=" + Arrays.toString(account) +
                '}';
    }
}

