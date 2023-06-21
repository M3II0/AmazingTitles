package sk.m3ii0.amazingtitles.code.spi;

public interface NmsBuilder {
	
	boolean checked(String version);
	NmsProvider build();
	
}
