package sk.m3ii0.amazingtitles.code.providers.R1_20_R3;

import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;

public class R1_20_R3_Builder implements NmsBuilder {
	
	
	@Override
	public boolean checked(String version) {
		return version.equals("v1_20_R3");
	}
	
	@Override
	public NmsProvider build() {
		return new R1_20_R3();
	}
	
}
