package sk.m3ii0.amazingtitles.code.providers.R1_13_R2;

import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;

public class R1_13_R2_Builder implements NmsBuilder {
	
	
	@Override
	public boolean checked(String version) {
		return version.equals("v1_13_R2");
	}
	
	@Override
	public NmsProvider build() {
		return new R1_13_R2();
	}
	
}
