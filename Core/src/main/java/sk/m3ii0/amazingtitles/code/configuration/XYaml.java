package sk.m3ii0.amazingtitles.code.configuration;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XYaml {
	
	private static int configCounter = 0;
	private static final Pattern id = Pattern.compile("\\'(.*)\\'(.*#.*)");
	
	public static XYaml fromPlugin(org.bukkit.plugin.Plugin plugin, String resource, File output) {
		if (!output.exists()) {
			tryToCreateFileRoad(output);
			try (InputStream stream = plugin.getResource(resource)) {
				Files.copy(stream, output.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new XYaml(++configCounter + "", output, 2);
	}
	public static XYaml fromFileToCopy(File resource, File output) {
		if (resource == null) return null;
		if (!resource.exists()) return null;
		if (!output.exists()) {
			tryToCreateFileRoad(output);
			try (InputStream stream = new FileInputStream(resource)) {
				Files.copy(stream, output.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new XYaml(++configCounter + "", output, 2);
	}
	public static XYaml fromFile(File file) {
		if (file == null) return null;
		if (!file.exists()) return null;
		return new XYaml(++configCounter + "", file, 2);
	}
	public static XYaml fromInputStream(InputStream stream, File output) {
		try (stream) {
			Files.copy(stream, output.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new XYaml(++configCounter + "", output, 2);
	}
	public static XYaml fromPlugin(org.bukkit.plugin.Plugin plugin, String resource, String output) {
		File file = new File(output);
		if (!file.exists()) {
			tryToCreateFileRoad(file);
			try (InputStream stream = plugin.getResource(resource)) {
				Files.copy(stream, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new XYaml(++configCounter + "", file, 2);
	}
	public static XYaml fromFileToCopy(File resource, String output) {
		if (resource == null) return null;
		if (!resource.exists()) return null;
		File file = new File(output);
		if (!file.exists()) {
			tryToCreateFileRoad(file);
			try (InputStream stream = new FileInputStream(resource)) {
				Files.copy(stream, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new XYaml(++configCounter + "", file, 2);
	}
	public static XYaml fromFile(String file) {
		File toRead = new File(file);
		if (!toRead.exists()) return null;
		return new XYaml(++configCounter + "", toRead, 2);
	}
	public static XYaml fromInputStream(InputStream stream, String output) {
		File file = new File(output);
		try (stream) {
			Files.copy(stream, file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new XYaml(++configCounter + "", file, 2);
	}
	private static void tryToCreateFileRoad(File outputFile) {
		File directories = outputFile.getParentFile();
		directories.mkdirs();
	}
	
	private PathClass configuration;
	private File file;
	private final int spaces;
	private final List<String> bases = new ArrayList<>();
	
	private XYaml(String name, File file, int spaces) {
		YamlReader reader = new YamlReader(file, spaces);
		this.configuration = reader.callByName(name);
		this.file = file;
		this.spaces = spaces;
		bases.addAll(getBases());
	}
	
	public Object getObject(String path) {
		PathSection section = configuration.get(path);
		if (section == null) return null;
		return configuration.get(path).getObject();
	}
	
	public String getString(String path) {
		return String.valueOf(getObject(path));
	}
	
	public boolean getBoolean(String path) {
		String object = getString(path);
		if (object == null) return false;
		return Boolean.parseBoolean(path);
	}
	
	public int getInt(String path) {
		return Integer.parseInt(getString(path));
	}
	
	public double getDouble(String path) {
		return Double.parseDouble(getString(path));
	}
	
	public char getChar(String path) {
		return getString(path).charAt(0);
	}
	
	public float getFloat(String path) {
		return Float.parseFloat(getString(path));
	}
	
	public long getLong(String path) {
		return Long.parseLong(getString(path));
	}
	
	public List<String> getKeys(String path) {
		if (path.isEmpty()) return bases;
		List<String> keys = new ArrayList<>();
		PathSection section = configuration.get(path);
		if (section == null) return new ArrayList<>();
		for (PathSection pathSection : section.getSubSections()) {
			keys.add(pathSection.getSection());
		}
		return keys;
	}
	
	public List<String> getTotalKeys(String path) {
		if (path.isEmpty()) return bases;
		List<String> keys = new ArrayList<>();
		PathSection section = configuration.get(path);
		if (section == null) return new ArrayList<>();
		for (PathSection pathSection : section.getSubSections()) {
			keys.add(pathSection.getPath());
		}
		return keys;
	}
	
	public List<String> getStringList(String path) {
		Object object = getObject(path);
		List<String> copy = new Stack<>();
		if (object instanceof List<?> list) {
			for (Object var : list) {
				copy.add(String.valueOf(var));
			}
		}
		return copy;
	}
	
	public List<Boolean> getBooleanList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Boolean> values = new ArrayList<>();
		for (String var : object) {
			values.add(Boolean.parseBoolean(var));
		}
		return values;
	}
	
	public List<Integer> getIntList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Integer> values = new ArrayList<>();
		for (String var : object) {
			values.add(Integer.parseInt(var));
		}
		return values;
	}
	
	public List<Double> getDoubleList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Double> values = new ArrayList<>();
		for (String var : object) {
			values.add(Double.parseDouble(var));
		}
		return values;
	}
	
	public List<Character> getCharList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Character> values = new ArrayList<>();
		for (String var : object) {
			values.add(var.charAt(0));
		}
		return values;
	}
	
	public List<Float> getFloatList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Float> values = new ArrayList<>();
		for (String var : object) {
			values.add(Float.parseFloat(var));
		}
		return values;
	}
	
	public List<Long> getLongList(String path) {
		List<String> object = getStringList(path);
		if (object == null) return null;
		List<Long> values = new ArrayList<>();
		for (String var : object) {
			values.add(Long.parseLong(var));
		}
		return values;
	}
	
	public String getOrCreateUnsafeString(String path, String defaultValue, boolean save) {
		String value = (String) getObject(path);
		if (value == null) {
			set(path, defaultValue);
			if (save) save();
		} else return value;
		return defaultValue;
	}
	
	public boolean containsPath(String path) {
		return configuration.contains(path);
	}
	
	public boolean isValueNull(String path) {
		if (!containsPath(path)) return false;
		return getObject(path) != null;
	}
	
	public void set(String path, Object object) {
		if (path.isEmpty()) return;
		PathSection section = configuration.createPath(path);
		section.setObject(object);
	}
	
	public void reload() {
		YamlReader reader = new YamlReader(file, spaces);
		configuration = reader.callByName(configuration.getName());
		bases.clear();
		bases.addAll(getBases());
	}
	
	public void save() {
		YamlWriter writer = new YamlWriter(configuration);
		writer.writeInto(file, spaces);
	}
	
	private List<String> getBases() {
		List<String> bases = new ArrayList<>();
		for (String key : configuration.getList()) {
			if (key.contains(".")) continue;
			PathSection section = configuration.get(key);
			if (section != null) {
				bases.add(section.getSection());
			}
		}
		return bases;
	}
	
	public File getFile() {
		return file;
	}
	
	public int getSpaces() {
		return spaces;
	}
	
	private class YamlWriter {
		
		private final PathClass pathClass;
		
		public YamlWriter(PathClass pathClass) {
			this.pathClass = pathClass;
		}
		
		public void writeInto(File file, int spaces) {
			try (FileWriter writer = new FileWriter(file)) {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				writer.write(pathClass.createToFormattedText(spaces).substring(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static class YamlReader {
		
		private final File file;
		private final int spaces;
		
		public YamlReader(File file, int spaces) {
			this.file = file;
			this.spaces = spaces;
		}
		
		public PathClass callByName(String name) {
			PathClass pathClass = new PathClass(name);
			try {
				String cachedText = Files.readString(file.toPath());
				String[] lines = cachedText.split(System.lineSeparator());
				Map<Integer, String> pathCache = new HashMap<>();
				PathSection lastSection = null;
				String comments = "";
				List<Object> objects = new ArrayList<>();
				for (String line : lines) {
					String spaces = startingSpaces(line);
					int start = spaces.length() / this.spaces;
					if (start == 0) pathCache.clear();
					String formatted = line.replaceFirst(spaces, "");
					if (formatted.startsWith("#")) {
						comments += System.lineSeparator() + line;
						continue;
					}
					boolean startWith = formatted.startsWith("- ");
					if (lastSection != null && startWith) {
						String listed = formatted.replaceFirst("- ", "").replace("'", "");
						objects.add(listed);
						continue;
					} else if (lastSection != null && !objects.isEmpty()) {
						lastSection.setObject(new ArrayList<>(objects));
						objects.clear();
					}
					String[] sections = splitFirst(formatted);
					pathCache.put(start, sections[0]);
					Object object = "";
					String lineComment = "";
					if (sections.length > 1) {
						Matcher matcher = id.matcher(sections[1]);
						if (matcher.find()) {
							object = XYaml.O.readObject(matcher.group(1));
							lineComment = matcher.group(2);
						} else {
							object = XYaml.O.readObject(sections[1].replaceFirst(" ", "").replace("'", ""));
						}
					}
					String path = "";
					for (int i = 0; i <= start; i++) {
						path += "." + pathCache.get(i);
					}
					path = path.substring(1);
					lastSection = pathClass.createPath(path);
					lastSection.setObject(object);
					if (comments.length() > 0) comments = comments.substring(1);
					lastSection.setComments(comments);
					lastSection.setLineComment(lineComment);
					comments = "";
				}
				if (lastSection != null && !objects.isEmpty()) {
					lastSection.setObject(new ArrayList<>(objects));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return pathClass;
		}
		
		private String startingSpaces(String line) {
			String var = "";
			for (char l : line.toCharArray()) {
				if (l == ' ') var += " ";
				else break;
			}
			return var;
		}
		
		private String[] splitFirst(String text) {
			String[] result = new String[2];
			String builder = "";
			boolean first = true;
			for (char var : text.toCharArray()) {
				if (var == ':' && first) {
					first = false;
					result[0] = builder;
					builder = "";
					continue;
				}
				builder += var;
			}
			result[1] = builder;
			return result;
		}
		
	}
	
	private static class PathClass {
		
		private final Map<String, PathSection> map = new LinkedHashMap<>();
		private final List<String> list = new ArrayList<>();
		
		private final String name;
		
		public PathClass(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void add(String path, PathSection section) {
			map.put(path, section);
			list.add(path);
		}
		
		public List<String> getList() {
			return list;
		}
		
		public PathSection get(String path) {
			return map.get(path);
		}
		
		public boolean contains(String path) {
			return map.containsKey(path);
		}
		
		public void remove(String path) {
			if (map.containsKey(path)) list.remove(map.remove(path));
		}
		
		public String createToFormattedText(int spaces) {
			String formattedText = "";
			Set<Integer> created = new HashSet<>();
			for (String key : list) {
				if (key.contains(".")) continue;
				PathSection section = map.get(key);
				formattedText += section.createText(created, spaces, 0, "");
			}
			return formattedText;
		}
		
		public PathSection createPath(String path) {
			if (map.containsKey(path)) return map.get(path);
			String[] paths = PathUtils.splitBySeparator(path);
			int length = paths.length;
			List<PathSection> preSections = new ArrayList<>();
			List<PathSection> created = new ArrayList<>();
			String buildingPath = "";
			for (int i = 0; i < length; i++) {
				String workingPath = paths[i];
				buildingPath += "." + workingPath;
				if (i == 0) {
					if (!map.containsKey(workingPath)) {
						new PathSection(paths, this);
						break;
					}
					buildingPath = buildingPath.substring(1);
				}
				PathSection builtSection;
				if (map.containsKey(buildingPath)) {
					builtSection = map.get(buildingPath);
				} else {
					builtSection = new PathSection(workingPath, i, this);
					builtSection.setPreSections(new ArrayList<>(preSections));
				}
				builtSection.updatePath();
				preSections.add(builtSection);
				created.add(builtSection);
			}
			for (int i = 0; i < created.size(); i++) {
				int picker = i + 1;
				PathSection section = created.get(i);
				if (picker < created.size()) {
					PathSection picked = created.get(picker);
					if (!section.getSubSections().contains(picked)) section.getSubSections().add(picked);
				}
			}
			return map.get(path);
		}
		
		public Map<String, PathSection> getMap() {
			return map;
		}
		
	}
	
	private static class PathSection {
		
		private static int counter = 0;
		
		private final String section;
		private final List<PathSection> subSections = new ArrayList<>();
		private List<PathSection> preSections = new ArrayList<>();
		private String comments;
		private String lineComment;
		private final int position;
		private final PathClass pathClass;
		private String path;
		private String[] paths;
		private String oldPath;
		private final int id;
		private Object object;
		
		protected PathSection(String[] paths, PathClass pathClass) {
			this.section = paths[0];
			this.pathClass = pathClass;
			this.position = 0;
			this.oldPath = getPath();
			pathClass.add(section, this);
			List<PathSection> preSections = new ArrayList<>() {{
				add(PathSection.this);
			}};
			List<PathSection> created = new ArrayList<>();
			for (int i = 1; i < paths.length; i++) {
				PathSection section = new PathSection(paths[i], i, pathClass);
				section.setPreSections(new ArrayList<>(preSections));
				section.updatePath();
				preSections.add(section);
				if (i == 1) {
					subSections.add(section);
				}
				created.add(section);
			}
			int size = created.size();
			for (int i = 0; i < created.size(); i++) {
				int pick = i + 1;
				if (pick < size) {
					created.get(i).getSubSections().add(created.get(pick));
				}
			}
			this.id = ++counter;
			this.comments = "";
			this.lineComment = "";
		}
		
		public Object getObject() {
			return object;
		}
		
		protected PathSection(String path, int position, PathClass pathClass) {
			this.section = path;
			this.position = position;
			this.pathClass = pathClass;
			this.id = ++counter;
			this.comments = "";
		}
		
		public void updatePath() {
			pathClass.remove(oldPath);
			this.oldPath = getPath();
			pathClass.add(this.oldPath, this);
			this.paths = PathUtils.splitBySeparator(this.oldPath);
		}
		
		public List<PathSection> getPreSections() {
			return preSections;
		}
		
		public String getSection() {
			return section;
		}
		
		public void setComments(String comments) {
			this.comments = comments;
		}
		
		public String getComments() {
			return comments;
		}
		
		public void setLineComment(String lineComment) {
			this.lineComment = lineComment;
		}
		
		protected String createText(Set<Integer> created, int spaces, int start, String space) {
			String totalText = "";
			for (int v = 0; v < spaces*start; v++) {
				space += " ";
			}
			for (PathSection preSection : preSections) {
				if (preSection.getPosition() <= start) continue;
				String[] road = preSection.getPaths();
				String totalPath = "";
				for (int i = 0; i < road.length; i++) {
					String workingPath = road[i];
					totalPath += "." + workingPath;
					if (i == 0) {
						totalPath = totalPath.substring(1);
					}
					if (!created.contains(preSection.getId())) {
						totalText += preSection.getComments();
						if (preSection.lineComment == null || preSection.lineComment.isEmpty()) totalText += System.lineSeparator() + space + workingPath + ": " + O.createStringFromObject(getPosition(), object, spaces);
						else totalText += System.lineSeparator() + space + workingPath + ": '" + O.createStringFromObject(getPosition(), object, spaces) + "'" + lineComment;
						created.add(preSection.getId());
					}
				}
			}
			if (!created.contains(getId())) {
				if (section != null) {
					totalText += this.comments;
					if (lineComment == null || lineComment.isEmpty()) totalText += System.lineSeparator() + space + section + ": " + O.createStringFromObject(getPosition(), object, spaces);
					else totalText += System.lineSeparator() + space + section + ": '" + O.createStringFromObject(getPosition(), object, spaces) + "'" + lineComment;
					created.add(getId());
				}
			}
			for (PathSection pathSection : subSections) {
				totalText += pathSection.createText(created, spaces, start+1, space);
			}
			return totalText;
		}
		
		public void setObject(Object object) {
			this.object = object;
		}
		
		public String[] getPaths() {
			return paths;
		}
		
		public void setPreSections(List<PathSection> sections) {
			this.preSections = sections;
		}
		
		public PathClass getPathClass() {
			return pathClass;
		}
		
		public int getId() {
			return id;
		}
		
		public int getPosition() {
			return position;
		}
		
		public String getPath() {
			if (path == null) {
				String path = "";
				for (PathSection var : preSections) {
					path += "." + var.getSection();
				}
				path += "." + this.section;
				this.path = path.substring(1);
			}
			return this.path;
		}
		
		public List<PathSection> getSubSections() {
			return subSections;
		}
		
	}
	
	private static class PathUtils {
		
		public static String[] splitBySeparator(String path) {
			return path.split("\\.");
		}
		
	}
	
	private static class O {
		
		public static String createStringFromObject(int begin, Object object, int spaces) {
			if (object == null) return "";
			if (object instanceof List<?> list) {
				if (list.isEmpty()) return "[]";
				String text = "";
				String space = " ";
				for (int i = 0; i < begin * spaces; i++) {
					space += " ";
				}
				for (Object var : list) {
					text += System.lineSeparator() + space + " - " + "" + String.valueOf(var) + "";
				}
				return text;
			}
			return "" + String.valueOf(object) + "";
		}
		
		public static Object readObject(String value) {
			return value;
		}
		
	}
	
}