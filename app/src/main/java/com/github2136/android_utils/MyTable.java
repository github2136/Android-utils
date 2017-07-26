package com.github2136.android_utils;

import com.github2136.sqlutil.Column;
import com.github2136.sqlutil.Table;

/**
 * Created by yubin on 2017/7/20.
 */
@Table
public class MyTable {
    @Column()
    private String str;
    @Column()
    private byte byt;
    @Column()
    private short shor;
    @Column()
    private int intt;
    @Column()
    private long lon;
    @Column()
    private float floa;
    @Column()
    private double doubl;
    @Column()
    private boolean boolea;
    @Column()
    private byte[] bytes;
    @Column()
    private Byte Byt1;
    @Column()
    private Short Shor1;
    @Column()
    private Integer Intege1;
    @Column()
    private Long Lon1;
    @Column()
    private Float Floa1;
    @Column()
    private Double Doubl1;
    @Column()
    private Boolean Boolea1;
    @Column()
    private Byte[] Bytes1;

    public String getStr() {
        return str == null ? "" : str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public byte getByt() {
        return byt;
    }

    public void setByt(byte byt) {
        this.byt = byt;
    }

    public short getShor() {
        return shor;
    }

    public void setShor(short shor) {
        this.shor = shor;
    }

    public int getIntt() {
        return intt;
    }

    public void setIntt(int intt) {
        this.intt = intt;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public float getFloa() {
        return floa;
    }

    public void setFloa(float floa) {
        this.floa = floa;
    }

    public double getDoubl() {
        return doubl;
    }

    public void setDoubl(double doubl) {
        this.doubl = doubl;
    }

    public boolean isBoolea() {
        return boolea;
    }

    public void setBoolea(boolean boolea) {
        this.boolea = boolea;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Byte getByt1() {
        return Byt1;
    }

    public void setByt1(Byte byt1) {
        Byt1 = byt1;
    }

    public Short getShor1() {
        return Shor1;
    }

    public void setShor1(Short shor1) {
        Shor1 = shor1;
    }

    public Integer getIntege1() {
        return Intege1;
    }

    public void setIntege1(Integer intege1) {
        Intege1 = intege1;
    }

    public Long getLon1() {
        return Lon1;
    }

    public void setLon1(Long lon1) {
        Lon1 = lon1;
    }

    public Float getFloa1() {
        return Floa1;
    }

    public void setFloa1(Float floa1) {
        Floa1 = floa1;
    }

    public Double getDoubl1() {
        return Doubl1;
    }

    public void setDoubl1(Double doubl1) {
        Doubl1 = doubl1;
    }

    public Boolean getBoolea1() {
        return Boolea1;
    }

    public void setBoolea1(Boolean boolea1) {
        Boolea1 = boolea1;
    }

    public Byte[] getBytes1() {
        return Bytes1;
    }

    public void setBytes1(Byte[] bytes1) {
        Bytes1 = bytes1;
    }
}
