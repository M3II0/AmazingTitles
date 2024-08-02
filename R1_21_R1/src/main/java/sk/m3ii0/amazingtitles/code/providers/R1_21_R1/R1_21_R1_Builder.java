package sk.m3ii0.amazingtitles.code.providers.R1_21_R1;

import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;

public class R1_21_R1_Builder implements NmsBuilder {
	
	
	@Override
	public boolean checked(String version) {
		return version.equals("v1_21_R1") || version.equals("1.21-R0.1-SNAPSHOT");
	}
	
	@Override
	public NmsProvider build() {
		return new R1_21_R1();
	}
	
}
