package sk.m3ii0.amazingtitles.code.api.interfaces;

import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;

import java.util.LinkedList;
import java.util.List;

public interface FramesBuilder {
	
	LinkedList<String> buildFrames(ComponentArguments arguments, String[] args);
	
}
