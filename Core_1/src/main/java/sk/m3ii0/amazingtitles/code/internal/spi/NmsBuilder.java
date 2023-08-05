package sk.m3ii0.amazingtitles.code.internal.spi;

public interface NmsBuilder {
	
	boolean checked(String version);
	NmsProvider build();
	
}
