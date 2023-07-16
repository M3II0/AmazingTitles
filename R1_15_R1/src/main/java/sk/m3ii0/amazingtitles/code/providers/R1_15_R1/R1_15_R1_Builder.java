package sk.m3ii0.amazingtitles.code.providers.R1_15_R1;

import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;

public class R1_15_R1_Builder implements NmsBuilder {
	
	
	@Override
	public boolean checked(String version) {
		return version.equals("v1_15_R1");
	}
	
	@Override
	public NmsProvider build() {
		return new R1_15_R1();
	}
	
}
