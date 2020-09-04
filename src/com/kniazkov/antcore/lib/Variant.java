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

    @Override
    public String toString() {
        return stringValue();
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

    public abstract byte sign();

    public abstract Variant neg();
    public abstract Variant not();
    public abstract Variant add(Variant var);
    public abstract Variant sub(Variant var);
    public abstract Variant mul(Variant var);
    public abstract Variant div(Variant var);
    public abstract Variant mod(Variant var);
    public abstract Variant leftShift(Variant var);
    public abstract Variant rightShift(Variant var);
    public abstract Variant and(Variant var);
    public abstract Variant or(Variant var);
    public abstract Variant xor(Variant var);
    public abstract Variant equals(Variant var);
    public abstract Variant diff(Variant var);
    public abstract Variant less(Variant var);
    public abstract Variant greater(Variant var);
    public abstract Variant lessEqual(Variant var);
    public abstract Variant greaterEqual(Variant var);

    private static class Null extends Variant {
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
        public byte sign() {
            return 0;
        }

        @Override
        public Variant neg() {
            return this;
        }

        @Override
        public Variant not() {
            return this;
        }

        @Override
        public Variant add(Variant var) {
            return this;
        }

        @Override
        public Variant sub(Variant var) {
            return this;
        }

        @Override
        public Variant mul(Variant var) {
            return this;
        }

        @Override
        public Variant div(Variant var) {
            return this;
        }

        @Override
        public Variant mod(Variant var) {
            return this;
        }

        @Override
        public Variant leftShift(Variant var) {
            return this;
        }

        @Override
        public Variant rightShift(Variant var) {
            return this;
        }

        @Override
        public Variant and(Variant var) {
            return this;
        }

        @Override
        public Variant or(Variant var) {
            return this;
        }

        @Override
        public Variant xor(Variant var) {
            return this;
        }

        @Override
        public Variant equals(Variant var) {
            return this;
        }

        @Override
        public Variant diff(Variant var) {
            return this;
        }

        @Override
        public Variant less(Variant var) {
            return this;
        }

        @Override
        public Variant greater(Variant var) {
            return this;
        }

        @Override
        public Variant lessEqual(Variant var) {
            return this;
        }

        @Override
        public Variant greaterEqual(Variant var) {
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
        public byte sign() {
            return 0;
        }

        @Override
        public Variant neg() {
            return Null.getInstance();
        }

        @Override
        public Variant not() {
            return new BooleanWrapper(!value);
        }

        @Override
        public Variant add(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            if (!var.isBoolean())
                return Null.getInstance();
            return new BooleanWrapper(value && var.booleanValue());
        }

        @Override
        public Variant or(Variant var) {
            if (!var.isBoolean())
                return Null.getInstance();
            return new BooleanWrapper(value || var.booleanValue());
        }

        @Override
        public Variant xor(Variant var) {
            if (!var.isBoolean())
                return Null.getInstance();
            return new BooleanWrapper(value != var.booleanValue());
        }

        @Override
        public Variant equals(Variant var) {
            if (!var.isBoolean())
                return Null.getInstance();
            return new BooleanWrapper(value == var.booleanValue());
        }

        @Override
        public Variant diff(Variant var) {
            if (!var.isBoolean())
                return Null.getInstance();
            return new BooleanWrapper(value != var.booleanValue());
        }

        @Override
        public Variant less(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant greater(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant lessEqual(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant greaterEqual(Variant var) {
            return Null.getInstance();
        }

        private boolean value;
    }

    private static abstract class BaseIntWrapper extends Variant {
        @Override
        public byte sign() {
            long value = longValue();
            if (value > 0) return 1;
            else if (value < 0) return -1;
            else return 0;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public Variant equals(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() == var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() == var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant diff(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() != var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() != var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant less(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() < var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() < var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant greater(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() > var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() > var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant lessEqual(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() <= var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() <= var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant greaterEqual(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(longValue() >= var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(longValue() >= var.longValue());
            return Null.getInstance();
        }
    }

    private static class ByteWrapper extends BaseIntWrapper {
        public ByteWrapper(byte value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
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
        public Variant neg() {
            return new ByteWrapper((byte) -value);
        }

        @Override
        public Variant not() {
            return new ByteWrapper((byte) ~value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.add(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value + var.longValue());
            if (var.isInt())
                return new IntWrapper(value + var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value + var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value + var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.sub(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value - var.longValue());
            if (var.isInt())
                return new IntWrapper(value - var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value - var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value - var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.mul(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value * var.longValue());
            if (var.isInt() || var.isShort())
                return new IntWrapper(value * var.intValue());
            if (var.isByte())
                return new ShortWrapper((short) (value * var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.div(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value / var.longValue());
            if (var.isInt())
                return new IntWrapper(value / var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value / var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value / var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            if (var.isLong())
                return new LongWrapper(value % var.longValue());
            if (var.isInt())
                return new IntWrapper(value % var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value % var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value % var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value << var.longValue());
            if (var.isInt())
                return new IntWrapper(value << var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value << var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value << var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value >>> var.longValue());
            if (var.isInt())
                return new IntWrapper(value >>> var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value >>> var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value >>> var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            if (var.isLong())
                return new LongWrapper(value & var.longValue());
            if (var.isInt())
                return new IntWrapper(value & var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value & var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value & var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            if (var.isLong())
                return new LongWrapper(value | var.longValue());
            if (var.isInt())
                return new IntWrapper(value | var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value | var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value | var.byteValue()));
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            if (var.isLong())
                return new LongWrapper(value ^ var.longValue());
            if (var.isInt())
                return new IntWrapper(value ^ var.intValue());
            if (var.isShort())
                return new ShortWrapper((short) (value ^ var.shortValue()));
            if (var.isByte())
                return new ByteWrapper((byte) (value ^ var.byteValue()));
            return Null.getInstance();
        }

        private byte value;
    }

    private static class ShortWrapper extends BaseIntWrapper {
        public ShortWrapper(short value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
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
        public Variant neg() {
            return new ShortWrapper((short) -value);
        }

        @Override
        public Variant not() {
            return new ShortWrapper((short) ~value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.add(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value + var.longValue());
            if (var.isInt())
                return new IntWrapper(value + var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value + var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.sub(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value - var.longValue());
            if (var.isInt())
                return new IntWrapper(value - var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value - var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.mul(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value * var.longValue());
            if (var.isNumber())
                return new IntWrapper(value * var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.div(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value / var.longValue());
            if (var.isInt())
                return new IntWrapper(value / var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value / var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            if (var.isLong())
                return new LongWrapper(value % var.longValue());
            if (var.isInt())
                return new IntWrapper(value % var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value % var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value << var.longValue());
            if (var.isInt())
                return new IntWrapper(value << var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value << var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value >>> var.longValue());
            if (var.isInt())
                return new IntWrapper(value >>> var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value >>> var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            if (var.isLong())
                return new LongWrapper(value & var.longValue());
            if (var.isInt())
                return new IntWrapper(value & var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value & var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            if (var.isLong())
                return new LongWrapper(value | var.longValue());
            if (var.isInt())
                return new IntWrapper(value | var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value | var.shortValue()));
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            if (var.isLong())
                return new LongWrapper(value ^ var.longValue());
            if (var.isInt())
                return new IntWrapper(value ^ var.intValue());
            if (var.isNumber())
                return new ShortWrapper((short) (value ^ var.shortValue()));
            return Null.getInstance();
        }

        private short value;
    }

    private static class IntWrapper extends BaseIntWrapper {
        public IntWrapper(int value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
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
        public Variant neg() {
            return new IntWrapper(-value);
        }

        @Override
        public Variant not() {
            return new IntWrapper(~value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.add(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value + var.longValue());
            if (var.isNumber())
                return new IntWrapper(value + var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.sub(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value - var.longValue());
            if (var.isNumber())
                return new IntWrapper(value - var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.mul(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value * var.longValue());
            if (var.isNumber())
                return new IntWrapper(value * var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.div(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isLong())
                return new LongWrapper(value / var.longValue());
            if (var.isNumber())
                return new IntWrapper(value / var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            if (var.isLong())
                return new LongWrapper(value % var.longValue());
            if (var.isNumber())
                return new IntWrapper(value % var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value << var.longValue());
            if (var.isNumber())
                return new IntWrapper(value << var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            if (var.isLong())
                return new LongWrapper(value >>> var.longValue());
            if (var.isNumber())
                return new IntWrapper(value >>> var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            if (var.isLong())
                return new LongWrapper(value & var.longValue());
            if (var.isNumber())
                return new IntWrapper(value & var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            if (var.isLong())
                return new LongWrapper(value | var.longValue());
            if (var.isNumber())
                return new IntWrapper(value | var.intValue());
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            if (var.isLong())
                return new LongWrapper(value ^ var.longValue());
            if (var.isNumber())
                return new IntWrapper(value ^ var.intValue());
            return Null.getInstance();
        }

        private int value;
    }

    private static class LongWrapper extends BaseIntWrapper {
        public LongWrapper(long value) {
            this.value = value;
        }

        @Override
        public boolean isNull() {
            return false;
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
        public Variant neg() {
            return new LongWrapper(-value);
        }

        @Override
        public Variant not() {
            return new LongWrapper(~value);
        }

        @Override
        public Variant add(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.add(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isNumber())
                return new LongWrapper(value + var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.sub(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isNumber())
                return new LongWrapper(value - var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.mul(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isNumber())
                return new LongWrapper(value * var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            if (var.isReal()) {
                FixedPoint result = new FixedPoint();
                FixedPoint.div(result, realValue(), var.realValue());
                return new RealWrapper(result);
            }
            if (var.isNumber())
                return new LongWrapper(value / var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value % var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value << var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value >>> var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value & var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value | var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            if (var.isNumber())
                return new LongWrapper(value ^ var.longValue());
            return Null.getInstance();
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
        public byte sign() {
            return value.sign();
        }

        @Override
        public Variant neg() {
            FixedPoint result = new FixedPoint();
            FixedPoint.neg(result, value);
            return new RealWrapper(result);
        }

        @Override
        public Variant not() {
            return Null.getInstance();
        }

        @Override
        public Variant add(Variant var) {
            if (!var.isNumber())
                return Null.getInstance();
            FixedPoint result = new FixedPoint();
            FixedPoint.add(result, value, var.realValue());
            return new RealWrapper(result);
        }

        @Override
        public Variant sub(Variant var) {
            if (!var.isNumber())
                return Null.getInstance();
            FixedPoint result = new FixedPoint();
            FixedPoint.sub(result, value, var.realValue());
            return new RealWrapper(result);
        }

        @Override
        public Variant mul(Variant var) {
            if (!var.isNumber())
                return Null.getInstance();
            FixedPoint result = new FixedPoint();
            FixedPoint.mul(result, value, var.realValue());
            return new RealWrapper(result);
        }

        @Override
        public Variant div(Variant var) {
            if (!var.isNumber())
                return Null.getInstance();
            FixedPoint result = new FixedPoint();
            FixedPoint.div(result, value, var.realValue());
            return new RealWrapper(result);
        }

        @Override
        public Variant mod(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant equals(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() == var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() == var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant diff(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() != var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() != var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant less(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() < var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() < var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant greater(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() > var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() > var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant lessEqual(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() <= var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() <= var.longValue());
            return Null.getInstance();
        }

        @Override
        public Variant greaterEqual(Variant var) {
            if (var.isReal())
                return new BooleanWrapper(value.getFloat() >= var.realValue().getFloat());
            if (var.isNumber())
                return new BooleanWrapper(value.getFloat() >= var.longValue());
            return Null.getInstance();
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
        public byte sign() {
            return 0;
        }

        @Override
        public Variant neg() {
            return Null.getInstance();
        }

        @Override
        public Variant not() {
            return Null.getInstance();
        }

        @Override
        public Variant add(Variant var) {
            if (!var.isNull())
                return new StringWrapper(value + var.stringValue());
            return Null.getInstance();
        }

        @Override
        public Variant sub(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant mul(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant div(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant mod(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant leftShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant rightShift(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant and(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant or(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant xor(Variant var) {
            return Null.getInstance();
        }

        @Override
        public Variant equals(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(value.equals(var.stringValue()));
        }

        @Override
        public Variant diff(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(!value.equals(var.stringValue()));
        }

        @Override
        public Variant less(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(value.compareTo(var.stringValue()) < 0);
        }

        @Override
        public Variant greater(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(value.compareTo(var.stringValue()) > 0);
        }

        @Override
        public Variant lessEqual(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(value.compareTo(var.stringValue()) <= 0);
        }

        @Override
        public Variant greaterEqual(Variant var) {
            if (!var.isString())
                return Null.getInstance();
            return new BooleanWrapper(value.compareTo(var.stringValue()) >= 0);
        }

        private String value;
    }
}
