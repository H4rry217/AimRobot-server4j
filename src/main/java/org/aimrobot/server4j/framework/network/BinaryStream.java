package org.aimrobot.server4j.framework.network;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @description: BinaryStream refer to project nukkit
 * @nukkit-proj-url: https://github.com/Nukkit/Nukkit
 * @author: H4rry217
 **/

public class BinaryStream {

    public int offset;
    private byte[] buffer = new byte[32];
    private int count;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public BinaryStream() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(byte[] buffer) {
        this(buffer, 0);
    }

    public BinaryStream(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
        this.count = buffer.length;
    }

    public void reset() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.count = buffer == null ? -1 : buffer.length;
    }

    public void setBuffer(byte[] buffer, int offset) {
        this.setBuffer(buffer);
        this.setOffset(offset);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte[] getBuffer() {
        return Arrays.copyOf(buffer, count);
    }

    public int getCount() {
        return count;
    }

    public byte[] get() {
        return this.get(this.count - this.offset);
    }

    public byte[] get(int len) {
        if (len < 0) {
            this.offset = this.count - 1;
            return new byte[0];
        }
        len = Math.min(len, this.getCount() - this.offset);
        this.offset += len;
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset);
    }

    public void put(byte[] bytes) {
        if (bytes == null) {
            return;
        }

        this.ensureCapacity(this.count + bytes.length);

        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.length);
        this.count += bytes.length;
    }

    public void putByte(byte b) {
        this.put(new byte[]{b});
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - this.buffer.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = this.buffer.length;
        int newCapacity = oldCapacity << 1;

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }

        this.buffer = Arrays.copyOf(this.buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) throw new OutOfMemoryError();

        return (minCapacity > MAX_ARRAY_SIZE)? Integer.MAX_VALUE: MAX_ARRAY_SIZE;
    }

    public long getLong(){
        return Binary.readLong(this.get(8));
    }

    public void putLong(long l){
        this.put(Binary.writeLong(l));
    }

    public void putBoolean(boolean b){
        this.putByte((byte) (b? 1: 0));
    }

    public boolean getBoolean(){
        return (this.get(1)[0] != 0);
    }

    public int getShort(){
        return Binary.readShort(this.get(2));
    }

    public void putShort(int s){
        this.put(Binary.writeShort(s));
    }

    public String getString(int size, Charset charset){
        return new String(this.get(size), charset);
    }

    public String getString(){
        int byteSize = this.getShort();
        return getString(byteSize, StandardCharsets.UTF_8);
    }

    public void putString(String s, Charset charset){
        if(s == null){
            this.putShort(0);
        }else{
            byte[] str = s.getBytes(charset);
            this.putShort(str.length);
            this.put(str);
        }

    }

    public void putString(String s){
        this.putString(s, StandardCharsets.UTF_8);
    }

}
