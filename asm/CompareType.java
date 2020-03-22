package asm;

public enum CompareType {
  EQ(0xF0), LT(0x01);
  
  private byte encoding;
  
  public byte encode() {
    return encoding;
  }

  private CompareType(int encoding) {
    this.encoding = (byte)encoding;
  }
}
