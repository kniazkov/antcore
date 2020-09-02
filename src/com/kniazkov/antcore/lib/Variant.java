package com.kniazkov.antcore.lib;

/**
 * Variant data type
 */
public abstract class Variant {

    public static Variant createNull() {
        return Null.getInstance();
    }

    public static Variant createShort(short shortValue) {
        return new ShortWrapper(shortValue);
    }

    public static Variant createInt(int intValue) {
        return new IntWrapper(intValue);
    }

    public abstract boolean isNull();
    public abstract boolean isNumber();

    public abstract boolean isByte();
    public abstract boolean hasByteValue();
    public abstract short byteValue();

    public abstract boolean isShort();
    public abstract boolean hasShortValue();
    public abstract short shortValue();

    public abstract boolean isInt();
    public abstract boolean hasIntValue();
    public abstract int intValue();

    public abstract Variant add(Variant var);

    public static class Null extends Variant {
        private Null() {
        }

        private static Null instance;

        public static Variant getInstance() {
            if (instance == null)
                instance = new Null();
            return instance;
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public boolean isByte() {
            return false;
        }

        @Override
        public boolean hasByteValue() {
            return false;
        }

        @Override
        public short byteValue() {
            return 0;
        }

        @Override
        public boolean isShort() {
            return false;
        }

        @Override
        public boolean hasShortValue() {
            return false;
        }

        @Override
        public short shortValue() {
            return 0;
        }

        @Override
        public boolean isInt() {
            return false;
        }

        @Override
        public boolean hasIntValue() {
            return false;
        }

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public Variant add(Variant var) {
            return this;
        }
    }

    private static class ByteWrapper extends Variant {
        public ByteWrapper(byte value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public boolean isByte() {
            return true;
        }

        @Override
        public boolean hasByteValue() {
            return true;
        }

        @Override
        public short byteValue() {
            return value;
        }

        @Override
        public boolean isShort() {
            return false;
        }

        @Override
        public boolean hasShortValue() {
            return true;
        }

        @Override
        public short shortValue() {
            return value;
        }

        @Override
        public boolean isInt() {
            return false;
        }

        @Override
        public boolean hasIntValue() {
            return true;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public Variant add(Variant var) {
            if (var.isInt())
                return new IntWrapper(value + var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value + var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value + var.byteValue()));
            return null;
        }

        private byte value;
    }

    private static class ShortWrapper extends Variant {
        public ShortWrapper(short value) {
            this.value = value;

        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public boolean isByte() {
            return false;
        }

        @Override
        public boolean hasByteValue() {
            return value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE;
        }

        @Override
        public short byteValue() {
            if (value < Byte.MIN_VALUE) return Byte.MIN_VALUE;
            if (value > Byte.MAX_VALUE) return Byte.MAX_VALUE;
            return (byte)value;
        }

        @Override
        public boolean isShort() {
            return true;
        }

        @Override
        public boolean hasShortValue() {
            return true;
        }

        @Override
        public short shortValue() {
            return value;
        }

        @Override
        public boolean isInt() {
            return false;
        }

        @Override
        public boolean hasIntValue() {
            return true;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public Variant add(Variant var) {
            if (var.isInt())
                return new IntWrapper(value + var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value + var.shortValue()));
            return null;
        }

        private short value;
    }

    private static class IntWrapper extends Variant {
        public IntWrapper(int value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public boolean isByte() {
            return false;
        }

        @Override
        public boolean hasByteValue() {
            return value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE;
        }

        @Override
        public short byteValue() {
            if (value < Byte.MIN_VALUE) return Byte.MIN_VALUE;
            if (value > Byte.MAX_VALUE) return Byte.MAX_VALUE;
            return (byte)value;
        }

        @Override
        public boolean isShort() {
            return false;
        }

        @Override
        public boolean hasShortValue() {
            return value >= Short.MIN_VALUE && value <= Short.MAX_VALUE;
        }

        @Override
        public short shortValue() {
            if (value < Short.MIN_VALUE) return Short.MIN_VALUE;
            if (value > Short.MAX_VALUE) return Short.MAX_VALUE;
            return (short)value;
        }

        @Override
        public boolean isInt() {
            return true;
        }

        @Override
        public boolean hasIntValue() {
            return true;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public Variant add(Variant var) {
            if (var.isNumber())
                return new IntWrapper(value + var.intValue());
            return null;
        }

        private int value;
    }
}
