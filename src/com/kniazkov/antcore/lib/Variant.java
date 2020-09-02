package com.kniazkov.antcore.lib;

/**
 * Variant data type
 */
public abstract class Variant {

    public static Variant createNull() {
        return Null.getInstance();
    }

    public static Variant createBoolean(boolean booleanValue) {
        return new BooleanWrapper(booleanValue);
    }

    public static Variant createByte(byte byteValue) {
        return new ByteWrapper(byteValue);
    }

    public static Variant createShort(short shortValue) {
        return new ShortWrapper(shortValue);
    }

    public static Variant createInt(int intValue) {
        return new IntWrapper(intValue);
    }

    public static Variant createLong(long longValue) {
        return new LongWrapper(longValue);
    }

    public static Variant createReal(FixedPoint realValue) {
        return new RealWrapper(realValue);
    }

    public static Variant createString(String stringValue) {
        return new StringWrapper(stringValue);
    }

    public abstract boolean isNull();
    public abstract boolean isNumber();

    public abstract boolean isBoolean();
    public abstract boolean hasBooleanValue();
    public abstract boolean booleanValue();

    public abstract boolean isByte();
    public abstract boolean hasByteValue();
    public abstract byte byteValue();

    public abstract boolean isShort();
    public abstract boolean hasShortValue();
    public abstract short shortValue();

    public abstract boolean isInt();
    public abstract boolean hasIntValue();
    public abstract int intValue();

    public abstract boolean isLong();
    public abstract boolean hasLongValue();
    public abstract long longValue();

    public abstract boolean isReal();
    public abstract boolean hasRealValue();
    public abstract FixedPoint realValue();

    public abstract boolean isString();
    public abstract boolean hasStringValue();
    public abstract String stringValue();

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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return false;
        }

        @Override
        public boolean booleanValue() {
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
        public byte byteValue() {
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
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return false;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return false;
        }

        @Override
        public FixedPoint realValue() {
            return null;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return false;
        }

        @Override
        public String stringValue() {
            return null;
        }

        @Override
        public Variant add(Variant var) {
            return this;
        }
    }

    private static class BooleanWrapper extends  Variant {
        public BooleanWrapper(boolean value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public boolean isBoolean() {
            return true;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value;
        }

        @Override
        public boolean isByte() {
            return false;
        }

        @Override
        public boolean hasByteValue() {
            return true;
        }

        @Override
        public byte byteValue() {
            return (byte) (value ? 1 : 0);
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
            return (short) (value ? 1 : 0);
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
            return value ? 1 : 0;
        }

        @Override
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value ? 1 : 0;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            FixedPoint result = new FixedPoint();
            result.setInteger(value ? 1 : 0);
            return result;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return value ? "TRUE" : "FALSE";
        }

        @Override
        public Variant add(Variant var) {
            return null;
        }

        private boolean value;
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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value != 0;
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
        public byte byteValue() {
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
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            FixedPoint result = new FixedPoint();
            result.setInteger(value);
            return result;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return String.valueOf(value);
        }

        @Override
        public Variant add(Variant var) {
            //if (var.isLong())
            //    return new LongWrapper(value + var.longValue());
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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value != 0;
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
        public byte byteValue() {
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
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            FixedPoint result = new FixedPoint();
            result.setInteger(value);
            return result;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return String.valueOf(value);
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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value != 0;
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
        public byte byteValue() {
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
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            FixedPoint result = new FixedPoint();
            result.setInteger(value);
            return result;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return String.valueOf(value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isNumber())
                return new IntWrapper(value + var.intValue());
            return null;
        }

        private int value;
    }

    private static class LongWrapper extends Variant {
        public LongWrapper(long value) {
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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value != 0;
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
        public byte byteValue() {
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
            return false;
        }

        @Override
        public boolean hasIntValue() {
            return value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE;
        }

        @Override
        public int intValue() {
            if (value < Integer.MIN_VALUE) return Integer.MIN_VALUE;
            if (value > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            return (int)value;
        }

        @Override
        public boolean isLong() {
            return true;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            FixedPoint result = new FixedPoint();
            result.setInteger(value);
            return result;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return String.valueOf(value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value + var.longValue());
            return null;
        }

        private long value;
    }

    private static class RealWrapper extends Variant {
        public RealWrapper(FixedPoint value) {
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
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return true;
        }

        @Override
        public boolean booleanValue() {
            return value.getFloat() != 0;
        }

        @Override
        public boolean isByte() {
            return false;
        }

        @Override
        public boolean hasByteValue() {
            if (!value.isInteger())
                return false;
            long intValue = value.getInteger();
            return intValue >= Byte.MIN_VALUE && intValue <= Byte.MAX_VALUE;
        }

        @Override
        public byte byteValue() {
            long intValue = value.getInteger();
            if (intValue < Byte.MIN_VALUE) return Byte.MIN_VALUE;
            if (intValue > Byte.MAX_VALUE) return Byte.MAX_VALUE;
            return (byte)intValue;
        }

        @Override
        public boolean isShort() {
            return false;
        }

        @Override
        public boolean hasShortValue() {
            if (!value.isInteger())
                return false;
            long intValue = value.getInteger();
            return intValue >= Short.MIN_VALUE && intValue <= Short.MAX_VALUE;
        }

        @Override
        public short shortValue() {
            long intValue = value.getInteger();
            if (intValue < Short.MIN_VALUE) return Short.MIN_VALUE;
            if (intValue > Short.MAX_VALUE) return Short.MAX_VALUE;
            return (short)intValue;
        }

        @Override
        public boolean isInt() {
            return false;
        }

        @Override
        public boolean hasIntValue() {
            if (!value.isInteger())
                return false;
            long intValue = value.getInteger();
            return intValue >= Integer.MIN_VALUE && intValue <= Integer.MAX_VALUE;
        }

        @Override
        public int intValue() {
            long intValue = value.getInteger();
            if (intValue < Integer.MIN_VALUE) return Integer.MIN_VALUE;
            if (intValue > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            return (int)intValue;
        }

        @Override
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return true;
        }

        @Override
        public long longValue() {
            return value.getInteger();
        }

        @Override
        public boolean isReal() {
            return true;
        }

        @Override
        public boolean hasRealValue() {
            return true;
        }

        @Override
        public FixedPoint realValue() {
            return value;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return value.toString();
        }

        @Override
        public Variant add(Variant var) {
            if (!var.isNumber())
                return null;
            FixedPoint rightValue = var.realValue();
            FixedPoint result = new FixedPoint();
            FixedPoint.add(result, value, rightValue);
            return new RealWrapper(result);
        }

        private FixedPoint value;
    }

    private static class StringWrapper extends Variant {
        public StringWrapper(String value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean hasBooleanValue() {
            return false;
        }

        @Override
        public boolean booleanValue() {
            return value.length() > 0;
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
        public byte byteValue() {
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
        public boolean isLong() {
            return false;
        }

        @Override
        public boolean hasLongValue() {
            return false;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public boolean isReal() {
            return false;
        }

        @Override
        public boolean hasRealValue() {
            return false;
        }

        @Override
        public FixedPoint realValue() {
            return null;
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public boolean hasStringValue() {
            return true;
        }

        @Override
        public String stringValue() {
            return value;
        }

        @Override
        public Variant add(Variant var) {
            return new StringWrapper(value + var.stringValue());
        }

        private String value;
    }
}
