package sk.m3ii0.amazingtitles.code.providers.R1_13_R1;


import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;

public class R1_13_R1_Builder implements NmsBuilder {
	
	
	@Override
	public boolean checked(String version) {
		return version.equals("v1_13_R1");
	}
	
	@Override
	public NmsProvider build() {
		return new R1_13_R1();
	}
	
}
